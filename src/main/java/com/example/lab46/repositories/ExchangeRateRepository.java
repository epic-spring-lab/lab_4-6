package com.example.lab46.repositories;

import com.example.lab46.entities.Currency;
import com.example.lab46.entities.DateEntity;
import com.example.lab46.entities.ExchangeRate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    //auto-generated by framework
    List<ExchangeRate> findBySourceCurrencyAndTargetCurrency(Currency sourceCurrency, Currency targetCurrency);

    //query example 0
    @Query(value = "select * from rates",
            nativeQuery = true)
    List<ExchangeRate> findAll();

    //query example 1
    @Query("SELECT er from ExchangeRate er where er.date.day = ?1 " +
            "and er.date.month = ?2 and er.date.year = ?3 and er.sourceCurrency.name = ?4")
    List<ExchangeRate> findByDateAAndSourceCurrency(Integer day, Integer month, Integer year, String currency);

    //query example 2
    @Modifying
    @Transactional
    @Query(value = "update ExchangeRate set rate = :rate where id = :id")
    void updateRate(@Param("rate") Double rate, @Param("id") Long id);

    //query example 3
    @Modifying
    @Transactional
    @Query(value = "update rates set rate = :newRate, source_currency_id = :sourceCurrency, target_currency_id = :targetCurrency, date_id = :dateId where id = :id",
            nativeQuery = true)
    void updateRates(@Param("dateId") Long dateId, @Param("sourceCurrency") Long sourceCurrency,
                     @Param("targetCurrency") Long targetCurrency, @Param("newRate") double newRate, @Param("id") Long id);


    //method for named query
    List<ExchangeRate> findAllTodayExchanges(@Param("day") Integer day, @Param("month") Integer month, @Param("year") Integer year);
}
