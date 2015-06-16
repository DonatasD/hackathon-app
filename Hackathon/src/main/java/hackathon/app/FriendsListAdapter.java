package hackathon.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hackathon.app.facebook.FacebookService;

import java.util.ArrayList;

/**
 * Created by don on 6/16/15.
 */
public class FriendsListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> names;
    private ArrayList<String> ids;

    public FriendsListAdapter(Context context, ArrayList<String> names) {
        super(context, R.layout.activity_events, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_friends, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo1);
        textView.setText(names.get(position));


        FacebookService facebookService = new FacebookService();
        facebookService.downloadFacebookImage(imageView, ids.get(position));

        return rowView;
    }

    public void addDataToList(ArrayList<String> friendNames, ArrayList<String> facebookIds ) {
        this.names.clear();
        for (String value: friendNames) {
            this.names.add(value);
        }
        this.ids = facebookIds;
        this.notifyDataSetChanged();
    }


}
