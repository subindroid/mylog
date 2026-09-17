package com.marumiru.mylog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marumiru.mylog.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRolename(String rolename);

}
