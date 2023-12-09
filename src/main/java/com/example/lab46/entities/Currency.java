package com.example.lab46.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "currency")
@Setter
@Getter
@AllArgsConstructor
@Builder
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "sourceCurrency", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ExchangeRate> sourceRates;

    @OneToMany(mappedBy = "targetCurrency", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ExchangeRate> targetRates;

    public Currency() {

    }
}
