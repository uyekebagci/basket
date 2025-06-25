package com.umutbasket.berkyazici.service.security; // PAKET YOLUNU GÜNCELLE

import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.repository.UserRepository;
import lombok.RequiredArgsConstructor; // YENİ IMPORT
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // @Autowired constructor yerine bunu kullanıyoruz
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Gereksiz olan (User) cast'ini kaldırıyoruz.
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}