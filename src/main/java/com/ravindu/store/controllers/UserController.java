package com.ravindu.store.controllers;

import com.ravindu.store.dtos.*;

import com.ravindu.store.dtos.ChangePasswordRequest;
import com.ravindu.store.dtos.RegisterUserRequest;
import com.ravindu.store.dtos.UpdateUserRequest;
import com.ravindu.store.dtos.UserDto;
import com.ravindu.store.entities.Role;
import com.ravindu.store.mappers.UserMapper;
import com.ravindu.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false,defaultValue = "",name = "sort") String sort
    ){

//        return userRepository.findAll()
//                .stream()
//                .map(user -> new UserDto(user.getId(),user.getName(),user.getEmail()))
//                .toList();

//here We can use object mapper like mapstrcut like fallow
//
        if(!Set.of("name","email").contains(sort)){
            sort="name";
        }

        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto).toList();
   }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        var user= userRepository.findById(id).orElse(null);
        if (user==null){
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder){
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(
                    Map.of("email","email already exist")
            );
        }
        var user= userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri=uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request){

        var user=userRepository.findById(id).orElse(null);
        System.out.println(user);
        if (user==null){
            return ResponseEntity.notFound().build();
        }

        userMapper.update(request,user);
        userRepository.save(user);
//here we alredy creat user so we do not need to creeat a new user we just map data coming from request to how eneity look like then save
        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id){
        var user=userRepository.findById(id).orElse(null);
        if (user==null){
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordRequest request){
       var user = userRepository.findById(id).orElse(null);
       if(user==null){
           return ResponseEntity.notFound().build();
       }

       if(!user.getPassword().equals(request.getOldPassword())){
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }

       user.setPassword(request.getNewPassword());
//       var password =request.getNewPassword();
//       userMapper.updatePassword(password,user);
       userRepository.save(user);
       return ResponseEntity.noContent().build();

    }

}
