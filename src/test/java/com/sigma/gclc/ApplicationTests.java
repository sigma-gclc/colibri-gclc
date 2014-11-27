package com.sigma.gclc;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sigma.gclc.ApplicationTests.StaticMockPropertyInitializer;
import com.sigma.gclc.objet.ImageCarrousel;
import com.sigma.gclc.service.ServiceImage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
// @ContextConfiguration(loader=SpringApplicationContextLoader.class,
// initializers=StaticMockPropertyInitializer.class)
public class ApplicationTests {

	static class StaticMockPropertyInitializer implements
			ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			applicationContext
					.getEnvironment()
					.getPropertySources()
					.addFirst(
							new MockPropertySource().withProperty(
									"imagesDirectory", "classpath:/images"));

		}

	}

	@Autowired
	private WebApplicationContext context;
	private MockMvc mockMvc;
	
	@Autowired
	private ServiceImage serviceImage;

	@Test
	public void contextLoads() {
	}

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void test_getImage() throws Exception {
		mockMvc.perform(get("/images/couchbase-circle-symbol.png")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(IMAGE_PNG));
	}
	
	@Test
	public void test_listerImages() throws Exception {
		List<ImageCarrousel> images = serviceImage.listerImagesRepertoire();
		assertEquals("Nombre d'image incorrect", 1, images.size());
	}
	
	
}
