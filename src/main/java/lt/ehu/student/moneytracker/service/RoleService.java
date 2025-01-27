package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findById(Integer id);
    Optional<Role> findByName(String name);
    Role getById(Integer id);
    Role getByName(String name);
    Role save(Role role);
    void delete(Integer id);
} 