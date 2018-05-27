package com.n26;


import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = N26ChallangeApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TransactionControllerTest {
	
	protected final String transactionUrl = "/api/transaction";
	protected final String statisticsUrl = "/api/statistics";
	
	@Autowired
	protected WebApplicationContext wac;
	
	protected MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build(); //Initializing mocked web context
	}
	
	@Test
	public void validTransactionTest() throws Exception {
		TimeZone.setDefault(TimeZone.getDefault());
		Long currentTime = new Date().getTime() / 1000;
		
		String json = "{\"amount\":10,\"timestamp\":"+currentTime+"}";
		getResponseResultForPost(transactionUrl, json).andExpect(status().isCreated());
	}
	
	protected ResultActions getResponseResultForPost(String testUrl, String json) throws Exception {
		return this.mockMvc.perform(post(testUrl).content(json).contentType(MediaType.APPLICATION_JSON)
				.accept("application/json"));
	}
	
	@Test
	public void validStatisticsTest() throws Exception{
		
		TimeZone.setDefault(TimeZone.getDefault());
		Long currentTime = new Date().getTime() / 1000;
		
		String input1 = "{\"amount\":10,\"timestamp\":" + (currentTime) +"}";
		getResponseResultForPost(transactionUrl, input1);
		
		Thread.sleep(4000);
		
		TimeZone.setDefault(TimeZone.getDefault());
		currentTime = new Date().getTime() / 1000;
		
		String input2 = "{\"amount\":10,\"timestamp\":" + (currentTime) +"}";
		getResponseResultForPost(transactionUrl, input2);

		String expectedStats = "{\"sum\":20.0,\"avg\":10.0,\"max\":10.0,\"min\":10.0,\"count\":2}";
		String response = getResponseResultForGet(statisticsUrl, expectedStats);
		
		assertTrue(expectedStats.equals(response));
	}
	
	protected String getResponseResultForGet(String testUrl, String json) throws Exception {
		return this.mockMvc.perform(get(testUrl).content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}

}
