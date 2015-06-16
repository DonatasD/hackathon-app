package hackathon.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookSdk;
import hackathon.app.dao.UserDao;
import hackathon.app.security.Authentication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by don on 6/16/15.
 */
public class FriendsActivity extends ListActivity {

    private final ArrayList<String> listViewData = new ArrayList<String>();
    private final ArrayList<String> listIds = new ArrayList<String>();
    private FriendsListAdapter friendsListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (!Authentication.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
        }

        this.friendsListAdapter = new FriendsListAdapter(this, listViewData);
        setListAdapter(this.friendsListAdapter);
        final UserDao userDao = new UserDao(this);

        new AsyncTask<Void, Void, JSONArray>() {
            @Override
            protected JSONArray doInBackground(Void... params) {
                return userDao.getFacebookFriends();
            }

            protected void onPostExecute(JSONArray result) {
                try {
                    Log.v("Friends", result.toString());
                    final ArrayList<String> friendsName = new ArrayList<String>();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        Log.v("Friends", jsonObject.toString());
                        String name = jsonObject.getString("name");
                        Log.v("Friends", name);
                        String id = jsonObject.getString("id");
                        Log.v("Friends", id);
                        friendsName.add(name);
                        listIds.add(id);
                    }
                    friendsListAdapter.addDataToList(friendsName, listIds);
                } catch (Exception e) {
                    Log.e("FriendsList", "Failed");
                }
            }
        }.execute();
    }

}
