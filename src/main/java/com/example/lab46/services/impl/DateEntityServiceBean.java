package com.example.lab46.services.impl;

import com.example.lab46.entities.DateEntity;
import com.example.lab46.repositories.DateEntityRepo;
import com.example.lab46.services.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateEntityServiceBean implements DateService {
    private final DateEntityRepo dateEntityRepo;

    @Override
    public List<DateEntity> getAll() {
        return dateEntityRepo.findAll();
    }

    @Override
    public DateEntity create(DateEntity dateEntity) {
        return dateEntityRepo.save(dateEntity);
    }

    @Override
    public List<DateEntity> createList(List<DateEntity> dateEntityList) {
        return (List<DateEntity>) dateEntityRepo.saveAll(dateEntityList);
    }

    @Override
    public void delete(Long id) {
        dateEntityRepo.deleteById(id);
    }

    @Override
    public void update(Long id, DateEntity dateEntity) {
        dateEntityRepo.findById(id).map(toUpdate -> {
            toUpdate.setDay(dateEntity.getDay());
            toUpdate.setMonth(dateEntity.getMonth());
            toUpdate.setYear(dateEntity.getYear());
            return dateEntityRepo.save(toUpdate);
        });
    }

    @Override
    public void deleteAll() {
        dateEntityRepo.deleteAll();
    }

    @Override
    public List<DateEntity> getDatesInRange(String dateFrom, String dateTo) {
        // Format - YYYY-MM-DD
        return dateEntityRepo.findAll().stream().filter(dateEntity -> {
            String stringDateEntityDate = dateEntity.getYear().toString() + "-"
                    + dateEntity.getMonth().toString() + "-"
                    + dateEntity.getDay().toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dateFromDate = format.parse(dateFrom);
                Date dateToDate = format.parse(dateTo);
                Date dateEntityDate = format.parse(stringDateEntityDate);
                if (dateEntityDate.before(dateToDate)
                        && dateEntityDate.after(dateFromDate)) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return false;
        }).toList();

    }
}
