package konkuk.kuit.durimong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //사용자 정의 ErrorCode로, 발생할 수 있는 예외들을 사용자 정의로 선언하시면 됩니다. 엔티티 별로 추가해주시면 됩니다.
    //Common
    SERVER_UNTRACKED_ERROR(-100, "미등록 서버 에러입니다. 서버 팀에 연락주세요.", 500),
    OBJECT_NOT_FOUND(-101, "조회된 객체가 없습니다.", 406),
    INVALID_PARAMETER(-102, "잘못된 파라미터입니다.", 422),
    PARAMETER_VALIDATION_ERROR(-103, "파라미터 검증 에러입니다.", 422),
    PARAMETER_GRAMMAR_ERROR(-104, "파라미터 문법 에러입니다.", 422),
    INVALID_TYPE_PARAMETER(-106, "잘못된 타입 파라미터입니다.", 422),
    NOT_FOUND_PATH(-108, "존재하지 않는 API 경로입니다.", 404),

    //User
    USER_NOT_FOUND(-300, "존재하지 않는 회원입니다.", 406),
    USER_DUPLICATE_ID(-301, "이미 존재하는 아이디입니다.", 401),
    USER_NOT_MATCH_PASSWORD(-302, "비밀번호가 일치하지 않습니다.", 403);
    //Mong



    private final int errorCode;
    private final String message;
    private final int httpCode;
}
