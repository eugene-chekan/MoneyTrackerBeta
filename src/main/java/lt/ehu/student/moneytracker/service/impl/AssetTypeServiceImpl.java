package lt.ehu.student.moneytracker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt.ehu.student.moneytracker.model.AssetType;
import lt.ehu.student.moneytracker.repository.AssetTypeRepository;
import lt.ehu.student.moneytracker.service.AssetTypeService;

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
    public AssetType save(AssetType assetType) {
        return assetTypeRepository.save(assetType);
    }

    @Override
    public void delete(Integer id) {
        assetTypeRepository.deleteById(id);
    }
}