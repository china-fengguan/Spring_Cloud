@@ -0,0 +1,54 @@
package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DemoApplication.class)
public class SpringBootJwtApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity()) 
				.build();
	}
	
	@Test
	public void testAUTHService() throws Exception {
		String json = "{\"username\":\"omar\",\"password\":\"12345\"}";
		String signUpURI = "http://localhost:8762/auth/";
		ResultActions result 
	      = mockMvc.perform(post(signUpURI, "json").characterEncoding("UTF-8").content(json.getBytes()).contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(header().exists("Authorization"));
	 
	    String resultString = result.andReturn().getResponse().getHeader("Authorization");
		String requestURI = "http://localhost:8762/gallery/admin";
		mockMvc.perform(post(requestURI, "json").header("Authorization", resultString).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isForbidden());

	}

}