package com.radovan.spring.service;

import com.radovan.spring.dto.CartDto;

public interface CartService {

	CartDto getCartByCartId(Integer cartId);

	Double calculateCartPrice(Integer cartId);

	void refreshCartState(Integer cartId);

	CartDto validateCart(Integer cartId);

	void deleteCartItem(Integer itemId, Integer cartId);

	void eraseAllCartItems(Integer cartId);
	
	void deleteCart(Integer cartId);
}
