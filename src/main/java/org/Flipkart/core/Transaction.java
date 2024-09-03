package org.Flipkart.core;


import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@NamedQueries({
        @NamedQuery(
                name = "core.Transaction.findAllTransactionByUserId",
                query = "SELECT t FROM Transaction t WHERE t.userId = :id"
        ),

        @NamedQuery(
                name = "core.Transaction.getTransactionByDate",
                query = "SELECT t FROM Transaction t WHERE t.userId = :id AND t.timestamp BETWEEN :startDate AND :endDate"
        ),

        @NamedQuery(
                name = "core.Transaction.findTransactionByType",
                query = "SELECT t FROM Transaction t WHERE t.userId = :userId AND t.type = :type"
        )
})

@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "date", nullable = false)
    private Date timestamp;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
