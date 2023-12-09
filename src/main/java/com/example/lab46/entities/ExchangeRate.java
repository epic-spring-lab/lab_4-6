package com.example.lab46.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rates")
@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "date_id", referencedColumnName = "id", nullable = false)
    private DateEntity date;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sourceCurrency_id", referencedColumnName = "id", nullable = false)
    private Currency sourceCurrency;
    @ManyToOne(optional = false)
    @JoinColumn(name = "targetCurrency_id", referencedColumnName = "id", nullable = false)
    private Currency targetCurrency;
    @Column(precision=10)
    private double rate;
}
