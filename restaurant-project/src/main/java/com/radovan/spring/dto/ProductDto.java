package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer productId;

	private String productName;

	private Double productPrice;

	private String description;

	private String category;

	private List<String> categoryList;

	private String imageName;

	public String getMainImagePath() {
		if (productId == null || imageName == null)
			return "/images/productImages/unknown.jpg";
		return "/images/productImages/" + this.imageName;
	}
	
	public ProductDto() {
		super();
		categoryList = new ArrayList<String>();
		categoryList.add("Breakfast");
		categoryList.add("Lunch");
		categoryList.add("Snack");
		categoryList.add("Dinner");
		categoryList.add("Drinks");
		
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

}
