package com.pentateuch.watersupply.model;

public interface IProductItem {
    int getId();

    void setId(int id);

    String getImageUrl();

    void setImageUrl(String imageUrl);

    String getName();

    void setName(String name);

    float getPrice();

    void setPrice(float price);

    String getDesc();

    void setDesc(String desc);

    String getType();

    void setType(String type);
}
