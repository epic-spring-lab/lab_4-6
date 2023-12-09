package com.example.lab46.services;

import com.example.lab46.entities.DateEntity;

import java.util.List;

public interface DateService {
    List<DateEntity> getAll();

    List<DateEntity> getDatesInRange(String dateFrom, String dateTo);

    DateEntity create(DateEntity dateEntity);

    List<DateEntity> createList(List<DateEntity> dateEntityList);

    void delete(Long id);

    void update(Long id, DateEntity dateEntity);

    void deleteAll();
}
