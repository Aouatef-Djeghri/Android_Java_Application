package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private Integer idReview;
    private User user;
    private Work work;
    private String title;
    private String body;
    private int rating;
    private String reviewDate;


    public Review() {
    }

    public Review(User user, Work work, String title, String body, int rating, String reviewDate) {
        this.user = user;
        this.work = work;
        this.title = title;
        this.body = body;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    public Review(String title, String body, int rating, String reviewDate) {

        this.title = title;
        this.body = body;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    protected Review(Parcel in) {
        if (in.readByte() == 0) {
            idReview = null;
        } else {
            idReview = in.readInt();
        }
        user = in.readParcelable(User.class.getClassLoader());
        work = in.readParcelable(Work.class.getClassLoader());
        title = in.readString();
        body = in.readString();
        rating = in.readInt();
        reviewDate = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public void setIdReview(Integer idReview) {
        this.idReview = idReview;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getIdReview() {
        return idReview;
    }

    public User getUser() {
        return user;
    }

    public Work getWork() {
        return work;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idReview == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idReview);
        }
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(work, i);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeInt(rating);
        parcel.writeString(reviewDate);
    }
}
