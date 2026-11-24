package art.timestop.users.service;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import art.timestop.users.dto.response.AuthorityResponse;
import art.timestop.users.dto.response.UserInfoResponse;
import art.timestop.users.entities.Authority;
import art.timestop.users.entities.User;
import art.timestop.users.enums.Role;
import art.timestop.users.repository.UserRepository;

public class AdminServiceImpl implements AdminService {

    public final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
    }

    @Transactional (readOnly = true)
	@Override
	public List<UserInfoResponse> getAllUsers() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), false)
		    .map(this::convertToResponse).toList();
	}

	private UserInfoResponse convertToResponse(User user) {
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
	public UserInfoResponse makeAdmin(long userId) {
		Optional<User> user = userRepository.findById(userId);

		if(user.isEmpty() || user.get().getAuthorities().stream().anyMatch(AuthorityResponse ->
		    Role.ROLE_ADMIN.name()
			.equals(AuthorityResponse.getAuthority()))) {
			    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already admin !!");
		}


		List<Authority> authorities = new ArrayList<>();
		authorities.add(new Authority(Role.ROLE_ENPLOYEE));
		authorities.add(new Authority(Role.ROLE_ADMIN));
		user.get().setAuthorities(authorities);

		User userSave = userRepository.save(user.get());

		return convertToResponse(userSave);

	}

	@Transactional
	@Override
	public void deleteNonAdmin(long userId) {
         Optional<User> user = userRepository.findById(userId);

       	if(user.isEmpty() || user.get().getAuthorities().stream().anyMatch(AuthorityResponse ->
       	    Role.ROLE_ADMIN.name()
      		.equals(AuthorityResponse.getAuthority()))) {
      		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or already admin !!");
       	}

        userRepository.delete(user.get());


	}

}
