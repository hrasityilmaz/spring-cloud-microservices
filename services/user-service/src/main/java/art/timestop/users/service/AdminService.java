package art.timestop.users.service;

import java.util.List;

import art.timestop.users.dto.response.UserInfoResponse;

public interface AdminService {
    List<UserInfoResponse> getAllUsers();
    UserInfoResponse makeAdmin(long userId);
    void deleteNonAdmin(long userId);
}
