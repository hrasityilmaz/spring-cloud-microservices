package art.timestop.todos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import art.timestop.todos.dto.request.LoginRequestDto;
import art.timestop.todos.dto.request.RegisterDto;
import art.timestop.todos.dto.response.LoginResponseDto;
import art.timestop.todos.entities.Authority;
import art.timestop.todos.entities.User;
import art.timestop.todos.enums.Role;
import art.timestop.todos.repository.UserRepository;



@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional // Writing to db dont forget !!!
    public void register(RegisterDto input) throws Exception {

        if(isEmailTaken(input.email())){
            throw new Exception("Email already taken!");
        }
        User user = buildNewUser(input);
        userRepository.save(user);
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private List<Authority> initialAuthority() {
        boolean isFirstUser = userRepository.count() == 0;
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority(Role.ROLE_ENPLOYEE));
        if (isFirstUser) {
            authorities.add(new Authority(Role.ROLE_ADMIN));
        }
        return authorities;
    }

    private User buildNewUser(RegisterDto input) {
        User user = new User();
        user.setId(0);
        user.setFirstName(input.firstName());
        user.setLastName(input.lastName());
        user.setEmail(input.email());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setAuthorities(initialAuthority());
        return user;
    }

    @Override
    @Transactional (readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
       authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequestDto.email(),
            loginRequestDto.password()
            )
       );

       User user = userRepository.findByEmail(loginRequestDto.email())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String jwtToken = jwtService.generateToken(new HashMap<>(), user);

        return new LoginResponseDto(jwtToken);
    }

}
