package com.example.deliveryfood.Service;

import android.app.NotificationChannel;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;   import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.LinearLayout;

import com.example.deliveryfood.Common.Common;
import com.example.deliveryfood.Model.Request;
import com.example.deliveryfood.OrderActivity;
import com.example.deliveryfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener {
    private FirebaseDatabase db;
    private DatabaseReference requests;
    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Request request=dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(),request);
    }

    private void showNotification(String key, Request request) {
//        Intent intent=new Intent(getBaseContext(), OrderActivity.class);
//        intent.putExtra("userPhone",request.getPhone());
//        PendingIntent conntentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
//        builder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setTicker("PDHDev")
//                .setContentInfo("Your order was uploaded")
//                .setContentText("Order #"+key+"was update status to "+ Common.changeCodeToStatus(request.getStatus()))
//                .setContentIntent(conntentIntent)
//                .setContentInfo("Info")
//                .setSmallIcon(R.mipmap.ic_launcher);
//        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1,builder.build());
        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "my_channel_01";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }
        Intent resultIntent = new Intent(getBaseContext(), OrderActivity.class);
        resultIntent.putExtra("userPhone",request.getPhone());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getBaseContext(),0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("PDHDev")
                .setContentInfo("Your order was uploaded")
                .setContentText("Order #"+key+"was update status to "+ Common.changeCodeToStatus(request.getStatus()))
                .setContentIntent(resultPendingIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);
       // builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}



