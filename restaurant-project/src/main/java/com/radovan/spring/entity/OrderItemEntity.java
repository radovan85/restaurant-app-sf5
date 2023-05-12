package com.radovan.spring.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "item_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer orderItemId;

	private Double price;

	private Integer quantity;

	@Column(name = "hotness_level")
	private String hotnessLevel;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_price")
	private Double productPrice;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderEntity order;

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
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

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
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

}
