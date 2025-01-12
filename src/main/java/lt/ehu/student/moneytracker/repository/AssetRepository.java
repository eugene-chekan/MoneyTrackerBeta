package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.Asset;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByUser(User user);
    List<Asset> findByUserAndType(User user, AssetType type);
    Optional<Asset> findByIdAndUser(UUID id, User user);
    boolean existsByIdAndUser(UUID id, User user);
} 