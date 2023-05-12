package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cart_items")
public class CartItemEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "item_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer cartItemId;
	
	private Double price;
	
	private Integer quantity;
	
	@Column(name = "hotness_level")
	private String hotnessLevel;
	
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private CartEntity cart;
	
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private ProductEntity product;
	
	@Transient
	private List<String> hotnessLevelList;

	public Integer getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	

	public String getHotnessLevel() {
		return hotnessLevel;
	}

	public void setHotnessLevel(String hotnessLevel) {
		this.hotnessLevel = hotnessLevel;
	}

	public CartEntity getCart() {
		return cart;
	}

	public void setCart(CartEntity cart) {
		this.cart = cart;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public List<String> getHotnessLevelList() {
		return hotnessLevelList;
	}

	public void setHotnessLevelList(List<String> hotnessLevelList) {
		this.hotnessLevelList = hotnessLevelList;
	}

	

	
	
	

}
