package com.example.caiguru.commonScreens.notificationHelper;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.example.caiguru.R;
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardBuyerActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import static kotlin.random.RandomKt.Random;

public class NotificationHelper {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private ArrayList myLocalArray= new ArrayList<NotificationModel>();

    NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public String getLocal(Context context) {
        return Locale.getDefault().getLanguage();
    }

    void createNotification(Map<String, String> messageBody) {
        /**Creates an explicit intent for an Activity in your app**/
        String title = "";
        String source_id = messageBody.get("source_id");
        String list_type = messageBody.get("list_type");
        String source = messageBody.get("source");
        String name = messageBody.get("name");
        String image = messageBody.get("image");
        String level = messageBody.get("level");
        String created_at = messageBody.get("created_at");
        String listingname = messageBody.get("listingname");
        String reputation = messageBody.get("reputation");
        String notification_id = messageBody.get("notification_id");
        String sender_id = messageBody.get("sender_id");
        title = messageBody.get("message_" + getLocal(mContext));
        Intent resultIntent = null;
        try {
            assert source != null;
//            NotificationModel model=new NotificationModel();
//            model.setSource_id(source_id);
//            model.setList_type(list_type);
//            model.setSource(source);
//            model.setName(name);
//            model.setImage(image);
//            model.setLevel(level);
//            model.setListingname(listingname);
//            model.setReputation(reputation);
//            model.setCreated_at(created_at);
//            model.setNotification_id(notification_id);
//            model.setSender_id(sender_id);
//            myLocalArray.add(model);
//            for (int counter = 0; counter < myLocalArray.size(); counter++) {
//                resultIntent = new Intent(mContext, DashBoardBuyerActivity.class);
//                resultIntent.putExtra("source_id", source_id);
//                resultIntent.putExtra("list_type", list_type);
//                resultIntent.putExtra("source", source);
//                resultIntent.putExtra("name", name);
//                resultIntent.putExtra("image", image);
//                resultIntent.putExtra("level", level);
//                resultIntent.putExtra("listingname", listingname);
//                resultIntent.putExtra("reputation", reputation);
//                resultIntent.putExtra("created_at", created_at);
//                resultIntent.putExtra("notification_id", notification_id);
//                resultIntent.putExtra("sender_id", sender_id);
//                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                break;
//            }
//
//            Intent notificationIntent = new Intent(mContext, DashBoardBuyerActivity.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
//            // Adds the back stack
//            stackBuilder.addParentStack(DashBoardBuyerActivity.class);
//            // Adds the Intent to the top of the stack
//            stackBuilder.addNextIntent(notificationIntent);
            // Gets a PendingIntent containing the entire back stack


            resultIntent = new Intent(mContext, DashBoardBuyerActivity.class);
            resultIntent.putExtra("source_id", source_id);
            resultIntent.putExtra("list_type", list_type);
            resultIntent.putExtra("source", source);
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("image", image);
            resultIntent.putExtra("level", level);
            resultIntent.putExtra("listingname", listingname);
            resultIntent.putExtra("reputation", reputation);
            resultIntent.putExtra("created_at", created_at);
            resultIntent.putExtra("notification_id", notification_id);
            resultIntent.putExtra("sender_id", sender_id);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert resultIntent != null;
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder = new NotificationCompat.Builder(mContext);
        //mBuilder.setSmallIcon(R.mipmap.push_logo);
        mBuilder.setSmallIcon(R.mipmap.push_logo_grey);
        //   mBuilder.setColor(mContext.getResources().getColor(R.color.mpsdk_error_red_pink));
        mBuilder
                .setContentText(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(title))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
                .setOngoing(false)//set false  for cancel the notification by swipe
                .setAutoCancel(true);
//                .build().flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.app_logo);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //  mBuilder.setSmallIcon(R.mipmap.push_logo);

            //mBuilder.setColor(mContext.getResources().getColor(R.color.purple));
            //  mBuilder.setSmallIcon(R.mipmap.push_logo_grey);
            mBuilder.setLargeIcon(icon);
            mBuilder.setColorized(true);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        } else {
//            mBuilder.setColor(mContext.getResources().getColor(R.color.black));
//            mBuilder.setSmallIcon(R.mipmap.push_logo);
            mBuilder.setLargeIcon(icon);
            mBuilder.setColorized(true);
        }
        assert mNotificationManager != null;


        int  id= Random(System.currentTimeMillis()).nextInt(1000);
        mNotificationManager.notify(id, mBuilder.build());
        //  NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //  mNotificationManager.cancel(id);
        // mNotificationManager.cancelAll();
    }

}

