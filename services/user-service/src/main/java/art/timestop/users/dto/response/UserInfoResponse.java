package art.timestop.users.dto.response;

import java.util.List;

public record UserInfoResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    List<AuthorityResponse> authorities
) {}
