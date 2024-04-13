package com.levi.controller;

import com.levi.api.TestApi;
import com.levi.utils.Result;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestApi {
    @Override
    public Result<String> testGet(String message) {
        return Result.success(message);
    }
}
