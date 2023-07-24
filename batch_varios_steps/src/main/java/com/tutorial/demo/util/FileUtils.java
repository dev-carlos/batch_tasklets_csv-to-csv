package com.tutorial.demo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.tutorial.demo.entity.LineCsv;



public class FileUtils {
	
	private final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	private CSVReader csvReader;
	private CSVWriter csvWriter;
	private String fileName;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private File file; 
	
	public FileUtils(String fileName) {
		this.fileName = fileName;
	}

	public LineCsv readLine() throws CsvValidationException, IOException {
		if(csvReader == null) {
			initReader();
		}
		String[] line = csvReader.readNext();
		if(line == null) {
			return null;
		}
		return new LineCsv(line[0], LocalDate.parse(line[1], DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}

	private void initReader() throws FileNotFoundException {
		
		ClassLoader loader = this.getClass().getClassLoader();
			
		if(file == null) {
			file = ResourceUtils.getFile("src/main/resources/"+ fileName);
			
		}
		
		if(fileReader == null) {
			fileReader = new FileReader(file);
		}
		if(csvReader == null) {
			csvReader = new CSVReader(fileReader);
		}
	}
	
	public void writeLine(LineCsv lineCsv) {
		try {
			if(csvWriter == null) {
				initWriter();
			}
			String[] lineStr = new String[2];
			lineStr[0] = lineCsv.getNombre();
			lineStr[1] = lineCsv.getEdad().toString();
			csvWriter.writeNext(lineStr);
			
		} catch (Exception e) {
			logger.error("Error escribiendo linea en archivo", e);
		}
	}

	private void initWriter() throws IOException {
		
		if(file == null) {
			file = new File("src/main/resources/"+ fileName);
			file.createNewFile();
			
		}
		if(fileWriter == null) {
			fileWriter = new FileWriter(file);
		}
		if(csvWriter == null) {
			csvWriter = new CSVWriter(fileWriter);
		}	
	}
	
	public void closeWriter() {
		try {
			csvWriter.close();
			fileWriter.close();			
			
		} catch (Exception e) {
			logger.error("Error cerrando writer...", e);
		}
	}
	
	public void closeReader() {
		try {
			csvReader.close();
			fileReader.close();			
			
		} catch (Exception e) {
			logger.error("Error cerrando reader...", e);
		}
	}

}
