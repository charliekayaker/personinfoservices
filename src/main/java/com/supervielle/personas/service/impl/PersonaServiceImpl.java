package com.supervielle.personas.service.impl;

import java.util.List;
import java.util.Optional;

/*import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;*/
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supervielle.personas.dtos.EstadisticaDTO;
import com.supervielle.personas.dtos.RelacionDTO;
import com.supervielle.personas.entity.Persona;
import com.supervielle.personas.enums.Relaciones;
import com.supervielle.personas.enums.Sex;
import com.supervielle.personas.exceptions.PersonException;
import com.supervielle.personas.exceptions.PersonExistExeption;
import com.supervielle.personas.repository.PersonaRepository;
import com.supervielle.personas.service.MensajeService;
import com.supervielle.personas.service.PersonaService;
import com.supervielle.personas.util.PersonaConverter;
import com.supervielle.personas.util.PersonaRelacion;

@Service
public class PersonaServiceImpl implements PersonaService {
	
	 @Autowired
	 private PersonaRepository repository;

	 @Autowired
	 private MensajeService messageServices;
	 
	 public List<Persona> retrieveAllPersons() {
		 return repository.findAll();
	 }
	 
	 public Persona retrievePerson(Integer id) throws PersonException {
		 Optional<Persona> existsPersona = repository.findById(id);
		 if(existsPersona.isPresent()) 
			 return existsPersona.get();
		 throw new PersonException(String.format(messageServices.getMessage("personaDoesntExist"),id));
	 }
	  
	 @Transactional
	 public Integer recordPerson(Persona persona) throws PersonException {
		 dataValidate(persona, true);
		 return repository.save(persona).getId();
	 }
	 
	 @Transactional
	 public Persona updatePerson(Persona persona, Integer existsId) throws PersonException {
		 Persona personaDB = retrievePerson(existsId);
		 persona.setId(personaDB.getId());
		 persona.setPadreId(personaDB.getPadreId());
		 dataValidate(persona, false);
		 persona = repository.save(persona);
		 return repository.save(persona);
	 }
	 
	 @Transactional
	 public void deletePersona(Integer id) {
		 
		 Optional<Persona> persona = repository.findById(id);
		 if(persona.isPresent())
			 repository.delete(persona.get());
	 }

	 public EstadisticaDTO dameEstadistica() 
	 {
		 EstadisticaDTO estadistica = new EstadisticaDTO();
		 estadistica.setCantidadMujeres(repository.findBySex(Sex.FEMENINO).size());
		 estadistica.setCantidadHombres(repository.findBySex(Sex.MASCULINO).size());

		 Double cantArg = (double) repository.findByPais(messageServices.getMessage("paisArg")).size();
		 Double cant = (double) retrieveAllPersons().size();
		 
		 if(cant > 0)
			 estadistica.setPorcentajeArgentinos((int) (Math.round(cantArg*100/cant)));
		 else 
			 estadistica.setPorcentajeArgentinos(0);

		 return estadistica;
	 }
	  
	 
	 private void dataValidate(Persona persona, Boolean esNueva) throws PersonException 
	 {
		 Persona personaDB;
		 if(esNueva)
			 personaDB = repository.findByTipoDocumentoAndNumeroDocumentoAndPaisAndSexAndIdNot(persona.getTipoDocumento(),
				 persona.getNumeroDocumento(), persona.getPais(),persona.getSex(), -1); 
		 else
			 personaDB = repository.findByTipoDocumentoAndNumeroDocumentoAndPaisAndSexAndIdNot(persona.getTipoDocumento(),
					 persona.getNumeroDocumento(), persona.getPais(),persona.getSex(), persona.getId());
			 
		 if(personaDB != null)
			 throw new PersonExistExeption(String.format(messageServices.getMessage("personaAlreadyExist"),personaDB.getId()));
		 else if(persona.getMail().isEmpty() && persona.getTelefono().isEmpty())
			 throw new PersonException(messageServices.getMessage("withOutContactInformation"));
	 }

	@Override
	public void relacionar(Integer id, Integer padreId) throws PersonException  {
		
		if(id.equals(padreId))
			throw new PersonException(messageServices.getMessage("incorrectRelation1"));
		else {
			Persona hijo = retrievePerson(id);
			Persona padre = retrievePerson(padreId);
	
			if(padre.getPadreId() != null && padre.getPadreId().equals(hijo.getId()))
				throw new PersonException(messageServices.getMessage("incorrectRelation2"));
			else {
				hijo.setPadreId(padre.getId());
				hijo = repository.save(hijo);
			}
		}
	}

	@Override
	public RelacionDTO dameRelacoion(Integer id1, Integer id2) throws PersonException {
		
		Persona personaUno = retrievePerson(id1);
		Persona personaDos = retrievePerson(id2);
		
		RelacionDTO relacionDTO = new RelacionDTO();
		relacionDTO.setPersonasUno(PersonaConverter.getPersonaDTO(personaUno));
		relacionDTO.setPersonasDos(PersonaConverter.getPersonaDTO(personaDos));
		
		if(personaUno.getPadreId() == null || personaDos.getPadreId() == null) 
			relacionDTO.setTipoRelacion(Relaciones.SIN_RELACION);
		else 
		{
			if(PersonaRelacion.sonHermanos(personaUno, personaDos))
				relacionDTO.setTipoRelacion(Relaciones.HERMANA_HERMANO);
			else {
				Optional<Persona> padreUno = repository.findById(personaUno.getPadreId());
				Optional<Persona> padreDos = repository.findById(personaDos.getPadreId());
				
				if(padreUno.isPresent() && padreDos.isPresent() &&
						PersonaRelacion.sonHermanos(padreUno.get(), padreDos.get()))
					relacionDTO.setTipoRelacion(Relaciones.PRIMO_PRIMA);
				else if (padreUno.isPresent() && 
						PersonaRelacion.sonHermanos(padreUno.get(), personaDos))
					relacionDTO.setTipoRelacion(Relaciones.TIA_TIO);
				else if (padreDos.isPresent() && 
						PersonaRelacion.sonHermanos(padreDos.get(), personaUno))
					relacionDTO.setTipoRelacion(Relaciones.TIA_TIO);
				else
					relacionDTO.setTipoRelacion(Relaciones.SIN_RELACION);
			}
			
		}
		
		return relacionDTO;
	} 
	
}
