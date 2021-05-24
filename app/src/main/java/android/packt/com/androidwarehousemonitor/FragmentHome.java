package android.packt.com.androidwarehousemonitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {
    public static String KEY_ACTIVITY = "msg_activity";

    Button btLocation, btnStartService, btnStopService;
    TextView text_latitude,text_longitude,text_negara,text_kecamatan,text_alamat, mUser;

    String logBook ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        text_latitude = (TextView) view.findViewById(R.id.text_latitude);
        text_longitude = (TextView) view.findViewById(R.id.text_longitude);
        text_negara = (TextView) view.findViewById(R.id.text_negara);
        text_kecamatan = (TextView) view.findViewById(R.id.text_kecamatan);
        text_alamat = (TextView) view.findViewById(R.id.text_alamat);
        mUser = (TextView) view.findViewById(R.id.user);

        btnStartService = (Button) view.findViewById(R.id.btnStartService);
        btnStopService = (Button) view.findViewById(R.id.btnStopService);

        try {
            String message = getArguments().getString(KEY_ACTIVITY);
            if (message != null) {
                text_alamat.setText(message);
            } else {
                text_alamat.setText("-");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return view;
    }

}
