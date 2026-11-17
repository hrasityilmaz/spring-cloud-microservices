package art.timestop.todos.service;

import art.timestop.todos.dto.request.LoginRequestDto;
import art.timestop.todos.dto.request.RegisterDto;
import art.timestop.todos.dto.response.LoginResponseDto;

public interface AuthenticationService {
    void register(RegisterDto input) throws Exception;
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
