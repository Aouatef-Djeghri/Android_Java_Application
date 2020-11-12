package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Categorie  implements Parcelable{


    private Integer idCategorie;
    private String name;
    private String image;

    public Categorie() {
    }

    public Categorie(String name, String image) {
        this.name = name;
        this.image = image;
    }

    protected Categorie(Parcel in) {
        if (in.readByte() == 0) {
            idCategorie = null;
        } else {
            idCategorie = in.readInt();
        }
        name = in.readString();
        image = in.readString();
    }


    public static final Creator<Categorie> CREATOR = new Creator<Categorie>() {
        @Override
        public Categorie createFromParcel(Parcel in) {
            return new Categorie(in);
        }

        @Override
        public Categorie[] newArray(int size) {
            return new Categorie[size];
        }
    };

    public Integer getIdCategorie() {
        return idCategorie;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setIdCategorie(Integer idCategorie) {
        this.idCategorie = idCategorie;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idCategorie == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idCategorie);
        }
        parcel.writeString(name);
        parcel.writeString(image);
    }
}
