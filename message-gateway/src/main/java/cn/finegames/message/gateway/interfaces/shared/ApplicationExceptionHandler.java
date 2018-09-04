package cn.finegames.message.gateway.interfaces.shared;

import cn.finegames.commons.util.BaseResp;
import cn.finegames.commons.util.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public BaseResp<Object> handleBaseException(HttpServletRequest req, BaseException ex) {
        BaseResp<Object> resp = BaseResp.of(ex.getCode());
        resp.setMessage(ex.getMessage());
        log.error("request {} failure, error info", req.getRequestURI(), ex);

        return resp;
    }


    @ExceptionHandler(RuntimeException.class)
    public BaseResp<Object> handleRuntimeException(HttpServletRequest req, RuntimeException ex) {
        BaseResp<Object> resp = getUnKnownException();
        log.error("request {} failure，error info", req.getRequestURI(), ex);
        return resp;
    }

    @ExceptionHandler(Exception.class)
    public BaseResp<Object> handleRuntimeException(HttpServletRequest req, Exception ex) {
        BaseResp<Object> resp = getUnKnownException();
        log.error("request {} failure，error info", req.getRequestURI(), ex);
        return resp;
    }

    /**
     * 返回未知异常响应
     *
     * @return
     */
    private BaseResp<Object> getUnKnownException() {
        BaseResp<Object> result = BaseResp.of(500);
        result.setMessage("消息系统内部异常");
        return result;
    }
}