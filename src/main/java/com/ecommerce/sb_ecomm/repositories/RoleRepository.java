package com.ecommerce.sb_ecomm.repositories;

import com.ecommerce.sb_ecomm.model.AppRole;
import com.ecommerce.sb_ecomm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole appRole);
}
