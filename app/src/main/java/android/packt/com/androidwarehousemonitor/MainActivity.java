package android.packt.com.androidwarehousemonitor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;


//batas program baru
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    //@Override
    //protected void onCreate(Bundle savedInstanceState){
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_main);


    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_LOCATION = 1;
    private static String TAG = "Deteksi Suhu Dan Kelembaban";
    private static String NAME_TAG = "SensorTag";

    //Service UUIDs
    private static final UUID UUID_IR_TEMPERATURE_SERVICE = UUID.fromString("f000aa00-0451-4000-b000-000000000000");
    private static final UUID UUID_HUMIDITY_SERVICE = UUID.fromString("f000aa20-0451-4000-b000-000000000000");

    //Characteristic UUIDs

    private static final UUID UUID_CHARACTERISTIC_TEMPERATURE_DATA = UUID.fromString("f000aa01-0451-4000-b000-000000000000");
    private static final UUID UUID_CHARACTERISTIC_TEMPERATURE_CONFIG = UUID.fromString("f000aa02-0451-4000-b000-000000000000");
    private static final UUID UUID_CHARACTERISTIC_HUMIDITY_DATA = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
    private static final UUID UUID_CHARACTERISTIC_HUMIDITY_CONFIG = UUID.fromString("f000aa22-0451-4000-b000-000000000000");

    //Descriptor
    private static final UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;

    private boolean connected = false;
    private boolean enableHumidityFetch = false;

    //initialize variable
    Button btLocation;
    TextView textView5,textView6,textView7,textView8,textView9;
    FusedLocationProviderClient fusedLocationProviderClient;

    //maps config
    private GoogleMap mMap;

    private DatabaseReference databaseReference;
    private LocationListener locationListener;

    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private double latitudeNow;
    private double longitudeNow;
    private String markerTittleNow;

    private EditText editTextLatitude;
    private EditText editTextLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // add text field
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btLocation = findViewById(R.id.bt_location);
        textView5 = findViewById(R.id.text_view5);
        textView6 = findViewById(R.id.text_view6);
        textView7 = findViewById(R.id.text_view7);
        textView8 = findViewById(R.id.text_view8);
        textView9 = findViewById(R.id.text_view9);

        //program baru
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);


        databaseReference=FirebaseDatabase.getInstance().getReference("Location");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    String databaseLatitudeString = dataSnapshot.child("latitude").getValue().toString().substring(1, dataSnapshot.child("latitude").getValue().toString().length()-1);
                    String databaseLongitudeString = dataSnapshot.child("Longitude").getValue().toString().substring(1, dataSnapshot.child("Longitude").getValue().toString().length()-1);

//                    fungsi regex

                    String[] stringLat = databaseLatitudeString.split(",");
                    Arrays.sort(stringLat);
                    String latitude = stringLat[stringLat.length-1].split("=")[1];

                    String[] stringLong = databaseLongitudeString.split(",");
                    Arrays.sort(stringLong);
                    String longitude = stringLong[stringLong.length-1].split("=")[1];

