package kr.springboot.dcinside.cartoon.postservice.service.impl;

import kr.springboot.dcinside.cartoon.postservice.dto.request.CarbtoonRequest;
import kr.springboot.dcinside.cartoon.postservice.dto.response.ApiResponse;
import kr.springboot.dcinside.cartoon.postservice.repository.CarbtoonRepository;
import kr.springboot.dcinside.cartoon.postservice.service.CarbtoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CarbtoonServiceImpl implements CarbtoonService {

    private final CarbtoonRepository carbtoonRepository;

    @Override
    public ApiResponse create(CarbtoonRequest carbtoonRequest) {
        log.info("{} user create carbtoon, title is {}", carbtoonRequest.getCarbtoonist(), carbtoonRequest.getTitle());
        if (carbtoonRequest.getTitle() == null) return new ApiResponse(false, "제목을 부탁합니다.");
        carbtoonRepository.save(carbtoonRequest.toEntity());
        return new ApiResponse(true, "캅툰 등록 완료!");
    }

}
