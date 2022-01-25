package kr.springboot.dcinside.cartoon.postservice.controller;

import kr.springboot.dcinside.cartoon.postservice.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    @GetMapping("/test")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> testResponse() {
        return ResponseEntity.ok(new ApiResponse(true, "그냥 통과"));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> testUserResponse(Principal principal) {
        System.out.println(principal.getName());
        return ResponseEntity.ok(new ApiResponse(true, "유저 통과"));
    }

}
