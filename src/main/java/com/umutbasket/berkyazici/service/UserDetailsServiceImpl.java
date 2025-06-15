package com.umutbasket.berkyazici.service;

import com.umutbasket.berkyazici.entity.User; // User'ı import ettiğinden emin ol
import com.umutbasket.berkyazici.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Bu metot, kullanıcı detaylarını getirmek için Spring Security tarafından çağrılır.
     * @param username Verisi istenen kullanıcıyı tanımlayan kullanıcı adı (bizim durumumuzda email).
     * @return bir UserDetails nesnesi
     * @throws UsernameNotFoundException eğer kullanıcı bulunamazsa
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Adım: Veritabanından email'e göre User nesnesini bul.
        // 2. Adım: Eğer bulunamazsa, standart hatayı fırlat.
        User user = (User) userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bu email ile kullanıcı bulunamadı: " + username));

        // 3. Adım: Bulunan User nesnesini doğrudan geri döndür.
        // User sınıfı UserDetails arayüzünü implemente ettiği için bu atama geçerlidir.
        return user;
    }
}