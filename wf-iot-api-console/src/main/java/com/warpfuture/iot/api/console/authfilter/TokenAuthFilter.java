package com.warpfuture.iot.api.console.authfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.iot.api.console.jwt.config.JwtProperties;
import com.warpfuture.iot.api.console.jwt.core.JwtPayload;
import com.warpfuture.iot.api.console.jwt.core.JwtUtils;
import com.warpfuture.iot.api.console.jwt.exception.JwtInvalidatedException;
import com.warpfuture.iot.api.console.jwt.exception.JwtMissException;
import com.warpfuture.iot.api.console.util.BeanUtil;
import com.warpfuture.vo.ResultVO;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component
@WebFilter(filterName = "tokenAuthFilter", urlPatterns = "/*")
@Order
public class TokenAuthFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(TokenAuthFilter.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String requestURI = request.getRequestURI();
//        logger.info("requestURI:======-> " + requestURI);
//        String remoteHost = request.getRemoteHost();
//        int remotePort = request.getRemotePort();
//
//        logger.info("remoteHost:======-> {} {}", remoteHost, remotePort);
        if ("/actuator/health".equals(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getHeader(jwtProperties.getToken().getHeader());

            try {
                Claims claims = jwtUtils.decode(token);
                logger.info("===========Get the claims : --> " + claims);
                if (claims.getExpiration().after(new Date())) {
                    JwtPayload payload = BeanUtil.toBean(claims);
                    logger.info("===========Get the Payload : --> " + payload);
                    if (!StringUtils.isEmpty(payload.getUserId())) {
                        request.setAttribute("accountId", payload.getUserId());
                        filterChain.doFilter(request, servletResponse);
                    } else {
                        writeResponse(servletResponse,ResponseMsg.TOKEN_MISS);
                    }
                } else {
                    writeResponse(servletResponse,ResponseMsg.TOKEN_EXPIRED);
                }
            } catch (JwtMissException e) {
                writeResponse(servletResponse,ResponseMsg.TOKEN_INVALID);
            } catch (JwtInvalidatedException e) {
                writeResponse(servletResponse,ResponseMsg.TOKEN_MISS);
            } catch (Exception e) {
                writeResponse(servletResponse,ResponseMsg.REQUEST_ERROR);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private void writeResponse(ServletResponse response, String msg) {
        PrintWriter printWriter = null;
        try {
//            response.setCharacterEncoding("gbk");
            printWriter = response.getWriter();
            printWriter.write(objectMapper.writeValueAsString(new ResultVO().fail(msg)));
        } catch (IOException e) {
            logger.error("Response Fail: {}, {}", msg, e.getMessage());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }
}
