package kr.springboot.dcinside.cartoon.postservice;

import io.jsonwebtoken.Claims;
import kr.springboot.dcinside.cartoon.postservice.dto.request.CarbtoonRequest;
import kr.springboot.dcinside.cartoon.postservice.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.postservice.service.CarbtoonService;
import kr.springboot.dcinside.cartoon.postservice.service.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostTests {

    @Autowired // 토큰 테스트를 위한
    JwtTokenProvider tokenProvider;

    @Autowired
    CarbtoonService carbtoonService;

    String testToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjYXJidG9vbiIsInN1YiI6Im5pY2U3Njc3IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY0NDczMDI0OCwiZXhwIjoxNjQ1MzM1MDQ4fQ.S_7T1AAGpueiaOUWixRo2Tnmnc-WKfzlehGW16AsHMM";

    @Transactional
    @Test // 유저가 생성 테스트
    @DisplayName("카툰 등록 테스트")
    void PostCreateTest() {

        Claims claims = tokenProvider.getClaimsFromJWT(testToken);
        String username = claims.getSubject();
        assertThat(username).isNotNull();

        CarbtoonRequest carbtoonRequest = CarbtoonRequest.builder()
                .carbtoonist(username)
                .title("첫번째 캅툰 테스트")
                .memo("안녕하세요 테스트 입니다.")
                .build();

        ApiResponse apiResponse = carbtoonService.create(carbtoonRequest);

        assertThat(apiResponse.getSuccess()).isTrue();

        carbtoonRequest = CarbtoonRequest.builder()
                .carbtoonist(username)
                .memo("안녕하세요 테스트 입니다.")
                .build();

        apiResponse = carbtoonService.create(carbtoonRequest);

        assertThat(apiResponse.getSuccess()).isFalse();

        // TODO 카툰 파일 등록 테스트

    }

}
