package kr.springboot.dcinside.cartoon.postservice.service;

import kr.springboot.dcinside.cartoon.postservice.dto.request.CarbtoonRequest;
import kr.springboot.dcinside.cartoon.postservice.dto.response.ApiResponse;

public interface CarbtoonService {

    ApiResponse create(CarbtoonRequest carbtoonRequest);

}
