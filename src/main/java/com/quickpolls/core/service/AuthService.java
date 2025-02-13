package com.quickpolls.core.service;

import com.quickpolls.core.exception.AuthException;
import com.quickpolls.core.model.User;
import com.quickpolls.core.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public String signIn(String nickname, String password) {
        // Verificar si el usuario existe
        Optional<User> user = userService.getUserByNickname(nickname);
        if(user.isPresent()) {
            // Verificar las credenciales
            if (!password.equals(user.get().getPassword())) {
                throw new AuthException("Invalid password");
            } else {
                // Generar un token JWT
                return jwtTokenProvider.generateToken(user.get());
            }
        } else {
            throw new AuthException("Invalid nickname");
        }
    }

    public String signUp(String nickname, String password) {
        //verificar que el nickname esta disponible
        boolean isNicknameTaken = userService.doesUserExists(nickname);
        if(!isNicknameTaken) {
            User newUser = userService.createUser(nickname, password);
            return jwtTokenProvider.generateToken(newUser);
        } else {
            throw new AuthException(nickname + " is not available");
        }
    }
}
