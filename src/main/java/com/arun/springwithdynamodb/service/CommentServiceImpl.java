package com.arun.springwithdynamodb.service;

import com.arun.springwithdynamodb.dao.CommentDao;
import com.arun.springwithdynamodb.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentDao commentDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public void createTable() {
        commentDao.createTable();
    }

    @Override
    public Message put(Message message) {
        return commentDao.put(message);
    }

    @Override
    public Message get(String itemId, String messageId) {
        return commentDao.get(itemId, messageId);
    }

    @Override
    public void delete(String itemId, String messageId) {
        commentDao.delete(itemId, messageId);
    }

    @Override
    public List<Message> getAll() {
        return commentDao.getAll();
    }
}
