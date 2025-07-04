package com.umutbasket.berkyazici.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // Changed from clientId to userId

    @NotNull
    @Column
    private String firstName;

    @NotNull
    @Column
    private String lastName;

    @Valid
    @NotNull
    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Kullanıcının rolünü bir string olarak saklayacağız (örn: "USER", "ADMIN")
        // Bu metot, bu string'i bir GrantedAuthority listesine çevirir.
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        // Giriş için benzersiz kimlik olarak email'i kullanacağız.
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Şimdilik, kullanıcı hesaplarının süresinin dolmadığını varsayıyoruz.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Şimdilik, kullanıcı hesaplarının kilitlenmediğini varsayıyoruz.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Şimdilik, giriş bilgilerinin süresinin dolmadığını varsayıyoruz.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Şimdilik, tüm kullanıcıların aktif olduğunu varsayıyoruz.
        return true;
    }

    @NotNull
    @Column
    private Date birthDay;

    @NotNull
    @Min(value = 50, message = "Height must be at least 50 cm")
    @Column
    private Integer height;

    @NotNull
    @Min(value = 10, message = "Weight must be at least 10 kg")
    @Column
    private Double weight;

    @NotNull
    @Min(value = 0, message = "Age must be a positive number")
    @Column
    private Integer age;

    @NotNull
    @Size(max = 10, message = "Gender must be 'Male', 'Female', or 'Other'")
    @Column
    private String gender;

    @Enumerated(EnumType.STRING) // Bu anotasyon enum'ın ismini (USER, ADMIN) veritabanına String olarak kaydeder.
    @Column
    private Role role = Role.USER; // Tipi String'den Role'a çeviriyoruz ve varsayılan değeri ayarlıyoruz.

    @Column
    private String plan = "Free";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // The mappedBy="user" remains the same as the property name in Subscriber is 'user'
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Subscriber subscriber;

    public User() {
    }

    // Constructor updated to reflect userId
    public User(Long userId, String firstName, String lastName, String email, String password, Date birthDay,
                Integer height, Double weight, String gender,
                Role role, String plan) {
        this.userId = userId; // Changed from clientId to userId
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthDay = birthDay;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.role = role;
        this.plan = plan;
    }

    private void calculateAge() {
        if (this.birthDay != null) {
            LocalDate birthLocalDate = this.birthDay.toLocalDate();
            LocalDate today = LocalDate.now();
            this.age = Period.between(birthLocalDate, today).getYears();
        } else {
            this.age = null;
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateAge();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateAge();
    }

    public void setSubscriber(Subscriber subscriber) {
        if (this.subscriber != null) {
            this.subscriber.setUser(null);
        }
        this.subscriber = subscriber;
        if (subscriber != null) {
            subscriber.setUser(this);
        }
    }




}