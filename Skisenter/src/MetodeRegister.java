/*
 * Klassen inneholder:
 *  - database
 *  - Metode for å set'e database
 *  - Metoder for som skriver ut informasjon
 * 
 * Lagd av Sondre
 * 
 * Sist endret: 02/05/13
 */

// Klassen henter riktig informasjon til riktig panel
public class MetodeRegister 
{
	private Database database;
	
	// Oppretter klassen og setter database til inn-parameteren
	public MetodeRegister(Database reg)
	{
		database = reg;
	}
	
	// Redefinerer database, med parameteren
	public void setRegister(Database reg)
	{
		database = reg;
	}
	
	// Skriver ut informasjon ut ifra hva type Object parameteren er
	public String toStringInfo(Object type)
	{
		if(type instanceof AktiveKort)
			return database.getAktive().toString();
		else if(type instanceof KeycardListe)
			return database.getKeycard().toString();
		else if(type instanceof KundeListe)
			return database.getKunde().toString();
		else
			return "Feil oppsto!";
	}

	/* Skriver ut informasjon i en String-array 
	   ut ifra hva type Obejt parameteren er */
	public String[] toStringInfoArray(Object type) 
	{
		if(type instanceof AktiveKort)
			return database.getAktive().toStringArray();
		else if(type instanceof KeycardListe)
			return database.getKeycard().toStringArray();

		return null;
	}
}
