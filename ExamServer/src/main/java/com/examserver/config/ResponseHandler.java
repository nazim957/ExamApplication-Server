package com.examserver.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler
{
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus statusCode)
    {
        Map<String, Object> mapObj = new HashMap<String, Object>();

        mapObj.put("Message", message);
        mapObj.put("Status Code", statusCode);
        return new ResponseEntity<Object>(mapObj, statusCode);

    }



}