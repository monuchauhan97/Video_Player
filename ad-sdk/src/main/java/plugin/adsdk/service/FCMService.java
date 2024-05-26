//package plugin.adsdk.service;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import plugin.adsdk.R;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import org.jetbrains.annotations.NotNull;
//
//public class FCMService extends FirebaseMessagingService {
//    private static final int NOTIFICATION_REQUEST_CODE = 1007;
//
//    @Override
//    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        if (notification != null) {
//            sendNotification(notification);
//        }
//    }
//
//    private void sendNotification(RemoteMessage.Notification notification) {
//        String packageName = getApplication().getPackageName();
//        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        int flags = PendingIntent.FLAG_ONE_SHOT;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            flags = PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE;
//        }
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, flags);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ad_ic_notification)
//                .setContentTitle(notification.getTitle())
//                .setContentText(notification.getBody())
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//
//    @Override
//    public void onNewToken(@NonNull @NotNull String s) {
//        super.onNewToken(s);
//    }
//}
