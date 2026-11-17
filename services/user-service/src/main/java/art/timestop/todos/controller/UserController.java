package art.timestop.todos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import art.timestop.todos.dto.request.PasswordUpdate;
import art.timestop.todos.dto.response.UserInfoResponse;
import art.timestop.todos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;


@Tag (name = "User REST API endpoints")
@RestController 
@RequestMapping ("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation (summary = "User info")
    @ResponseStatus (HttpStatus.OK)
    @GetMapping("/info")
    public UserInfoResponse getUserInfo() {
        return userService.getUserInfo(
        );
    }

    @Operation (summary = "Delete a user")
    @ResponseStatus (HttpStatus.OK)
    @DeleteMapping
    public void deleteUser() {
        userService.deleteUser();;
    }
    
    @Operation (summary = "Update Password")
    @ResponseStatus (HttpStatus.OK)
    @PutMapping ("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdate updateRequest) throws Exception {
        userService.passwordUpdate(updateRequest);
    }

}
