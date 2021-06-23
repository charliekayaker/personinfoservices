package com.supervielle.personas.dtos;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.supervielle.personas.enums.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class PersonaDTO {

	@ApiModelProperty(value = "Identificador único de la persona", accessMode = AccessMode.READ_ONLY, example = "")
	@JsonProperty(value = "id")
	private Integer  id;
	
	@ApiModelProperty(value = "Indica el tipo de documento de la persona.", required = true, example = "DNI")
	@JsonProperty(value = "document_numero")
	@NotEmpty
	private String tipoDocumento;
	
	@ApiModelProperty(value = "Indica el número de documento de la persona (Numero de identificacion)", required = true, example = "12123123")
	@JsonProperty(value = "document_number")
	@NotEmpty
    private String numeroDocumento;
	
	@ApiModelProperty(value = "Indica el país de nacimiento de la persona", required = true, example = "Argentina")
	@JsonProperty(value = "pais")
	@NotEmpty
    private String pais;
	
	@ApiModelProperty(value = "Indica el nombre de la persona. ", required = true, example = "Alejandro")
	@JsonProperty(value = "nombre")
	@NotEmpty
    private String nombre;
	
	@ApiModelProperty(value = "Identifica el apellido de la persona", required = true, example = "Garcia")
	@JsonProperty(value = "apellido")
	@NotEmpty
    private String apellido;
	
	@ApiModelProperty(value = "Indica el sexo de la persona.", required = true, example = "MASCULINO", allowableValues="MASCULINO, FEMENINO")
	@JsonProperty(value = "sex")
	@NotNull
	private Sex sex;
	
	@ApiModelProperty(value = "Indica la edad de la persona. Debe de ser mayor a 18.", required = true, example = "25")
	@JsonProperty(value = "edad")
	@NotNull
	@Min(18)
    private Integer edad;
	
	@ApiModelProperty(value = "Indica la forma de contacto de la persona, por telefono y/o mail", required = true)
	@JsonProperty(value = "dato_contacto")
	@NotNull
	@Valid
	private ContactoDTO contactoDTO;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public ContactoDTO getContactoDTO() {
		return contactoDTO;
	}

	public void setContactoDTO(ContactoDTO contactData) {
		this.contactoDTO = contactData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + ((contactoDTO == null) ? 0 : contactoDTO.hashCode());
		result = prime * result + ((edad == null) ? 0 : edad.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((numeroDocumento == null) ? 0 : numeroDocumento.hashCode());
		result = prime * result + ((pais == null) ? 0 : pais.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		PersonaDTO other = (PersonaDTO) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (id.equals(other.id))
			return true;
		else
		{
			if (apellido == null) {
				if (other.apellido != null)
					return false;
			} else if (!apellido.equals(other.apellido))
				return false;
			if (contactoDTO == null) {
				if (other.contactoDTO != null)
					return false;
			} else if (!contactoDTO.equals(other.contactoDTO))
				return false;
			if (edad == null) {
				if (other.edad != null)
					return false;
			} else if (!edad.equals(other.edad))
				return false;
			if (nombre == null) {
				if (other.nombre != null)
					return false;
			} else if (!nombre.equals(other.nombre))
				return false;
			if (numeroDocumento == null) {
				if (other.numeroDocumento != null)
					return false;
			} else if (!numeroDocumento.equals(other.numeroDocumento))
				return false;
			if (pais == null) {
				if (other.pais != null)
					return false;
			} else if (!pais.equals(other.pais))
				return false;
			if (sex!= other.sex)
				return false;
			if (tipoDocumento == null) {
				if (other.tipoDocumento != null)
					return false;
			} else if (!tipoDocumento.equals(other.tipoDocumento))
				return false;
		}
		return true;
	}


	

	
	
	
}
