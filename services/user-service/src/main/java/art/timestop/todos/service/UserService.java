package art.timestop.todos.service;
import art.timestop.todos.dto.request.PasswordUpdate;
import art.timestop.todos.dto.response.UserInfoResponse;

public interface  UserService {
    UserInfoResponse getUserInfo();
    void deleteUser();
    void passwordUpdate(PasswordUpdate passwordUpdate);
}
