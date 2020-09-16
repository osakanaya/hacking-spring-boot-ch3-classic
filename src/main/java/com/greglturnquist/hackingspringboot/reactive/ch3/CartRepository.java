package com.greglturnquist.hackingspringboot.reactive.ch3;

import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {

}
