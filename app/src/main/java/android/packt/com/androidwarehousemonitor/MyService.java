package android.packt.com.androidwarehousemonitor;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyService  extends Service {
    LogBook  logBook;

    @Override
    public void onCreate() {
        
    }

    public MyService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        logBook =  (LogBook) intent.getSerializableExtra("logBook");
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

//        while (logBook.getLongitude() != 0 && logBook.getLatitude() >= 0) {
//            logBook =  MainActivity.logBook;

            logBook.setTime(java.text.DateFormat.getDateTimeInstance().format(new Date()));

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Log");
            myRef.push().setValue(logBook);

//            Toast.makeText(this, "Telah di upload", Toast.LENGTH_SHORT).show();
//
//            SystemClock.sleep(60000); // 30 seconds
//
//        }
            }

        }, 60000, 60000);
        Toast.makeText(this, "End of Start", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}