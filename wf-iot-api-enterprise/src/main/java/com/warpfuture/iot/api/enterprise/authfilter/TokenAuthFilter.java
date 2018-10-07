package com.warpfuture.iot.api.enterprise.authfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpfuture.constant.ResponseMsg;
import com.warpfuture.entity.RepayEntity;
import com.warpfuture.iot.api.enterprise.service.TokenAuthEnterpriseService;
import com.warpfuture.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@WebFilter(filterName = "tokenAuthFilter", urlPatterns = "/*")
@Order
public class TokenAuthFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(TokenAuthFilter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenAuthEnterpriseService tokenAuthEnterpriseService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        if (!StringUtils.isEmpty(requestURI) && ("/login".equals(StringUtils.trimAllWhitespace(requestURI)) || "/actuator/health".equals(requestURI))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getHeader("token");
            if (!StringUtils.isEmpty(token)) {
                RepayEntity<Map<String, Object>> result = tokenAuthEnterpriseService.verifyJwt(token);
                logger.info("===========Get the claims : --> " + result);
                if (result != null && result.isTokenSuccess()) {
                    Map<String, Object> claims = result.getData();
                    String accountId = String.valueOf(claims.get("accountId"));
                    String applicationId = String.valueOf(claims.get("applicationId"));
                    String userId = String.valueOf(claims.get("userId"));
                    if (!StringUtils.isEmpty(accountId)) {
                        request.setAttribute("accountId", accountId);
                        request.setAttribute("applicationId", applicationId);
                        request.setAttribute("userId", userId);
                        filterChain.doFilter(request, servletResponse);
                    } else {
                        writeResponse(servletResponse, ResponseMsg.TOKEN_INVALID);
                    }
                } else {
                    writeResponse(servletResponse, ResponseMsg.TOKEN_ERROR);
                }
            } else {
                writeResponse(servletResponse, ResponseMsg.TOKEN_MISS);
            }
        }
    }

    private void writeResponse(ServletResponse response, String msg) {
        PrintWriter writer = null;
        try {
//            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(new ResultVO().fail(msg)));
        } catch (IOException e) {
            logger.error("Send Response: {}, {}", msg, e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
