package android.packt.com.androidwarehousemonitor;

public class LogBook {
    private String time;
    private double humidity;
    private double temperature;
    private double latitude;
    private double longitude;
//    private String idDevice;

    public LogBook (String time, double humidity, double temperature, double latitude, double longitude){
        this.time = time;
        this.humidity = humidity;
        this.temperature = temperature;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LogBook (){

    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public void setIdDevice(String idDevice) {
//        this.idDevice = idDevice;
//    }

    public String getTime() {
        return time;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTemperature() {
        return temperature;
    }

//    public String getIdDevice() {
//        return idDevice;
//    }
}
