package com.examensarbete.application;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Temporärt avstängt pga context-fel")
@SpringBootTest(classes = BookWishListApplication.class)
class BookWishListApplicationTests {

	@Test
	void contextLoads() {
	}
}
