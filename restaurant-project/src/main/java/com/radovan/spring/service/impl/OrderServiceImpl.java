package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.entity.AddressEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Override
	public OrderDto addOrder() {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOptional = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		CustomerEntity customerEntity = null;
		OrderEntity orderEntity = new OrderEntity();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		if (customerOptional.isPresent()) {
			customerEntity = customerOptional.get();
			Optional<CartEntity> cartEntityOptional = Optional.ofNullable(customerEntity.getCart());
			if (cartEntityOptional.isPresent()) {
				CartEntity cartEntity = cartEntityOptional.get();
				Optional<List<CartItemEntity>> allCartItems = Optional.ofNullable(cartEntity.getCartItems());
				if (!allCartItems.isEmpty()) {
					for (CartItemEntity cartItem : allCartItems.get()) {
						OrderItemEntity orderItem = tempConverter.cartItemToOrderItemEntity(cartItem);
						orderedItems.add(orderItem);
					}

					cartItemRepository.removeAllByCartId(cartEntity.getCartId());
					cartService.refreshCartState(cartEntity.getCartId());

					AddressEntity address = customerEntity.getAddress();
					OrderAddressEntity orderAddress = tempConverter.addressToOrderAddress(address);
					OrderAddressEntity storedOrderAddress = orderAddressRepository.save(orderAddress);

					orderEntity.setCustomer(customerEntity);
					orderEntity.setAddress(storedOrderAddress);
					OrderEntity storedOrder = orderRepository.save(orderEntity);

					for (OrderItemEntity orderItem : orderedItems) {
						orderItem.setOrder(storedOrder);
						orderItemRepository.save(orderItem);
					}

					orderedItems = orderItemRepository.findAllByOrderId(storedOrder.getOrderId());
					Optional<Double> orderPrice = Optional
							.ofNullable(orderItemRepository.calculateGrandTotal(storedOrder.getOrderId()));
					if (orderPrice.isPresent()) {
						storedOrder.setPrice(orderPrice.get());
					}

					storedOrderAddress.setOrder(storedOrder);
					orderAddressRepository.saveAndFlush(storedOrderAddress);

					storedOrder.setOrderItems(orderedItems);
					storedOrder = orderRepository.saveAndFlush(storedOrder);
					returnValue = tempConverter.orderEntityToDto(storedOrder);

				}
			}
		}

		return returnValue;
	}

	@Override
	public List<OrderDto> getTodaysOrders() {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDateStr = currentDate.format(formatter);
		String timestamp1Str = currentDateStr + " 00:00:00";
		String timestamp2Str = currentDateStr + " 23:59:59";
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Timestamp timestamp1 = Timestamp.valueOf(LocalDateTime.parse(timestamp1Str, formatter));
		Timestamp timestamp2 = Timestamp.valueOf(LocalDateTime.parse(timestamp2Str,formatter));

		Optional<List<OrderEntity>> allOrders = Optional
				.ofNullable(orderRepository.findAllTodaysOrders(timestamp1, timestamp2));
		if (!allOrders.isEmpty()) {
			for (OrderEntity order : allOrders.get()) {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			}
		}

		return returnValue;
	}

	@Override
	public List<OrderDto> listAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<>();
		Optional<List<OrderEntity>> allOrders = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId));
		if (!allOrders.isEmpty()) {
			for (OrderEntity order : allOrders.get()) {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			}
		}
		return returnValue;
	}

	@Override
	public Double calculateOrderPrice(Integer orderId) {

		// TODO Auto-generated method stub
		Optional<Double> orderTotal = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId));
		Double returnValue = 0d;

		if (orderTotal.isPresent()) {
			returnValue = orderTotal.get();
		}

		return returnValue;
	}

	@Override
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderEntity> allOrders = orderRepository.findAll();
		List<OrderDto> returnValue = new ArrayList<>();

		for (OrderEntity order : allOrders) {
			OrderDto orderDto = tempConverter.orderEntityToDto(order);
			returnValue.add(orderDto);
		}
		return returnValue;
	}

	@Override
	public OrderDto getOrder(Integer orderId) {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
		if (orderEntity.isPresent()) {
			returnValue = tempConverter.orderEntityToDto(orderEntity.get());
		}
		return returnValue;
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		orderRepository.eraseById(orderId);
		orderRepository.flush();
	}

}
