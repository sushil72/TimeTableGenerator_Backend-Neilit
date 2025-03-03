package com.university.timetable.TimeTable.Controller;
import com.university.timetable.TimeTable.Entity.User;
import com.university.timetable.TimeTable.Services.AuthService;
import com.university.timetable.TimeTable.requests.LoginRequest;
import com.university.timetable.TimeTable.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private  AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse>  register(@RequestBody User registerRequest) {
        System.out.println("registerRequest "+registerRequest);
        RegisterResponse response = authService.register(registerRequest);
        System.out.println("\n\n\tresponse "+response);
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestBody LoginRequest loginRequest) {
//        System.out.println("Controller loginRequest "+loginRequest);
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }
}
