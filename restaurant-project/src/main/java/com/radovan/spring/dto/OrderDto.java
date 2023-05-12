package com.radovan.spring.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class OrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer orderId;

	private Integer customerId;

	private Integer addressId;

	private List<Integer> orderItemsIds;

	private Double price;

	private Timestamp date;
	
	private String dateStr;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public List<Integer> getOrderItemsIds() {
		return orderItemsIds;
	}

	public void setOrderItemsIds(List<Integer> orderItemsIds) {
		this.orderItemsIds = orderItemsIds;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	

}
