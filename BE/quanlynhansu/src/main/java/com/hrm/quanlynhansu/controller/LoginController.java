package com.hrm.quanlynhansu.controller;

import com.hrm.quanlynhansu.entity.Login;

public class LoginController {
    private final Login login;

    public LoginController(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }
}
