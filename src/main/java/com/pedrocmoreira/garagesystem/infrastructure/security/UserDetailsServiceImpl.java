package com.pedrocmoreira.garagesystem.infrastructure.security;

import com.pedrocmoreira.garagesystem.domain.model.User;
import com.pedrocmoreira.garagesystem.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.filterByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + username
                ));

        if(!user.getActive()) {
            throw new UsernameNotFoundException("Usuário inativo: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()).
                authorities(List.of(new SimpleGrantedAuthority(
                        "ROLE_" + user.getUserType().name()
                ))).build();

    }
}
