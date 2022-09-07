package gr.hua.ds.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class RequestNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> exceptionHandler(RequestNotFoundException exception){

        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("errorMessage",exception.getMessage());

        return errorMap;

    }
}
