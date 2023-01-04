package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.ValidateUserPostDto;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.enums.AlarmType;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.AlarmRepository;
import com.likelion.finalproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final ValidateService validateService;
    private final AlarmRepository alarmRepository;

    public String controlLike(Long postId, String userName) {
        //유저와 게시물의 존재 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //like db에 user과 post가 같은게 존재하는지 확인
        Optional<Like> like = likeRepository.findByUserAndPost(validateUserPost.getUser(), validateUserPost.getPost());

        //좋아요와 취소 로직
        if(like.isPresent()){
            //알람에서 type, 작성한 유저, 해당 포스트가 같을경우
            Optional<Alarm> alarm = alarmRepository.cancelAlarm(AlarmType.LIKE.getAlarmType(), validateUserPost.getUser().getId(), validateUserPost.getPost().getId());
            if(alarm.isPresent()){
                alarmRepository.delete(alarm.get());
            }
            likeRepository.delete(like.get());

            return "좋아요를 취소했습니다.";
        }else{
            likeRepository.save(Like.toEntity(validateUserPost.getPost(), validateUserPost.getUser()));

            //알람 저장
            Alarm alarm = Alarm.toEntity(AlarmType.LIKE, validateUserPost.getUser(), validateUserPost.getPost());
            alarmRepository.save(alarm);
            return "좋아요를 눌렀습니다.";
        }
    }

    public Integer countLike(Long postId) {
        //유저와 게시물의 존재 유무 확인
        Post post = validateService.validatePost(postId);

        //해당 포스트의 like 개수 확인
        Integer likeCnt = likeRepository.countByPost(post);

        return likeCnt;
    }
}
