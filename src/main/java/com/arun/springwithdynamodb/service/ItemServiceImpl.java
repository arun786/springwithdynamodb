package com.arun.springwithdynamodb.service;

import com.arun.springwithdynamodb.dao.ItemDao;
import com.arun.springwithdynamodb.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemServiceImpl implements ItemService {

    private ItemDao itemDao;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void createItem(Item item) {
        itemDao.createItem(item);
    }

}
