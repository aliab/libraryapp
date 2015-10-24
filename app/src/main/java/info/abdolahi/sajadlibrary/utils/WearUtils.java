package info.abdolahi.sajadlibrary.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by aliabdolahi on 9/4/15 AD.
 */
public class WearUtils {


    public static void creatNotification(
            Context ctx,
            Class<?> cls,
            String action,
            int smallIcon,
            String notifyTitle,
            String notifyText,
            String EXTRA_EVENT_ID,
            int eventId
    ) {

        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(ctx, cls);
        viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        viewIntent.putExtra("action", action);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(ctx, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(smallIcon)
                        .setContentTitle(notifyTitle)
                        .setContentText(notifyText)
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(ctx);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}
