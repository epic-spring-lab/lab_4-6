package com.example.lab46.repositories;

import com.example.lab46.entities.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    List<Currency> findAll();
}
