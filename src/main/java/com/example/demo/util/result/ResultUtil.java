package com.example.demo.util.result;

import org.springframework.http.ResponseEntity;

import com.example.demo.domain.Result;

public class ResultUtil {

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("success");
        result.setData(object);
        return result;
    }

    public static Result success() {
        return success(null);
    }
//  TODO210915
    public static Result success(Integer code,String message,Object object) {
    	 Result result = new Result();
         result.setCode(code);
         result.setMsg(message);
         result.setData(object);
         return result;
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static Result error(String errorType, String msg) {
    	Result result = new Result();
    	result.setErrorType(errorType);
    	result.setMsg(msg);
    	return result;
    }
    public static ResponseEntity errorEntity() {
    	return ResponseEntity.notFound().build();
    }
}