//                    atur picker

                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                    mMap.addMarker(new MarkerOptions().position(latLng).title(latitude + " , " + longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    //batas program baru

        //initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //when permission granted
                    getLocation();

                }else{
                    //when permission denied
                    ActivityCompat.requestPermissions(MainActivity.this,
                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //initialize location
                final Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());


                        //initialize address
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        latitudeNow = addresses.get(0).getLatitude();
                        longitudeNow = addresses.get(0).getLongitude();
                        markerTittleNow = addresses.get(0).getLocality() + ", " +  addresses.get(0).getCountryName();

                        //set latitude on text view
                        textView5.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Latitude :</b><br></font>"
                                        + latitudeNow
                        ));
                        //set longitude on text view
                        textView6.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Longitude :</b><br></font>"
                                        + longitudeNow
                        ));
                        //set country name
                        textView7.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Country Name :</b><br></font>"
                                        + addresses.get(0).getCountryName()
                        ));
                        //set locality
                        textView8.setText(addresses.get(0).getLocality());
                        //set address
                        Log.d("Lokasi", addresses.get(0).getLocality());
                        textView9.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address :</b><br></font>"
                                        + addresses.get(0).getAddressLine(0)
                        ));

                        LatLng now = new LatLng(latitudeNow, longitudeNow);
                        mMap.addMarker(new MarkerOptions().position(now).title(markerTittleNow));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(now));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // override marker position
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//         Add a marker in Sydney and move the camera
//        LatLng now = new LatLng(latitudeNow, longitudeNow);
//        mMap.addMarker(new MarkerOptions().position(now).title(markerTittleNow));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(now));


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        try {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);

        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothManager btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager != null ? btManager.getAdapter() : null;
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "No Bluetooth Support found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        checkPermissions(btAdapter);
    }

    private void checkPermissions(BluetoothAdapter bluetoothAdapter) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }
        ensureLocationPermissionIsEnabled();
    }

    //Location permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission Granted");
                    startScanning();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Location Permission Not granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
            default:
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == -1) {
            ensureLocationPermissionIsEnabled();
            return;
        }
        Toast.makeText(this, "Bluetooth not turned on", Toast.LENGTH_LONG).show();
        finish();
    }

    //TODO: Update the UI here
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    private void ensureLocationPermissionIsEnabled() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        startScanning();
    }

    protected void startScanning() {
        bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        AsyncTask.execute(new Runnable() { @Override public void run() { bluetoothLeScanner.startScan(leScanCallback); } });
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getDevice() != null) {
                if (result.getDevice().getName() != null && result.getDevice().getName().contains(NAME_TAG)) {
                    Log.i(TAG, result.getDevice().getName());
                    if (connected == false) {
                        connected = true;
                        bluetoothLeScanner.stopScan(leScanCallback);
                        result.getDevice().connectGatt(MainActivity.this, true, gattCallback);
                    }
                }
            }
        }
    };

    protected BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.i(TAG, "onConnectionStateChange() - STATE_CONNECTED");
                gatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                Log.i(TAG, "onConnectionStateChange() - STATE_DISCONNECTED");
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            gatt.readCharacteristic(gatt.getService(UUID_IR_TEMPERATURE_SERVICE).getCharacteristic(UUID_CHARACTERISTIC_TEMPERATURE_DATA));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if(characteristic.getUuid().equals(UUID_CHARACTERISTIC_TEMPERATURE_DATA) || characteristic.getUuid().equals(UUID_CHARACTERISTIC_HUMIDITY_DATA)) {
                //Enable local notifications
                gatt.setCharacteristicNotification(characteristic, true);
                //Enabled remote notifications
                BluetoothGattDescriptor desc = characteristic.getDescriptor(CONFIG_DESCRIPTOR);
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(desc);
            }
            else if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_TEMPERATURE_CONFIG) || characteristic.getUuid().equals(UUID_CHARACTERISTIC_HUMIDITY_CONFIG)) {
                characteristic.setValue(new byte[] {0x01});
                gatt.writeCharacteristic(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_TEMPERATURE_DATA)) {
                final double ambient = Utilities.extractAmbientTemperature(characteristic);

                // Upload to Firebase Backend
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Temperature");
                myRef.push().setValue(ambient);

                //Update the UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.textView)).setText("Temperature: "+ambient+"\u00b0"+"C");
                    }
                });
            }else if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_HUMIDITY_DATA)) {
                final double humidity = Utilities.extractHumidity(characteristic);
                final String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());


                // Upload to Firebase Backend
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Humidity");
                DatabaseReference myRef2 = database.getReference("Waktu");
                myRef.push().setValue(humidity);
                myRef2.push().setValue(currentTime);
                //TimeStamp.setValue(ServerValue.TIMESTAMP);

                runOnUiThread(new Runnable() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.textView2)).setText(String.format("Humidity: "+"%.0f%%", humidity));
                        ((TextView)findViewById(R.id.textView3)).setText(currentTime);
                    }
                });
            }

            if(enableHumidityFetch == false) {
                enableHumidityFetch = true;
                gatt.readCharacteristic(gatt.getService(UUID_HUMIDITY_SERVICE).getCharacteristic(UUID_CHARACTERISTIC_HUMIDITY_DATA));
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            enableConfigurationForCharacteristic(gatt, descriptor.getCharacteristic());
        }

        private void enableConfigurationForCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(UUID_CHARACTERISTIC_TEMPERATURE_DATA)) {
                gatt.readCharacteristic(gatt.getService(UUID_IR_TEMPERATURE_SERVICE).getCharacteristic(UUID_CHARACTERISTIC_TEMPERATURE_CONFIG));
            } else if(characteristic.getUuid().equals(UUID_CHARACTERISTIC_HUMIDITY_DATA)) {
                gatt.readCharacteristic(gatt.getService(UUID_HUMIDITY_SERVICE).getCharacteristic(UUID_CHARACTERISTIC_HUMIDITY_CONFIG));
            }
        }
    };
}
