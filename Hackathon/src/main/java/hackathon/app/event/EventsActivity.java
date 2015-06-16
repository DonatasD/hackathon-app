package hackathon.app.event;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.facebook.FacebookSdk;
import hackathon.app.MainActivity;
import hackathon.app.dao.Event;
import hackathon.app.dao.EventDao;
import hackathon.app.security.Authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class EventsActivity extends ListActivity {

    private final ArrayList<String> listViewData = new ArrayList<String>();
    private final ArrayList<Long> listIds = new ArrayList<Long>();
    private EventListAdapter eventListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (!Authentication.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
        }

        this.eventListAdapter = new EventListAdapter(this, listViewData);
        setListAdapter(this.eventListAdapter);
        final EventDao eventDao = new EventDao();

        new AsyncTask<Void, Void, List<Event>>() {
            @Override
            protected List<Event> doInBackground(Void... params) {
                return eventDao.getEvents();
            }

            protected void onPostExecute(List<Event> result) {
                final ArrayList<String> eventNames = new ArrayList<String>();
                final ArrayList<String> eventCategory = new ArrayList<String>();
                for (Event event: result) {
                    eventNames.add(event.getName());
                    eventCategory.add(event.getCategory());
                    // Limit hack
                    int count = 0;
                    if (count > 50) {
                        break;
                    }
                    count++;
                    listIds.add(event.getId());
                }
                eventListAdapter.addDataToList(eventNames,eventCategory);
            }
        }.execute();

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent eventView = new Intent(this, EventActivity.class);
        Bundle b = new Bundle();
        b.putLong("eventId", listIds.get(position)); //Your id
        eventView.putExtras(b); //Put your id to your next Intent
        startActivity(eventView);
    }

}
