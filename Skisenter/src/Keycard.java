/*
 * Klassen inneholder:
 *  - Id
 *  - get-metode for id
 *  - metode for å skrive ut informasjon
 * 
 * Lagd av Sondre
 * 
 * Sist endret: 24/04/13
 */

import java.io.Serializable;

/* Klassen representerer et Keycard. Et enkelt objekt
 * representerer det fysiske kortet som brukes i skiheisen */
public class Keycard implements Serializable
{
	private static final long serialVersionUID = 10035L;
	
	int id;
	
	// Oppretter keycard, og setter id'en
	public Keycard(int id)
	{
		this.id = id;
	}

	// Henter id
	public int getId()
	{
		return id;
	}
	
	// Skriver ut id'en i en String
	public String toString()
	{
		return "Keycard id: " + id;
	}
}