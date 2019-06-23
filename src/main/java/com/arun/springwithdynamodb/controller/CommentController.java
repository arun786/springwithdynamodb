package com.arun.springwithdynamodb.controller;

import com.arun.springwithdynamodb.model.Message;
import com.arun.springwithdynamodb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/v1/comments/create")
    public ResponseEntity<HttpStatus> createTable() {
        commentService.createTable();
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @PostMapping("/comment/v1/comments")
    public ResponseEntity<Message> put(@RequestBody Message message) {
        Message put = commentService.put(message);
        return new ResponseEntity<>(put, HttpStatus.CREATED);

    }

    @GetMapping("/comment/v1/comments")
    public ResponseEntity<Message> get(@RequestParam String itemId,
                                       @RequestParam String messageId) {
        Message put = commentService.get(itemId, messageId);
        return new ResponseEntity<>(put, HttpStatus.OK);

    }

    @DeleteMapping("/comment/v1/comments")
    public ResponseEntity<HttpStatus> delete(@RequestParam String itemId,
                                             @RequestParam String messageId) {
        commentService.delete(itemId, messageId);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @GetMapping("/comment/v1/comments/all")
    public ResponseEntity<List<Message>> getAll(){
        List<Message> getAll = commentService.getAll();
        return new ResponseEntity<>(getAll, HttpStatus.OK);
    }
}
