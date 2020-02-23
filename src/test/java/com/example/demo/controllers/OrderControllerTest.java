package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    private Cart cart;
    private User user;
    private Item item1;
    private Item item2;

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        user = new User();
        user.setId(1L);
        user.setUsername("u12345");
        user.setPassword("pwd12345");
        item1 = new Item(1L,"Round Widget",  new BigDecimal(3), "A widget that is round");
        item2 = new Item (2L,"Square Widget",  new BigDecimal(2), "A widget that is square");
    }

    @Test
    public void submit_order(){
        cart = new Cart();
        cart.addItem(item1);
        cart.addItem(item2);
        cart.setUser(user);

        user.setCart(cart);
        when (userRepo.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<UserOrder> orderRes = orderController.submit(user.getUsername());
        assertNotNull(orderRes);
        assertEquals(200, orderRes.getStatusCodeValue());
    }

    @Test
    public void user_order_history() {
        UserOrder userOrder1 = new UserOrder();
        UserOrder userOrder2 = new UserOrder();

        userOrder1.setUser(user);
        userOrder1.setItems(Arrays.asList(item1));
        userOrder1.setItems(Arrays.asList(item1));
        userOrder1.setTotal(new BigDecimal(6));

        userOrder2.setUser(user);
        userOrder2.setItems(Arrays.asList(item1));
        userOrder2.setItems(Arrays.asList(item2));
        userOrder2.setTotal(new BigDecimal(5));

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(Arrays.asList( userOrder1, userOrder2));

        ResponseEntity<List<UserOrder>> userOrderRes = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(userOrderRes);
        assertEquals(200, userOrderRes.getStatusCodeValue());

    }

}
