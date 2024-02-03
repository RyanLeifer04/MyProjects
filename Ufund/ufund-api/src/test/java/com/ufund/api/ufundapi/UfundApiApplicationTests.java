package com.ufund.api.ufundapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UfundApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMain() {
		try {
			UfundApiApplication.main(new String[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
