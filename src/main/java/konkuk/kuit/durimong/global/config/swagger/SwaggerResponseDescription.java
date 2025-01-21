package konkuk.kuit.durimong.global.config.swagger;


import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public enum SwaggerResponseDescription {

    USER_SIGNUP(new LinkedHashSet<>(Set.of(
            ErrorCode.USER_DUPLICATE_ID,
            ErrorCode.USER_DUPLICATE_EMAIL,
            ErrorCode.USER_PASSWORD_ENGLISH,
            ErrorCode.USER_PASSWORD_SHORT,
            ErrorCode.USER_PASSWORD_NONUM,
            ErrorCode.USER_EMAIL_VERIFY_FAILED
    ))),
    USER_LOGIN(new LinkedHashSet<>(Set.of(
            ErrorCode.USER_NOT_MATCH_PASSWORD
    ))),
    USER_MYPAGE(new LinkedHashSet<>(Set.of())),
    REISSUE_TOKEN(new LinkedHashSet<>(Set.of(
            ErrorCode.USER_NOT_FOUND
    )))
    ;


    private Set<ErrorCode> errorCodeList;

    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        // 공통 에러
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
                ErrorCode.SERVER_UNTRACKED_ERROR,
                ErrorCode.INVALID_PARAMETER,
                ErrorCode.PARAMETER_VALIDATION_ERROR,
                ErrorCode.PARAMETER_GRAMMAR_ERROR,
                ErrorCode.UNAUTHORIZED,
                ErrorCode.FORBIDDEN,
                ErrorCode.JWT_ERROR_TOKEN,
                ErrorCode.JWT_EXPIRE_TOKEN,
                ErrorCode.AUTHORIZED_ERROR,
                ErrorCode.OBJECT_NOT_FOUND
        )));

        if (this.name().contains("USER_")) {
            errorCodeList.add(ErrorCode.USER_NOT_FOUND);
        }

        this.errorCodeList = errorCodeList;
    }
}