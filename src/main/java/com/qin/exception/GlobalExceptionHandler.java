package com.qin.exception;// package com.qin.springtest.exception;

// import com.qin.utils.WebResult;
// import lombok.extern.slf4j.Slf4j;
// import org.aspectj.lang.annotation.Aspect;
// import org.springframework.core.MethodParameter;
// import org.springframework.http.HttpStatus;
// import org.springframework.jdbc.BadSqlGrammarException;
// import org.springframework.stereotype.Controller;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import javax.validation.ConstraintViolation;
// import javax.validation.ConstraintViolationException;
// import java.io.PrintWriter;
// import java.io.StringWriter;
// import java.util.Map;
// import java.util.Set;

// @Slf4j
// @RestControllerAdvice
// @Controller
// // @Aspect
// public class GlobalExceptionHandler {

//     /**
//      * 处理所有不可知的异常
//      */
//     @ExceptionHandler(Throwable.class)
//     @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//     public Map<String, Object> handleException(final Throwable e){
//         // 打印堆栈信息
//         final StringWriter sw = new StringWriter();
//         try (PrintWriter pw = new PrintWriter(sw)) {
//             e.printStackTrace(pw);

//         }
//         GlobalExceptionHandler.log.error(sw.toString());
//         return WebResult.error(e.getMessage());
//     }

//     @ExceptionHandler(ConstraintViolationException.class)
//     public Map<String, Object> methodArgumentNotValidExceptionHandle(final ConstraintViolationException methodArgumentNotValidException) {
//         final Set<ConstraintViolation<?>> constraintViolations = methodArgumentNotValidException.getConstraintViolations();
//         String message = "";
//         for (final ConstraintViolation<?> next : constraintViolations) {
//             message += next.getMessage()+" , ";
//         }
//         GlobalExceptionHandler.log.error(message);
//         return WebResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "参数不正确:" + message);
//     }
// //
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public Map<String, Object> methodArgumentNotValidExceptionHandle(final MethodArgumentNotValidException exception) {
//         final FieldError fieldError = exception.getBindingResult().getFieldError();
//         final MethodParameter parameter = exception.getParameter();
//         GlobalExceptionHandler.log.error("在执行{}.{}的时候出现绑定JSON数据参数异常，字段名称为：{}，提示消息：{}", parameter.getDeclaringClass().getName(),
//                 parameter.getMethod().getName(), fieldError.getField(), fieldError.getDefaultMessage());
//         return WebResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "参数不正确:" + fieldError.getDefaultMessage());

//     }


//     @ExceptionHandler(BadSqlGrammarException.class)
//     public Map<String, Object> badSqlGrammarException(final BadSqlGrammarException exception) {

//         return WebResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "错误:" + exception.getMessage());
//     }
// }