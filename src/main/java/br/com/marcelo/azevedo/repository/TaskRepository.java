package br.com.marcelo.azevedo.repository;

import br.com.marcelo.azevedo.entity.TaskEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, String> {

}
