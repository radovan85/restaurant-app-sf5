package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private TempConverter tempConverter;

	@Override
	public CartDto getCartByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		CartDto returnValue = null;
		Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
		if(cartEntity.isPresent()) {
			returnValue = tempConverter.cartEntityToDto(cartEntity.get());
		}
		return returnValue;
	}

	@Override
	public Double calculateCartPrice(Integer cartId) {
		// TODO Auto-generated method stub
		Double returnValue = 0d;
		Optional<Double> cartPrice = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId));
		if(cartPrice.isPresent()) {
			returnValue = cartPrice.get();
		}
		return returnValue;
	}

	@Override
	public void refreshCartState(Integer cartId) {
		// TODO Auto-generated method stub
		CartEntity cartEntity = cartRepository.getById(cartId);
		Optional<Double> price = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId));
		if (price.isPresent()) {
			cartEntity.setCartPrice(price.get());
		} else {
			cartEntity.setCartPrice(0d);
		}
		cartRepository.saveAndFlush(cartEntity);
		
	}

	@Override
	public CartDto validateCart(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
		CartDto returnValue = new CartDto();
		Error error = new Error("Invalid Cart");

		if (cartEntity.isPresent()) {
			if (cartEntity.get().getCartItems().size() == 0) {
				throw new InvalidCartException(error);
			}

			returnValue = tempConverter.cartEntityToDto(cartEntity.get());

		} else {
			throw new InvalidCartException(error);
		}

		return returnValue;
	}

	@Override
	public void deleteCartItem(Integer itemId, Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeCartItem(itemId);
		cartItemRepository.flush();
	}

	@Override
	public void eraseAllCartItems(Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByCartId(cartId);
		cartItemRepository.flush();
	}

	@Override
	public void deleteCart(Integer cartId) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(cartId);
		cartItemRepository.flush();
	}

}
