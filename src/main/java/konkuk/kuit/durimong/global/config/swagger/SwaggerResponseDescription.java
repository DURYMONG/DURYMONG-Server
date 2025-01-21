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