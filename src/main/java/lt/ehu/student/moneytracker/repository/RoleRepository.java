package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.id = :id")
    Optional<Role> findByIdWithUsers(Integer id);
    
    @Query("SELECT r FROM Role r WHERE r.name IN :names")
    List<Role> findByNames(Collection<String> names);
    
    @Query("SELECT DISTINCT r FROM Role r " +
           "LEFT JOIN r.users u " +
           "WHERE u.id = :userId")
    List<Role> findByUserId(Integer userId);
} 