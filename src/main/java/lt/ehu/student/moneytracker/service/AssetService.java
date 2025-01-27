package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.Asset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetService {
    Asset save(Asset asset);
    Optional<Asset> findById(UUID id);
    Asset getById(UUID id);
    List<Asset> findByUserId(Integer userId);
    List<Asset> findByUserIdAndType(Integer userId, Integer typeId);
    Optional<Asset> findByIdAndUserId(UUID id, Integer userId);
    void delete(UUID id);
    Long count();
    void validateUserAccess(UUID assetId, Integer userId);
    boolean existsById(UUID id);
} 