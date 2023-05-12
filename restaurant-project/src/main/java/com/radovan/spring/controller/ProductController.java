package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.service.ProductService;

@Controller
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/allProducts",method = RequestMethod.GET)
	public String allProductsList(ModelMap map) {
		List<ProductDto> allProducts = productService.listAll();
		ProductDto product = new ProductDto();
		map.put("product", product);
		map.put("allProducts", allProducts);
		map.put("recordsPerPage", 9);
		return "fragments/productList :: ajaxLoadedContent";
		
	}
	
	@RequestMapping(value = "/allProductsByCategory/{category}",method = RequestMethod.GET)
	public String allProductsByCategory(@PathVariable ("category") String category,ModelMap map) {
		List<ProductDto> allProducts = productService.listByCategory(category);
		ProductDto product = new ProductDto();
		map.put("product", product);
		map.put("allProducts", allProducts);
		return "fragments/productList :: ajaxLoadedContent";
	}
	
}
