package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepo = mock(ItemRepository.class);

    private List<Item> items = new ArrayList<Item>();
    private Item item1;
    private Item item2;
    @Before
    public void setUp() {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        item1 = new Item (1L,"Round Widget",  new BigDecimal(2.99), "A widget that is round");
        item2 = new Item (2L,"Square Widget",  new BigDecimal(1.99), "A widget that is square");
    }

    @Test
    public void find_allItems(){
        items.add(item1);
        items.add(item2);
        when (itemRepo.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> res = itemController.getItems();
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        List<Item> itemsRes = res.getBody();
        assertEquals(2, itemsRes.size());
    }

    @Test
    public void find_by_itemId_1(){
        when (itemRepo.findById(1L)).thenReturn(Optional.of(item1));
        final ResponseEntity<Item> res = itemController.getItemById(1L);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        Item items1Res = res.getBody();
        assertEquals("Round Widget", items1Res.getName());
        assertEquals("A widget that is round", items1Res.getDescription());
    }

    @Test
    public void find_by_itemName(){
        items.add(item2);
        when (itemRepo.findByName("Square Widget")).thenReturn(items);
        final ResponseEntity<List<Item>> res = itemController.getItemsByName("Square Widget");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        List<Item> itemFromRes = res.getBody();
        Item squareItem = itemFromRes.get(0);
        assertEquals("Square Widget", squareItem.getName());
        assertEquals("A widget that is square", squareItem.getDescription());
    }





}
