package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "products")
public class ProductEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	private Integer productId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_price")
	private Double productPrice;
	
	private String description;
	
	private String category;
	
	@Transient
	private List<String> categoryList;
	
	@Column(name="image_name")
	private String imageName;
	
	@Transient
	public String getMainImagePath() {
		if (productId == null || imageName == null)
			return "/images/productImages/unknown.jpg";
		return "/images/productImages/" + this.imageName;
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
