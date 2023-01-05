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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final ValidateService validateService;
    private final AlarmRepository alarmRepository;

    public String controlLike(Long postId, String userName) {
        //유저와 게시물의 존재 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //like db에 user과 post가 같은게 존재하는지 확인
        Optional<Like> like = likeRepository.findByUserAndPost(validateUserPost.getUser(), validateUserPost.getPost());
        //좋아요를 처음 누를 경우
        if (!like.isPresent()) {
            likeRepository.save(Like.toEntity(validateUserPost.getPost(), validateUserPost.getUser()));

            Alarm alarm = Alarm.toEntity(AlarmType.LIKE, validateUserPost.getUser(), validateUserPost.getPost());
            alarmRepository.save(alarm);
            return "좋아요를 눌렀습니다.";

        //이미 좋아요 및 취소를 누른 적 있는 경우
        }else{
            //deletedAt 기록 확인
            LocalDateTime likeDeleteAt = like.get().getDeletedAt();

            //좋아요 취소
            if (likeDeleteAt == null) {
                Optional<Alarm> alarm = alarmRepository.cancelAlarm(AlarmType.LIKE.getAlarmType(), validateUserPost.getUser().getId(), validateUserPost.getPost().getId());
                if (alarm.isPresent()) {
                    alarmRepository.delete(alarm.get());
                }
                likeRepository.delete(like.get());

                return "좋아요를 취소했습니다.";

                //다시 좋아요 누르기
            } else {
                likeRepository.reSave(like.get().getId());

                //알람 저장
                Alarm alarm = Alarm.toEntity(AlarmType.LIKE, validateUserPost.getUser(), validateUserPost.getPost());
                alarmRepository.save(alarm);
                return "좋아요를 눌렀습니다.";

            }

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
