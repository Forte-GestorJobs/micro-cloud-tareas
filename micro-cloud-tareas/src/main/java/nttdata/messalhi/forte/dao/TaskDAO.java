package nttdata.messalhi.forte.dao;


import nttdata.messalhi.forte.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskDAO extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(String userId, Pageable pageable);
    Optional<Task> findByNameAndUserId(String name, String userId);

    long countByUserId(String userId);
}
