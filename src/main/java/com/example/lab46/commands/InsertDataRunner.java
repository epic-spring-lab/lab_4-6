package com.example.lab46.commands;

import com.example.lab46.entities.Currency;
import com.example.lab46.entities.DateEntity;
import com.example.lab46.entities.ExchangeRate;
import com.example.lab46.services.CurrencyService;
import com.example.lab46.services.DateService;
import com.example.lab46.services.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class InsertDataRunner implements CommandLineRunner {
    private final CurrencyService currencyService;

    private final ExchangeService exchangeService;

    private final DateService dateService;

    @Override
    public void run(String... args) throws IOException {
        //clear data
        exchangeService.deleteAllRates();
        //clear dates
        dateService.deleteAll();
        //insert currencies
        if (currencyService.getAll().isEmpty()) {
            currencyService.createList(List.of(Currency.builder().name("USD").build(),
                    Currency.builder().name("UAH").build(),
                    Currency.builder().name("EUR").build(),
                    Currency.builder().name("GBP").build()
            ));
        }
        // List<Entity> create list of data, in order to insert it then in the database
        List<ExchangeRate> rates = new ArrayList<>();
        //insert dates
        List<DateEntity> dateEntityList = new ArrayList<>();
        LocalDateTime dt = LocalDateTime.of(LocalDate.now(), LocalTime.now());  // Start date
        dt = dt.minusDays(30);
        for (int i = 0; i < 30; i++) {
            dt = dt.plusDays(1);
            Date date = Date.valueOf(dt.toLocalDate());
            DateEntity dateEntity = DateEntity.builder()
                    .day(date.getDate())
                    .month(date.getMonth() + 1)
                    .year(date.getYear() + 1900)
                    .build();
            dateEntityList.add(dateEntity);
        }
        dateService.createList(dateEntityList);
        // pairs
        for (Currency source : currencyService.getAll()) {
            for (Currency target : currencyService.getAll()) {
                if (!source.getName().equals(target.getName())) {
                    Map<String, String> map = new HashMap<>(Map.of("A", "1"));
                    map.put("C1", source.getName());
                    map.put("C2", target.getName());

                    for (DateEntity date : dateService.getAll()) {
                        putDateInMap(date, map);
                        //create entity
                        ExchangeRate exchangeRate = ExchangeRate.builder()
                                .sourceCurrency(source)
                                .targetCurrency(target)
                                .date(date)
                                .rate(Double.parseDouble(parse(map)))
                                .build();
                        //fill it in the list
                        rates.add(exchangeRate);
                    }
                }
            }
        }
        //insert all the data into database
        exchangeService.createExchangeRates(rates);
    }

    private void putDateInMap(DateEntity dt, Map<String, String> map) {
        map.put("DD1", Integer.toString(dt.getDay()));
        map.put("MM1", Integer.toString(dt.getMonth()));
        map.put("YYYY1", Integer.toString(dt.getYear()));
        map.put("DD2", Integer.toString(dt.getDay()));
        map.put("MM2", Integer.toString(dt.getMonth()));
        map.put("YYYY2", Integer.toString(dt.getYear()));
    }

    private String parse(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://fxtop.com/en/historical-currency-converter.php?");
        for (String parameter : keySet) {
            stringBuilder.append("&");
            stringBuilder.append(parameter);
            stringBuilder.append("=");
            stringBuilder.append((map.get(parameter)));
        }
        stringBuilder.append("&B=1&P=&I=1&btnOK=Go%21");
        String url = stringBuilder.toString();
        Document doc = getDocument(url);
        Elements elements = doc.getElementsByTag("tbody");
        Element element = elements.get(28);
        Elements childs = element.children().get(2).children();
        String mes = childs.get(1).toString();
        return getDigitFromString(mes);
    }

    private String getDigitFromString(String mes) {
        Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher = pat.matcher(mes.replaceAll("\\s+", ""));
        int k = 0;
        while (matcher.find()) {
            if (k == 2) {
                mes = matcher.group();
            }
            k++;
        }
        return mes;
    }

    private Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
