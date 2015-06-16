package hackathon.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import hackathon.app.dao.User;
import hackathon.app.dao.UserDao;
import hackathon.app.event.EventsActivity;
import hackathon.app.facebook.FacebookService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    private CallbackManager callbackManager;

    private Intent mainActivityIntent;

    private Intent eventsActivityIntent;

    private Intent friendsActivityIntent;

    private TokenTracker tokenTracker;

    private UserDao userDao;

    private CurrentUserHolder currentUserHolder;

    private final FacebookService facebookService = new FacebookService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        userDao = new UserDao(getApplicationContext());
        eventsActivityIntent = new Intent(this, EventsActivity.class);
        friendsActivityIntent = new Intent(this, FriendsActivity.class);
        mainActivityIntent = new Intent(this, MainActivity.class);
        callbackManager = CallbackManager.Factory.create();
        tokenTracker = new TokenTracker();
        currentUserHolder = new CurrentUserHolder(this);

        if (AccessToken.getCurrentAccessToken() != null) {
            final Button eventListButton = (Button) findViewById(R.id.button);
            eventListButton.setVisibility(View.VISIBLE);
            eventListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(eventsActivityIntent);
                }
            });
            final Button friendsButton = (Button) findViewById(R.id.button2);
            friendsButton.setVisibility(View.VISIBLE);
            friendsButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(friendsActivityIntent);
                }
            });

            final ImageView imageView = (ImageView) findViewById(R.id.imageView);
            final TextView textView = (TextView) findViewById(R.id.textView);
            final FacebookService facebookService = new FacebookService();
            facebookService.getUserInfo(new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    try {
                        String facebookId = jsonObject.getString("id");
                        facebookService.downloadFacebookImage(imageView, facebookId);
                        String welcomeMessage = "Welcome " + jsonObject.getString("name");
                        textView.setText(welcomeMessage);
                        textView.setEnabled(true);

                        new AsyncTask<Void, Void, JSONArray>() {
                            @Override
                            protected JSONArray doInBackground(Void... params) {
                                return userDao.getFacebookFriends();
                            }

                            @Override
                            protected void onPostExecute(JSONArray result) {
                                final TextView textView1 = (TextView) findViewById(R.id.textView2);
                                String message = "You have " + result.length() + " friends using this application";
                                textView1.setText(message);
                            }
                        }.execute();

                    } catch (JSONException e) {
                        Log.e("Error", e.getMessage());
                    }

                }
            });

            if (currentUserHolder.getCurrentUserId() == -1) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        FacebookService facebookService = new FacebookService();
                        facebookService.getUserInfo(new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                try {
                                    List<User> users = userDao.getUsers();
                                    String facebookId = jsonObject.getString("id");
                                    for (User user: users) {
                                       if (user.getFacebookId().equals(facebookId)) {
                                           currentUserHolder.setCurrentUserId(user.getId());
                                       }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        return null;
                    }

                }.execute();
            }
        }

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookLoginCallback());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.v("Facebook Login", "success");

            facebookService.getUserInfo(new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    try {
                        //checks if user is registered (inside the method)
                        Log.v("MainActivity", jsonObject.toString());
                        userDao.registerUser(jsonObject.getString("id"), jsonObject.getString("name"));
                    } catch (JSONException e) {
                        Log.v("MainActivity", e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onCancel() {
            Log.v("Facebook Login", "cancel");
        }

        @Override
        public void onError(FacebookException exception) {
            Log.v("Facebook Login", exception.getMessage());
        }
    }

    private class TokenTracker extends AccessTokenTracker {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Log.v("MainActivity", "Access token set to null");
                new CurrentUserHolder(getApplicationContext()).setCurrentUserId(-1);
                startActivity(mainActivityIntent);
            } else {
                Log.v("MainActivity", "Access token set to " + currentAccessToken);
                startActivity(eventsActivityIntent);
            }
        }
    }
}
