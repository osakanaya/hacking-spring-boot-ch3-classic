package com.greglturnquist.hackingspringboot.reactive.ch3;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class CartItem {
	
	@Id
	@GeneratedValue
	private long id;

	@OneToOne
	private Item item;
	
	private int quantity;
	
	private CartItem() {}
	
	CartItem(Item item) {
		this.item = item;
		this.quantity = 1;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, item, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItem other = (CartItem) obj;
		return id == other.id && Objects.equals(item, other.item) && quantity == other.quantity;
	}

	@Override
	public String toString() {
		return "CartItem [item=" + item + ", quantity=" + quantity + "]";
	}
	
}
