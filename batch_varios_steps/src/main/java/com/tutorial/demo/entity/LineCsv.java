package com.tutorial.demo.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class LineCsv implements Serializable {

	private static final long serialVersionUID = -8026812621219337432L;
	
	private String nombre;
	private LocalDate fechaNacimiento;
	private Long edad;
	
	public LineCsv(String nombre, LocalDate fechaNacimiento) {
		
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
	}

	public LineCsv() {
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Long getEdad() {
		return edad;
	}

	public void setEdad(Long edad) {
		this.edad = edad;
	}

	@Override
	public String toString() {
		return "LineCsv [nombre=" + nombre + ", fechaNacimiento=" + fechaNacimiento + ", edad=" + edad + "]";
	}

	
	
	
}
