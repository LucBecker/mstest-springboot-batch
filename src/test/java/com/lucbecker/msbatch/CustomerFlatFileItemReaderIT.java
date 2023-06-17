package com.lucbecker.msbatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BatchConfig.class, DatasourceConfig.class, CustomerValidator.class})
@PropertySource("classpath:application.properties")
@AutoConfigureDataJdbc
@ActiveProfiles("test")
@SpringBatchTest
public class CustomerFlatFileItemReaderIT {

    @Autowired
    private FlatFileItemReader<Object> flatFileItemReader;

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("customersFile", "classpath:customersFile.csv").toJobParameters();
        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void testTypeConversion() throws Exception {
        this.flatFileItemReader.open(new ExecutionContext());
        assertTrue(this.flatFileItemReader.read() instanceof Customer);
        assertTrue(this.flatFileItemReader.read() instanceof Account);
    }
}
