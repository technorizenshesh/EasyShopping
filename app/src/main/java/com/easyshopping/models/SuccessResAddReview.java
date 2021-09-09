package com.easyshopping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SuccessResAddReview implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("reviews_date")
    @Expose
    public String reviewsDate;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    @SerializedName("result")
    @Expose
    public String result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewsDate() {
        return reviewsDate;
    }

    public void setReviewsDate(String reviewsDate) {
        this.reviewsDate = reviewsDate;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}