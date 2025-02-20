package konkuk.kuit.durimong.domain.mong.service;

import konkuk.kuit.durimong.domain.activity.service.ActivityService;
import konkuk.kuit.durimong.domain.mong.dto.request.MongCreateReq;
import konkuk.kuit.durimong.domain.mong.dto.response.MongGrowthRes;
import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.entity.MongImage;
import konkuk.kuit.durimong.domain.mong.repository.MongImageRepository;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MongService {
    private final MongRepository mongRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final MongImageRepository mongImageRepository;

    public String validateName(String mongName) {
        if (mongName.length() > 6) {
            throw new CustomException(MONG_NAME_LENGTH);
        }
        return "두리몽의 이름이 " + mongName + "으로 설정되었습니다.";
    }

    public String createMong(MongCreateReq req, Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        if(mongRepository.findByUser(user).isPresent()){
            throw new CustomException(MONG_ALREADY_EXISTS);
        }
        MongImage mongImage = mongImageRepository.findByMongTypeAndMongColorAndLevel(req.getMongType(),req.getMongColor(),1)
                .orElseThrow(() -> new CustomException(MONG_IMAGE_NOT_FOUND));
        Mong mong = Mong.create(req.getMongName(),mongImage,user);
        mongRepository.save(mong);
        return "두리몽 생성이 완료되었습니다.";
    }

    public MongGrowthRes growMong(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        if(LocalDate.now().isBefore(user.getCreatedAt().minusDays(14).toLocalDate())){
            throw new CustomException(USER_SIGNUP_LESSTHAN_TWOWEEK);
        }
        Mong mong = mongRepository.findByUser(user).orElseThrow(() -> new CustomException(MONG_NOT_FOUND));
        if(mong.getLevel() == 3){
            throw new CustomException(MONG_MAX_LEVEL);
        }
        if (mong.getLastGrowthDate() != null &&
                mong.getLastGrowthDate().plusDays(15).isAfter(LocalDate.now())) {
            throw new CustomException(MONG_CANNOT_GROW_YET);
        }
        boolean hasCompletedAllDays = activityService.hasUserCompletedThreeActivitiesDailyFor15Days(user);
        if(!hasCompletedAllDays){
            throw new CustomException(USER_NOT_COMPLETED_15DAYS);
        }
        MongImage currentMongImage = mong.getMongImage();

        MongImage nextMongImage = mongImageRepository.findByMongTypeAndMongColorAndLevel(currentMongImage.getMongType(),
                currentMongImage.getMongColor(),
                currentMongImage.getLevel() + 1).
                orElseThrow( () -> new CustomException(MONG_IMAGE_NOT_FOUND));
        mong.levelUp(nextMongImage);
        mongRepository.save(mong);

        return new MongGrowthRes(mong.getMongImage().getImageUrl(),"안녕하세요! 잘 부탁해요!");
    }
}

