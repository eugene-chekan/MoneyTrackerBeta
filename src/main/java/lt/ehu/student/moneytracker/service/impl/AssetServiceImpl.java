package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.Asset;
import lt.ehu.student.moneytracker.repository.AssetRepository;
import lt.ehu.student.moneytracker.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public Optional<Asset> findById(UUID id) {
        return assetRepository.findById(id);
    }

    @Override
    public List<Asset> findByUserId(Integer userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public List<Asset> findByUserIdAndType(Integer userId, Integer typeId) {
        return assetRepository.findByUserIdAndTypeId(userId, typeId);
    }

    @Override
    public Optional<Asset> findByIdAndUserId(UUID id, Integer userId) {
        return assetRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public void delete(UUID id) {
        assetRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return assetRepository.count();
    }
}