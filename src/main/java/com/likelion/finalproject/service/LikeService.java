package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.ValidateUserPostDto;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.enums.AlarmType;
import com.likelion.finalproject.repository.AlarmRepository;
import com.likelion.finalproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final ValidateService validateService;
    private final AlarmRepository alarmRepository;

    /**
     * 좋아요 및 좋아요 취소
     */
    public String controlLike(Long postId, String userName) {
        //결과 메세지
        String resultMessage;

        //유저와 게시물의 존재 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //like db에 user과 post가 같은게 존재하는지 확인
        Optional<Like> like = likeRepository.findByUserAndPost(validateUserPost.getUser(), validateUserPost.getPost());

        //알람 데이터 생성
        Alarm alarm = Alarm.toEntity(AlarmType.LIKE, validateUserPost.getUser(), validateUserPost.getPost());

        //좋아요를 처음 누를 경우
        if (!like.isPresent()) {
            resultMessage = firstPressLike(alarm, validateUserPost);

        //이미 좋아요 및 취소를 누른 적 있는 경우
        }else{
            //알람 기록 확인
            Optional<Integer> deleteAlarm = alarmRepository.cancelAlarm(AlarmType.LIKE.getAlarmType(), validateUserPost.getUser().getId(), validateUserPost.getPost().getId());

            //좋아요 취소
            if (like.get().getDeletedAt() == null) {
                resultMessage = cancelLike(like, deleteAlarm);

            //다시 좋아요 누르기
            } else {
                resultMessage = pressLike(like, validateUserPost, deleteAlarm);
            }
        }
        return resultMessage;
    }

    /**
     * 좋아요를 처음 누를 경우
     */
    public String firstPressLike(Alarm alarm, ValidateUserPostDto validateUserPost){
        //좋아요
        likeRepository.save(Like.toEntity(validateUserPost.getPost(), validateUserPost.getUser()));

        //본인이 아닐경우는 알람에 저장
        if(validateUserPost.getUser().getId() != validateUserPost.getPost().getUser().getId()) {
            alarmRepository.save(alarm);
        }
        return "좋아요를 눌렀습니다.";
    }

    /**
     * 좋아요 데이터 존재 - 좋아요 취소
     */
    public String cancelLike(Optional<Like> like, Optional<Integer> deleteAlarm){
        if (deleteAlarm.isPresent()) {
            alarmRepository.deleteById(deleteAlarm.get());
        }
        likeRepository.delete(like.get());
        return "좋아요를 취소했습니다.";
    }

    /**
     * 좋아요 데이터 존재 - 좋아요
     */
    public String pressLike(Optional<Like> like, ValidateUserPostDto validateUserPost, Optional<Integer> deleteAlarm){
        likeRepository.reSave(like.get().getId());
        //본인이 아닐경우는 알람에 저장 - deleteAt null값으로 바꾸기
        if(validateUserPost.getUser().getId() != validateUserPost.getPost().getUser().getId()){
            alarmRepository.reSave(deleteAlarm.get());
        }
        return "좋아요를 눌렀습니다.";
    }



    /**
     * 좋아요 조회
     */
    public Integer countLike(Long postId) {
        //유저와 게시물의 존재 유무 확인
        Post post = validateService.validatePost(postId);

        //해당 포스트의 like 개수 확인
        Integer likeCnt = likeRepository.countBypost(post);

        return likeCnt;
    }
}
