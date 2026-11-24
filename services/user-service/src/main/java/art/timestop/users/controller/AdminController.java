package art.timestop.users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import art.timestop.users.dto.response.UserInfoResponse;
import art.timestop.users.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;

@Tag (name = "Admin Rest API Endpoint")
@RequestMapping ("/api/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
		this.adminService = adminService;
    }

    @Operation (summary = "Get all users")
    @ResponseStatus ( HttpStatus.OK )
    @GetMapping
    public List<UserInfoResponse> getAllUsers(){
        return adminService.getAllUsers();
    }

    @Operation (summary = "Promote user to admin")
    @ResponseStatus ( HttpStatus.OK )
    @GetMapping ("/{userId}/role")
    public UserInfoResponse makeAdmin(@PathVariable @Min (1) long userId) {
        return adminService.makeAdmin(userId);
    }

    @Operation (summary = "Delete non-admin User")
    @ResponseStatus ( HttpStatus.NO_CONTENT )
    @DeleteMapping ("/{userId}/delete")
    public void deleteUser(@PathVariable @Min (1) long userId){
        adminService.deleteNonAdmin(userId);
    }

}
