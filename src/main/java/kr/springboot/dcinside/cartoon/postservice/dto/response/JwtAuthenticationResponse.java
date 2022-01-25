package kr.springboot.dcinside.cartoon.postservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class JwtAuthenticationResponse {

    @NonNull
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType = "Bearer";

    /**
     * TODO
     */
    @NonNull
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @Builder
    public JwtAuthenticationResponse(@NonNull String accessToken, @NonNull String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
