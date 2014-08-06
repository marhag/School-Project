/*
 * Klassen inneholder:
 * - id, korttype, pris
 * - fornavn, etternavn, fødselsdato
 * - metoder for endring og henting av datafeltene
 * - metode som skriver ut informasjon
 * 
 * Sist endret: 04/05/13
 */


import java.util.Date;

/* Klassen representerer et skikort i handlelista
   før en transaksjon blir gjennomført og registrert */
public class HandleObjekt 
{
	private int id;
	private Korttype kort;
	private String fornavn, etternavn;
	private Date fodselsdato;
	private double pris;
	
	/* Definerer et handleobjekt, samt id, korttype og pris.
	   Dersom handleobjekt representerer et sesongkort lagres 
	   også fornavn, etternavn og fødselsdato. Hvis ikke blir 
	   disse parametrene lagret som "" og null */
	public HandleObjekt(int id, Korttype kort, String fornavn, String etternavn, Date fodselsdato,double pris)
	{
		this.id = id;
		this.kort = kort;
		this.fornavn = fornavn;
		this.etternavn = etternavn;
		this.fodselsdato = fodselsdato;
		this.pris = pris;
	}
	
	// Henter pris
	public double getPris()
	{
		return pris;
	}
	
	// Henter id
	public int getId()
	{
		return id;
	}
	
	// Henter kort-typen
	public Korttype getKorttype()
	{
		return kort;
	}
	
	// Henter fornavn. Returnerer "" om korttype ikke er Sesongkort
	public String getFornavn()
	{
		return fornavn;
	}
	
	// Henter etternavn. Returnerer "" om korttype ikke er Sesongkort
	public String getEtternavn()
	{
		return etternavn;
	}
	
	// Henter fødselsdato. Returnerer null om korttype ikke er Sesongkort
	public Date getFodselsdato()
	{
		return fodselsdato;
	}
	
	// Metode for å endre kort-typen
	public void endreKorttype(Korttype nyttkort)
	{
		kort = nyttkort;
	}
	
	// Skriver ut informasjon om selve objektet
	public String toString()
	{
		if(kort == null)
			return "ID: " + id;
		
		return "ID: " + id + " - " + kort.getClass().getName();
	}
}
