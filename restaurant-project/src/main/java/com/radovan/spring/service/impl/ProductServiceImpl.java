package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TempConverter tempConverter;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public ProductDto getProduct(Integer productId) {
		// TODO Auto-generated method stub
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		ProductDto returnValue = null;
		if (productEntity.isPresent()) {
			returnValue = tempConverter.productEntityToDto(productEntity.get());
		}
		return returnValue;
	}

	@Override
	public List<ProductDto> listByCategory(String category) {
		// TODO Auto-generated method stub
		Optional<List<ProductEntity>> allProducts = Optional.ofNullable(productRepository.findAllByCategory(category));
		List<ProductDto> returnValue = new ArrayList<ProductDto>();
		if (!allProducts.isEmpty()) {
			for (ProductEntity product : allProducts.get()) {
				ProductDto productDto = tempConverter.productEntityToDto(product);
				returnValue.add(productDto);
			}
		}
		return returnValue;
	}

	@Override
	public ProductDto addProduct(ProductDto product) {
		// TODO Auto-generated method stub
		Optional<Integer> productId = Optional.ofNullable(product.getProductId());
		ProductEntity productEntity = tempConverter.productDtoToEntity(product);
		ProductEntity storedProduct = productRepository.save(productEntity);
		ProductDto returnValue = tempConverter.productEntityToDto(storedProduct);
		
		if(productId.isPresent()) {
			Optional<List<CartItemEntity>> allCartItems = 
					Optional.ofNullable(cartItemRepository.findAllByProductId(returnValue.getProductId()));
			if(!allCartItems.isEmpty()) {
				for(CartItemEntity itemEntity : allCartItems.get()) {
					Double price = returnValue.getProductPrice();
					price = price * itemEntity.getQuantity();
					itemEntity.setPrice(price);
					cartItemRepository.saveAndFlush(itemEntity);
				}
				
				Optional<List<CartEntity>> allCarts = Optional.ofNullable(cartRepository.findAll());
				if(!allCarts.isEmpty()) {
					for(CartEntity cartEntity : allCarts.get()) {
						cartService.refreshCartState(cartEntity.getCartId());
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public List<ProductDto> listAll() {
		// TODO Auto-generated method stub
		Optional<List<ProductEntity>> allProducts = Optional.ofNullable(productRepository.findAll());
		List<ProductDto> returnValue = new ArrayList<ProductDto>();

		if (!allProducts.isEmpty()) {
			for (ProductEntity productEntity : allProducts.get()) {
				ProductDto productDto = tempConverter.productEntityToDto(productEntity);
				returnValue.add(productDto);
			}
		}
		return returnValue;
	}

	@Override
	public ProductDto updateProduct(Integer productId, ProductDto product) {
		// TODO Auto-generated method stub
		ProductEntity tempProduct = productRepository.getById(productId);
		ProductEntity productEntity = tempConverter.productDtoToEntity(product);
		productEntity.setProductId(tempProduct.getProductId());

		ProductEntity updatedProduct = productRepository.saveAndFlush(productEntity);
		ProductDto returnValue = tempConverter.productEntityToDto(updatedProduct);
		return returnValue;
	}

	@Override
	public void deleteProduct(Integer productId) {
		// TODO Auto-generated method stub
		productRepository.deleteById(productId);
		productRepository.flush();
	}

}
