package lt.ehu.student.moneytracker.service;

import java.util.List;
import java.util.Optional;

import lt.ehu.student.moneytracker.model.AssetType;

public interface AssetTypeService {
    List<AssetType> findAll();
    Optional<AssetType> findById(Integer id);
    Optional<AssetType> findByName(String name);
    AssetType save(AssetType assetType);
    AssetType getById(Integer id);
    void delete(Integer id);
}