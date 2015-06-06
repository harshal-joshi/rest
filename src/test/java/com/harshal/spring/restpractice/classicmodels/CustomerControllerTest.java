package com.harshal.spring.restpractice.classicmodels;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.harshal.spring.restpractice.classicmodels.dao.CustomerDao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-config.xml"})
@WebAppConfiguration
@EnableWebMvc
public class CustomerControllerTest {

	private MockMvc mockMvc;
	
	/*@Autowired
	private CustomerDao customerDao;*/
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup(){
		CustomerDao mock = Mockito.mock(CustomerDao.class); 
		Mockito.reset(mock);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void customers_test() throws Exception{
		mockMvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
		//.andExpect(content().contentType("application/json"));
	}
}
