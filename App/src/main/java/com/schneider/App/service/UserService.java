package com.schneider.App.service;


import com.schneider.App.dto.UserDto;
import com.schneider.App.model.Role;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.RoleRepository;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserEntity register(UserDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Korisnicko ime vec postoji.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email vec postoji.");
        }

        Role userRole = roleRepository.findByName("USER");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setCity(request.getCity());
        //userEntity.setPassword(request.getPassword());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.getRoles().add(userRole);

        return userRepository.save(userEntity);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
