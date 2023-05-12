package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;



public class CartDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer cartId;

	private Integer customerId;

	
	private List<Integer> cartItemIds;

	private Double cartPrice;

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<Integer> getCartItemIds() {
		return cartItemIds;
	}

	public void setCartItemIds(List<Integer> cartItemIds) {
		this.cartItemIds = cartItemIds;
	}

	public Double getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(Double cartPrice) {
		this.cartPrice = cartPrice;
	}
	
	

}
