package com.ravindu.store.mappers;

import com.ravindu.store.dtos.RegisterUserRequest;
import com.ravindu.store.dtos.UpdateUserRequest;
import com.ravindu.store.dtos.UserDto;
import com.ravindu.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request,@MappingTarget User user); //since we updateing user no need for return

    //void updatePassword(String password,@MappingTarget User user);
}
