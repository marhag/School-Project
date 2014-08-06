/*
 * Klassen inneholder:
 *  - fornavn, etternavn, fødselsdato og keycard
 *  - get-metoder for øvrige datafelt
 *  - metode som skriver ut navn
 * 
 * Lagd av Martin og sondre
 * 
 * Sist endre: 26/04/13
 */

import java.io.Serializable;
import java.util.Date;

/* Klassen representerer en kunde. I tillegg til å
   ha personlig informasjon har kunde også et keycard. 
   Kunden ligger registrert i kunderegisteret */
public class Kunde implements Serializable
{
	private static final long serialVersionUID = 10045L;
	
	private String fornavn, etternavn;
	private Date fodselsdato;
	private Keycard kort;
	
	/* Oppretter kunde. Datafelt registreres med parametrene:
	   fornavn, etternavn, fødselsdato og keycard */	
	public Kunde(String fnavn, String enavn, Date fdato, Keycard k)
	{
		fornavn = fnavn;
		etternavn = enavn;
		fodselsdato = fdato;
		kort = k;
	}
	
	// Henter fornavn
	public String getFornavn()
	{
		return fornavn;
	}
	
	// Henter etternavn
	public String getEtternavn()
	{
		return etternavn;
	}
	
	// Henter keycard
	public Keycard getKeycard()
	{
		return kort;
	}
	
	// Henter fødselsdato
	public Date getDato()
	{
		return fodselsdato;
	}
	
	// Skriver ut navn på kunde
	public String toString()
	{
		return fornavn + " " + etternavn;
	}
}
