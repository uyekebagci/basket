package com.umutbasket.berkyazici;

import com.umutbasket.berkyazici.entity.Plan;
import com.umutbasket.berkyazici.entity.Role;
import com.umutbasket.berkyazici.entity.User;
import com.umutbasket.berkyazici.repository.PlanRepository;
import com.umutbasket.berkyazici.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private PlanRepository planRepository;

    @Override
    public void run(String... args) throws Exception {
        // DAHA SAĞLAM KONTROL: Veritabanında admin@example.com email'ine sahip bir kullanıcı var mı?
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            createAdminUser();
        }
    }

    private void createAdminUser() {
        User adminUser = new User();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(Role.ADMIN); // ROLÜ KESİNLİKLE ADMIN OLARAK AYARLIYORUZ
        adminUser.setBirthDay(Date.valueOf("1990-01-01"));
        adminUser.setHeight(175);
        adminUser.setWeight(70.0);
        adminUser.setGender("Other");

        userRepository.save(adminUser);
        System.out.println(">>> Varsayılan ADMIN kullanıcısı oluşturuldu: admin@example.com <<<");
    }
}