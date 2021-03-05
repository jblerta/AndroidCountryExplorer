package com.example.finalandroidassignment;

public class CityModel {

    private String cityImage,img1,img2;
    private String cityName,cityInfo,cityId,firstFragment,secondFragment,longitude,latitude;

    public CityModel(){

    }


    public CityModel(String cityId,String cityImage, String img1, String img2, String cityName, String cityInfo,  String firstFragment, String secondFragment, String longitude, String latitude) {
        this.cityImage = cityImage;
        this.img1 = img1;
        this.img2 = img2;
        this.cityName = cityName;
        this.cityInfo = cityInfo;
        this.cityId = cityId;
        this.firstFragment = firstFragment;
        this.secondFragment = secondFragment;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public CityModel(String cityId,String cityImage, String img1, String img2, String cityName, String cityInfo, String firstFragment, String secondFragment) {
        this.cityImage = cityImage;
        this.img1 = img1;
        this.img2 = img2;
        this.cityName = cityName;
        this.cityInfo = cityInfo;
        this.cityId = cityId;
        this.firstFragment = firstFragment;
        this.secondFragment = secondFragment;
    }

    public CityModel(String cityId, String cityName, String cityImage) {
        this.cityImage = cityImage;
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getFirstFragment() {
        return firstFragment;
    }

    public void setFirstFragment(String firstFragment) {
        this.firstFragment = firstFragment;
    }

    public String getSecondFragment() {
        return secondFragment;
    }

    public void setSecondFragment(String secondFragment) {
        this.secondFragment = secondFragment;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityImage() {
        return cityImage;
    }

    public void setCityImage(String cityImage) {
        this.cityImage = cityImage;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(String cityInfo) {
        this.cityInfo = cityInfo;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
