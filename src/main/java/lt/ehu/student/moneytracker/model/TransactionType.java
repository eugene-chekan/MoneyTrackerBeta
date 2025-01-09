package lt.ehu.student.moneytracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transaction_type")
@Data
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(columnDefinition = "text")
    private String description;
}
