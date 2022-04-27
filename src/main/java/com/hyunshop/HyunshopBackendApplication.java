package com.hyunshop;

import com.hyunshop.seeder.BatchSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HyunshopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyunshopBackendApplication.class, args);
		new BatchSeeder().seed();
	}

}
