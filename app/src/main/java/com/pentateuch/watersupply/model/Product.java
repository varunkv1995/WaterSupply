package com.pentateuch.watersupply.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Product implements Parcelable {
    private String name;
    private String desc;
    private int drawable;
    private float price;
    private String type;

    public Product(String name, String desc, int drawable, float price) {
        this.name = name;
        this.desc = desc;
        this.drawable = drawable;
        this.price = price;
        type = "Can";
    }

    public Product(String name, String desc, int drawable, float price, String type) {
        this.name = name;
        this.desc = desc;
        this.drawable = drawable;
        this.price = price;
        this.type = type;
    }

    private Product(Parcel in) {
        name = in.readString();
        desc = in.readString();
        drawable = in.readInt();
        price = in.readFloat();
        type = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeInt(drawable);
        dest.writeFloat(price);
        dest.writeString(type);
    }
}
