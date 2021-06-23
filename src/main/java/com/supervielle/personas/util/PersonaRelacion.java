package com.supervielle.personas.util;

import com.supervielle.personas.entity.Persona;

public class PersonaRelacion 
{
	
	public static boolean sonHermanos(Persona personaUno, Persona personaDos)
	{
		if(personaUno.getPadreId() != null && personaDos.getPadreId() != null) 
			return personaUno.getPadreId().equals(personaDos.getPadreId());
		else
			return false;

	}
	
}
