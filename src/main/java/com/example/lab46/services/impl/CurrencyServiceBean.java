package com.example.lab46.services.impl;

import com.example.lab46.entities.Currency;
import com.example.lab46.repositories.CurrencyRepository;
import com.example.lab46.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public class CurrencyServiceBean implements CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepo;

    @Override
    public List<Currency> getAll() {
        return currencyRepo.findAll();
    }

    @Override
    public Currency create(Currency currency) {
        return currencyRepo.save(currency);
    }

    @Override
    public List<Currency> createList(List<Currency> currencyList) {
        return (List<Currency>) currencyRepo.saveAll(currencyList);
    }

    @Override
    public void delete(Long id) {
        currencyRepo.deleteById(id);
    }

    @Override
    public void update(Long id, Currency currency) {
        currencyRepo.findById(id).map(toUpdate -> {
            toUpdate.setName(currency.getName());
            return currencyRepo.save(toUpdate);
        });
    }

    @Override
    public Currency getById(Long id) {
        return currencyRepo.findById(id).orElse(null);
    }
}
