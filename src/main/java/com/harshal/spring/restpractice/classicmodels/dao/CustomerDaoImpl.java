package com.harshal.spring.restpractice.classicmodels.dao;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.harshal.spring.restpractice.classicmodels.CustomerController;
import com.harshal.spring.restpractice.classicmodels.pojos.Customer;
import com.harshal.spring.restpractice.classicmodels.pojos.CustomerResponse;

public class CustomerDaoImpl implements CustomerDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate parametrisedJdbcTemplate;
	
	public CustomerResponse getAllCustomers(int offset, int limit) {
		
		String sql=null;
		boolean flag = false;
		if(offset==0 && limit==0)
		{
			sql = "SELECT * FROM customers";
			flag=false;
		}
		else
		{
			sql = "SELECT * FROM customers LIMIT :offset,:limit";
			flag=true;
		}
		CustomerResponse response = new CustomerResponse();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("offset", offset);
		paramMap.put("limit", offset+(limit-1));
		List<Customer> customers = parametrisedJdbcTemplate.query(sql, paramMap, new RowMapper<Customer>(){

			public Customer mapRow(ResultSet resultSet, int rowNum)
					throws SQLException {
				 	Customer customer = new Customer();
				 	customer.setCustomerId(resultSet.getInt("customerNumber"));
					customer.setCustomerName(resultSet.getString("customerName"));
					customer.setContactFirstName(resultSet.getString("contactFirstName"));
					customer.setContactLastName(resultSet.getString("contactLastName"));
					customer.setPhone(resultSet.getString("phone"));
					customer.setAddressLine(resultSet.getString("addressLine1"));
					customer.setAddressLine2(resultSet.getString("addressLine2"));
					customer.setCity(resultSet.getString("city"));
					customer.setState(resultSet.getString("state"));
					customer.setPostalCode(resultSet.getString("postalCode"));
					customer.setCountry(resultSet.getString("country"));
					customer.setSalesRepEmployeeNumber(resultSet.getInt("salesRepEmployeeNumber"));
					customer.setCreditLimit(resultSet.getFloat("creditLimit"));
					customer.add(linkTo(CustomerController.class).slash(resultSet.getInt("customerNumber")).withSelfRel());
			        return customer;
			}
			
		});
		
		response.setCustomers(customers);
		
		if(flag)
		{
			response.add(linkTo(methodOn(CustomerController.class).customers(1, 5)).withRel("first"));
			response.add(linkTo(methodOn(CustomerController.class).customers(offset+limit, limit)).withRel("next"));
			if(offset-limit>0)
			{
				response.add(linkTo(methodOn(CustomerController.class).customers(offset-limit, limit)).withRel("prev"));
			}
			int last = getLastOffset("SELECT COUNT(*) FROM customers", limit);
			response.add(linkTo(methodOn(CustomerController.class).customers(last, last+limit)).withRel("last"));
		}
		return response;
	}

	public Customer getCustomerById(int customerId) {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("customerid", customerId);
			Customer customer = parametrisedJdbcTemplate.queryForObject("SELECT * FROM customers WHERE customerid=:customerId", paramMap, Customer.class);
			customer.add(linkTo(CustomerController.class).slash(customerId).withSelfRel());
			return customer;
	}

	public Customer createCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Customer updateCustomerDetails(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	private int getLastOffset(String sql, int limit){
		int count = jdbcTemplate.queryForInt(sql);
		int div = count/limit;
		return ((div*limit)+1);
	}
}
