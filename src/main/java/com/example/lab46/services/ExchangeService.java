package com.example.lab46.services;

import com.example.lab46.dtos.CurrencyDTO;
import com.example.lab46.dtos.DateWithExchangeRatesDTO;
import com.example.lab46.entities.ExchangeRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExchangeService {
    List<ExchangeRate> createExchangeRates(List<ExchangeRate> rates);

    List<DateWithExchangeRatesDTO> getAllCurrenciesByDate(String dateFrom, String dateTo, String currency1);

    List<CurrencyDTO> getAllTodayCurrencies();

    CurrencyDTO getTodayCurrency(String currency1, String currency2);

    void deleteAllRates();

    List<ExchangeRate> getAll();

    List<ExchangeRate> getAllByPair(String source, String target);

    void updateRate(Long id, Double rate);

    ExchangeRate getById(Long id);

    ExchangeRate create(ExchangeRate exchangeRate);

    void delete(Long id);

    void update(Long id, ExchangeRate exchangeRate);
}
