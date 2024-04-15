package nttdata.messalhi.forte.dao;


import nttdata.messalhi.forte.entities.TaskDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDestinationDAO extends JpaRepository<TaskDestination, String> {
}
