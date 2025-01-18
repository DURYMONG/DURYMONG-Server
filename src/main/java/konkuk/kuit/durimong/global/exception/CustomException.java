package konkuk.kuit.durimong.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private Exception originException;
    private final ErrorCode errorCode;
    private final String message;
    private final String additionalInfo;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.additionalInfo = null;
    }

    public CustomException(ErrorCode errorCode, String additionalMessage) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.additionalInfo = additionalMessage;
    }

    public CustomException(ErrorCode errorCode, Exception exception) {
        this.errorCode = errorCode;
        this.originException = exception;
        this.message = errorCode.getMessage();
        this.additionalInfo = null;
    }
}
