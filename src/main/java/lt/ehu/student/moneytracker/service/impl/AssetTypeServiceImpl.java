package lt.ehu.student.moneytracker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt.ehu.student.moneytracker.model.AssetType;
import lt.ehu.student.moneytracker.repository.AssetTypeRepository;
import lt.ehu.student.moneytracker.service.AssetTypeService;
import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssetTypeServiceImpl implements AssetTypeService {
    private final AssetTypeRepository assetTypeRepository;

    @Autowired
    public AssetTypeServiceImpl(AssetTypeRepository assetTypeRepository) {
        this.assetTypeRepository = assetTypeRepository;
    }

    @Override
    public List<AssetType> findAll() {
        return assetTypeRepository.findAll();
    }

    @Override
    public Optional<AssetType> findById(Integer id) {
        return assetTypeRepository.findById(id);
    }

    @Override
    public AssetType getById(Integer id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AssetType not found"));
    }

    @Override
    public Optional<AssetType> findByName(String name) {
        return assetTypeRepository.findByName(name);
    }

    @Override
    public AssetType save(AssetType assetType) {
        if (assetType.getId() == null && assetTypeRepository.existsByName(assetType.getName())) {
            throw new IllegalArgumentException("AssetType with this name already exists");
        }
        return assetTypeRepository.save(assetType);
    }

    @Override
    public void delete(Integer id) {
        if (!assetTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("AssetType not found");
        }
        assetTypeRepository.deleteById(id);
    }
}