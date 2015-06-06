package com.harshal.spring.restpractice.classicmodels.pojos;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class CustomerResponse extends ResourceSupport{

	private List<Customer> customers;

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	
	
	
}
