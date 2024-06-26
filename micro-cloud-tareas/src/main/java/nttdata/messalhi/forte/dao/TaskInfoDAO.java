package nttdata.messalhi.forte.dao;


import nttdata.messalhi.forte.entities.TaskInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskInfoDAO extends JpaRepository<TaskInfo, Long> {
    Optional<TaskInfo> findById(Long Id);
}
