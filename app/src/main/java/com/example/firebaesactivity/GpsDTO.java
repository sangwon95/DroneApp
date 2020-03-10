package com.example.firebaesactivity;

public class GpsDTO {
    private double latitude;// 위도
    private double longitude ;//경도


    public GpsDTO() {}
    public GpsDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getlatitude() {
        return latitude;
    }

    public double getlongitude() {
        return longitude;
    }
}
