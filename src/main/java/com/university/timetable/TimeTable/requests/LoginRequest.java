package com.university.timetable.TimeTable.requests;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
