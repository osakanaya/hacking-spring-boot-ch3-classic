package com.greglturnquist.hackingspringboot.reactive.ch3;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
