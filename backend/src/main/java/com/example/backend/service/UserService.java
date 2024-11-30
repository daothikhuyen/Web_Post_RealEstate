package com.example.backend.service;

import com.example.backend.dto.request.User.UserCreationRequest;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.enums.ActivityType;
import com.example.backend.enums.Role;
import com.example.backend.exception.AppException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.enity.ActivityLogEntity;
import com.example.backend.enity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.reponsitory.ActivityLogEntityResponsitory;
import com.example.backend.reponsitory.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor // lombok tạo các contructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // đưa các final thành private nếu null
@Slf4j // log.
public class UserService {

    ActivityLogEntityResponsitory activityLogEntityRepo;
    PasswordEncoder passwordEncoder;
    UserRepository userRespository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request){

        User user = userMapper.toUser(request);

        String password =passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setIs_activated("1");
        user.setIsDeleted(0);

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRole(roles);

        ActivityLogEntity entity =new ActivityLogEntity();
        try {
            user = userRespository.save(user);

            entity.setEmail(user.getEmail());
            entity.setActivity(ActivityType.REGISTER);
            activityLogEntityRepo.save(entity);

            return userMapper.toUserResponse(user);
        }catch (DataIntegrityViolationException e){
            System.out.println("Lỗi: "+ e.getMessage());
            entity.setActivity(ActivityType.INVALID_REGISTER);
            entity.setEmail("_");
            activityLogEntityRepo.save(entity);
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    };

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") // kiểm tra method trước khi vào nếu thoả điều kiện thì thực hiện
    public List<UserResponse> getUsers() {
        log.info("Vào method getUsers");
        return userRespository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    // returnObject.email == authentication.name kiểm tra xem dữ liệu sau khi lâý có email trùng với email đang đăng nhập không
    public UserResponse getUser(Long id){

        return userMapper.toUserResponse(userRespository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getMyInfo(){
        try {
            var context = SecurityContextHolder.getContext();
            String email = context.getAuthentication().getName();

            User user = userRespository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            System.out.println(user.getPhone());
            return userMapper.toUserResponse(user);
        }catch (Exception e){
            throw  new UsernameNotFoundException(e.getMessage());
        }
    }


    public String deleted_Account(){

        try {
            UserResponse userResponse = getMyInfo();
            User user = userRespository.findByEmail(userResponse.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            user.setIsDeleted(1);
            userRespository.save(user);

            return "Xoá tài khoản thành công";
        }catch (Exception e){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }




}
