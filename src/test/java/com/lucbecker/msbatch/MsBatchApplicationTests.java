package com.lucbecker.msbatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BatchConfig.class, DatasourceConfig.class, CustomerValidator.class })
@PropertySource("classpath:application.properties")
@AutoConfigureDataJdbc
@ActiveProfiles("test")
@SpringBatchTest
class MsBatchApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;


	@Test
	public void test() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("customersFile", "file:src/test/resources/customersFile.csv").toJobParameters();
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
		assertEquals(BatchStatus.COMPLETED, stepExecution.getStatus());
		assertEquals(4, stepExecution.getReadCount());
		assertEquals(4, stepExecution.getWriteCount());
	}

}
