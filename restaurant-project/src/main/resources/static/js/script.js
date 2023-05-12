window.onload = redirectHome;

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");

}

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/userRegistration");

}

function redirectAllProducts() {
	$("#ajaxLoadedContent").load("/products/allProducts");
}

function redirectAddProduct() {
	$("#ajaxLoadedContent").load("/admin/createProduct");
}

function redirectAllProductsByCategory(category) {
	$("#ajaxLoadedContent").load("/products/allProductsByCategory/" + category);
}

function redirectAdmin() {
	$("#ajaxLoadedContent").load("/admin/");
}

function redirectAdminProductList() {
	$("#ajaxLoadedContent").load("/admin/allProducts");
}

function redirectUpdateProduct(productId) {
	$("#ajaxLoadedContent").load("/admin/updateProduct/" + productId);
}

function redirectProductDetails(productId) {
	$("#ajaxLoadedContent").load("/admin/productDetails/" + productId);
}

function redirectItemForm(productId) {
	$("#ajaxLoadedContent").load("/cart/addToCart/" + productId);
}

function redirectItemAdded() {
	$("#ajaxLoadedContent").load("/cart/itemAddCompleted");
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/cart/getCart");
}

function redirectOrderCompleted() {
	$("#ajaxLoadedContent").load("/orders/orderCompleted");
}

function redirectAccountDetails() {
	$("#ajaxLoadedContent").load("/accountInfo");
}

function redirectUpdateAddress(addressId) {
	$("#ajaxLoadedContent").load("/addresses/updateAddress/" + addressId);
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	$("#ajaxLoadedContent").load("/admin/customerDetails/" + customerId);
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/getOrder/" + orderId);
}

function redirectTodaysOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrdersToday");
}

function redirectAbout() {
	$("#ajaxLoadedContent").load("/about");
}

function redirectInvalidPath() {
	$("#ajaxLoadedContent").load("/admin/invalidPath");
}

function formInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateRegForm()) {
			$.ajax({
				url : "http://localhost:8080/saveUser",
				type : 'post',
				data : $form.serialize(),
				success : function() {
					$("#ajaxLoadedContent").load("/registerComplete");
				},
				error : function() {
					$("#ajaxLoadedContent").load("/registerFail");

				}
			});
		}
		;
	});
};

function loginInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();

		$.ajax({
			url : "http://localhost:8080/login",
			type : 'post',
			data : $form.serialize(),
			success : function() {
				confirmLoginPass();
			},
			error : function() {
				alert("Failed!");

			}
		});

	});
};

function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST",
		success : function() {
			window.location.href = "/";
		},
		error : function() {
			$("#ajaxLoadedContent").load("/loginErrorPage");
		}
	});
}

function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout",
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function() {
			window.location.href = "/";
		},
		error : function() {
			alert("Logout error");

		}
	});
}

function deleteProduct(productId) {
	if (confirm('Are you sure you want to delete this product?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteProduct/" + productId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function() {
				redirectAdminProductList();
			},
			error : function() {
				redirectInvalidPath();
			}
		});
	}
};

function executeProductForm() {

	var formData = new FormData();
	var files = $('input[type=file]');
	for (var i = 0; i < files.length; i++) {
		if (files[i].value == "" || files[i].value == null) {
			alert("Please provide image");
			return false;
		} else {
			formData.append(files[i].name, files[i].files[0]);
		}
	}
	var formSerializeArray = $("#productAddForm").serializeArray();
	for (var i = 0; i < formSerializeArray.length; i++) {
		formData
				.append(formSerializeArray[i].name, formSerializeArray[i].value)
	}
	if (validateProduct()) {
		$.ajax({
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			url : "http://localhost:8080/admin/createProduct",
			success : function() {
				redirectAdminProductList();
			},
			error : function() {
				redirectInvalidPath();
			}
		});
	}
	;
};

function itemInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateItem()) {
			$.ajax({
				url : "http://localhost:8080/cart/addToCart",
				type : 'post',
				data : $form.serialize(),
				success : function() {
					redirectItemAdded();
				},
				error : function() {
					alert("Failed");
				}
			});
		}
		;
	});
};

function eraseItem(cartId, itemId) {
	if (confirm('Remove this item from cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteItem/" + cartId + "/"
					+ itemId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function() {
				redirectCart();
			},
			error : function() {
				alert("Failed!");
			}
		});
	}
	;
}

function eraseAllItems(cartId) {
	if (confirm('Are you sure you want to clear your cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteAllItems/" + cartId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function() {
				redirectCart();
			},
			error : function() {
				alert("Failed!");
			}
		});
	}
	;
}

function redirectOrderConfirmation(cartId) {
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/orders/confirmOrder/" + cartId,
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function() {
			$("#ajaxLoadedContent").load("orders/confirmOrder/" + cartId);
		},
		error : function() {
			$("#ajaxLoadedContent").load("/cart/invalidCart");
		}
	});
}

function executeOrder() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/orders/processOrder",
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function() {
			redirectOrderCompleted();
		},
		error : function() {
			alert("Failed");
		}
	});
}

function addressInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateAddress()) {
			$.ajax({
				url : "http://localhost:8080/addresses/createAddress",
				type : "POST",
				data : $form.serialize(),
				success : function() {
					redirectAccountDetails();
				},
				error : function() {
					alert("Failed");

				}
			});
		}
		;
	});
};

function deleteOrder(orderId) {
	if (confirm('Remove this order?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function() {
				redirectAllOrders();
			},
			error : function() {
				alert("Failed");
			}
		});
	}
	;
};

function deleteCustomer(customerId) {
	if (confirm('Are you sure you want to remove this customer?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function() {
				redirectAllCustomers();
			},
			error : function() {
				alert("Failed");
			}
		});
	}
	;
};

function ValidatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(e) {
	var pattern = /^\d{0,4}(\.\d{0,4})?$/g;

	return pattern.test(e.key)
};

function validateItem() {
	var quantity = $("#quantity").val();
	var quantityNum = Number(quantity);
	var hotnessLevel = $("#hotnessLevel").val();
	var returnValue = true;

	if (quantity === "" || quantityNum < 1 || quantityNum > 50) {
		$("#quantityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#quantityError").css({
			"visibility" : "hidden"
		});
	}

	if (hotnessLevel === "") {
		$("#hotnessLevelError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#hotnessLevelError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
}
