package thuvien.config;

import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class CustomUserDetails extends User {
    private final thuvien.entity.User user; // Entity User của bạn

    public CustomUserDetails(thuvien.entity.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), user.getIsActive(), true, true, true, authorities);
        this.user = user;
    }

    public thuvien.entity.User getUser() {
        return user;
    }
}