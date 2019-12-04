package com.example.utome;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    public String uId;

    public String deliverer_uId;

    public int raiting;

    public int status;

    public int id;

    public float distance;

    public String category;

    public String name;

    public String address;

    public String info;

    public int price;

    public int pay;

    public String date;

    Post() {

    }
    Post(Post post) {
        this.name = post.name;
        this.date = post.date;
        this.category = post.category;
        this.distance = post.distance;
        this.address = post.address;
        this.info = post.info;
        this.price = post.price;
        this.pay = post.pay;
    }

    protected Post(Parcel in) {
        name = in.readString();
        date = in.readString();
        category = in.readString();
        distance = in.readFloat();
        address = in.readString();
        info = in.readString();
        price = in.readInt();
        pay = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeFloat(distance);
        dest.writeString(address);
        dest.writeString(info);
        dest.writeInt(price);
        dest.writeInt(pay);
        dest.writeString(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
