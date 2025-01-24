package com.github.enma11235.generic.poll.system.service;

import com.github.enma11235.generic.poll.system.dto.model.UserDTO;
import com.github.enma11235.generic.poll.system.dto.response.CreateUserResponseBody;
import com.github.enma11235.generic.poll.system.exception.AuthException;
import com.github.enma11235.generic.poll.system.exception.NicknameAlreadyInUseException;
import com.github.enma11235.generic.poll.system.exception.UserNotFoundException;
import com.github.enma11235.generic.poll.system.model.User;
import com.github.enma11235.generic.poll.system.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import com.github.enma11235.generic.poll.system.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    //GET USER
    public UserDTO getUserById(Long id, String token) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            boolean validToken = jwtTokenProvider.validateToken(token);
            if(validToken) {
                String nickname = jwtTokenProvider.getUsernameFromToken(token);
                if(user.get().getUsername().equals(nickname)) {
                    return new UserDTO(user.get().getId(), user.get().getUsername(), user.get().getCreated_at(), user.get().getPassword(), user.get().getImg());
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

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    //CREATE USER
    public CreateUserResponseBody createUser(String nickname, String password) {
        Optional<User> userWithSameNickname = userRepository.findByNickname(nickname);
        if(userWithSameNickname.isPresent()) {
            throw new NicknameAlreadyInUseException("Nickname '" + nickname + "' is already taken.");
        } else {
            LocalDate now = LocalDate.now();

            User user = new User();
            user.setUsername(nickname);
            user.setPassword(password);
            user.setCreated_at(now.toString());
            User savedUser = userRepository.save(user);
            String token = authService.authenticate(user.getUsername(), user.getPassword());
            return new CreateUserResponseBody(savedUser.getId(), token);
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
                user.get().setUsername(new_nickname);
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

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
}
