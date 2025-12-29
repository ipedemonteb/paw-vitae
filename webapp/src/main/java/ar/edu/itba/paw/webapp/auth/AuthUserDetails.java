package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

public class AuthUserDetails extends User {

    @Serial
    private static final long serialVersionUID = 1L;
    private final long userId;

    public AuthUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,long userId) {
        super(username, password, authorities);
        this.userId=userId;
    }

    public AuthUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,long userId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId=userId;
    }
    public long getUserId() {
        return userId;
    }
}
