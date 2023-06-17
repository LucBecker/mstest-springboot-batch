package com.lucbecker.msbatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BatchConfig.class, DatasourceConfig.class, CustomerValidator.class })
@PropertySource("classpath:application.properties")
@AutoConfigureDataJdbc
@ActiveProfiles("test")
@SpringBatchTest
public class StepImportCustomerIT {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DataSource dataSource;

    private JdbcOperations jdbcTemplate;

    @BeforeEach
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    /**
     * Testa o step e verifica o resultado esperado, por exemplo, no banco de dados.
     */
    @Test
    public void test() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("customersFile", "classpath:customersFile.csv").toJobParameters();
        JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("stepImportCustomers", jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        List<Map<String, String>> customers = this.jdbcTemplate.query("select * from customer order by name",
                (rs, rowNum) -> {
                    Map<String, String> item = new HashMap<>();
                    item.put("name", rs.getString("name"));
                    return item;
                });
        Map<String, String> customer1 = customers.get(0);
        Map<String, String> customer2 = customers.get(1);
        Map<String, String> customer3 = customers.get(2);
        Map<String, String> customer4 = customers.get(3);

        assertEquals("Cliente Teste 1", customer1.get("name"));
        assertEquals("Cliente Teste 2", customer2.get("name"));
        assertEquals("Cliente Teste 3", customer3.get("name"));
        assertEquals("Cliente Teste 4", customer4.get("name"));
    }
}
