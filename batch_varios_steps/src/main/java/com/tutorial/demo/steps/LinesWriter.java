package com.tutorial.demo.steps;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.tutorial.demo.entity.LineCsv;
import com.tutorial.demo.util.FileUtils;

public class LinesWriter implements Tasklet, StepExecutionListener {
	
	private final Logger log = LoggerFactory.getLogger(LinesWriter.class);
	private List<LineCsv> lineasCsv;
	private FileUtils fileUtils;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		lineasCsv = (List<LineCsv>) stepExecution.getJobExecution().getExecutionContext().get("lineasCsv");
		fileUtils = new FileUtils("personas_edad.csv");
		log.debug("LinesWriter inicializado!");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		for(LineCsv lineaCsv : lineasCsv) {
			log.debug(lineaCsv.toString());
			fileUtils.writeLine(lineaCsv);
		}
		
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		fileUtils.closeWriter();
		log.debug("Step writer finalizado!");
		return ExitStatus.COMPLETED;
	}
	
	

}
