package art.timestop.users.service;
import art.timestop.users.dto.request.PasswordUpdate;
import art.timestop.users.dto.response.UserInfoResponse;

public interface  UserService {
    UserInfoResponse getUserInfo();
    void deleteUser();
    void passwordUpdate(PasswordUpdate passwordUpdate);
}
