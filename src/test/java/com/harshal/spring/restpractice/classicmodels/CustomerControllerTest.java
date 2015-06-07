package com.harshal.spring.restpractice.classicmodels;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-config.xml"})
@WebAppConfiguration
@EnableWebMvc
public class CustomerControllerTest {

	private MockMvc mockMvc;
	
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
		mockMvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(124)))
		.andExpect(jsonPath("$.customers[0].customerName",is("Atelier graphique")))
		.andExpect(jsonPath("$.customers[0].links[0].rel").exists());
	}
	
	@Test
	public void customers_pagination_test() throws Exception{
		mockMvc.perform(get("/customers?offset=0&limit=10").accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.links",hasSize(3)));
		
		mockMvc.perform(get("/customers?offset=10&limit=10").accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.links",hasSize(4)));
	}
	
	@Test
	public void customerById_test() throws Exception{
		mockMvc.perform(get("/customers/121").accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/customers/122").accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isNotFound())
		.andExpect(content().contentType("application/vnd.error"));
	}
}
