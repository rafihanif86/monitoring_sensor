package android.packt.com.androidwarehousemonitor;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseUser fAuth = FirebaseAuth.getInstance().getCurrentUser();

        if(fAuth != null){
            startActivity(new Intent(Home.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
