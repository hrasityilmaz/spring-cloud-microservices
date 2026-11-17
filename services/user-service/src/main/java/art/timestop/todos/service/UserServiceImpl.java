package art.timestop.todos.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import art.timestop.todos.dto.request.PasswordUpdate;
import art.timestop.todos.dto.response.AuthorityResponse;
import art.timestop.todos.dto.response.UserInfoResponse;
import art.timestop.todos.entities.User;
import art.timestop.todos.enums.Role;
import art.timestop.todos.repository.UserRepository;
import art.timestop.todos.utils.FindAuthenticatedUser;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
	private final FindAuthenticatedUser findAuthenticatedUser;

	public UserServiceImpl(UserRepository userRepository, FindAuthenticatedUser findAuthenticatedUser, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.findAuthenticatedUser = findAuthenticatedUser;
        this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional (readOnly = true)
	public UserInfoResponse getUserInfo() {
		User user = findAuthenticatedUser.getAuthenticatedUser();
		return new UserInfoResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getAuthorities().stream()
                .map(auth -> new AuthorityResponse(auth.getAuthority()))
                .toList()
        );
	}

	@Override
	public void deleteUser() {
		User user = findAuthenticatedUser.getAuthenticatedUser();
		if(isLastAdmin(user)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "First assign new admin!!");
		}
		userRepository.delete(user);
	}

	private boolean isLastAdmin(User user) {
		boolean isAdmin = user.getAuthorities().stream()
			.anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

		if(isAdmin) {
			long adminCount = userRepository.countAdminUsers(Role.ROLE_ADMIN); // Enum issue custom query error fixed !!
			return adminCount <= 1;
		}

		return false;
	}

	@Override
	public void passwordUpdate(PasswordUpdate passwordUpdateRequest) {
		User user = findAuthenticatedUser.getAuthenticatedUser();
		if(!isOldPasswordCorrect(user.getPassword(), passwordUpdateRequest.oldPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
		}

		if(!isPasswordConfirmed(passwordUpdateRequest.newPassword(), passwordUpdateRequest.repeatPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password not match with confirm!");
		}

		if(!isNewPasswordDifferent(passwordUpdateRequest.oldPassword(), passwordUpdateRequest.newPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password cant ne same with old one !");
		}

		user.setPassword(passwordEncoder.encode(passwordUpdateRequest.newPassword()));
		userRepository.save(user);
	}

	private boolean isPasswordConfirmed(String newPassword, String newPasswordConfirm) {
		return newPassword.equals(newPasswordConfirm);
	}

	private boolean isOldPasswordCorrect(String currentPassword, String oldPassword) {
		return passwordEncoder.matches(oldPassword, currentPassword);
	}

	private boolean isNewPasswordDifferent(String oldPassword, String newPAssword){
		return !oldPassword.equals(newPAssword);
	}


}
