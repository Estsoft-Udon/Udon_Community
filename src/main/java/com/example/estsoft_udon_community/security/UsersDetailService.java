package com.example.estsoft_udon_community.security;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByLoginId(username);

        if (user == null || user.getIsDeleted()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 로그인 성공 시 lastLoginAt update
        user.updateLastLoginAt();
        usersRepository.save(user);

        return new CustomUserDetails(user);
    }
}