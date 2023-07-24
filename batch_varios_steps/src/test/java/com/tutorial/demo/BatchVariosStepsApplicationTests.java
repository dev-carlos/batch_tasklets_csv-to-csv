package com.tutorial.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = TaskletsConfig.class)
class BatchVariosStepsApplicationTests {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void givenTaskletJob_whenJobEnds_thenStatusCompleted() throws Exception {
		JobExecution execution = jobLauncherTestUtils.launchJob();
		assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}

}
