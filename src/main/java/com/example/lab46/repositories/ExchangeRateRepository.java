package com.example.lab46.repositories;

import com.example.lab46.entities.ExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    List<ExchangeRate> findAll();
}
