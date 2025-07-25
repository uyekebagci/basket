package com.umutbasket.berkyazici.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @NotNull
    private User user;

    @ManyToOne
    private Plan plan;

    @Column(name = "subscription_start_date")
    private LocalDateTime subscriptionStartDate;

    @Column(name = "subscription_end_date")
    private LocalDateTime subscriptionEndDate;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "plan_type")
    private String planType;

    public Subscriber() {
    }

    public Subscriber(User user, LocalDateTime subscriptionStartDate, LocalDateTime subscriptionEndDate, Boolean isActive, String planType) {
        this.user = user;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
        this.isActive = isActive;
        this.planType = planType;
    }

}
