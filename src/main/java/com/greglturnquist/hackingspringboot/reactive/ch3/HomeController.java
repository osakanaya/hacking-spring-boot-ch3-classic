package com.greglturnquist.hackingspringboot.reactive.ch3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class HomeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;
	private final CartItemRepository cartItemRepository;
	
	HomeController(CartRepository cartRepository, 
			ItemRepository itemRepository,
			CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.itemRepository = itemRepository;
		this.cartItemRepository = cartItemRepository;
	}
	
	@GetMapping
	String home(Model model, @RequestParam(required = false) Long cartId) {
		Cart cart;
		
		if (cartId != null) {
			cart = this.cartRepository.findById(cartId)
					.orElseThrow(() -> new CartNotFoundExcetion(cartId));
		} else {
			cart = new Cart();
			
			for (Item item : this.itemRepository.findAll()) {
				CartItem cartItem = this.cartItemRepository.save(new CartItem(item));
				cart.getCartItems().add(cartItem);
			}
			
			cart = this.cartRepository.save(cart);
		}
		
		model.addAttribute("cartId", cart.getId());
		model.addAttribute("cartItems", cart.getCartItems());
		
		return "home.html";
	}
	
	@PostMapping("/remove/{cartId}/{itemId}")
	String removeFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
		Cart cart = this.cartRepository.findById(cartId)
			.orElseThrow(() -> new CartNotFoundExcetion(cartId));
		
		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem.getItem().getId().equals(itemId)) {
				if (cartItem.getQuantity() > 0) {
					cartItem.setQuantity(cartItem.getQuantity() - 1);
					LOGGER.info("Dropping one " + cartItem.getItem() + " from the cart.");
				}
			}
		}
		
		this.cartRepository.save(cart);
		
		return "redirect:/?cartId=" + cartId;
	}
	
	@PostMapping("/add/{cartId}/{itemId}")
	String addToCart(@PathVariable Long cartId, @PathVariable Long itemId) {
		Cart cart = this.cartRepository.findById(cartId)
			.orElseThrow(() -> new CartNotFoundExcetion(cartId));
		
		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem.getItem().getId().equals(itemId)) {
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				LOGGER.info("Adding one " + cartItem.getItem() + " to the cart.");
			}
		}
		
		this.cartRepository.save(cart);
		
		return "redirect:/?cartId=" + cartId;
	}
	
	@PostMapping("/order/{cartId}")
	String placeOrder(@PathVariable Long cartId) {
		Cart cart = this.cartRepository.findById(cartId)
			.orElseThrow(() -> new CartNotFoundExcetion(cartId));
		
		LOGGER.info("Firing off some other service to fulfill " + cart);
		
		this.cartRepository.delete(cart);
		
		return "redirect:/";
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such cart")
	class CartNotFoundExcetion extends RuntimeException {
		private static final long serialVersionUID = 7309225444965969500L;

		public CartNotFoundExcetion(Long cartId) {
			super("Could not find cart " + cartId);
		}
	}
}
