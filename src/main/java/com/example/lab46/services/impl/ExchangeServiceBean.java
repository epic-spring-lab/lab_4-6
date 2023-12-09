package com.example.lab46.services.impl;

import com.example.lab46.dtos.CurrencyDTO;
import com.example.lab46.dtos.DateWithExchangeRatesDTO;
import com.example.lab46.entities.DateEntity;
import com.example.lab46.entities.ExchangeRate;
import com.example.lab46.repositories.ExchangeRateRepository;
import com.example.lab46.services.DateService;
import com.example.lab46.services.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExchangeServiceBean implements ExchangeService {
    private final ExchangeRateRepository exchangeRepo;
    private final DateService dateService;

    @Override
    public List<ExchangeRate> createExchangeRates(List<ExchangeRate> rates) {
        return (List<ExchangeRate>) exchangeRepo.saveAll(rates);
    }

    @Override
    public List<DateWithExchangeRatesDTO> getAllCurrenciesByDate(String dateFrom, String dateTo, String currency1) {
        // Format - YYYY-MM-DD
        List<DateEntity> dates = dateService.getDatesInRange(dateFrom, dateTo);

        return dates.stream().map(dateEntity -> {
            List<ExchangeRate> exchangeRates = exchangeRepo.findAll().stream().filter(exchangeRate -> Objects.equals(exchangeRate.getDate().getDay(), dateEntity.getDay())
                    && Objects.equals(exchangeRate.getDate().getMonth(), dateEntity.getMonth())
                    && Objects.equals(exchangeRate.getDate().getYear(), dateEntity.getYear())
                    && exchangeRate.getSourceCurrency().getName().equals(currency1)).toList();
            return DateWithExchangeRatesDTO.builder()
                    .date(dateEntity)
                    .exchangeRates(exchangeRates)
                    .build();
        }).filter(
                dateWithExchangeRatesDTO -> dateWithExchangeRatesDTO.getExchangeRates().size() > 0
        ).toList();
    }

    @Override
    public List<CurrencyDTO> getAllTodayCurrencies() {
        return GetTodayCurrencies().stream().map(CurrencyMapper::toDto).toList();
    }

    @Override
    public CurrencyDTO getTodayCurrency(String currency1, String currency2) {
        return GetTodayCurrencies().stream().filter(exchangeRate -> exchangeRate.getSourceCurrency().getName().equals(currency1)
                && exchangeRate.getTargetCurrency().getName().equals(currency2)).
                findFirst().map(CurrencyMapper::toDto).get();
    }

    private List<ExchangeRate> GetTodayCurrencies() {
        LocalDate localDate = LocalDate.now();
        return exchangeRepo.findAll().stream().filter(exchangeRate -> exchangeRate.getDate().getDay() == localDate.getDayOfMonth()
                && exchangeRate.getDate().getMonth() == localDate.getMonthValue()).toList();
    }

    @Override
    public void deleteAllRates() {
        exchangeRepo.deleteAll();
    }

    @Override
    public List<ExchangeRate> getAll() {
        return exchangeRepo.findAll();
    }

    @Override
    public void updateRate(Long id, Double rate) {
        if(exchangeRepo.findById(id).isEmpty()) throw new NullPointerException();
        ExchangeRate exchangeRate = exchangeRepo.findById(id).get();
        exchangeRate.setRate(rate);
        exchangeRepo.save(exchangeRate);
    }

    @Override
    public ExchangeRate getById(Long id) {
        return exchangeRepo.findById(id).orElse(null);
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        return exchangeRepo.save(exchangeRate);
    }

    @Override
    public void delete(Long id) {
        exchangeRepo.deleteById(id);
    }

    @Override
    public ExchangeRate update(Long id, ExchangeRate exchangeRate) {
        if(exchangeRepo.findById(id).isEmpty()) throw new NullPointerException();
        ExchangeRate exchangeRateToUpdate = exchangeRepo.findById(id).get();
        exchangeRateToUpdate.setRate(exchangeRate.getRate());
        exchangeRateToUpdate.setDate(exchangeRate.getDate());
        exchangeRateToUpdate.setSourceCurrency(exchangeRate.getSourceCurrency());
        exchangeRateToUpdate.setTargetCurrency(exchangeRate.getTargetCurrency());
        return exchangeRepo.save(exchangeRateToUpdate);
    }


    private static class CurrencyMapper {
        private static CurrencyDTO toDto(ExchangeRate exchangeRate) {
            return CurrencyDTO.builder()
                    .sourceCurrency(exchangeRate.getSourceCurrency().getName())
                    .targetCurrency(exchangeRate.getTargetCurrency().getName())
                    .rate(exchangeRate.getRate())
                    .exchangeDate(exchangeRate.getDate())
                    .build();
        }
    }

}
