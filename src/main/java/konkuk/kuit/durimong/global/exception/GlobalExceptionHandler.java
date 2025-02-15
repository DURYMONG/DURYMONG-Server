package konkuk.kuit.durimong.global.exception;

import io.jsonwebtoken.ExpiredJwtException;
import konkuk.kuit.durimong.global.response.ErrorResponse;
import konkuk.kuit.durimong.global.response.result.ParameterData;
import konkuk.kuit.durimong.global.response.result.ServerErrorData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

import static konkuk.kuit.durimong.global.exception.ErrorCode.JWT_EXPIRE_TOKEN;
import static konkuk.kuit.durimong.global.exception.ErrorCode.NOT_FOUND_PATH;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 등록되지 않은 예외
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse<ServerErrorData> handleUntrackedException(Exception e) {
        log.error("[UNTRACKED ERROR] class: [{}], message: [{}]",
                e.getClass().getSimpleName(),
                e.getMessage());

        ServerErrorData serverErrorData = ServerErrorData.builder()
                .errorClass(e.getClass().toString())
                .errorMessage(e.getMessage())
                .build();
        return ErrorResponse.of(ErrorCode.SERVER_UNTRACKED_ERROR, serverErrorData);
    }

    /**
     * 파라미터 검증 예외
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse<List<ParameterData>> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.warn("[PARAMETER VALIDATION EXCEPTION] class: [{}], message: [{}], localizedMessage: [{}]",
                e.getClass().getSimpleName(),
                e.getMessage(),
                e.getLocalizedMessage());

        List<ParameterData> list = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ParameterData parameterData = ParameterData.builder()
                    .key(fieldError.getField())
                    .value(fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString())
                    .reason(fieldError.getDefaultMessage())
                    .build();
            list.add(parameterData);
        }

        return ErrorResponse.of(ErrorCode.PARAMETER_VALIDATION_ERROR, list);
    }


    /**
     * 파라미터 문법 예외
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse<String> handleHttpMessageParsingExceptions(HttpMessageNotReadableException e) {
        log.warn("[PARAMETER GRAMMAR EXCEPTION] class: [{}], message: [{}]",
                e.getClass().getSimpleName(),
                e.getMessage());

        return ErrorResponse.of(ErrorCode.PARAMETER_GRAMMAR_ERROR);
    }

    /**
     * 매개변수 타입 예외
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse<String> handleHttpMessageParsingExceptions(MethodArgumentTypeMismatchException e) {
        log.warn("[METHOD ARGUMENT TYPE EXCEPTION] class: [{}], message: [{}]",
                e.getClass().getSimpleName(),
                e.getMessage());

        return ErrorResponse.of(ErrorCode.INVALID_PARAMETER, e.getMessage());
    }

    /**
     * 커스텀 예외
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse<Object>> handleCustomExceptions(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        if (e.getOriginException() != null) {
            log.error("[ORIGIN ERROR] class: [{}], message: [{}], localizedMessage: [{}]",
                    e.getOriginException().getClass().getSimpleName(),
                    e.getOriginException().getMessage(),
                    e.getOriginException().getLocalizedMessage());
        }
        log.warn("[CUSTOM EXCEPTION] class: [{}], message: [{}]",
                e.getClass().getSimpleName(),
                errorCode.getMessage());

        ErrorResponse<Object> body = ErrorResponse.of(errorCode, e.getAdditionalInfo());
        HttpStatus httpStatus = HttpStatus.valueOf(errorCode.getHttpCode());
        return new ResponseEntity<>(body, httpStatus);
    }
    /**
     * JWT 만료 예외 처리
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse<String>> handleExpiredJwtException(ExpiredJwtException e) {
        log.warn("JWT ERROR] 만료된 토큰: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(JWT_EXPIRE_TOKEN, "토큰이 만료되었습니다."));
    }

    /**
     * 경로 존재하지 않음
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse<String>> handleInvalidPathExceptions(NoHandlerFoundException e) {
        ErrorResponse<String> body = ErrorResponse.of(NOT_FOUND_PATH, e.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(NOT_FOUND_PATH.getHttpCode());
        return new ResponseEntity<>(body, httpStatus);
    }
}
