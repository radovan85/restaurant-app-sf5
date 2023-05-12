package com.radovan.spring.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.exceptions.ImagePathException;
import com.radovan.spring.service.AddressService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderAddressService;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.ProductService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private OrderAddressService orderAddressService;

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private CartService cartService;

	@RequestMapping(value = "/")
	public String adminHome() {
		return "fragments/admin :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/createProduct", method = RequestMethod.GET)
	public String renderProductForm(ModelMap map) {
		ProductDto product = new ProductDto();
		map.put("product", product);
		map.put("allCategories", product.getCategoryList());
		return "fragments/productForm :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/createProduct", method = RequestMethod.POST)
	public String createProduct(@ModelAttribute("product") ProductDto product, ModelMap map,
			@RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String imgName)
			throws Throwable {

		String fileLocation = "C:\\Users\\pc\\workspace2\\restaurant-project\\src\\main\\resources\\static\\images\\productImages\\";
		String imageUUID;

		Path locationPath = Paths.get(fileLocation);

		if (!Files.exists(locationPath)) {
			Error error = new Error("Invalid file path!");
			throw new ImagePathException(error);
		}

		imageUUID = file.getOriginalFilename();
		Path fileNameAndPath = Paths.get(fileLocation, imageUUID);

		if (file != null && !file.isEmpty()) {
			Files.write(fileNameAndPath, file.getBytes());
			System.out.println("IMage Save at:" + fileNameAndPath.toString());

		} else {
			imageUUID = imgName;
		}

		product.setImageName(imageUUID);
		productService.addProduct(product);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allProducts", method = RequestMethod.GET)
	public String allProductsList(ModelMap map) {
		List<ProductDto> allProducts = productService.listAll();
		map.put("allProducts", allProducts);
		map.put("recordsPerPage", 5);
		return "fragments/adminProductList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/updateProduct/{productId}", method = RequestMethod.GET)
	public String renderUpdateForm(@PathVariable("productId") Integer productId, ModelMap map) {
		ProductDto product = new ProductDto();
		ProductDto currentProduct = productService.getProduct(productId);
		map.put("product", product);
		map.put("currentProduct", currentProduct);
		map.put("allCategories", product.getCategoryList());
		return "fragments/updateProduct :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteProduct/{productId}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable("productId") Integer productId) throws Throwable {

		ProductDto product = productService.getProduct(productId);
		Path path = Paths.get(
				"C:\\Users\\pc\\workspace2\\restaurant-project\\src\\main\\resources\\static\\images\\productImages\\"
						+ product.getImageName());

		if (Files.exists(path)) {
			Files.delete(path);
		} else {
			Error error = new Error("Invalid file path!");
			throw new ImagePathException(error);
		}

		cartItemService.eraseAllByProductId(productId);
		productService.deleteProduct(productId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/productDetails/{productId}", method = RequestMethod.GET)
	public String getProductDetails(@PathVariable("productId") Integer productId, ModelMap map) {
		ProductDto currentProduct = productService.getProduct(productId);
		map.put("currentProduct", currentProduct);
		return "fragments/productDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allCustomers", method = RequestMethod.GET)
	public String listAllCustomers(ModelMap map) {
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 7);
		return "fragments/customerList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/customerDetails/{customerId}", method = RequestMethod.GET)
	public String getCustomerDetails(@PathVariable("customerId") Integer customerId, ModelMap map) {
		CustomerDto customer = customerService.getCustomer(customerId);
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		UserDto user = userService.getUserById(customer.getUserId());
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		map.put("customer", customer);
		map.put("address", address);
		map.put("user", user);
		map.put("allOrders", allOrders);
		return "fragments/customerDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allOrders", method = RequestMethod.GET)
	public String getAllOrders(ModelMap map) {
		List<OrderDto> allOrders = orderService.listAll();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 10);
		return "fragments/orderList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/allOrdersToday", method = RequestMethod.GET)
	public String getAllOrdersToday(ModelMap map) {
		List<OrderDto> allOrders = orderService.getTodaysOrders();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 10);
		return "fragments/ordersTodayList :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/getOrder/{orderId}", method = RequestMethod.GET)
	public String orderDetails(@PathVariable("orderId") Integer orderId, ModelMap map) {

		OrderDto order = orderService.getOrder(orderId);
		CustomerDto customer = customerService.getCustomer(order.getCustomerId());
		OrderAddressDto address = orderAddressService.getAddressById(order.getAddressId());
		Double orderPrice = orderService.calculateOrderPrice(orderId);
		List<OrderItemDto> orderedItems = orderItemService.listAllByOrderId(orderId);
		map.put("order", order);
		map.put("address", address);
		map.put("orderPrice", orderPrice);
		map.put("orderedItems", orderedItems);
		map.put("customer", customer);
		return "fragments/orderDetails :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/deleteOrder/{orderId}", method = RequestMethod.GET)
	public String deleteOrder(@PathVariable("orderId") Integer orderId) {

		OrderDto order = orderService.getOrder(orderId);
		Integer addressId = order.getAddressId();
		orderItemService.eraseAllByOrderId(orderId);
		orderService.deleteOrder(orderId);
		orderAddressService.deleteAddress(addressId);

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/invalidPath", method = RequestMethod.GET)
	public String invalidImagePath() {
		return "fragments/invalidImagePath :: ajaxLoadedContent";
	}
	
	@RequestMapping(value = "/deleteCustomer/{customerId}", method = RequestMethod.GET)
	public String removeCustomer(@PathVariable("customerId") Integer customerId) {
		CustomerDto customer = customerService.getCustomer(customerId);
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		UserDto user = userService.getUserById(customer.getUserId());
		
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		for(OrderDto order:allOrders) {
			orderItemService.eraseAllByOrderId(order.getOrderId());
			orderService.deleteOrder(order.getOrderId());
			orderAddressService.deleteAddress(order.getAddressId());
		}
			
		
		cartItemService.eraseAllCartItems(cart.getCartId());
		customerService.resetCustomer(customerId);
		addressService.deleteAddress(address.getAddressId());
		cartService.deleteCart(cart.getCartId());		
		customerService.deleteCustomer(customerId);
		userService.deleteUser(user.getId());
		return "fragments/homePage :: ajaxLoadedContent";
	}
}
