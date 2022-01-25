package kr.springboot.dcinside.cartoon.postservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostServiceLogPayload {

    @JsonProperty("log_uuid")
    private String logUUID;

    @JsonProperty("log_type")
    private Object logType;

    private String ip;

    private String uri;

    private String method;

    private Map<String, String> headers;

    private String query;

    @JsonProperty("content_body")
    private Object contentBody;

    @Builder
    public PostServiceLogPayload(String logUUID, Map<String, String> headers, String uri, String query, String method, String ip, Object contentBody, Object logType) {
        this.logUUID = logUUID;
        this.uri = uri;
        this.ip = ip;
        this.method = method;
        this.query = query;
        this.headers = headers;
        this.contentBody = contentBody;
        this.logType = logType;
    }

}
