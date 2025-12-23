package org.example.householdbackend.repositories;

import org.example.householdbackend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
