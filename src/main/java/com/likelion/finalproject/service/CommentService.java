package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.ValidateUserPostDto;
import com.likelion.finalproject.domain.dto.comment.CommentDeleteResponse;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.enums.AlarmType;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.AlarmRepository;
import com.likelion.finalproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final ValidateService validateService;
    private final AlarmRepository alarmRepository;

    /**
     * 댓글 전체 조회
     */
    public List<CommentResponse> getAllComment(Long postId, Pageable pageable) {
        //post 유무 확인
        Post post = validateService.validatePost(postId);

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
        //로그인한 사용자와 포스트 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //댓글 저장
        Comment comment = Comment.toEntity(request, validateUserPost.getPost(), validateUserPost.getUser());
        Comment savedComment = commentRepository.save(comment);

        //본인이 아닐경우는 알람에 저장
        Alarm alarm = Alarm.toEntity(AlarmType.COMMENT, validateUserPost.getUser(), validateUserPost.getPost());
        if(validateUserPost.getUser().getId() != validateUserPost.getPost().getUser().getId()) {
            alarmRepository.save(alarm);
        }

        return CommentResponse.fromEntity(savedComment);
    }

    /**
     * 댓글 수정
     */
    public CommentResponse updateComment(Long postId, Long commentId, CommentRequest request, String userName) {
        //로그인한 사용자와 포스트 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));

        comment.update(request.getComment());
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }


    /**
     * 댓글 삭제
     */
    public CommentDeleteResponse deleteComment(Long postId, Long id, String userName) {
        //로그인한 사용자와 포스트 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //작성자 != 삭제자
        if(validateUserPost.getUser().getId() != validateUserPost.getPost().getUser().getId()){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }

        commentRepository.deleteById(id);
        return new CommentDeleteResponse("댓글 삭제 완료", id);

    }

}
