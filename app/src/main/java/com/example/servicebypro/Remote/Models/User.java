package com.example.servicebypro.Remote.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    private Integer idUser;
    private Address address;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private String password;
    private String avatar;
    private String joinDate;
    byte accountActivation;
    private String aboutMe;
    private String identityImage;
    private int isActive;
    private double distance = 0;

    private ArrayList<Service> services = new ArrayList<>(0);



    public User(Address address, String firstName, String lastName,
                String phone, String email, String role, String password, String identityImage) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.password = password;
        this.identityImage = identityImage;
    }

    public User() {
    }

    public User(Integer idUser, Address address, String firstName, String lastName,
                String phone, String email, String role, String password, String avatar,
                String joinDate, byte accountActivation, String aboutMe, String identityImage,
                int isActive, ArrayList<Service> services) {
        this.idUser = idUser;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.password = password;
        this.avatar = avatar;
        this.joinDate = joinDate;
        this.accountActivation = accountActivation;
        this.aboutMe = aboutMe;
        this.identityImage = identityImage;
        this.isActive = isActive;
        this.services = services;
    }

    public User(Integer idUser, Address address, String firstName, String lastName,
                String phone, String email, String role, String password, String avatar,
                String joinDate, byte accountActivation, String aboutMe, String identityImage,
                int isActive) {
        this.idUser = idUser;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.password = password;
        this.avatar = avatar;
        this.joinDate = joinDate;
        this.accountActivation = accountActivation;
        this.aboutMe = aboutMe;
        this.identityImage = identityImage;
        this.isActive = isActive;
    }

    public User(Integer idUser, Address address, String phone, String email, String aboutMe) {
        this.idUser = idUser;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.aboutMe = aboutMe;
    }


    protected User(Parcel in) {
        if (in.readByte() == 0) {
            idUser = null;
        } else {
            idUser = in.readInt();
        }
        address = in.readParcelable(Address.class.getClassLoader());
        firstName = in.readString();
        lastName = in.readString();
        phone = in.readString();
        email = in.readString();
        role = in.readString();
        password = in.readString();
        avatar = in.readString();
        joinDate = in.readString();
        accountActivation = in.readByte();
        aboutMe = in.readString();
        identityImage = in.readString();
        isActive = in.readInt();
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Address getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public byte getAccountActivation() {
        return accountActivation;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getIdentityImage() {
        return identityImage;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public void setAccountActivation(byte accountActivation) {
        this.accountActivation = accountActivation;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setIdentityImage(String identityImage) {
        this.identityImage = identityImage;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idUser == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idUser);
        }
        parcel.writeParcelable(address, i);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(role);
        parcel.writeString(password);
        parcel.writeString(avatar);
        parcel.writeString(joinDate);
        parcel.writeByte(accountActivation);
        parcel.writeString(aboutMe);
        parcel.writeString(identityImage);
        parcel.writeInt(isActive);
        parcel.writeTypedList(services);
    }


}
