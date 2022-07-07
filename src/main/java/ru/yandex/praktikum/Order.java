package ru.yandex.praktikum;

import java.util.List;

public class Order {
   private long id;
   private String courierId;
   private String firstName;
   private String lastName;

   public Order() {
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getCourierId() {
      return courierId;
   }

   public void setCourierId(String courierId) {
      this.courierId = courierId;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getMetroStation() {
      return metroStation;
   }

   public void setMetroStation(String metroStation) {
      this.metroStation = metroStation;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public long getRentTime() {
      return rentTime;
   }

   public void setRentTime(long rentTime) {
      this.rentTime = rentTime;
   }

   public String getDeliveryDate() {
      return deliveryDate;
   }

   public void setDeliveryDate(String deliveryDate) {
      this.deliveryDate = deliveryDate;
   }

   public long getTrack() {
      return track;
   }

   public void setTrack(long track) {
      this.track = track;
   }

   public List<String> getColor() {
      return color;
   }

   public void setColor(List<String> color) {
      this.color = color;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public String getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(String createdAt) {
      this.createdAt = createdAt;
   }

   public String getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(String updatedAt) {
      this.updatedAt = updatedAt;
   }

   public long getStatus() {
      return status;
   }

   public void setStatus(long status) {
      this.status = status;
   }

   private String address;
   private String metroStation;
   private String phone;
   private long rentTime;
   private String deliveryDate;
   private long track;
   private List<String> color;
   private String comment;
   private String createdAt;
   private String updatedAt;
   private long status;
}
