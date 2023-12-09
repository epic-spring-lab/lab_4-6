package com.example.lab46.repositories;

import com.example.lab46.entities.DateEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DateEntityRepo extends CrudRepository<DateEntity, Long> {
    List<DateEntity> findAll();
}
