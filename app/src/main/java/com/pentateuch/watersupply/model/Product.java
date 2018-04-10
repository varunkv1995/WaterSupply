package com.pentateuch.watersupply.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Locale;

public class Product implements Parcelable {
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
    private String name;
    private int id;
    private String desc;
    private int drawable;
    private int quantity;
    private float price;
    private String type;
    private String date;
    private String time;
    private String key;
    private String status;


    public Product(String name, int id, String desc, int drawable, float price) {
        this.name = name;
        this.id = id;
        this.desc = desc;
        this.drawable = drawable;
        this.price = price;
        type = "Can";
        quantity = 1;

    }

    public Product(String name, int id, String desc, int drawable, float price, String type) {
        this.name = name;
        this.id = id;
        this.desc = desc;
        this.drawable = drawable;
        this.price = price;
        this.type = type;
        quantity = 1;
    }

    private Product(Parcel in) {
        name = in.readString();
        id = in.readInt();
        desc = in.readString();
        drawable = in.readInt();
        price = in.readFloat();
        type = in.readString();
        quantity = in.readInt();
        date = in.readString();
        time = in.readString();
    }

    public Product() {
    }

    public Product(String name, int id, int drawable, int quantity, float price, String type, String date, String time, String status) {
        this.name = name;
        this.id = id;
        this.drawable = drawable;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getDrawable() {
        return drawable;
    }

    public float getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeString(desc);
        dest.writeInt(drawable);
        dest.writeFloat(price);
        dest.writeString(type);
        dest.writeInt(quantity);
        dest.writeString(date);
        dest.writeString(time);
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreamentQuantity() {
        quantity--;
    }

    public int getId() {
        return id;
    }
    @Exclude
    public String getCostInRs() {
        return String.format(Locale.ENGLISH, "%.2f Rs", price);
    }

    @Exclude
    public String getTotalCostInRs(){
        return String.format(Locale.ENGLISH, "%.2f Rs", price * quantity);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
