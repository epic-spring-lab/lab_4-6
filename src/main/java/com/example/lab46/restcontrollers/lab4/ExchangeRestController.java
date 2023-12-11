package com.example.lab46.restcontrollers.lab4;

import com.example.lab46.dtos.CurrencyDTO;
import com.example.lab46.entities.ExchangeRate;
import com.example.lab46.services.CurrencyService;
import com.example.lab46.services.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lab4/exchange")
@RequiredArgsConstructor
public class ExchangeRestController {
    private final ExchangeService exchangeService;

    @GetMapping("/today")
    public ResponseEntity<List<CurrencyDTO>> getAllTodayExchanges() {
        return ResponseEntity.ok(exchangeService.getAllTodayCurrencies());
    }

    @GetMapping("/pair")
    public ResponseEntity<List<ExchangeRate>> getAllExchangesByPair(@RequestParam(required = false, name = "source") String source, @RequestParam(required = false, name = "target") String target) {
        return ResponseEntity.ok(exchangeService.getAllByPair(source, target));
    }

    @GetMapping("")
    public ResponseEntity<List<ExchangeRate>> getAllExchanges() {
        return ResponseEntity.ok(exchangeService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<ExchangeRate> createExchange(@RequestBody ExchangeRate exchangeRate) {
        return new ResponseEntity<>(exchangeService.create(exchangeRate), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public void updateExchange(@RequestBody ExchangeRate exchangeRate, @PathVariable Long id) {
        exchangeService.update(id, exchangeRate);
    }

    @PutMapping("/rate/{id}")
    public void updateExchangeRate(@RequestBody String rate, @PathVariable Long id) {
        exchangeService.updateRate(id, Double.parseDouble(rate));
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        if (exchangeService.getById(id) == null) {
            return HttpStatus.NO_CONTENT;
        } else {
            exchangeService.delete(id);
            return HttpStatus.OK;
        }
    }
}
