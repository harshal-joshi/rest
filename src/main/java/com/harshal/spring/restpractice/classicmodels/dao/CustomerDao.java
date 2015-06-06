package com.harshal.spring.restpractice.classicmodels.dao;

import com.harshal.spring.restpractice.classicmodels.pojos.Customer;
import com.harshal.spring.restpractice.classicmodels.pojos.CustomerResponse;

public interface CustomerDao {

	public CustomerResponse getAllCustomers(int offset, int limit);
	public Customer getCustomerById(int customerId);
	public Customer createCustomer(Customer customer);
	public Customer updateCustomerDetails(Customer customer);
}
