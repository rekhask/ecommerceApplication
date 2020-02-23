package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    private User user;
    private Item item1;
    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        user = new User();
        user.setId(1L);
        user.setUsername("u12345");
        user.setPassword("pwd12345");
        item1 = new Item (1L,"Round Widget",  new BigDecimal(2.99), "A widget that is round");

    }

    @Test
    public void add_to_cart(){

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));

        Cart cart = new Cart();
        user.setCart(cart);

        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1);
        req.setQuantity(4);
        req.setUsername("u12345");

        ResponseEntity<Cart> res = cartController.addTocart(req);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        Cart cartRes = res.getBody();
        assertNotNull(cartRes);
        assertEquals(200, res.getStatusCodeValue());
     }


    @Test
    public void add_to_cart_user_notFound() {
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("newUser1234");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_few_items(){
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));

        Cart cart = new Cart();
        user.setCart(cart);

        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1);
        req.setQuantity(4);
        req.setUsername("u12345");

        ResponseEntity<Cart> res = cartController.addTocart(req);

        req.setQuantity(2);
        ResponseEntity<Cart> cartRes = cartController.removeFromcart(req);
        assertNotNull(cartRes);
        assertEquals(200, cartRes.getStatusCodeValue());
        Cart cartPostRemove = cartRes.getBody();
        assertNotNull(cartPostRemove);
        assertEquals(2, cartPostRemove.getItems().size());

    }

    @Test
    public void remove_from_cart_all_items(){
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));

        Cart cart = new Cart();
        user.setCart(cart);

        ModifyCartRequest req = new ModifyCartRequest();
        req.setItemId(1);
        req.setQuantity(4);
        req.setUsername("u12345");

        ResponseEntity<Cart> res = cartController.addTocart(req);

        req.setQuantity(4);
        ResponseEntity<Cart> cartRes = cartController.removeFromcart(req);
        assertNotNull(cartRes);
        assertEquals(200, cartRes.getStatusCodeValue());
        Cart cartPostRemove = cartRes.getBody();
        assertNotNull(cartPostRemove);
        assertEquals(0, cartPostRemove.getItems().size());
    }

}
