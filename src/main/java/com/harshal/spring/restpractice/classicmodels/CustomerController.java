package com.harshal.spring.restpractice.classicmodels;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harshal.spring.restpractice.classicmodels.dao.CustomerDao;
import com.harshal.spring.restpractice.classicmodels.pojos.Customer;
import com.harshal.spring.restpractice.classicmodels.pojos.CustomerResponse;

//import static 

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	CustomerDao customerDao;
	
	@RequestMapping(produces={MediaType.APPLICATION_JSON_VALUE},method=RequestMethod.GET)
	public ResponseEntity<CustomerResponse> customers(@RequestParam(value="offset",required=false) Integer offset, 
			@RequestParam(value="limit",required=false) Integer limit){
		
		System.out.println("In get all customers-- offset "+offset+" & limit "+limit);
		if(offset==null && limit==null)
		{
			offset=0;
			limit=0;
		}
		CustomerResponse customers = customerDao.getAllCustomers(offset,limit);
		System.out.println("Size ::"+customers.getCustomers().size());
		return new ResponseEntity<>(customers, HttpStatus.OK);
		/*return Optional.of(customerDao.getAllCustomers(offset,limit))
				.map(u -> new ResponseEntity<>(u,HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
	}
	
	@RequestMapping(value="{customerId}",produces={MediaType.APPLICATION_JSON_VALUE},method=RequestMethod.GET)
	public ResponseEntity<Customer> customer(@PathVariable int customerId){
		
		System.out.println("In get Customer By Id: "+customerId);
		return new ResponseEntity<>(customerDao.getCustomerById(customerId),HttpStatus.OK);
		/*return Optional.of(customerDao.getCustomerById(customerId))
		.map(u -> new ResponseEntity<Customer>(u, HttpStatus.OK))
		.orElse(new ResponseEntity<Customer>(HttpStatus.NOT_FOUND));*/
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
		
		System.out.println("In register customer profile");
		return new ResponseEntity<>(customerDao.createCustomer(customer),HttpStatus.OK);
		/*return Optional.of(customerDao.createCustomer(customer))
				.map(u -> new ResponseEntity<Customer>(u, HttpStatus.OK))
				.orElse(new ResponseEntity<Customer>(HttpStatus.INTERNAL_SERVER_ERROR));*/
	}
}
