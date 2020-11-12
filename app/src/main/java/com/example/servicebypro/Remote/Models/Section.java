package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Section implements Parcelable {

    private String sectionName;
    private ArrayList<Service> sectionItems;

    public Section(String sectionName, ArrayList<Service> sectionItems) {
        this.sectionName = sectionName;
        this.sectionItems = sectionItems;
    }

    protected Section(Parcel in) {
        sectionName = in.readString();
        sectionItems = in.createTypedArrayList(Service.CREATOR);
    }


    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setSectionItems(ArrayList<Service> sectionItems) {
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public ArrayList<Service> getSectionItems() {
        return sectionItems;
    }


    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionItems=" + sectionItems +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sectionName);
        parcel.writeTypedList(sectionItems);
    }
}