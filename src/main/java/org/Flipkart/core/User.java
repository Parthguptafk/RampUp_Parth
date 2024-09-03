package org.Flipkart.core;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(
                name = "core.Author.findAllUsers",
                query = "SELECT a FROM User a"
        )
})

@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_age", nullable = false)
    private Integer userAge;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Wallet wallet;

}