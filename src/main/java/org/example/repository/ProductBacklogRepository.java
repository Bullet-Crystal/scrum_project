package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.model.ProductBacklog;
import org.example.model.RoleType;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {
<<<<<<< HEAD
	Optional<ProductBacklog> findFirstByOrderByIdAsc();
=======
    Optional<ProductBacklog> findFirstByOrderByIdAsc();
>>>>>>> origin/servicePull
}
