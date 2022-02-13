package kr.springboot.dcinside.cartoon.postservice.dto.request;

import kr.springboot.dcinside.cartoon.postservice.domain.Carbtoon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CarbtoonRequest {

    private String title;

    private String carbtoonist;

    private String memo;

    public Carbtoon toEntity() {
        return Carbtoon.builder()
                .title(title)
                .active(false)
                .carbtoonist(carbtoonist)
                .memo(memo)
                .build();
    }

    @Builder
    public CarbtoonRequest(String title, String carbtoonist, String memo) {
        this.title = title;
        this.carbtoonist = carbtoonist;
        this.memo = memo;
    }

}
