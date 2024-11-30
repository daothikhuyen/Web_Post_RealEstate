package com.example.backend.mapper;

import com.example.backend.dto.request.User.UserCreationRequest;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.enity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);// nhận đối tuưởng userrespone và chuyển thành user

    UserResponse toUserResponse(User user);

}
