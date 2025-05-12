package com.deustermix.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest
@EnableTransactionManagement
class DeusterMixApplicationTests {
    @Test
	void contextLoads() {
		DeusterMixApplication.main(new String[]{});
	}
}
