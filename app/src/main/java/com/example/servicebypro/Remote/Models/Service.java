package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Service  implements Parcelable{


    private Integer idService;
    private Categorie categorie;
    private String name;
    private boolean isSelected;

    public Service() {
    }

    public Service(Integer idService, Categorie categorie, String name) {
        this.idService = idService;
        this.categorie = categorie;
        this.name = name;
    }

    public Service(Categorie categorie, String name) {
        this.categorie = categorie;
        this.name = name;
    }

    public Service(Integer idService, String name) {
        this.idService = idService;
        this.name = name;
    }

    public Service(Integer idService, Categorie categorie, String name, boolean isSelected) {
        this.idService = idService;
        this.categorie = categorie;
        this.name = name;
        this.isSelected = isSelected;
    }

    protected Service(Parcel in) {
        if (in.readByte() == 0) {
            idService = null;
        } else {
            idService = in.readInt();
        }
        categorie = in.readParcelable(Categorie.class.getClassLoader());
        name = in.readString();
    }


    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public Integer getIdService() {
        return idService;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public String getName() {
        return name;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idService == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idService);
        }
        parcel.writeParcelable(categorie, i);
        parcel.writeString(name);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
