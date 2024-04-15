package nttdata.messalhi.forte.dao;


import nttdata.messalhi.forte.entities.TaskSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskScheduleDAO extends JpaRepository<TaskSchedule, String> {
    Optional<TaskSchedule> findById(String id);
}
