package org.Flipkart.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@Table(name = "wallet")


@NamedQueries({
        @NamedQuery(
                name = "core.Wallet.findBalance",
                query = "SELECT w.balance FROM Wallet w WHERE w.userId = :userId"
        ),
        @NamedQuery(
                name = "core.Wallet.updateBalance",
                query = "UPDATE Wallet w SET w.balance = :balance WHERE w.userId = :userId"
        ),
        @NamedQuery(
                name = "core.Wallet.getWalletByUserId",
                query = "SELECT w FROM Wallet w WHERE w.userId = :userId"
        )
})


@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Column(name = "user_balance", nullable = false)
    private Long balance;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
