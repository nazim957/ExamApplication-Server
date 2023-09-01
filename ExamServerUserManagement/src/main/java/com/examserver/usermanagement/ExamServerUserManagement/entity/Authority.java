package com.examserver.usermanagement.ExamServerUserManagement.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {


    private  String authority;


    @Override
    public String getAuthority() {
        return authority;
    }
}
