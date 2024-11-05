package com.example.estsoft_udon_community.security;

import com.example.estsoft_udon_community.entity.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Users user; // Users 엔티티

    public Users getUser() {
        return user; // 추가적으로 엔티티 접근 가능
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
        // 사용자의 권한을 반환. grade에 따라 권한 설정.
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getGrade().name())); // grade 필드 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }
}
