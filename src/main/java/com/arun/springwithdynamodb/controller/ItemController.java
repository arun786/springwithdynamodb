package com.arun.springwithdynamodb.controller;

import com.arun.springwithdynamodb.model.Item;
import com.arun.springwithdynamodb.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/items/v1/item")
    public ResponseEntity<HttpStatus> createItem(@RequestBody Item item) {
        itemService.createItem(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
