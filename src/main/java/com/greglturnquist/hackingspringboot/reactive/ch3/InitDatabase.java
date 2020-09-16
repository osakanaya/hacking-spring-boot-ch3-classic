package com.greglturnquist.hackingspringboot.reactive.ch3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

	@Bean
	CommandLineRunner initialize(ItemRepository itemRepository) {
		return args -> {
			itemRepository.save(new Item("Cool lego kit", 39.00));
			itemRepository.save(new Item("Hot Wheels race track", 59.99));
			itemRepository.save(new Item("Slick video game", 149.95));
		};
	}
}
