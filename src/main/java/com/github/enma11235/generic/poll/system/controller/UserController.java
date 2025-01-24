package com.github.enma11235.generic.poll.system.controller;

import com.github.enma11235.generic.poll.system.dto.model.UserDTO;
import com.github.enma11235.generic.poll.system.dto.request.CreateUserRequestBody;
import com.github.enma11235.generic.poll.system.dto.request.EditUserRequestBody;
import com.github.enma11235.generic.poll.system.dto.request.GetUserRequestBody;
import com.github.enma11235.generic.poll.system.dto.response.EditUserResponseBody;
import com.github.enma11235.generic.poll.system.dto.response.GetUserResponseBody;
import com.github.enma11235.generic.poll.system.dto.response.CreateUserResponseBody;
import com.github.enma11235.generic.poll.system.model.User;
import com.github.enma11235.generic.poll.system.service.AuthService;
import com.github.enma11235.generic.poll.system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private AuthController authController;
    private AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthController authController, AuthService authService) {
        this.userService = userService;
        this.authController = authController;
        this.authService = authService;
    }

    // GET USER
    @PostMapping("/{id}")
    public ResponseEntity<GetUserResponseBody> getUserById(@PathVariable Long id, @RequestBody @Valid GetUserRequestBody body) {
        UserDTO user = userService.getUserById(id, body.getToken());
        GetUserResponseBody responseBody = new GetUserResponseBody(user.getId(), user.getNickname(), user.getPassword(), user.getImage());
        return ResponseEntity.ok(responseBody);
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<CreateUserResponseBody> createUser(@RequestBody @Valid CreateUserRequestBody body) {
        CreateUserResponseBody response = userService.createUser(body.getNickname(), body.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //EDIT USER
    @PostMapping("/edit")
    public ResponseEntity<EditUserResponseBody> editUser(@RequestBody @Valid EditUserRequestBody body) {
        User editedUser = userService.editUser(body.getNickname(), body.getPassword(), body.getImage(), body.getToken());
        String newToken = authService.authenticate(editedUser.getUsername(), editedUser.getPassword());
        EditUserResponseBody responseBody = new EditUserResponseBody(newToken);
        return ResponseEntity.ok(responseBody);
    }

    // Endpoint to delete a user by ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
//        if(authorizationHeader != null) {
//            // Extraer el token
//            String token = authorizationHeader.substring(7);
//            Optional<User> user = userService.findUserById(id);
//            if (user.isPresent()) {
//                try {
//                    String nickname = authController.getNicknameFromToken(token);
//                    if(user.get().getNickname().equals(nickname)) {
//                        userService.deleteUserById(id);
//                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//                    } else {
//                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//                    }
//                } catch (Exception e) {
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
}