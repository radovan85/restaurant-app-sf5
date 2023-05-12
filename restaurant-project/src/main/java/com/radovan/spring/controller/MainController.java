package com.radovan.spring.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.model.RegistrationForm;
import com.radovan.spring.service.AddressService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.UserService;

@Controller
public class MainController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String indexPage() {
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "fragments/login :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homePage() {
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/userRegistration", method = RequestMethod.GET)
	public String registration(ModelMap map) {
		RegistrationForm tempForm = new RegistrationForm();
		map.put("tempForm", tempForm);
		return "fragments/registration :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public String createUser(@ModelAttribute("tempForm") RegistrationForm tempForm) {
		customerService.registerCustomer(tempForm);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/registerComplete", method = RequestMethod.GET)
	public String registrationCompleted() {
		return "fragments/registration_completed :: ajaxLoadedContent";
	}

	@RequestMapping(value = "/registerFail", method = RequestMethod.GET)
	public String registrationFailed() {
		return "fragments/registration_failed :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/loginErrorPage",method = RequestMethod.GET)
	public String logError(ModelMap map) {
		map.put("alert", "Invalid username or password!");
		return "fragments/login :: ajaxLoadedContent";
	}
	
	
	

	@RequestMapping(value = "/loginPassConfirm", method = RequestMethod.POST)
	public String confirmLoginPass(Principal principal) {
		Optional<Principal> authPrincipal = Optional.ofNullable(principal);
		if (!authPrincipal.isPresent()) {
			Error error = new Error("Invalid user");
			throw new InvalidUserException(error);
		}

		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	
	@RequestMapping(value = "/loggedout", method = RequestMethod.POST)
	public String logout(RedirectAttributes redirectAttributes) {
		SecurityContextHolder.clearContext();
		return "fragments/homePage :: ajaxLoadedContent";
	}
	
	
	@Secured(value="ROLE_USER")
	@RequestMapping(value="/accountInfo")
	public String userAccountInfo(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		map.put("authUser", authUser);
		map.put("address", address);
		return "fragments/accountDetails :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/about",method = RequestMethod.GET)
	public String aboutPage() {
		return "fragments/about :: ajaxLoadedContent";
	}

}
