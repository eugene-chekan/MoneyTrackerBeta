package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
import lt.ehu.student.moneytracker.model.Role;
import lt.ehu.student.moneytracker.repository.RoleRepository;
import lt.ehu.student.moneytracker.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role getById(Integer id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role getByName(String name) {
        return findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));
    }

    @Override
    public Role save(Role role) {
        if (role.getId() == null && roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role with this name already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void delete(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }
} 