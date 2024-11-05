package com.example.estsoft_udon_community.security;
import com.example.estsoft_udon_community.entity.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // 추가적으로 엔티티 접근 가능
    private final Users user; // Users 엔티티

    public Users getUser() {
        return user;
    }

    @Override
    public String getUsername() { // getLogInId() 대신 getUsername() 사용
        return user.getLoginId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return !user.getIsDeleted(); // 활성화 여부
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getGrade().name()));

        System.out.println("User authorities: " + authorities); // 로그로 권한 확인
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return false; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false; // 자격 증명 만료 여부
    }
}