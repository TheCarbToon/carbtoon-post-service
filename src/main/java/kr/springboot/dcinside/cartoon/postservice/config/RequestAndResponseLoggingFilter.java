package kr.springboot.dcinside.cartoon.postservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.springboot.dcinside.cartoon.postservice.messaging.KafkaLogProducer;
import kr.springboot.dcinside.cartoon.postservice.messaging.PostServiceLogType;
import kr.springboot.dcinside.cartoon.postservice.payload.PostServiceLogPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

    private final KafkaLogProducer kafkaLogProducer;

    private final ObjectMapper objectMapper;

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String logUUID = UUID.randomUUID().toString();
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(logUUID, wrapRequest(request), wrapResponse(response), filterChain);
        }
    }
    
    protected void doFilterWrapped(String logUUID, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(logUUID, request, response);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(logUUID, request, response);
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(String logUUID, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequestHeader(logUUID, request, request.getRemoteAddr() + "|>");
        }
    }

    protected void afterRequest(String logUUID, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequestBody(logUUID, request, request.getRemoteAddr());
            logResponse(logUUID, response, request.getRemoteAddr());
        }
    }

    private void logRequestHeader(String logUUID, ContentCachingRequestWrapper request, String prefix) {
        String queryString = request.getQueryString();
        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
                        headers.put(headerName, headerValue)));
        kafkaLogProducer.send(
                PostServiceLogPayload.builder()
                        .logUUID(logUUID)
                        .logType(PostServiceLogType.REQUEST)
                        .ip(request.getRemoteAddr())
                        .uri(request.getRequestURI())
                        .method(request.getMethod())
                        .headers(headers)
                        .query(queryString)
                        .contentBody(null)
                        .build());
    }

    private void logRequestBody(String logUUID, ContentCachingRequestWrapper request, String prefix) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(logUUID, content, request.getContentType(), prefix, PostServiceLogType.REQUEST);
        }
    }

    private void logResponse(String logUUID, ContentCachingResponseWrapper response, String prefix) {
        response.getHeaderNames().forEach(headerName ->
                response.getHeaders(headerName).forEach(headerValue ->
                        log.info("{} {}: {}", prefix, headerName, headerValue)));
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(logUUID, content, response.getContentType(), prefix, PostServiceLogType.RESPONSE);
        }
    }

    private void logContent(String logUUID, byte[] content, String contentType, String prefix, PostServiceLogType type) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            Object contentObject = null;
            try {
                contentObject = objectMapper.readTree(content);
            } catch (IOException e) {
                log.error(e.getMessage());
                Map<String, String> body = new ConcurrentHashMap<>();
                body.put("body", String.valueOf(content));
                contentObject = body.toString();
                log.info("log uuid -> {}, body can't deserializer so made body map, value is -> {}", logUUID , body.toString());
            }
            kafkaLogProducer.send(
                    PostServiceLogPayload.builder()
                            .logUUID(logUUID)
                            .logType(type)
                            .ip(prefix)
                            .uri(null)
                            .method(null)
                            .headers(null)
                            .query(null)
                            .contentBody(contentObject)
                            .build());
        }
    }

    /**
     * Request content caching
     *
     * @param request
     * @return
     */
    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    /**
     * Response content caching
     *
     * @param response
     * @return
     */
    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

}
