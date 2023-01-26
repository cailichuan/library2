package com.cai.library.dao.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
;
    private String userId;
    private String account;
    private String password;
}
