package com.arun.springwithdynamodb.controller;

import com.arun.springwithdynamodb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/v1/comments")
    public ResponseEntity<HttpStatus> createTable() {
        commentService.createTable();
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
