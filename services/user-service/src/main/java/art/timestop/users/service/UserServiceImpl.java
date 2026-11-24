package art.timestop.users.service;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import art.timestop.users.dto.request.PasswordUpdate;
import art.timestop.users.dto.response.AuthorityResponse;
import art.timestop.users.dto.response.UserInfoResponse;
import art.timestop.users.entities.OutboxEvent;
import art.timestop.users.entities.User;
import art.timestop.users.enums.Role;
import art.timestop.users.repository.OutboxRepository;
import art.timestop.users.repository.UserRepository;
import art.timestop.users.utils.FindAuthenticatedUser;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
	private final FindAuthenticatedUser findAuthenticatedUser;
	private final OutboxRepository outboxRepository;

	public UserServiceImpl(UserRepository userRepository, FindAuthenticatedUser findAuthenticatedUser, PasswordEncoder passwordEncoder, OutboxRepository outboxRepository) {
		this.userRepository = userRepository;
		this.findAuthenticatedUser = findAuthenticatedUser;
        this.passwordEncoder = passwordEncoder;
		this.outboxRepository = outboxRepository;
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

	@Transactional
	@Override
	public void deleteUser() {
		User user = findAuthenticatedUser.getAuthenticatedUser();
		if(isLastAdmin(user)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "First assign new admin!!");
		}

		userRepository.delete(user);
		//kafkaTemplate.send(
       //    "user.deleted",
       //    String.valueOf(user.getId())
       //);

        OutboxEvent event = new OutboxEvent(
                "USER_DELETED",
                String.valueOf(user.getId()),
                String.valueOf(user.getId())
            );

        outboxRepository.save(event);
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
