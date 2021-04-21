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
    Timer loopingTimer;

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

        loopingTimer = new Timer();
        loopingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logBook.setTime(java.text.DateFormat.getDateTimeInstance().format(new Date()));

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Log");
                myRef.push().setValue(logBook);
            }
        }, 0, 60000);

        Toast.makeText(this, "End of Start", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        loopingTimer.cancel();
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}