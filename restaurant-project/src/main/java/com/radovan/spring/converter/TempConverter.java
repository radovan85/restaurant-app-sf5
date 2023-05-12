package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.AddressEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.AddressRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;

public class TempConverter {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private OrderAddressRepository orderAddressRepository;
	
	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);
		
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(cartEntity.getCustomer());
		if(customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}
		
		
		Optional<List<CartItemEntity>> cartItems = Optional.ofNullable(cartEntity.getCartItems());
		List<Integer> cartItemsIds = new ArrayList<Integer>();
		if(cartItems.isPresent()) {
			for(CartItemEntity item:cartItems.get()) {
				cartItemsIds.add(item.getCartItemId());
			}
		}
		
		returnValue.setCartItemIds(cartItemsIds);
		return returnValue;
		
	}
	
	
	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);
		
		Optional<Integer> customerId = Optional.ofNullable(cartDto.getCustomerId());
		if(customerId.isPresent()) {
			CustomerEntity customer = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customer);
		}
		
		Optional<List<Integer>> cartItemsIds = Optional.ofNullable(cartDto.getCartItemIds());
		List<CartItemEntity> cartItems = new ArrayList<>();
		if(!cartItemsIds.isEmpty()) {
			for(Integer itemId : cartItemsIds.get()) {
				CartItemEntity itemEntity = cartItemRepository.getById(itemId);
				cartItems.add(itemEntity);
			}
		}
		
		returnValue.setCartItems(cartItems);
		return returnValue;
	}
	
	
	public CartItemDto cartItemEntityToDto(CartItemEntity itemEntity) {
		CartItemDto returnValue = mapper.map(itemEntity, CartItemDto.class);
		
		Optional<CartEntity> cartEntity = Optional.ofNullable(itemEntity.getCart());
		if(cartEntity.isPresent()) {
			returnValue.setCartId(cartEntity.get().getCartId());
		}
		
		Optional<ProductEntity> productEntity = Optional.ofNullable(itemEntity.getProduct());
		if(productEntity.isPresent()) {
			returnValue.setProductId(productEntity.get().getProductId());
		}
		
		return returnValue;
	}
	
	public CartItemEntity cartItemDtoToEntity(CartItemDto itemDto) {
		CartItemEntity returnValue = mapper.map(itemDto, CartItemEntity.class);
		
		Optional<Integer> cartId = Optional.ofNullable(itemDto.getCartId());
		if(cartId.isPresent()) {
			CartEntity cartEntity = cartRepository.getById(cartId.get());
			returnValue.setCart(cartEntity);
		}
		
		Optional<Integer> productId = Optional.ofNullable(itemDto.getProductId());
		if(productId.isPresent()) {
			ProductEntity productEntity = productRepository.getById(productId.get());
			returnValue.setProduct(productEntity);
		}
		
		return returnValue;
	}
	
	
	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);
		
		Optional<UserEntity> userEntity = Optional.ofNullable(customerEntity.getUser());
		if(userEntity.isPresent()) {
			returnValue.setUserId(userEntity.get().getId());
		}
		
		Optional<List<OrderEntity>> orders = Optional.ofNullable(customerEntity.getOrders());
		List<Integer> ordersIds = new ArrayList<>();
		if(!orders.isEmpty()) {
			for(OrderEntity orderEntity:orders.get()) {
				ordersIds.add(orderEntity.getOrderId());
			}
		}
		
		returnValue.setOrdersIds(ordersIds);
		
		Optional<CartEntity> cartEntity = Optional.ofNullable(customerEntity.getCart());
		if(cartEntity.isPresent()) {
			returnValue.setCartId(cartEntity.get().getCartId());
		}
		
		Optional<AddressEntity> addressEntity = Optional.ofNullable(customerEntity.getAddress());
		if(addressEntity.isPresent()) {
			returnValue.setAddressId(addressEntity.get().getAddressId());
		}
		
		return returnValue;
	}
	
	
	public CustomerEntity customerDtoToEntity(CustomerDto customer) {
		CustomerEntity returnValue = mapper.map(customer, CustomerEntity.class);
		
		Optional<Integer> userId = Optional.ofNullable(customer.getUserId());
		if(userId.isPresent()) {
			UserEntity userEntity = userRepository.getById(userId.get());
			returnValue.setUser(userEntity);
		}
		
		Optional<List<Integer>> ordersIds = Optional.ofNullable(customer.getOrdersIds());
		List<OrderEntity> orders = new ArrayList<>();
		if(ordersIds.isPresent()) {
			for(Integer orderId : ordersIds.get()) {
				OrderEntity orderEntity = orderRepository.getById(orderId);
				orders.add(orderEntity);
			}
		}
		
		returnValue.setOrders(orders);
		
		Optional<Integer> cartId = Optional.ofNullable(customer.getCartId());
		if(cartId.isPresent()) {
			CartEntity cartEntity = cartRepository.getById(cartId.get());
			returnValue.setCart(cartEntity);
		}
		
		Optional<Integer> addressId =Optional.ofNullable(customer.getAddressId());
		if(addressId.isPresent()) {
			AddressEntity address = addressRepository.getById(addressId.get());
			returnValue.setAddress(address);
		}
		
		return returnValue;
	}
	
	
	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);
		
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(orderEntity.getCustomer());
		if(customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}
		
		Optional<OrderAddressEntity> address = Optional.ofNullable(orderEntity.getAddress());
		if(address.isPresent()) {
			returnValue.setAddressId(address.get().getOrderAddressId());
		}
		
		Optional<List<OrderItemEntity>> orderItems = Optional.ofNullable(orderEntity.getOrderItems());
		List<Integer> orderItemsIds = new ArrayList<>();
		if(!orderItems.isEmpty()) {
			for(OrderItemEntity itemEntity : orderItems.get()) {
				orderItemsIds.add(itemEntity.getOrderItemId());
			}
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> dateOpt = Optional.ofNullable(orderEntity.getDate());
		if (dateOpt.isPresent()) {
			String dateStr = dateOpt.get().toLocalDateTime().format(formatter);
			returnValue.setDateStr(dateStr);
		}
		
		returnValue.setOrderItemsIds(orderItemsIds);
		return returnValue;
	}
	
	
	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);
		
		Optional<Integer> customerId = Optional.ofNullable(order.getCustomerId());
		if(customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}
		
		Optional<Integer> addressId = Optional.ofNullable(order.getAddressId());
		if(addressId.isPresent()) {
			OrderAddressEntity addressEntity = orderAddressRepository.getById(addressId.get());
			returnValue.setAddress(addressEntity);
		}
		
		Optional<List<Integer>> orderItemsIds = Optional.ofNullable(order.getOrderItemsIds());
		List<OrderItemEntity> orderItems = new ArrayList<>();
		if(!orderItemsIds.isEmpty()) {
			for(Integer itemId : orderItemsIds.get()) {
				OrderItemEntity itemEntity = orderItemRepository.getById(itemId);
				orderItems.add(itemEntity);
			}
		}
		
		returnValue.setOrderItems(orderItems);
		return returnValue;
	}
	
	
	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);
		
		Optional<OrderEntity> orderEntity = Optional.ofNullable(itemEntity.getOrder());
		if(orderEntity.isPresent()) {
			returnValue.setOrderId(orderEntity.get().getOrderId());
		}
		
		
		
		return returnValue;
	}
	
	
	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);
		
		Optional<Integer> orderId = Optional.ofNullable(itemDto.getOrderId());
		if(orderId.isPresent()) {
			OrderEntity orderEntity = orderRepository.getById(orderId.get());
			returnValue.setOrder(orderEntity);
		}
		
		return returnValue;
	}
	
	
	public ProductDto productEntityToDto(ProductEntity productEntity) {
		ProductDto returnValue = mapper.map(productEntity, ProductDto.class);
		return returnValue;
	}
	
	public ProductEntity productDtoToEntity(ProductDto product) {
		ProductEntity returnValue = mapper.map(product, ProductEntity.class);
		return returnValue;
	}
	
	
	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> roles = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!roles.isEmpty()) {
			for (RoleEntity roleEntity : roles.get()) {
				rolesIds.add(roleEntity.getId());
			}
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIds = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIds.isEmpty()) {
			for (Integer roleId : rolesIds.get()) {
				RoleEntity role = roleRepository.getById(roleId);
				roles.add(role);
			}
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> users = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();

		if (!users.isEmpty()) {
			for (UserEntity user : users.get()) {
				userIds.add(user.getId());
			}
		}

		returnValue.setUserIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIds = Optional.ofNullable(roleDto.getUserIds());
		List<UserEntity> users = new ArrayList<>();

		if (!usersIds.isEmpty()) {
			for (Integer userId : usersIds.get()) {
				UserEntity userEntity = userRepository.getById(userId);
				users.add(userEntity);
			}
		}
		returnValue.setUsers(users);
		return returnValue;
	}
	
	
	public OrderItemDto cartItemDtoToOrderItemDto(CartItemDto cartItem) {
		OrderItemDto returnValue = mapper.map(cartItem, OrderItemDto.class);
		return returnValue;
	}
	
	public OrderItemEntity cartItemEntityToOrderItemEntity(CartItemEntity cartItem) {
		OrderItemEntity returnValue = mapper.map(cartItem, OrderItemEntity.class);
		return returnValue;
	}
	
	
	public OrderItemEntity cartItemToOrderItemEntity(CartItemEntity cartItemEntity) {
		OrderItemEntity returnValue = mapper.map(cartItemEntity, OrderItemEntity.class);
		
		Optional<ProductEntity> product = Optional.ofNullable(cartItemEntity.getProduct());
		if(product.isPresent()) {
			returnValue.setProductName(product.get().getProductName());
			returnValue.setProductPrice(product.get().getProductPrice());
		}
		
		return returnValue;
	}
	
	
	public AddressDto addressEntityToDto(AddressEntity addressEntity) {
		AddressDto returnValue = mapper.map(addressEntity, AddressDto.class);
		
		Optional<CustomerEntity> customerEntity = Optional.ofNullable(addressEntity.getCustomer());
		if(customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}
		
		return returnValue;
	}
	
	public AddressEntity addressDtoToEntity(AddressDto address) {
		AddressEntity returnValue = mapper.map(address, AddressEntity.class);
		
		Optional<Integer> customerId = Optional.ofNullable(address.getCustomerId());
		if(customerId.isPresent()) {
			CustomerEntity customerEntity = customerRepository.getById(customerId.get());
			returnValue.setCustomer(customerEntity);
		}
		
		return returnValue;
	}
	
	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderEntity = Optional.ofNullable(address.getOrder());
		if(orderEntity.isPresent()) {
			returnValue.setOrderId(orderEntity.get().getOrderId());
		}
		
		return returnValue;
	}
	
	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderId = Optional.ofNullable(address.getOrderId());
		if(orderId.isPresent()) {
			OrderEntity orderEntity = orderRepository.getById(orderId.get());
			returnValue.setOrder(orderEntity);
		}
		
		return returnValue;
	}
	
	public OrderAddressEntity addressToOrderAddress(AddressEntity address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		return returnValue;
	}

}
