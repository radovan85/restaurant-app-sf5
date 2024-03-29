package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.ProductDto;

public interface ProductService {

	ProductDto getProduct(Integer productId);
	
	List<ProductDto> listByCategory(String category);
	
	ProductDto addProduct(ProductDto product);
	
	List<ProductDto> listAll();
	
	ProductDto updateProduct(Integer productId,ProductDto product);
	
	void deleteProduct(Integer productId);
	
}
