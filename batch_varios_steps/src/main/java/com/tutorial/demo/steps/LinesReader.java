package com.tutorial.demo.steps;

import java.util.ArrayList;
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

public class LinesReader implements Tasklet, StepExecutionListener {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private List<LineCsv> lineasCsv;
	private FileUtils fileUtils;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		lineasCsv = new ArrayList<>();
		fileUtils = new FileUtils("personas_fecha.csv");
		log.info("LinesReader inicializado!");
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LineCsv lineaCsv= fileUtils.readLine();
		while(lineaCsv != null) {
			lineasCsv.add(lineaCsv);
			log.debug("Linea leida: " + lineaCsv.toString());
			lineaCsv = fileUtils.readLine();
		}
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		fileUtils.closeReader();
		
		stepExecution
		.getJobExecution()
		.getExecutionContext()
		.put("lineasCsv", lineasCsv);
		
		log.debug("Step Reader terminado!");
		
		return ExitStatus.COMPLETED;
	}

	

}
