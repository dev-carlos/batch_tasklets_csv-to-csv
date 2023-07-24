package com.tutorial.demo.steps;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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

public class LinesProcessor implements Tasklet, StepExecutionListener {
	
	public final Logger log = LoggerFactory.getLogger(LinesProcessor.class);
	
	private List<LineCsv> lineasCsv;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		lineasCsv = (List<LineCsv>) stepExecution.getJobExecution().getExecutionContext().get("lineasCsv");
		log.debug("Lineas CSV processor inicializado!");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		for(LineCsv lineaCsv : lineasCsv) {
			long edad = ChronoUnit.YEARS.between(lineaCsv.getFechaNacimiento(), LocalDate.now());
			log.debug("Calculada edad {} para linea {}", edad, lineaCsv);
			lineaCsv.setEdad(edad);
		}
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.debug("Processor terminado!");
		return ExitStatus.COMPLETED;
	}
	
	

}
