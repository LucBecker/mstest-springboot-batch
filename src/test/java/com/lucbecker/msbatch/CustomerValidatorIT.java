package com.lucbecker.msbatch;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@Sql(scripts="classpath:script1.sql")
@JdbcTest
public class CustomerValidatorIT {

    @Autowired
    private DataSource dataSource;
    private CustomerValidator customerValidator;

    @BeforeEach
    public void setUp() {
        this.customerValidator = new CustomerValidator(dataSource);
    }

    @Test
    public void tesCustomerExistente() {
        Customer customer = new Customer();
        customer.setName("joao");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> this.customerValidator.validate(customer));
        assertEquals("Customer joao já existe!", exception.getMessage());
    }

    @Test
    public void tesCustomerValido() {
        Customer customer = new Customer();
        customer.setName("maria");
        assertDoesNotThrow(() -> this.customerValidator.validate(customer));
    }
}
