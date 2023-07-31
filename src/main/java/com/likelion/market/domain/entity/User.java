package com.likelion.market.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String phone;

    private String email;

    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SalesItem> salesItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Negotiation> negotiations;

    public void updateInfo(CustomUserDetails userDetails) {
        this.phone = userDetails.getPhone();
        this.email = userDetails.getEmail();
        this.address = userDetails.getAddress();
    }
}
