package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.Asset;
import lt.ehu.student.moneytracker.model.AssetType;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.repository.AssetRepository;
import lt.ehu.student.moneytracker.service.AssetService;
import lt.ehu.student.moneytracker.service.UserService;
import lt.ehu.student.moneytracker.service.AssetTypeService;
import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final UserService userService;
    private final AssetTypeService assetTypeService;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, UserService userService, AssetTypeService assetTypeService) {
        this.assetRepository = assetRepository;
        this.userService = userService;
        this.assetTypeService = assetTypeService;
    }

    @Override
    public Asset save(Asset asset) {
        if (asset.getId() == null) {
            validateNewAsset(asset);
        } else {
            validateExistingAsset(asset);
        }
        return assetRepository.save(asset);
    }

    @Override
    public Optional<Asset> findById(UUID id) {
        return assetRepository.findById(id);
    }

    @Override
    public Asset getById(UUID id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
    }

    @Override
    public List<Asset> findByUserId(Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return assetRepository.findByUser(user);
    }

    @Override
    public List<Asset> findByUserIdAndType(Integer userId, Integer typeId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        AssetType type = assetTypeService.findById(typeId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset type not found"));
        return assetRepository.findByUserAndType(user, type);
    }

    @Override
    public Optional<Asset> findByIdAndUserId(UUID id, Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return assetRepository.findByIdAndUser(id, user);
    }

    @Override
    public void delete(UUID id) {
        if (!assetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asset not found");
        }
        assetRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return assetRepository.count();
    }

    @Override
    public void validateUserAccess(UUID assetId, Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Asset asset = getById(assetId);
        if (!asset.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have access to this asset");
        }
    }

    private void validateNewAsset(Asset asset) {
        if (asset.getBalance() == null || asset.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Asset balance cannot be negative");
        }
        if (asset.getName() == null || asset.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Asset name cannot be empty");
        }
    }

    private void validateExistingAsset(Asset asset) {
        Asset existing = getById(asset.getId());
        if (!existing.getUser().getId().equals(asset.getUser().getId())) {
            throw new AccessDeniedException("Cannot modify asset ownership");
        }
        validateNewAsset(asset);
    }

    @Override
    public boolean existsById(UUID id) {
        return assetRepository.existsById(id);
    }
}