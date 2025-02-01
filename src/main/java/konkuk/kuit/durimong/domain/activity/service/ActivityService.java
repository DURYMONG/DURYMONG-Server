package konkuk.kuit.durimong.domain.activity.service;

import konkuk.kuit.durimong.domain.activity.dto.request.CheckActivityReq;
import konkuk.kuit.durimong.domain.activity.dto.response.ActivityBoxDescriptionRes;
import konkuk.kuit.durimong.domain.activity.dto.response.ActivityDescriptionRes;
import konkuk.kuit.durimong.domain.activity.dto.response.ActivityTestListRes;
import konkuk.kuit.durimong.domain.activity.dto.response.CheckActivityRes;
import konkuk.kuit.durimong.domain.activity.entity.Activity;
import konkuk.kuit.durimong.domain.activity.entity.ActivityBox;
import konkuk.kuit.durimong.domain.activity.entity.UserRecord;
import konkuk.kuit.durimong.domain.activity.repository.ActivityBoxRepository;
import konkuk.kuit.durimong.domain.activity.repository.ActivityRepository;
import konkuk.kuit.durimong.domain.activity.repository.UserRecordRepository;
import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.domain.test.entity.Test;
import konkuk.kuit.durimong.domain.test.repository.TestRepository;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.annotation.UserId;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final TestRepository testRepository;
    private final ActivityBoxRepository activityBoxRepository;
    private final UserRecordRepository userRecordRepository;
    private final UserRepository userRepository;
    private final MongRepository mongRepository;

    // 모든 활동 & 테스트 리스트 조회
    public ActivityTestListRes getActivityTestList(Long userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        Mong mong = mongRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Mong data not found for userId: {}", userId);
                    return new CustomException(ErrorCode.MONG_NOT_FOUND);
                });

        String mongImage = mong.getImage();
        String mongName = mong.getName();

        List<ActivityTestListRes.ActivityListDTO> activityList = activityRepository.findAll().stream()
                .map(activity -> {
                            // 이 activity를 넘기면 이 아이디 및 현재 유저id 해당하는 userRecordId가 있는지 확인
                            boolean isChecked = userRecordRepository.existsByUser_UserIdAndActivity_ActivityId(userId, activity.getActivityId());
                            return new ActivityTestListRes.ActivityListDTO(activity.getName(), isChecked);
                })
                .toList();

        if(activityList.isEmpty() ){
            throw new CustomException(ErrorCode.ACTIVITY_NOT_FOUND);
        }

        List<ActivityTestListRes.TestListDTO> testList = testRepository.findAll().stream()
                .map(test -> new ActivityTestListRes.TestListDTO(
                        test.getTestId(),
                        test.getName())
                ).toList();

        return new ActivityTestListRes(
                mongImage,
                mongName,
                activityList,
                testList
        );
    }

    // 활동 설명화면 조회
    public Object getActivityDetails(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_ID_NOT_EXISTS));

        boolean hasBoxes = activityBoxRepository.existsByActivity(activity);

        // 박스가 있는 경우
        if (hasBoxes) {
            List<ActivityBoxDescriptionRes.ActivityBoxDTO> activityBoxList = activityBoxRepository.findByActivity(activity)
                    .stream()
                    .map(activityBox -> new ActivityBoxDescriptionRes.ActivityBoxDTO(
                            activityBox.getBoxName(),
                            activityBox.getContent(),
                            activityBox.getEffect(),
                            activityBox.getImage()
                    )).toList();

            return new ActivityBoxDescriptionRes(activity.getName(), activityBoxList);
        }
        // 박스가 없는 경우
        else {
            return new ActivityDescriptionRes(
                    activity.getName(),
                    activity.getIntro(),
                    activity.getEffect(),
                    activity.getTip()
            );
        }
    }

    // 활동 체크
    public CheckActivityRes makeUserRecord(CheckActivityReq req, Long userId) {
        // 현재 날짜 얻는 로직
        LocalDate today = LocalDate.now();
        Activity activity = activityRepository.findById(req.getActivityId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_ID_NOT_EXISTS));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // userRecord 생성
        UserRecord userRecord = UserRecord.builder()
                .createdAt(LocalDate.now())
                .activity(activity)
                .user(user)
                .build();

        // 생성된 userRecord 저장
        UserRecord madeUserRecord = userRecordRepository.save(userRecord);

        return new CheckActivityRes(madeUserRecord.getUserRecordId(),today);
    }

    // 활동 체크 취소
    public void deleteUserRecord(Long userRecordId, Long userId) {
        // 해당 userRecordId에 대한 userRecord삭제

        UserRecord userRecord = userRecordRepository.findById(userRecordId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_RECORD_NOT_FOUND));

        // userRecordId가 userId의 user소유가 아니라면
        if(! userRecord.getUser().getUserId().equals(userId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        userRecordRepository.delete(userRecord);
    }


}
