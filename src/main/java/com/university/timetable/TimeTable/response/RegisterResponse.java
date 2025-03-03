package com.university.timetable.TimeTable.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private String token;
    private String message;
   public  RegisterResponse() {}
    public RegisterResponse(String accessToken, String userLoginSuccessfully) {
        this.token = accessToken;
        this.message = userLoginSuccessfully;
    }

}
