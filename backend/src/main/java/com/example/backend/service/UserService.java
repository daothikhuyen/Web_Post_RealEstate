package com.example.backend.service;

import com.example.backend.dto.request.UserCreationRequest;
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
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // lombok tạo các contructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // đưa các final thành private nếu null
@Slf4j // log.
public class UserService {

    ActivityLogEntityResponsitory activityLogEntityRepo;
    PasswordEncoder passwordEncoder;
    UserRepository userRespository;
    UserMapper userMapper;

//    public UserService(ActivityLogEntityResponsitory activityLogEntityRepo) {
//        this.activityLogEntityRepo = activityLogEntityRepo;
//    }

    public UserResponse createUser(UserCreationRequest request){

        User user = userMapper.toUser(request);

        String password =passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());

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
    @PostAuthorize("returnObject.email == authentication.name")// kiểm tra method sau khi đã thực hiện xong
    public UserResponse getUser(int id){
        log.info("Vào method getUser theo id");
        return userMapper.toUserResponse(userRespository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getMyInfo(){
        try {
            var context = SecurityContextHolder.getContext();
            String email = context.getAuthentication().getName();
            System.out.println(context.getAuthentication());

            User user = userRespository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            return userMapper.toUserResponse(user);
        }catch (Exception e){
            throw  new UsernameNotFoundException(e.getMessage());
        }
    }


}
