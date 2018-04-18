package com.pentateuch.watersupply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductItem implements Parcelable {
    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };
    private int id;
    private String desc;
    private String imageUrl;
    private String name;
    private float price;
    private String type;

    public ProductItem() {

    }

    private ProductItem(Parcel in) {
        id = in.readInt();
        desc = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        price = in.readFloat();
        type = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(desc);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeString(type);
    }
}
