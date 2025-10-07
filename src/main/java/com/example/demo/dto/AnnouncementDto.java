package com.example.demo.dto;

public class AnnouncementDto {
	
	 private Long id;
	 private String message;
	 private boolean hasImage;
	 private String imageUrl;
	 private String formattedCreatedAt;
	 public Long getId() {
		 return id;
	 }
	 public void setId(Long id) {
		 this.id = id;
	 }
	 public String getMessage() {
		 return message;
	 }
	 public void setMessage(String message) {
		 this.message = message;
	 }
	 public boolean isHasImage() {
		 return hasImage;
	 }
	 public void setHasImage(boolean hasImage) {
		 this.hasImage = hasImage;
	 }
	 public String getImageUrl() {
		 return imageUrl;
	 }
	 public void setImageUrl(String imageUrl) {
		 this.imageUrl = imageUrl;
	 }
	 public String getFormattedCreatedAt() {
		 return formattedCreatedAt;
	 }
	 public void setFormattedCreatedAt(String formattedCreatedAt) {
		 this.formattedCreatedAt = formattedCreatedAt;
	 }
}
