package com.geekandroid.sdk.sample.maplibrary;

/**
 * date        :  2016-02-16  13:38
 * author      :  Mickaecle gizthon
 * description :  通用定位类,通用于百度地图，高德地图，等等
 */
public class Location {
    private String time;
    private int errorCode;
    private double latitude;
    private double lontitude;
    private float radius;
    private String countryCode;
    private String country;
    private String province;

    private String cityCode;
    private String city;
    private String street;
    private String address;
    private String describe;
    private float direction;
    private float speed;
    private int satellite;
    private double height;
    private String district;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLontitude() {
        return lontitude;
    }

    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getSatellite() {
        return satellite;
    }

    public void setSatellite(int satellite) {
        this.satellite = satellite;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Location{" +
                "time='" + time + '\'' +
                ", errorCode=" + errorCode +
                ", latitude=" + latitude +
                ", lontitude=" + lontitude +
                ", radius=" + radius +
                ", countryCode='" + countryCode + '\'' +
                ", country='" + country + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", address='" + address + '\'' +
                ", describe='" + describe + '\'' +
                ", direction=" + direction +
                ", speed=" + speed +
                ", satellite=" + satellite +
                ", height=" + height +
                ", district='" + district + '\'' +
                '}';
    }
}
