package art.timestop.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import art.timestop.users.dto.request.LoginRequestDto;
import art.timestop.users.dto.request.RegisterDto;
import art.timestop.users.dto.response.LoginResponseDto;
import art.timestop.users.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping ("/api/auth")
@Tag (name = "Authentication REST API Endpoints", description = "Register and Login operations")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation (summary = "Register a user")
    @ResponseStatus (HttpStatus.CREATED)
    @PostMapping ("/register")
    public void register(@Valid @RequestBody RegisterDto registerDto) throws Exception {
        authenticationService.register(registerDto);
    }

    @Operation(summary = "Login a user")
    @ResponseStatus (HttpStatus.OK)
    @PostMapping ("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequest){
        return authenticationService.login(loginRequest);
    }
}
