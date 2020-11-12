package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Work implements Parcelable {

    private Integer idWork;
    private Address address;
    private Service service;
    private User user;
    private String status;
    private String dueDate;
    private String firstImage;
    private String secondImage;
    private String thirdImage;
    private String title;
    private String description;
    private String type;
    private ArrayList<Application> applications = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private String paymentMethod;
    private Double maxPrice;
    private Double minPrice;

    public Work() {
    }

    public Work(Integer idWork, Address address, Service service, User user, String status,
                String dueDate, String firstImage, String secondImage, String thirdImage,
                String title, String description, String type) {
        this.idWork = idWork;
        this.address = address;
        this.service = service;
        this.user = user;
        this.status = status;
        this.dueDate = dueDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public Work(Integer idWork, Address address, Service service, User user, String status,
                String dueDate, String firstImage, String secondImage, String thirdImage,
                String title, String description, String type,
                ArrayList<Application> applications, String paymentMethod, Double maxPrice, Double minPrice) {
        this.idWork = idWork;
        this.address = address;
        this.service = service;
        this.user = user;
        this.status = status;
        this.dueDate = dueDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
        this.title = title;
        this.description = description;
        this.type = type;
        this.applications = applications;
        this.paymentMethod = paymentMethod;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public Work(Integer idWork, Address address, Service service, User user, String title, String description, String dueDate,
                String firstImage, String secondImage, String thirdImage, String status, String type, String paymentMethod,
                Double maxPrice, Double minPrice, ArrayList<Application> applications, ArrayList<Review> reviews) {
        this.idWork = idWork;
        this.address = address;
        this.service = service;
        this.user = user;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
        this.status = status;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.applications = applications;
        this.reviews = reviews;
    }



    public Work(Address address, Service service, User user, String title, String description, String dueDate,
                String firstImage, String secondImage, String thirdImage, String status, String type, String paymentMethod,
                Double maxPrice, Double minPrice) {
        this.address = address;
        this.service = service;
        this.user = user;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
        this.status = status;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public Work(Address address, Service service, User user, String status,
                String dueDate, String firstImage, String secondImage,
                String thirdImage, String title, String description, String type) {
        this.address = address;
        this.service = service;
        this.user = user;
        this.status = status;
        this.dueDate = dueDate;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
        this.title = title;
        this.description = description;
        this.type = type;
    }


    protected Work(Parcel in) {
        if (in.readByte() == 0) {
            idWork = null;
        } else {
            idWork = in.readInt();
        }
        address = in.readParcelable(Address.class.getClassLoader());
        service = in.readParcelable(Service.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        status = in.readString();
        dueDate = in.readString();
        firstImage = in.readString();
        secondImage = in.readString();
        thirdImage = in.readString();
        title = in.readString();
        description = in.readString();
        type = in.readString();
        applications = in.createTypedArrayList(Application.CREATOR);
        reviews = in.createTypedArrayList(Review.CREATOR);
        paymentMethod = in.readString();
        if (in.readByte() == 0) {
            maxPrice = null;
        } else {
            maxPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            minPrice = null;
        } else {
            minPrice = in.readDouble();
        }
    }

    public static final Creator<Work> CREATOR = new Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

    public void setIdWork(Integer idWork) {
        this.idWork = idWork;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public void setSecondImage(String secondImage) {
        this.secondImage = secondImage;
    }

    public void setThirdImage(String thirdImage) {
        this.thirdImage = thirdImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setApplications(ArrayList<Application> applications) {
        this.applications = applications;
    }

    public Integer getIdWork() {
        return idWork;
    }

    public Address getAddress() {
        return address;
    }

    public Service getService() {
        return service;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public String getSecondImage() {
        return secondImage;
    }

    public String getThirdImage() {
        return thirdImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }



    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return getService().getName();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idWork == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idWork);
        }
        parcel.writeParcelable(address, i);
        parcel.writeParcelable(service, i);
        parcel.writeParcelable(user, i);
        parcel.writeString(status);
        parcel.writeString(dueDate);
        parcel.writeString(firstImage);
        parcel.writeString(secondImage);
        parcel.writeString(thirdImage);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(type);
        parcel.writeTypedList(applications);
        parcel.writeTypedList(reviews);
        parcel.writeString(paymentMethod);
        if (maxPrice == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(maxPrice);
        }
        if (minPrice == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(minPrice);
        }
    }
}
