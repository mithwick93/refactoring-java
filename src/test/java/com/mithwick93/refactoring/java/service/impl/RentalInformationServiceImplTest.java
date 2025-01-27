package com.mithwick93.refactoring.java.service.impl;

import com.mithwick93.refactoring.java.entity.Customer;
import com.mithwick93.refactoring.java.entity.MovieRental;
import com.mithwick93.refactoring.java.repositroy.MovieRepository;
import com.mithwick93.refactoring.java.repositroy.impl.MovieRepositoryImpl;
import com.mithwick93.refactoring.java.service.RentalInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentalInformationServiceImplTest {
    private RentalInformationService rentalInformationService;

    @BeforeEach
    void setUp() {
        MovieRepository movieRepository = new MovieRepositoryImpl();
        rentalInformationService = new RentalInformationServiceImpl(movieRepository);
    }

    @Test
    void givenNullCustomer_whenStatement_thenThrowIllegalArgumentException() {
        Customer customer = null;

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Customer cannot be null"
        );
    }

    @Test
    void givenCustomerWithNullRentalsAndNullName_whenStatement_thenThrowIllegalArgumentException() {
        List<MovieRental> movieRentals = null;
        Customer customer = new Customer(null, movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Customer name cannot be null"
        );
    }

    @Test
    void givenCustomerWithNoRentalsAndNoName_whenStatement_thenThrowIllegalArgumentException() {
        List<MovieRental> movieRentals = null;
        Customer customer = new Customer("", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Customer name cannot be empty"
        );
    }

    @Test
    void givenCustomerWithNullRentals_whenStatement_thenIllegalArgumentException() {
        List<MovieRental> movieRentals = null;
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Customer rentals cannot be null"
        );
    }

    @Test
    void givenCustomerWithNoRentals_whenStatement_thenIllegalArgumentException() {
        List<MovieRental> movieRentals = new ArrayList<>();
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Customer rentals cannot be empty"
        );
    }

    @Test
    void givenCustomerWithRentalWithNullId_whenStatement_thenIllegalArgumentException() {
        List<MovieRental> movieRentals = List.of(new MovieRental(null, 3));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Movie id cannot be null"
        );
    }

    @Test
    void givenCustomerWithRentalWithEmptyId_whenStatement_thenIllegalArgumentException() {
        List<MovieRental> movieRentals = List.of(new MovieRental("", 3));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Movie id cannot be empty"
        );
    }

    @Test
    void givenInvalidMovieRental_whenStatement_thenThrowIllegalArgumentException() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F005", 3));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Invalid movie id"
        );
    }

    @Test
    void givenCustomerWithRentalWithZeroDays_whenStatement_thenIllegalArgumentException() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F001", 0));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Days cannot be zero"
        );
    }

    @Test
    void givenCustomerWithRentalWithNegativeDays_whenStatement_thenIllegalArgumentException() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F001", -4));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        assertThrows(
                IllegalArgumentException.class, () -> rentalInformationService.getStatement(customer),
                "Days cannot be negative"
        );
    }

    @Test
    void givenCustomerWithOnlyRegularRentals_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1)
        );
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	You've Got Mail	3.5
                	Matrix	2.0
                Amount owed is 5.5
                You earned 2 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyRegularRentalsWithDaysLessThanTwo_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F002", 1));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Matrix	2.0
                Amount owed is 2.0
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyRegularRentalsWithDaysEqualToTwo_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F002", 2));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Matrix	2.0
                Amount owed is 2.0
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyRegularRentalsWithDaysMoreThanTwo_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F001", 5));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	You've Got Mail	6.5
                Amount owed is 6.5
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyNewRentalsWithDaysLessThanTwo_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F004", 1));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Fast & Furious X	3.0
                Amount owed is 3.0
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyNewRentalsWithDaysEqualToTwo_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F004", 2));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Fast & Furious X	6.0
                Amount owed is 6.0
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyNewRentalsWithDaysMoreThanTwo_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F004", 4));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Fast & Furious X	12.0
                Amount owed is 12.0
                You earned 2 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyChildrenRentalsWithDaysLessThanThree_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F003", 2));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Cars	1.5
                Amount owed is 1.5
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyChildrenRentalsWithDaysEqualToThree_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F003", 3));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Cars	1.5
                Amount owed is 1.5
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }

    @Test
    void givenCustomerWithOnlyChildrenRentalsWithDaysMoreThanThree_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(new MovieRental("F003", 6));
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	Cars	6.0
                Amount owed is 6.0
                You earned 1 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }


    @Test
    void givenCustomerWithManyRentalTypes_whenStatement_thenReturnCorrectStatement() {
        List<MovieRental> movieRentals = List.of(
                new MovieRental("F001", 2),
                new MovieRental("F003", 3),
                new MovieRental("F004", 7)
        );
        Customer customer = new Customer("C. U. Stomer", movieRentals);

        String expected = """
                Rental Record for C. U. Stomer
                	You've Got Mail	2.0
                	Cars	1.5
                	Fast & Furious X	21.0
                Amount owed is 24.5
                You earned 4 frequent points
                """;

        String result = rentalInformationService.getStatement(customer);

        assertEquals(expected, result, "Statement should be as expected");
    }
}
