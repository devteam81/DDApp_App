package com.dd.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harshit on 16/10/17.
 */

public class UserSubscription implements Serializable {

    private int id;
    private String name;
    private SubscriptionPlan consumerSubscription;
    private List<PayPerSubscription> videoPaySubscriptionList;
    private List<PayPerSubscription> seasonsPaySubscriptionList;
    private String status;

    public UserSubscription() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubscriptionPlan getConsumerSubscription() {
        return consumerSubscription;
    }

    public void setConsumerSubscription(SubscriptionPlan consumerSubscription) {
        this.consumerSubscription = consumerSubscription;
    }

    public List<PayPerSubscription> getVideoPaySubscriptionList() {
        return videoPaySubscriptionList;
    }

    public void setVideoPaySubscriptionList(List<PayPerSubscription> videoPaySubscriptionList) {
        this.videoPaySubscriptionList = videoPaySubscriptionList;
    }

    public List<PayPerSubscription> getSeasonsPaySubscriptionList() {
        return seasonsPaySubscriptionList;
    }

    public void setSeasonsPaySubscriptionList(List<PayPerSubscription> seasonsPaySubscriptionList) {
        this.seasonsPaySubscriptionList = seasonsPaySubscriptionList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserSubscription{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", consumerSubscription=" + consumerSubscription +
                ", videoPaySubscriptionList=" + videoPaySubscriptionList +
                ", seasonsPaySubscriptionList=" + seasonsPaySubscriptionList +
                ", status='" + status + '\'' +
                '}';
    }
}
