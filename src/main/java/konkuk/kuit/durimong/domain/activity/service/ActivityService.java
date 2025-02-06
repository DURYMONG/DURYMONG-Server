package konkuk.kuit.durimong.domain.activity.service;

import konkuk.kuit.durimong.domain.activity.dto.request.CheckActivityReq;
import konkuk.kuit.durimong.domain.activity.dto.request.WriteDiaryReq;
import konkuk.kuit.durimong.domain.activity.dto.response.*;
import konkuk.kuit.durimong.domain.activity.entity.Activity;
import konkuk.kuit.durimong.domain.activity.entity.Diary;
import konkuk.kuit.durimong.domain.activity.entity.UserRecord;
import konkuk.kuit.durimong.domain.activity.repository.ActivityBoxRepository;
import konkuk.kuit.durimong.domain.activity.repository.ActivityRepository;
import konkuk.kuit.durimong.domain.activity.repository.DiaryRepository;
import konkuk.kuit.durimong.domain.activity.repository.UserRecordRepository;
import konkuk.kuit.durimong.domain.mong.entity.Mong;
import konkuk.kuit.durimong.domain.mong.repository.MongRepository;
import konkuk.kuit.durimong.domain.test.repository.TestRepository;
import konkuk.kuit.durimong.domain.user.entity.User;
import konkuk.kuit.durimong.domain.user.repository.UserRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final DiaryRepository diaryRepository;

    // 모든 활동 & 테스트 리스트 조회
    public ActivityTestListRes getActivityTestList(Long userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() ->  new CustomException(ErrorCode.USER_NOT_FOUND));

        Mong mong = mongRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.MONG_NOT_FOUND));

        String mongImage = mong.getImage();
        String mongName = mong.getName();
        // 오늘 날짜에 체크됐는 지 확인

        List<ActivityTestListRes.ActivityListDTO> activityList = activityRepository.findAll().stream()
                .map(activity -> {
                            // 이 activity를 넘기면 이 아이디 및 현재 유저id 해당하는 userRecordId가 있는지 확인
                            boolean isChecked = userRecordRepository.existsByUser_UserIdAndActivity_ActivityIdAndCreatedAt(userId, activity.getActivityId(), LocalDate.now());
                            return new ActivityTestListRes.ActivityListDTO(activity.getActivityId(),activity.getName(), isChecked);
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
    public void makeUserRecord(CheckActivityReq req, Long userId) {
        Activity activity = activityRepository.findById(req.getActivityId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_ID_NOT_EXISTS));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean alreadyChecked = userRecordRepository.existsByUser_UserIdAndActivity_ActivityIdAndCreatedAt(
                userId, activity.getActivityId(), LocalDate.now());

        if(alreadyChecked){
            throw new CustomException(ErrorCode.ACTIVITY_ALREADY_CHECKED);
        }

        // userRecord 생성
        UserRecord userRecord = UserRecord.builder()
                .createdAt(LocalDate.now())
                .activity(activity)
                .user(user)
                .build();

        // 생성된 userRecord 저장
        userRecordRepository.save(userRecord);
    }

    // 활동 체크 취소
    public void deleteUserRecord(Long activityId, Long userId) {
        UserRecord userRecord = userRecordRepository.findByActivity_ActivityIdAndCreatedAtAndUser_UserId(activityId, LocalDate.now(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_RECORD_NOT_FOUND));

        userRecordRepository.delete(userRecord);
    }

    // 성장일지 조회
    public ActivityRecordRes getMonthActivityRecord(int targetYear, int targetMonth, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String nickName = user.getNickname();

        // 현재 시간의 년/월
        LocalDate today = LocalDate.now().withDayOfMonth(1);
        // 유저가 가입한 년/월
        LocalDate createdAt = LocalDate.from(user.getCreatedAt().withDayOfMonth(1));
        // 타겟일의 년/월
        LocalDate targetDate = LocalDate.of(targetYear,targetMonth,1);
        if(! isValidRange(targetDate,createdAt,today)){
            throw  new CustomException(ErrorCode.USER_RECORD_DATE_NOT_VALID);
        }

        // userId가 특정 targetDate의 모든 월에 대한 날짜를 가져온다.
        Map<LocalDate, Integer> countPerDays = userRecordRepository.findActivityCountByMonth(userId, targetYear, targetMonth).stream()
                .collect(Collectors.toMap(row -> (LocalDate) row[0], row -> ((Number) row[1]).intValue()));

        int lastDayofMonth = targetDate.lengthOfMonth();
        List<ActivityRecordRes.DayActivityCountDTO> activityCountList = IntStream.rangeClosed(1, lastDayofMonth)
                .mapToObj(day -> {
                    LocalDate date = LocalDate.of(targetYear,targetMonth,day);
                    return new ActivityRecordRes.DayActivityCountDTO(date, countPerDays.getOrDefault(date,0));
                })
                .toList();

        return new ActivityRecordRes(nickName, targetMonth, activityCountList);
    }

    //일지 일별 조회
    public ActivityDayRecordRes getDayActivityRecord(LocalDate targetDate, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유저가 가입한 날짜
        LocalDate createdAt = LocalDate.from(user.getCreatedAt());
        // 현재 시간의 년/월
        LocalDate today = LocalDate.now();
        // 날짜 유효성 검사
        if(! isValidRange(targetDate,createdAt,today)){
            throw  new CustomException(ErrorCode.USER_RECORD_DATE_NOT_VALID);
        }

        Mong mong = mongRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.MONG_NOT_FOUND));

        String mongName = mong.getName();
        String mongImage = mong.getImage();

        return new ActivityDayRecordRes(targetDate,mongName,mongImage);
    }

    // 일기 조회
    public DiaryRes getDiary(LocalDate targetDate, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Mong mong = mongRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.MONG_NOT_FOUND));
        String mongImage = mong.getImage();

        // 유저가 가입한 날짜
        LocalDate createdAt = LocalDate.from(user.getCreatedAt());
        // 현재 시간의 년/월
        LocalDate today = LocalDate.now();
        // 날짜 유효성 검사
        if(! isValidRange(targetDate,createdAt,today)){
            throw  new CustomException(ErrorCode.USER_RECORD_DATE_NOT_VALID);
        }

        // 날짜와 유저 이름에 해당하는 일기 내용을 가져온다
        // 존재하지 않으면 null반환
        Optional<Diary> diary = diaryRepository.findByUserAndCreatedAt(user,targetDate);
        String content = diary.map(Diary::getContent).orElse("");

        return new DiaryRes(targetDate, content, mongImage);
    }

    // 일기 작성 or 수정
    public DiaryRes writeDiary(WriteDiaryReq req, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String content = req.getContent();

        Mong mong = mongRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.MONG_NOT_FOUND));
        String mongImage = mong.getImage();

        Optional<Diary> existingDiary = diaryRepository.findByUserAndCreatedAt(user,LocalDate.now());
        Diary diary;
        if (existingDiary.isPresent()) {
            diary = existingDiary.get();
            diary.setContent(content);
        }
        else {
            diary = Diary.builder()
                    .content(content)
                    .createdAt(LocalDate.now())
                    .user(user)
                    .build();
        }

        diaryRepository.save(diary);

        return new DiaryRes(LocalDate.now(), content, mongImage);
    }

    // 날짜  유효성 검사
    public boolean isValidRange(LocalDate targetDate, LocalDate createdDate, LocalDate today) {
        return !targetDate.isBefore(createdDate) && !targetDate.isAfter(today);
    }

}
