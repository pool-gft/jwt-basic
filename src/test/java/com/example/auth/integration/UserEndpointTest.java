package com.example.auth.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.auth.jpa.UserEntity;
import com.example.auth.jpa.UserRepo;
import com.example.auth.service.AuthService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class UserEndpointTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mvc;
	private static Cookie jwtCookie;

	@BeforeEach
	public void setup() {
		if (jwtCookie == null) {
			AuthService service = context.getBean(AuthService.class);
			UserEntity entity = new UserEntity();
			entity.setEmail("admin@admin.uk");
			entity.setPassword("1234");
			jwtCookie = service.registerUser(entity);

		}
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	public void register() throws Exception {
		UserEntity entity = new UserEntity();
		entity.setEmail("test@test.uk");
		entity.setPassword("1234");
		mapper.disable(MapperFeature.USE_ANNOTATIONS); // UserEntity annotation will null the field
		String json = mapper.writeValueAsString(entity);

		mvc.perform(MockMvcRequestBuilders.post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.cookie().exists("jwt"));
		UserRepo repo = context.getBean(UserRepo.class);
		assertTrue(repo.findByEmail("test@test.uk").isPresent());
	}

	@Test
	public void userInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/users/info")
				.cookie(jwtCookie))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
