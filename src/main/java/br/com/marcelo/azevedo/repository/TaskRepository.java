package br.com.marcelo.azevedo.repository;

import br.com.marcelo.azevedo.entity.TaskEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@EnableScan
@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, String> {

    List<TaskEntity> findAllByBelongsToUserId(String userOwnerId);

    Optional<TaskEntity> findByIdAndBelongsToUserId(String taskId, String userOwnerId);

}
