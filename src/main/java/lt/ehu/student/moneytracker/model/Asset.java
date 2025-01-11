package lt.ehu.student.moneytracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "asset")
@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private AssetType type;

    @Column(length = 50)
    private String name;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(columnDefinition = "text")
    private String description;
}
