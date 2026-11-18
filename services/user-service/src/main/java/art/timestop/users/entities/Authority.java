package art.timestop.users.entities;

import org.springframework.security.core.GrantedAuthority;

import art.timestop.users.enums.Role;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Authority implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    private Role role;

    public Authority() {}

    public Authority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    public Role getRole() {
        return role;
    }
}
