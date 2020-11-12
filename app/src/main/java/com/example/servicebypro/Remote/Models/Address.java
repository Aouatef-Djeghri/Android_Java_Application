package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private Integer idAddress;
    private float latitude;
    private float longitude;
    private String wilaya;
    private String commune;



    public Address(float latitude, float longitude,  String wilaya, String commune) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.wilaya = wilaya;
        this.commune = commune;
    }

    public Address(String wilaya, String commune) {

        this.wilaya = wilaya;
        this.commune = commune;
    }

    public Address() {
    }

    protected Address(Parcel in) {
        if (in.readByte() == 0) {
            idAddress = null;
        } else {
            idAddress = in.readInt();
        }
        latitude = in.readFloat();
        longitude = in.readFloat();
        wilaya = in.readString();
        commune = in.readString();
    }


    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Integer getIdAddress() {
        return idAddress;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getWilaya() {
        return wilaya;
    }

    public String getCommune() {
        return commune;
    }

    public void setIdAddress(Integer idAddress) {
        this.idAddress = idAddress;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idAddress == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idAddress);
        }
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
        parcel.writeString(wilaya);
        parcel.writeString(commune);
    }

    @Override
    public String toString() {
        return "Address{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", wilaya='" + wilaya + '\'' +
                ", commune='" + commune + '\'' +
                '}';
    }
}
