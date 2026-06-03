package com.spring.auth_service.admin;


import com.spring.auth_service.exception.AuthUserNotFoundException;
import com.spring.auth_service.mapper.AuthUserMapper;
import com.spring.auth_service.repository.AuthUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {

    private final AuthUserRepository userRepository;
    private final AuthUserMapper userMapper;

    @GetMapping("/home")
    public String sayHello(){
        return "Hello Admin!";
    }

    @GetMapping ("/get-users")
    public ResponseEntity<?> getAllUser(){

        List<AdminDto> allUsers =  userRepository.findAll().stream().map(userMapper::toDtO).toList();

        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/assign-role")
    public ResponseEntity<AdminDto> createUserToAdmin(@RequestBody AssignRoleRequest roleRequest){
        var user = userRepository.findById(roleRequest.getId()).orElseThrow(AuthUserNotFoundException::new);
        user.setRole(roleRequest.getRole());
        userRepository.save(user);
        return ResponseEntity.ok( userMapper.toDtO(user));
    }

}