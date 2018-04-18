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
    private int quantity;
    private String date;
    private String time;
    private String dest;
    private Status status;
    private ProductItem item;
    private String key;

    private Product(Parcel in) {
        quantity = in.readInt();
        date = in.readString();
        time = in.readString();
        key = in.readString();
        item = in.readParcelable(ProductItem.class.getClassLoader());
    }

    public Product(ProductItem item) {
        this.item = item;
        quantity = 1;
    }

    public Product() {
        item = new ProductItem();
        quantity = 1;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Exclude
    public ProductItem getItem() {
        return item;
    }

    public void setItem(ProductItem item) {
        this.item = item;
    }

    public int getId() {
        return item.getId();
    }

    public void setId(int id) {
        item.setId(id);
    }

    @Exclude
    public String getName() {
        return item.getName();
    }

    public float getPrice() {
        return item.getPrice();
    }

    public void setPrice(float price) {
        item.setPrice(price);
    }

    public String getImageUrl() {
        return item.getImageUrl();

    }

    public void setImageUrl(String url) {
        item.setImageUrl(url);
    }

    @Exclude
    public String getTotalCostInRs() {
        return String.format(Locale.ENGLISH, "%.2f Rs", quantity * item.getPrice());
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }

    @Exclude
    public String getCostInRs() {
        return String.format(Locale.ENGLISH, "%.2f Rs", item.getPrice());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(key);
        dest.writeParcelable(item, flags);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
