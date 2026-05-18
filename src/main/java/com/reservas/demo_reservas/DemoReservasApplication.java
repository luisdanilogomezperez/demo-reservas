package com.reservas.demo_reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoReservasApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoReservasApplication.class, args);
	}

	@GetMapping("/")
	public List<String> listString(){
		return List.of(
				"Luisa",
				"Natalia",
				"Camila"
		);
	}
}

