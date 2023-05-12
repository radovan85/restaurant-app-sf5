package com.radovan.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.service.AddressService;



@Controller
@RequestMapping(value="/addresses")
public class AddressController {
	
	@Autowired
	private AddressService addressService;

	@RequestMapping(value="/updateAddress/{addressId}")
	public String renderAddressForm(@PathVariable ("addressId") Integer addressId,ModelMap map) {
		AddressDto address = new AddressDto();
		AddressDto currentAddress = addressService.getAddressById(addressId);
		map.put("address", address);
		map.put("currentAddress", currentAddress);
		return "fragments/updateAddressForm :: ajaxLoadedContent";
	}
	
	@RequestMapping(value="/createAddress",method = RequestMethod.POST)
	public String createAddress(@ModelAttribute ("address") AddressDto address) {
		addressService.createAddress(address);
		return "fragments/homePage :: ajaxLoadedContent";
	}
}
