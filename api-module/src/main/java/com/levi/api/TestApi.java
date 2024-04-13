package com.levi.api;

import com.dtflys.forest.annotation.Get;
import com.levi.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
public interface TestApi {
    @Get("/test/get")
    @GetMapping("/get")
    Result<String> testGet(String message);
}
