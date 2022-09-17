package com.github.wesleyegberto;

import java.math.BigDecimal;

public class Order {
	private String clientId;
	private BigDecimal totalAmount;

	public String getClientId() {
		return clientId;
	}

	public boolean hasClientId() {
		return clientId != null && !clientId.isBlank();
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
}
