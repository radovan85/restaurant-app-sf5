package com.radovan.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ProductService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@RequestMapping(value = "/addToCart/{productId}")
	public String renderItemForm(@PathVariable("productId") Integer productId, ModelMap map) {

		CartItemDto cartItem = new CartItemDto();
		ProductDto selectedProduct = productService.getProduct(productId);
		map.put("cartItem", cartItem);
		map.put("selectedProduct", selectedProduct);
		map.put("allHotnessLevels", cartItem.getHotnessLevelList());
		return "fragments/cartItemForm :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/addToCart", method = RequestMethod.POST)
	public String addCartItem(@ModelAttribute("cartItem") CartItemDto cartItem) {
		Integer productId = cartItem.getProductId();
		String hotnessLevel = cartItem.getHotnessLevel();
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		ProductDto product = productService.getProduct(productId);

		Optional<CartItemDto> existingCartItem = Optional.ofNullable(cartItemService
				.getCartItemByCartIdAndProductIdAndHotnessLevel(cart.getCartId(), productId, hotnessLevel));
		if (existingCartItem.isPresent()) {
			cartItem.setCartItemId(existingCartItem.get().getCartItemId());
			cartItem.setCartId(cart.getCartId());
			cartItem.setQuantity(existingCartItem.get().getQuantity() + cartItem.getQuantity());
			if (cartItem.getQuantity() > 50) {
				cartItem.setQuantity(50);
			}
			cartItem.setPrice(product.getProductPrice() * cartItem.getQuantity());
			cartItemService.addCartItem(cartItem);
			cartService.refreshCartState(cart.getCartId());
		} else {
			cartItem.setQuantity(cartItem.getQuantity());
			if (cartItem.getQuantity() > 50) {
				cartItem.setQuantity(50);
			}
			cartItem.setCartId(cart.getCartId());
			cartItem.setPrice(product.getProductPrice() * cartItem.getQuantity());
			cartItemService.addCartItem(cartItem);
			cartService.refreshCartState(cart.getCartId());
		}

		return "fragments/homePage :: ajaxLoadedContent";

	}

	@RequestMapping(value = "/itemAddCompleted", method = RequestMethod.GET)
	public String itemAdded() {
		return "fragments/itemAdded :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/getCart", method = RequestMethod.GET)
	public String cartDetails(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(customer.getCartId());
		List<ProductDto> allProducts = productService.listAll();
		map.put("allCartItems", allCartItems);
		map.put("allProducts", allProducts);
		map.put("cart", cart);
		return "fragments/cart :: ajaxLoadedContent";

	}

	@RequestMapping(value = "/deleteItem/{cartId}/{itemId}", method = RequestMethod.GET)
	public String deleteCartItem(@PathVariable("cartId") Integer cartId, @PathVariable("itemId") Integer itemId) {
		cartItemService.removeCartItem(cartId, itemId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteAllItems/{cartId}", method = RequestMethod.GET)
	public String deleteAllCartItems(@PathVariable("cartId") Integer cartId) {
		cartItemService.eraseAllCartItems(cartId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/invalidCart", method = RequestMethod.GET)
	public String invalidCartEx(ModelMap map) {
		return "fragments/invalidCart :: ajaxLoadedContent";
	}
}
