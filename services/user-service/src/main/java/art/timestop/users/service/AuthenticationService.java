package art.timestop.users.service;

import art.timestop.users.dto.request.LoginRequestDto;
import art.timestop.users.dto.request.RegisterDto;
import art.timestop.users.dto.response.LoginResponseDto;

public interface AuthenticationService {
    void register(RegisterDto input) throws Exception;
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
