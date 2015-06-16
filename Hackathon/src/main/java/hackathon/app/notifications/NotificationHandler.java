package hackathon.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import hackathon.app.R;
import hackathon.app.event.EventActivity;

/**
 * Created by don on 6/16/15.
 */
public class NotificationHandler {

    private final Context context;

    public NotificationHandler(Context context) {
        this.context = context;
    }

    public void show(String title, String text, long eventId) {

        Intent futureIntent = new Intent(context, EventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("eventId", eventId);
        futureIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, futureIntent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }
}
