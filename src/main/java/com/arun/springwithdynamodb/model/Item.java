package com.arun.springwithdynamodb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private String id;
    private String name;
    private String description;
    private int totalRating;
    private int totalComments;
}
