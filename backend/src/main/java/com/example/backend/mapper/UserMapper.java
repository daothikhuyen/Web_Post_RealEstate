package com.example.backend.mapper;

import com.example.backend.dto.request.UserCreationRequest;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.enity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);// nhận đối tuưởng userrespone và chuyển thành user

    UserResponse toUserResponse(User user);

//    @Mapping(target = "roles", ignore = true)
//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
