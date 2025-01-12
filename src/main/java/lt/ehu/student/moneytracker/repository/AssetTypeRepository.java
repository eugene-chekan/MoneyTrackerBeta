package lt.ehu.student.moneytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lt.ehu.student.moneytracker.model.AssetType;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Integer> {
    Optional<AssetType> findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT at FROM AssetType at LEFT JOIN FETCH at.assets WHERE at.id = :id")
    Optional<AssetType> findByIdWithAssets(Integer id);
    
    @Query("SELECT DISTINCT at FROM AssetType at " +
           "LEFT JOIN at.assets a " +
           "WHERE a.user.id = :userId")
    List<AssetType> findByUserId(Integer userId);
}