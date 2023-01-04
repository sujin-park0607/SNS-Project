package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommentServiceTest {
    CommentService commentService;
    CommentRepository commentRepository = mock(CommentRepository.class);

}