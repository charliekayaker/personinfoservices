package com.supervielle.personas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supervielle.personas.entity.Persona;
import com.supervielle.personas.enums.Sex;

public interface PersonaRepository extends JpaRepository<Persona, Integer>   {
	
	public Persona findByTipoDocumentoAndNumeroDocumentoAndPaisAndSexAndIdNot(String tipoDocumento, String numeroDucumento, String pais, Sex sex, Integer id);
	public List<Persona> findBySex(Sex sex);
	public List<Persona> findByPais(String pais);

}
