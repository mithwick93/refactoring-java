package com.mithwick93.refactoring.java.service.rentalstrategy.impl;

import com.mithwick93.refactoring.java.service.rentalstrategy.RentalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegularMovieRentalStrategyTest {
    private RentalStrategy rentalStrategy;

    @BeforeEach
    void setUp() {
        rentalStrategy = new RegularMovieRentalStrategy();
    }

    @Test
    void givenDaysRentedLessThanStandardRentalPeriod_whenGetRentalAmount_thenReturnRentalAmount() {
        int daysRented = 1;
        BigDecimal expectedRentalAmount = new BigDecimal("2.0");

        BigDecimal actualRentalAmount = rentalStrategy.getRentalAmount(daysRented);

        assertEquals(expectedRentalAmount, actualRentalAmount, "Rental amount should be 2.0");
    }

    @Test
    void givenDaysRentedMoreThanStandardRentalPeriod_whenGetRentalAmount_thenReturnRentalAmount() {
        int daysRented = 5;
        BigDecimal expectedRentalAmount = new BigDecimal("6.5");

        BigDecimal actualRentalAmount = rentalStrategy.getRentalAmount(daysRented);

        assertEquals(expectedRentalAmount, actualRentalAmount, "Rental amount should be 6.5");
    }

    @Test
    void givenCorrectDaysRented_whenGetFrequentPoints_thenReturnsFrequentPoints() {
        int daysRented = 1;
        int expectedFrequentPoints = 1;

        int actualFrequentPoints = rentalStrategy.getFrequentPoints(daysRented);

        assertEquals(
                expectedFrequentPoints, actualFrequentPoints, "Frequent points should be 1"
        );
    }
}
