package android.packt.com.androidwarehousemonitor;

import java.io.Serializable;

public class LogBook implements Serializable {
    private String time, user;
    private double humidity, temperature, latitude, longitude;

    public LogBook (String user, String time, double humidity, double temperature, double latitude, double longitude){
        this.time = time;
        this.user = user;
        this.humidity = humidity;
        this.temperature = temperature;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LogBook (){
        this.time = "";
        this.user = "";
        this.humidity = 0;
        this.temperature = 0;
        this.latitude = 0;
        this.longitude = 0;
    }

    public void setUser(String user) { this.user = user; }

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

    public String getUser() { return user; }

}
