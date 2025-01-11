package lt.ehu.student.moneytracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "asset_types")
@Data
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}