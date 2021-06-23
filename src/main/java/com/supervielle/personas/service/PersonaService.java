package com.supervielle.personas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.supervielle.personas.dtos.EstadisticaDTO;
import com.supervielle.personas.dtos.RelacionDTO;
import com.supervielle.personas.entity.Persona;
import com.supervielle.personas.exceptions.PersonException;

@Service
public interface PersonaService {

	public List<Persona> retrieveAllPersons();
	public Persona retrievePerson(Integer id) throws PersonException;
	public Integer recordPerson(Persona persona) throws PersonException;	 
	public Persona updatePerson(Persona persona, Integer existsId) throws PersonException;
	public void deletePersona(Integer id);
	
	public EstadisticaDTO dameEstadistica();
	public void relacionar(Integer id, Integer padreId) throws PersonException;
	public RelacionDTO dameRelacoion(Integer id1, Integer id2) throws PersonException;
}
