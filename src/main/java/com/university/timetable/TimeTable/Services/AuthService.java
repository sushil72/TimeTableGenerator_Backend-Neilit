package com.university.timetable.TimeTable.Services;

import com.university.timetable.TimeTable.Entity.User;
import com.university.timetable.TimeTable.Exceptions.AlreadyRegisteredException;
import com.university.timetable.TimeTable.Repository.UserRepository;
import com.university.timetable.TimeTable.requests.LoginRequest;
import com.university.timetable.TimeTable.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisterResponse register(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyRegisteredException("User Already Registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        List<String> roleNames = request.getRoles() != null ? request.getRoles() : List.of("USER"); // Default role if none provided
        user.setRoles(roleNames);

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);
        System.out.println("token "+token);
        log.info("User registered successfully");
        RegisterResponse response = new RegisterResponse();
        response.setToken(token);
        response.setMessage("User registered successfully");
        return  response;
    }

    public RegisterResponse authenticate(LoginRequest request) {
//        System.out.println("\nrequest "+request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
//        System.out.println("\nLoginRequest "+request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateToken(user);
        System.out.println("\naccessToken "+accessToken);
        RegisterResponse response = new RegisterResponse();
        response.setToken(accessToken);
        response.setMessage("User Login Successfully");
//        System.out.println("\n\n\tresponse "+response);
        return response;
    }
}
