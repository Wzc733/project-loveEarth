package com.fwzc.common.exception;

/**
 * @Auther:wzc
 * @Data:2021/11/23 - 11 - 23 - 21:06
 */

import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice //异常切面  Spring AOP。在Controller添加通知。需要返回json数据，要在方法上加@ResponseBody。
public class UnifiedExceptionHandler {
//    @ExceptionHandler(value = Exception.class) //异常通知
//    public Result handleException(Exception e){
//            log.error(e.getMessage(),e);
//            return Result.error();
//    }
//
//    @ExceptionHandler(value = BadSqlGrammarException.class) //异常通知
//    public Result handleException(BadSqlGrammarException e){
//        log.error(e.getMessage(),e);
//        return Result.setResult(ResponseEnum.BAD_SQL_GRAMMAR_ERROR);
//    }

    /**
     * 各种业务异常
     */
    @ExceptionHandler(value = BusinessException.class) //异常通知
    public Result handleException(BusinessException e){  //抛出的自定义异常有我们设置的msg和code
        log.error(e.getMessage(),e);
        return Result.error().setMsg(e.getMessage()).setCod(e.getCode());
    }

    /**
     * Controller上一层的相关异常
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public Result handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        // SERVLET_ERROR(-102, "servlet请求异常"),
        return Result.error().setCod(ResponseEnum.SERVLET_ERROR.getCode()).setMsg(ResponseEnum.SERVLET_ERROR.getMessage());
    }

}
