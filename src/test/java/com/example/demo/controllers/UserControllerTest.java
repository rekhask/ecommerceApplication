package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private User user;

    private String hashedPwd;

    @Before
    public  void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        user = new User();
        user.setId(1L);
        user.setUsername("u12345");
        user.setPassword("pwd12345");
        hashedPwd="hashedU12345Pwd";
    }

    @Test
    public void find_by_userId() {
        user.setPassword(hashedPwd);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        final ResponseEntity<User> res = userController.findById(1L);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        User user1 = res.getBody();
        assertNotNull(user1);
        assertEquals(1, user1.getId());
        assertEquals(user.getUsername(), user1.getUsername());
        assertEquals(hashedPwd, user1.getPassword());
    }

    @Test
    public void find_by_username() {
        user.setPassword(hashedPwd);
        when(userRepo.findByUsername("u12345")).thenReturn(user);
        final ResponseEntity<User> res = userController.findByUserName("u12345");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        User user1 = res.getBody();
        assertNotNull(user1);
        assertEquals(1, user1.getId());
        assertEquals("u12345", user1.getUsername());
        assertEquals(hashedPwd, user1.getPassword());
    }


    @Test
    public void create_user() throws Exception {
        when(encoder.encode(user.getPassword())).thenReturn(hashedPwd);
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername(user.getUsername());
        req.setPassword(user.getPassword());
        req.setConfirmPassword(user.getPassword());

        ResponseEntity<User> res = userController.createUser(req);

        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        User user1 = res.getBody();
        assertNotNull(user);
        assertEquals(0, user1.getId());
        assertEquals("u12345", user1.getUsername());
        assertEquals(hashedPwd, user1.getPassword());
    }





}
