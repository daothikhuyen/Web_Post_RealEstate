package com.example.backend.controller;

import com.example.backend.dto.request.UserCreationRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.enity.User;
import com.example.backend.reponsitory.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
@Slf4j // log.
public class UserController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRespository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @PostMapping("/processregister")
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request) throws Exception {
        System.out.println(request.getEmail());
        ApiResponse<UserResponse> response = null;

        UserResponse saveUser = userService.createUser(request);

        response = ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(saveUser)
                .build();
        return  response;
    }

    @GetMapping("/getUsers")
    public ApiResponse<List<UserResponse>> getUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("email: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @PostMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") int userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/getMyInfo")
    public ApiResponse<UserResponse> getMyInfo(){

        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }




}
