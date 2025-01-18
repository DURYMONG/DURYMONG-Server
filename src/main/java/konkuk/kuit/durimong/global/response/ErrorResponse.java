package konkuk.kuit.durimong.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse<T> {
    @Schema(description = "성공 여부", nullable = false, example = "false")
    private boolean success = false;

    @Schema(description = "예외 코드", nullable = false, example = "-100")
    private int code;

    @Schema(description = "예외 메세지", nullable = false, example = "예외 메세지")
    private String message;

    @Schema(description = "예외 참고 데이터", nullable = false)
    private T result;

    public ErrorResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> ErrorResponse<T> of(int code, String message) {
        return new ErrorResponse<>(code, message, null);
    }

    public static <T> ErrorResponse<T> of(ErrorCode errorCode) {
        return new ErrorResponse<>(errorCode.getErrorCode(), errorCode.getMessage(), null);
    }

    public static <T> ErrorResponse<T> of(int code, String message, T data) {
        return new ErrorResponse<>(code, message, data);
    }

    public static <T> ErrorResponse<T> of(ErrorCode errorCode, T data) {
        return new ErrorResponse<>(errorCode.getErrorCode(), errorCode.getMessage(), data);
    }
}
