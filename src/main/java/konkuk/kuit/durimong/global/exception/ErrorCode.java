package konkuk.kuit.durimong.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //사용자 정의 ErrorCode로, 발생할 수 있는 예외들을 사용자 정의로 선언하시면 됩니다. 엔티티 별로 추가해주시면 됩니다.
    /*
    400 (Bad Request): 잘못된 요청 (파라미터, 문법, 인증 정보 등)
    401 (Unauthorized): 인증되지 않은 사용자
    403 (Forbidden): 권한이 없는 사용자
    404 (Not Found): 리소스나 경로가 존재하지 않는 경우
    422 (Unprocessable Entity): 요청이 의미적으로 잘못되었을 때
    500 (Internal Server Error): 서버에서 처리할 수 없는 예외 발생

     */
    // Common
    SERVER_UNTRACKED_ERROR(-100, "미등록 서버 에러입니다. 서버 팀에 연락주세요.", 500),
    OBJECT_NOT_FOUND(-101, "조회된 객체가 없습니다.", 404),
    INVALID_PARAMETER(-102, "잘못된 파라미터입니다.", 400),
    PARAMETER_VALIDATION_ERROR(-103, "파라미터 검증 에러입니다.", 400),
    PARAMETER_GRAMMAR_ERROR(-104, "파라미터 문법 에러입니다.", 400),
    INVALID_TYPE_PARAMETER(-106, "잘못된 타입 파라미터입니다.", 400),
    NOT_FOUND_PATH(-108, "존재하지 않는 API 경로입니다.", 404),
    UNABLE_TO_SEND_EMAIL(-109,"이메일을 전송할 수 없습니다.", 500),
    NO_SUCH_ALGORITHM(-110,"사용 불가능한 암호화 알고리즘입니다.", 500),
    BAD_REQUEST(-111,"로그아웃된 사용자입니다.", 400),
    // Auth
    UNAUTHORIZED(-200, "인증 자격이 없습니다.", 401),
    FORBIDDEN(-201, "권한이 없습니다.", 403),
    JWT_ERROR_TOKEN(-202, "잘못된 토큰입니다.", 401),
    JWT_EXPIRE_TOKEN(-203, "만료된 토큰입니다.", 401),
    AUTHORIZED_ERROR(-204, "인증 과정 중 에러가 발생했습니다.", 500),
    AUTHENTICATION_SETTING_FAIL(-207, "인증정보 처리에 실패했습니다.", 500),
    INVALID_TOKEN(-208,"저장된 토큰과 일치하지 않습니다.", 401),
    NOT_FOUND_AVAILABLE_PORT(-209,"이용 가능한 포트를 찾지 못했습니다.", 500),
    ERROR_EXECUTING_EMBEDDED_REDIS(-210,"REDIS 서버 실행 중 오류가 발생했습니다.", 500),
    JWT_LOGOUT_TOKEN(-211,"로그아웃된 토큰입니다.", 401),
    // User
    USER_NOT_FOUND(-300, "존재하지 않는 회원입니다.", 404),
    USER_DUPLICATE_ID(-301, "이미 존재하는 아이디입니다.", 409),
    USER_NOT_MATCH_PASSWORD(-302, "비밀번호가 일치하지 않습니다.", 403),
    USER_DUPLICATE_EMAIL(-303,"이미 사용중인 이메일입니다.", 409),
    USER_PASSWORD_SHORT(-304,"비밀번호는 6자 이상 10자 이하입니다.", 400),
    USER_PASSWORD_NONUM(-305,"비밀번호에 숫자가 포함되어야 합니다.", 400),
    USER_PASSWORD_ENGLISH(-306,"비밀번호에 영문자가 포함되어야 합니다.", 400),
    USER_EMAIL_VERIFY_FAILED(-307,"이메일 인증에 실패하였습니다.", 400),
    USER_LOGOUTED(-308,"로그아웃 되어 있는 상태입니다.", 401),
    USER_SAME_NAME(-309,"현재 사용자의 이름과 수정하시려는 이름이 일치합니다.", 400),
    USER_SAME_PASSWORD(-310,"현재 비밀번호와 새 비밀번호가 일치합니다.", 400),
    // Mong
    MONG_NAME_LENGTH(-400,"이름은 1~6자로 입력해주세요.", 400),
    MONG_NOT_FOUND(-401, "캐릭터를 생성하지 않았습니다.", 404),
    QUESTION_NOT_EXISTS(-402, "등록된 몽의 질문이 없습니다.", 404),
    MONG_SAME_NAME(-403,"현재 몽의 이름과 수정하시려는 몽의 이름이 일치합니다.", 400),
    //UserMongConversation
    CONVERSATION_NOT_EXISTS(-500,"몽과의 대화가 존재하지 않습니다,",404),
    //Column
    COLUMN_CATEGORY_NOT_FOUND(-600, "등록된 카테고리가 없습니다",404),
    COLUMN_CATEGORY_DETAIL_NOT_FOUND(-601, "등록된 카테고리 설명이 없습니다", 404),
    COLUMN_NOT_FOUND(-602,"등록된 칼럼이 없습니다.", 404);

    private final int errorCode;
    private final String message;
    private final int httpCode;
}
