package org.example.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            select distinct p from Project p
            join p.members m
            where m.user.username = :username
            order by p.id
            """)
    List<Project> findAllForUsername(@Param("username") String username);
}
