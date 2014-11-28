package com.sigma.gclc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sigma.gclc.objet.ImageCarrousel;
import com.sigma.gclc.service.LoadImageRepositoryScheduledTask;
import com.sigma.gclc.service.ServiceImage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
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
	
	@Autowired
	private LoadImageRepositoryScheduledTask loadImageRepositoryTask;

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
		loadImageRepositoryTask.refreshImageRepository();
		
		List<ImageCarrousel> images = serviceImage.listerImagesRepertoire();
		
		assertEquals("Nombre d'image incorrect", 1, images.size());
		assertEquals("images/couchbase-circle-symbol.png", images.get(0).getNom());
		assertTrue(StringUtils.isNotBlank(images.get(0).getDescription()));
	}
	
	@Test
	public void test_getIndex() throws Exception{
		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk());
	}
	
	
}
