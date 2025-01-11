package lt.ehu.student.moneytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ehu.student.moneytracker.model.AssetType;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Integer> {
}