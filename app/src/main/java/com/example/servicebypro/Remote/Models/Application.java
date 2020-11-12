package com.example.servicebypro.Remote.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Application implements Parcelable {

    private Integer idApplication;
    private User user;
    private Work work;
    private String status;

    public Application() {
    }

    public Application(User user, Work work, String status) {
        this.user = user;
        this.work = work;
        this.status = status;
    }

    public Application(Integer idApplication, User user, String status) {
        this.idApplication = idApplication;
        this.user = user;
        // this.work = work;
        this.status = status;
    }

    public Application(User user, String status) {
        this.user = user;
        //this.work = work;
        this.status = status;
    }


    protected Application(Parcel in) {
        if (in.readByte() == 0) {
            idApplication = null;
        } else {
            idApplication = in.readInt();
        }
        user = in.readParcelable(User.class.getClassLoader());
        work = in.readParcelable(Work.class.getClassLoader());
        status = in.readString();
    }

    public static final Creator<Application> CREATOR = new Creator<Application>() {
        @Override
        public Application createFromParcel(Parcel in) {
            return new Application(in);
        }

        @Override
        public Application[] newArray(int size) {
            return new Application[size];
        }
    };

    public Integer getIdApplication() {
        return idApplication;
    }

    public void setIdApplication(Integer idApplication) {
        this.idApplication = idApplication;
    }

    public User getUser() {
        return user;
    }

    //public Work getWork() {
    //   return work;
    //}

    public String getStatus() {
        return status;
    }


    public void setUser(User user) {
        this.user = user;
    }

    //public void setWork(Work work) {
    //     this.work = work;
    // }

    public void setStatus(String status) {
        this.status = status;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idApplication == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(idApplication);
        }
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(work, i);
        parcel.writeString(status);
    }
}
