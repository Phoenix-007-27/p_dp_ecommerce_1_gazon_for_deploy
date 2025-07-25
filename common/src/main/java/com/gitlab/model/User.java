package com.gitlab.model;

import lombok.*;

import java.time.LocalDate;
import javax.persistence.*;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "public", catalog = "postgres")
@NamedEntityGraph(name = "userWithSets",
        attributeNodes = {
        @NamedAttributeNode("bankCardsSet"),
        @NamedAttributeNode("shippingAddressSet"),
        @NamedAttributeNode("rolesSet")})
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "security_question")
    private String securityQuestion;

    @Column(name = "answer_question")
    private String answerQuestion;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Passport passport;

    @Column(name = "create_date")
    private LocalDate createDate;

    @OneToMany(mappedBy="id", cascade = CascadeType.ALL)
    private Set<BankCard> bankCardsSet;

    @OneToMany(mappedBy="id",cascade = CascadeType.ALL)
    private Set<ShippingAddress> shippingAddressSet;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> rolesSet;


    @AllArgsConstructor
    @Getter
    public enum Gender {
        MALE("МУЖСКОЙ"),FEMALE("ЖЕНСКИЙ"),NOT_SPECIFIED("НЕ УКАЗАН");

        private final String sex;

    }

}
