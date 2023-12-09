package com.example.lab46.services;

import com.example.lab46.entities.Currency;

import java.util.List;

public interface CurrencyService {

    Currency getById(Long id);
    List<Currency> getAll();

    Currency create(Currency currency);

    List<Currency> createList(List<Currency> currencyList);

    void delete(Long id);

    void update(Long id, Currency currency);
}
