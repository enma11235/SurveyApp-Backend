package com.quickpolls.service;

import com.quickpolls.dto.model.UserData;
import com.quickpolls.exception.AuthException;
import com.quickpolls.exception.NicknameAlreadyInUseException;
import com.quickpolls.exception.UserNotFoundException;
import com.quickpolls.model.User;
import com.quickpolls.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import com.quickpolls.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    //GET USER
    public UserData getUserById(Long id, String token) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            boolean validToken = jwtTokenProvider.validateToken(token);
            if(validToken) {
                String nickname = jwtTokenProvider.getUsernameFromToken(token);
                if(user.get().getNickname().equals(nickname)) {
                    return new UserData(user.get().getId(), user.get().getNickname(), user.get().getImg());
                } else {
                    throw new AuthException("Not authorized to get this user info");
                }
            } else {
                throw new AuthException("Invalid Token");
            }
        } else {
            throw new UserNotFoundException("There is no user with that id");
        }
    }

    //CREATE USER
    public User createUser(String nickname, String password) {
        Optional<User> userWithSameNickname = userRepository.findByNickname(nickname);
        if(userWithSameNickname.isPresent()) {
            throw new NicknameAlreadyInUseException("Nickname '" + nickname + "' is already taken.");
        } else {
            LocalDate now = LocalDate.now();

            User user = new User();
            user.setNickname(nickname);
            user.setPassword(password);
            user.setCreated_at(now.toString());
            return userRepository.save(user);
        }
    }

    public long getUserId(String token) {
        boolean validToken = jwtTokenProvider.validateToken(token);
        if(validToken) {
            String nickname = jwtTokenProvider.getUsernameFromToken(token);
            Optional<User> user = userRepository.findByNickname(nickname);
            if(user.isPresent()) {
                return user.get().getId();
            } else {
                throw new UserNotFoundException("There is no user with that id");
            }
        }
        return -1;
    }

    public User editUser(String new_nickname, String new_password, String new_image, String token) {
        boolean validToken = jwtTokenProvider.validateToken(token);
        if(validToken) {
            String nickname = jwtTokenProvider.getUsernameFromToken(token);
            Optional<User> user = userRepository.findByNickname(nickname);
            if(user.isPresent()) {
                user.get().setNickname(new_nickname);
                user.get().setPassword(new_password);
                user.get().setImg(new_image);
                userRepository.save(user.get());
                return user.get();
            } else {
                throw new UserNotFoundException("User does not exist");
            }
        } else {
            throw new AuthException("Invalid Token");
        }
    }

    public boolean doesUserExists(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        return user.isPresent();
    }

    public Optional<User> getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
}
