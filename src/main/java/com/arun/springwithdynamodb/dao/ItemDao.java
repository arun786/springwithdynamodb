package com.arun.springwithdynamodb.dao;

import com.arun.springwithdynamodb.model.Item;

public interface ItemDao {

    void createItem(Item item);

}
