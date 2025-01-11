package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByUserId(Integer userId);
    List<Asset> findByUserIdAndTypeId(Integer userId, Integer typeId);
    Optional<Asset> findByIdAndUserId(UUID id, Integer userId);
    boolean existsByIdAndUserId(UUID id, Integer userId);
} 