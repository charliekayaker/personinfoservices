package com.supervielle.personas.controller;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supervielle.personas.dtos.EstadisticaDTO;
import com.supervielle.personas.dtos.PersonaDTO;
import com.supervielle.personas.dtos.RelacionDTO;
import com.supervielle.personas.entity.Persona;
import com.supervielle.personas.enums.Relaciones;
import com.supervielle.personas.exceptions.PersonException;
import com.supervielle.personas.logger.LogServ;
import com.supervielle.personas.logger.LoggerFactory;
import com.supervielle.personas.service.PersonaService;
import com.supervielle.personas.util.PersonaConverter;
import com.supervielle.personas.util.ReflectionHelper;
import com.supervielle.personas.util.Utils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@Singleton
@RequestMapping("api")
public class PersonaController {

	private Logger logger; // En ambientes productivos loguearíamos en un archivo no en la consola. 

	
	@Autowired
	private PersonaService personaService;

	public PersonaController() {
		logger = LoggerFactory.getLogger(PersonaController.class);
	}

	@ApiOperation(value = "Retrieve all persons.", response = List.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping("/personas")
	public ResponseEntity<List<PersonaDTO>> retrieveAllPersons(HttpServletRequest request) { // podríamos loguear con interceptors tambien.
		
		long mediationTime = System.currentTimeMillis(); 																				
		
		LogServ.getInstance().LogGRequest("/api/personas " + "retrieveAllPersons ", logger); // <- esto se puede sacar del request pero por temas de tiempo vamos a hacerlo así.
											             
		List<Persona> listaPersonas = personaService.retrieveAllPersons();

		List<PersonaDTO> listaPersonasDTO = new ArrayList<>();

		listaPersonas.forEach(persona -> listaPersonasDTO.add(PersonaConverter.getPersonaDTO(persona)));
				
		LogServ.getInstance().serviceLogResponse(listaPersonasDTO, System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(listaPersonasDTO);
	}

	@ApiOperation(value = "Retrieve person by id.", response = PersonaDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/persona/{id}")
	public ResponseEntity<PersonaDTO> getById(HttpServletRequest request, @PathVariable Integer id)
			throws PersonException {
		
		long mediationTime = System.currentTimeMillis(); 
		
		LogServ.getInstance().LogGRequest("/persona/"+id, logger);
		
		try {
			Utils.getRequestData(request);
		} catch (IOException e) {
			logger.error(e);
		}
		Persona persona = personaService.retrievePerson(id);
		 
		PersonaDTO personasDTO = PersonaConverter.getPersonaDTO(persona);
		
		LogServ.getInstance().serviceLogResponse(personasDTO, System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(personasDTO);
	}

	@ApiOperation(value = "Save a new person.", response = PersonaDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Resource created OK"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PostMapping(value = "/persona")
	public ResponseEntity<Integer> SavePersona(HttpServletRequest request, @RequestBody @Valid PersonaDTO personaDTO)
			throws PersonException {
		
		long mediationTime = System.currentTimeMillis(); 
		
		//LogServ.getInstance().LogPRequest(request, logger);
		
		Persona persona = PersonaConverter.getPersona(personaDTO);		
		Integer personaId = personaService.recordPerson(persona);
		
		LogServ.getInstance().serviceLogResponse(personaId,  System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(personaId);
	}

	@ApiOperation(value = "Update data person.", response = PersonaDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PutMapping(value = "/persona/{existsId}")
	public ResponseEntity<PersonaDTO> UpdatePersona(HttpServletRequest request,
			@RequestBody @Valid PersonaDTO personaDTO, @PathVariable Integer existsId) throws PersonException {
		
		long mediationTime = System.currentTimeMillis(); 
		
		LogServ.getInstance().LogPRequest(request, logger);
		
		Persona persona = personaService.updatePerson(PersonaConverter.getPersona(personaDTO), existsId);
		
		LogServ.getInstance().serviceLogResponse(persona,  System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(PersonaConverter.getPersonaDTO(persona));
	}

	@ApiOperation(value = "Delete a person.", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@DeleteMapping(value = "/persona/{existsId}")
	public void deletePersona(HttpServletRequest request, @PathVariable Integer existsId) {
		
		long mediationTime = System.currentTimeMillis();
		
		LogServ.getInstance().LogGRequest("/persona/{existsId}", logger);
		
		personaService.deletePersona(existsId);
		
		LogServ.getInstance().serviceLogResponse(null,  System.currentTimeMillis() - mediationTime, logger);

	}

	@ApiOperation(value = "Statistics about persons in the system", response = EstadisticaDTO.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/estadisticas")
	public ResponseEntity<EstadisticaDTO> dameEstadistica(HttpServletRequest request) {
		
		long mediationTime = System.currentTimeMillis();
		
		LogServ.getInstance().LogGRequest("/estadisticas/", logger);
		
		EstadisticaDTO estadisticaDTO = personaService.dameEstadistica();
		
		LogServ.getInstance().serviceLogResponse(estadisticaDTO,  System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(estadisticaDTO);

	}

	@ApiOperation(value = "Save the relation between persons.", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Resource created OK"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PostMapping(value = "/persona/{id}/padre/{idPadre}")
	public void relacionar(HttpServletRequest request, @PathVariable Integer id, @PathVariable Integer idPadre)
			throws PersonException {
		
		long mediationTime = System.currentTimeMillis();
		
		LogServ.getInstance().LogGRequest("/persona/{id}/padre/{idPadre}", logger);
		
		personaService.relacionar(id, idPadre);
		
		LogServ.getInstance().serviceLogResponse(null,  System.currentTimeMillis() - mediationTime, logger);		
		
	}

	@ApiOperation(value = "Retrieve relations between persons.", response = Relaciones.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/relaciones/{id1}/{id2}")
	public ResponseEntity<RelacionDTO> getRelationships(HttpServletRequest request, @PathVariable Integer id1,
			@PathVariable Integer id2) throws PersonException {
		
		long mediationTime = System.currentTimeMillis();
		
		LogServ.getInstance().LogGRequest("/relaciones/{id1}/{id2}", logger);
		
		RelacionDTO relacion = personaService.dameRelacoion(id1, id2);		
		
		LogServ.getInstance().serviceLogResponse(relacion,  System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(relacion);
	}

	@ApiOperation(value = "Retrieve a person as a text", response = String.class, produces = "pain/text")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success Operation"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/persona_as_string/{id}")
	public ResponseEntity<String> getPersonaAsString(HttpServletRequest request, @PathVariable Integer id)
			throws PersonException {
		
		long mediationTime = System.currentTimeMillis();
		
		LogServ.getInstance().LogGRequest("/persona_as_string/{id}", logger);
		
		Persona persona = personaService.retrievePerson(id);
		
		String result = ReflectionHelper.getObjectAsPlainText(persona);
		
		LogServ.getInstance().serviceLogResponse(result,  System.currentTimeMillis() - mediationTime, logger);
		
		return ResponseEntity.ok(result);
	}



}
