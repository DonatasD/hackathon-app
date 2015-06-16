package hackathon.app.event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import hackathon.app.CurrentUserHolder;
import hackathon.app.MainActivity;
import hackathon.app.R;
import hackathon.app.dao.*;
import hackathon.app.notifications.NotificationHandler;

import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

public class EventActivity extends FragmentActivity {

    private long _eventId;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(this, MainActivity.class));
        }

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Long timestamp = calendar.getTime().getTime();
                final Long userId = new CurrentUserHolder(getApplicationContext()).getCurrentUserId();

                new AsyncTask<Void, Void, Event>() {
                    @Override
                    protected Event doInBackground(Void... params) {
                        EventDao eventDao = new EventDao();
                        List<Event> events = eventDao.getEvents();
                        for (Event event : events) {
                            if (_eventId == event.getId()) {
                                return event;
                            }
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(final Event event) {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                UserDao userDao = new UserDao(getApplicationContext());
                                List<User> users = userDao.getUsers();
                                for (User user : users) {
                                    if (userId == user.getId()) {
                                        return user.getName() + " is attending " + event.getName();
                                    }
                                }
                                return "";
                            }
                            @Override
                            protected void onPostExecute(String message) {
                                NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());
                                notificationHandler.show("Friend Attend Event", message, _eventId);
                            }
                        }.execute();
                    }
                }.execute();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        Bundle b = getIntent().getExtras();
        _eventId = b.getLong("eventId");
        fetchEvent(_eventId);


        setContentView(R.layout.activity_event);

        Button attendButton = (Button) findViewById(R.id.attendingButton);
        attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        new RatingDao(getApplicationContext()).createRating(_eventId, (int) rating);
                        return null;
                    }
                }.execute();
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final List<Rating> ratings = new RatingDao(getApplicationContext()).getRatings();
                final long userId = new CurrentUserHolder(getApplicationContext()).getCurrentUserId();

                for (final Rating rating : ratings) {
                    if (rating.getEventId() == _eventId && rating.getUserId() == userId) {
                        ratingBar.setRating(rating.getValue());
                        return null;
                    }
                }

                return null;
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void fetchEvent(final long id) {
        new AsyncTask<Void, Void, List<Event>>() {
            @Override
            protected List<Event> doInBackground(Void... voids) {
                return new EventDao().getEvents();
            }

            @Override
            protected void onPostExecute(List<Event> events) {
                ListIterator<Event> iter = events.listIterator();
                Event event = null;
                while (iter.hasNext()) {
                    event = iter.next();
                    if (event.getId() == id) {
                        break;
                    }
                }
                populateEventData(event);
            }
        }.execute();
    }

    private void populateEventData(Event event) {
        final TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        eventDescription.setText(Html.fromHtml(event.getDescription()));

        final TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        eventLocation.setText(event.getStreet() + ", " + event.getTown() + ", " + event.getPostcode());

    }
}
