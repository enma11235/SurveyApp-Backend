package com.quickpolls.core.controller;

import com.quickpolls.core.dto.request.AuthRequestBody;
import com.quickpolls.core.service.AuthService;
import com.quickpolls.core.dto.response.ResponseBody;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // SIGN IN
    @PostMapping("/signin")
    public ResponseEntity<ResponseBody> signIn(@RequestBody @Valid AuthRequestBody body) {
        String token = authService.signIn(body.getNickname(), body.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        headers.add("nickname", body.getNickname());
        headers.add("Access-Control-Expose-Headers", "token");
        ResponseBody responseBody = new ResponseBody("Signed in successfully");
        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    //SIGN UP
    @PostMapping("/signup")
    public ResponseEntity<ResponseBody> signUp(@RequestBody @Valid AuthRequestBody body) {
        String token = authService.signUp(body.getNickname(), body.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        headers.add("nickname", body.getNickname());
        headers.add("Access-Control-Expose-Headers", "token");
        ResponseBody responseBody = new ResponseBody("Signed up successfully");
        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

}
