package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import com.likelion.finalproject.domain.dto.post.PostRequest;
import com.likelion.finalproject.domain.dto.post.PostUpdateResponse;
import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.enums.UserRole;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.CommentRepository;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 전체 조회
     */
    public List<CommentResponse> getAllComment(Long postId, Pageable pageable) {
        //post 유무 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        //해당 포스트의 댓글 가져오기
        Page<Comment> comments = commentRepository.findCommentByPost(post, pageable);
        List<CommentResponse> commentResponseList = comments.stream()
                .map(comment -> CommentResponse.fromEntity(comment)).collect(Collectors.toList());

        return commentResponseList;
    }

    /**
     * 댓글 작성
     */
    public CommentResponse addComment(Long postId, CommentRequest request, String userName) {
        //존재하는 user인지 확인
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        //post 유무 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        Comment comment = Comment.toEntity(request, post, user);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.fromEntity(savedComment);

    }

    /**
     * 댓글 수정
     */
    public CommentResponse updateComment(Long postId, Long commentId, CommentRequest request, String userName) {
        //존재하는 user인지 확인
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
        //post 유무 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        comment.update(request.getComment());
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    /**
     * 댓글 삭제
     */



}
