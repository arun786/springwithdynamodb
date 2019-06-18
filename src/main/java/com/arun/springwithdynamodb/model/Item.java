package com.arun.springwithdynamodb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    @JsonIgnore
    private String id;
    private String name;
    private String description;
    private int totalRating;
    private int totalComments;
}
