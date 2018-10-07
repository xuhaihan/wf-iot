package com.warpfuture.iot.oauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class VerifyWxFileController {

    /**
     * 返回微信验证文件内容
     *
     * @param content
     * @return
     */
    @RequestMapping("/MP_verify_{content}.txt")
    public String getVerifyFileContent(@PathVariable String content,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        log.info("Get File From -> {}://{}:{}", request.getScheme(), request.getRemoteHost(), request.getRemotePort());
        response.setHeader("Accept-Ranges","bytes");
//        response.setContentLength(135);
        response.setContentType("text/plain");
        return content;
    }

}
