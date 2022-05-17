package io.oauth2.server.common.response;

import io.oauth2.server.config.AuthorizationConfig;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Collections;

/**
 *
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseHandler {

    private static final Logger logger = LogManager.getLogger(AuthorizationConfig.class);

    /**
     * <p> response 데이터를 가진 성공계 응답을 반환한다. </p>
     * <pre>
     *     {
     *         "state" : status,
     *         "message" : message,
     *         "data" : [{data1}, {data2}...],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param data 응답 바디 data 필드에 포함될 정보
     * @return 응답 객체
     */
    public ResponseEntity<ResponseBody> success(Object data) {
        ResponseBody body = ResponseBody.builder()
                .errorCode(ErrorCode.PROCESS_SUCCESS)
                .data(data)
                .build();
        return ResponseEntity.ok().body(body);
    }


    /**
     * <p> 성공계 응답만을 반환한다. </p>
     * <pre>
     *     {
     *         "state" : status,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @return 응답 객체
     */
    public ResponseEntity<ResponseBody> success() {
        return success(Collections.emptyList());
    }

    /**
     * <p> response 데이터를 가진 실패계 응답을 반환한다. </p>
     * <pre>
     *     {
     *         "state" : status,
     *         "message" : message,
     *         "data" : [{data1}, {data2}...],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param data 응답 바디 필드에 포함될 Data 정보
     * @param errorCode 응답 바디 필드에 포함될 Error 정보
     * @return 응답 객체
     */
    public ResponseEntity<ResponseBody> fail(Object data, ErrorCode errorCode) {
        ResponseBody body = ResponseBody.builder()
                .errorCode(errorCode)
                .data(data)
                .build();
        return ResponseEntity.ok().body(body);
    }

    /**
     * <p> response 데이터를 가진 실패계 응답을 반환한다. </p>
     * <pre>
     *     {
     *         "state" : status,
     *         "message" : message,
     *         "data" : [{data1}, {data2}...],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param errorCode 응답 바디 필드에 포함될 Error 정보
     * @return 응답 객체
     */
    public ResponseEntity<ResponseBody> fail(ErrorCode errorCode) {
        return fail(Collections.emptyList(), errorCode);
    }

    /**
     * <p> Request Parameter가 올바르지 않을 경우 </p>
     * <pre>
     *     {
     *         "state" : 400,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param error MissingServletRequestParameterException
     * @return 에러 응답 객체
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ResponseBody> handleRequestParamException(final MissingServletRequestParameterException error) {
        logger.error("handleMissingServletRequestParameterException", error);
        String parameterName = error.getParameterName();
        return fail(parameterName, ErrorCode.INVALID_INPUT_VALUE);
    }

    /**
     * <p> 해당 유저의 ID가 등록되지 않았을 경우 </p>
     * <pre>
     *     {
     *         "state" : 400,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param error UsernameNotFoundException
     * @return 에러 응답 객체
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ResponseBody> handleUsernameNotFoundException(final UsernameNotFoundException error) {
        logger.error("handleUsernameNotFoundException", error);
        return fail(ErrorCode.INVALID_INPUT_ID);
    }

    /**
     * <p> 해당 유저의 Password 인증을 실패했을 경우 </p>
     * <pre>
     *     {
     *         "state" : 400,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param error BadCredentialsException
     * @return 에러 응답 객체
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ResponseBody> handleBadCredentialsException(final BadCredentialsException error) {
        logger.error("handleBadCredentialsException", error);
        return fail(ErrorCode.INVALID_INPUT_PW);
    }

    /**
     * <p> 해당 유저의 인증을 실패했을 경우 </p>
     * <pre>
     *     {
     *         "state" : 403,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param error AccessDeniedException
     * @return 에러 응답 객체
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseBody> handleAccessDeniedException(final AccessDeniedException error) {
        logger.error("handleAccessDeniedException", error);
        return fail(ErrorCode.ACCESS_DENIED);
    }

    /**
     * <p> 허가되지 않은 경로(URI)로 접속을 요청할 경우 </p>
     * <pre>
     *     {
     *         "state" : 404,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param error NoHandlerFoundException
     * @return 에러 응답 객체
     */
    // 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseBody> handleNoHandlerFoundException(final NoHandlerFoundException error) {
        logger.error("handleNoHandlerFoundException", error);
        return fail(ErrorCode.SITE_NOT_FOUND);
    }

    /**
     * <p> 그외 서버에서 에러가 발생할 경우 </p>
     * <pre>
     *     {
     *         "state" : 500,
     *         "message" : message,
     *         "data" : [],
     *         "code" : code
     *     }
     * </pre>
     *
     * @param error Exception
     * @return 에러 응답 객체
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseBody> handleRuntimeException(final RuntimeException error) {
        logger.error("handleRuntimeException", error);
        return fail(ErrorCode.EXIT_ERROR);
    }

}
