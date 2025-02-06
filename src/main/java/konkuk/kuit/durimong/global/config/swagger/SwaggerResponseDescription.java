package konkuk.kuit.durimong.global.config.swagger;


import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static konkuk.kuit.durimong.global.exception.ErrorCode.*;

@Getter
public enum SwaggerResponseDescription {

    //User
    USER_SIGNUP(new LinkedHashSet<>(Set.of(
            USER_DUPLICATE_ID
    ))),
    USER_ID(new LinkedHashSet<>(Set.of(
            USER_DUPLICATE_ID
    ))),
    USER_PASSWORD(new LinkedHashSet<>(Set.of(
            USER_PASSWORD_ENGLISH,
            USER_PASSWORD_SHORT,
            USER_PASSWORD_NONUM
    ))),
    USER_EMAIL(new LinkedHashSet<>(Set.of(
            USER_DUPLICATE_EMAIL
    ))),
    USER_LOGIN(new LinkedHashSet<>(Set.of(
            USER_NOT_MATCH_PASSWORD,
            USER_NOT_FOUND
    ))),
    USER_EMAIL_VERIFICATION(new LinkedHashSet<>(Set.of(
            USER_EMAIL_VERIFY_FAILED
    ))),
    USER_MYPAGE(new LinkedHashSet<>(Set.of())),
    REISSUE_TOKEN(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            JWT_EXPIRE_TOKEN,
            JWT_ERROR_TOKEN,
            BAD_REQUEST
    ))),
    USER_HOME(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            MONG_NOT_FOUND,
            QUESTION_NOT_EXISTS
    ))),
    USER_LOGOUT(new LinkedHashSet<>(Set.of(
            JWT_LOGOUT_TOKEN,
            USER_LOGOUTED
    ))),
    USER_EDIT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            MONG_NOT_FOUND,
            USER_SAME_NAME,
            MONG_SAME_NAME
    ))),
    USER_EDIT_PWD(new LinkedHashSet<>(Set.of(
            USER_NOT_MATCH_PASSWORD,
            USER_NOT_FOUND,
            USER_SAME_PASSWORD,
            USER_PASSWORD_ENGLISH,
            USER_PASSWORD_SHORT,
            USER_PASSWORD_NONUM
    ))),
    USER_UNREGISTER(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            MONG_NOT_FOUND
    ))),
    USER_ELIMINATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    USER_ANSWER(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            CONVERSATION_NOT_EXISTS
    ))),
    USER_EDIT_ANSWER(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            CONVERSATION_NOT_EXISTS
    ))),
    USER_CHAT_HISTORY(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    USER_DELETE_CHAT(new LinkedHashSet<>(Set.of(
            CONVERSATION_NOT_EXISTS
    ))),
    USER_NOTIFICATION(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            MONG_NOT_FOUND
    ))),

    //Mong
    MONG_NAME(new LinkedHashSet<>(Set.of(
            MONG_NAME_LENGTH
    ))),
    MONG_CREATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),

    //COLUMN
    COLUMN_CATEGORY(new LinkedHashSet<>(Set.of(
            COLUMN_CATEGORY_NOT_FOUND,
            COLUMN_CATEGORY_DETAIL_NOT_FOUND
    ))),
    COLUMN_SEARCH(new LinkedHashSet<>(Set.of(
            KEYWORD_NOT_EXISTS,
            KEYWORD_LENGTH_OVER,
            KEYWORD_RESULT_NOT_EXISTS
    ))),
    COLUMN_VIEW(new LinkedHashSet<>(Set.of(
            COLUMN_NOT_EXISTS
    ))),
    CHAT_BOT(new LinkedHashSet<>(Set.of(
            CHATBOT_NOT_EXISTS
    ))),
    CHAT_START(new LinkedHashSet<>(Set.of(
            CHATBOT_NOT_FOUND,
            CHATBOT_PARSE_ERROR
    ))),
    CHATBOT_PREDICT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            CHATBOT_NOT_FOUND,
            CHATBOT_PARSE_ERROR,
            CHATBOT_SYMPOMS_EMPTY,
            CHATBOT_PREDICT_ERROR
    ))),

    //ACTIVITY
    ACTIVITY_TEST_LIST(new LinkedHashSet<>(Set.of(
            ACTIVITY_NOT_FOUND,
            TEST_NOT_FOUND,
            USER_NOT_FOUND,
            MONG_NOT_FOUND
    ))),
    ACTIVITY_EXIST(new LinkedHashSet<>(Set.of(
            ACTIVITY_ID_NOT_EXISTS,
            USER_NOT_FOUND,
            ACTIVITY_ALREADY_CHECKED
    ))),

    //USER RECORD
    USER_RECORD(new LinkedHashSet<>(Set.of(
            USER_RECORD_NOT_FOUND
    ))),
    USER_RECORD_DATE(new LinkedHashSet<>(Set.of(
            USER_RECORD_DATE_NOT_VALID,
            USER_NOT_FOUND
    ))),
    USER_RECORD_DAY(new LinkedHashSet<>(Set.of(
            USER_RECORD_DATE_NOT_VALID,
            USER_NOT_FOUND,
            MONG_NOT_FOUND
    ))),
    // Diary
    Diary(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            MONG_NOT_FOUND,
            USER_RECORD_DATE_NOT_VALID
    ))),
    // Test
    TEST_DESCRIPTION(new LinkedHashSet<>(Set.of(
            TEST_ID_NOT_EXISTS
    )));

    private Set<ErrorCode> errorCodeList;

    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        // 공통 에러
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
                SERVER_UNTRACKED_ERROR,
                INVALID_PARAMETER,
                PARAMETER_VALIDATION_ERROR,
                PARAMETER_GRAMMAR_ERROR,
                UNAUTHORIZED,
                FORBIDDEN,
                JWT_ERROR_TOKEN,
                JWT_EXPIRE_TOKEN,
                AUTHORIZED_ERROR,
                OBJECT_NOT_FOUND
        )));

        if (this.name().contains("USER_")) {
            errorCodeList.add(USER_NOT_FOUND);
        }

        this.errorCodeList = errorCodeList;
    }
}