package com.example.user_api_archives.filter;

import com.example.user_api_archives.controller.UserRemoteController;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private final UserRemoteController userRemoteController;

    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter(UserRemoteController userRemoteController) {
        this.userRemoteController = userRemoteController;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException   {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        userRemoteController.auth();

        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
        String responseBody;
        if (!request.getRequestURI().toLowerCase().contains("log") ||
                !request.getRequestURI().toLowerCase().contains("logs")) {
            responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                    response.getCharacterEncoding());
        } else {
            responseBody = "";
        }

        log.info(
                "\nFINISHED PROCESSING:\n"
                        + "===============REQUEST================\n"
                        + " METHOD={}; URI={};\n"
                        + "===============HEADERS================\n"
                        + "{}\n"
                        + "{}"
                        + "===============RESPONSE===============\n"
                        + " STATUS={}\n"
                        + "=============RESPONSE BODY============\n"
                        + "{}\n"
                        + "---> TIME TAKEN= {}",
                request.getMethod(), request.getRequestURI(),
                request.getContentType(),
                !requestBody.isEmpty() ? "=============REQUEST BODY=============\n" + requestBody + "\n" : "",
                response.getStatus(), responseBody, sdf.format(timeTaken));
        responseWrapper.copyBodyToResponse();
    }

    private @NotNull String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}