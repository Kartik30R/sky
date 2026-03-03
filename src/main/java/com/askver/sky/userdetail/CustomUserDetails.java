package com.askver.sky.userdetail;

import com.askver.sky.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails
        implements UserDetails {

    private UUID userId;
    private UUID companyId;
    private String email;
    private String password;
    private Role role;

    public CustomUserDetails(
            UUID userId,
            UUID companyId,
            String email,
            String password,
            Role role
    ) {
        this.userId = userId;
        this.companyId = companyId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getUserId() { return userId; }
    public UUID getCompanyId() { return companyId; }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(
                        role.name()
                )
        );
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired(){ return true; }
    @Override public boolean isAccountNonLocked(){ return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled(){ return true; }
}