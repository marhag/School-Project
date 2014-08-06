/* 
 * Klassen innholder:
 *  - LinkedList av Keycard
 *  - Metoder for innsetting, sjekking og fjerning av keycard
 *  - Get-metoder for keycards og siste id i registeret
 *  - Metoder for å skrive ut informasjon
 * 
 * Lagd av Sondre
 * 
 * Sist endret: 29/04/13
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/* Klassen er et register for alle solgte keycard.
   Keycards som ikke er i denne listen, kan ikke brukes. */
public class KeycardListe implements Serializable
{	
	private static final long serialVersionUID = 10030L;
	
	private List<Keycard> keycards = new LinkedList<>();
	
	/* Legger til et keycard i listen. Innsetting 
	   skjer kun når et keycard blir opprettet. 
	   Lista er derfor alltid sortert. */
	public void leggTil(Keycard kcard)
	{
		keycards.add(kcard);
	}
	
	// Sjekker om et keycard fins i lista
	public boolean keycardFins(int it)
	{
		for(Keycard k : keycards)
		{
			if(k.getId() == it)
				return true;
		}
		
		return false;
	}
	
	// Fjerner keycard med innkommende id
	public void fjern(int id)
	{
		Keycard kc = getKeycard(id);
		keycards.remove(keycards.indexOf(kc));
	}
	
	// Henter keycard med innkommende id
	public Keycard getKeycard(int id)
	{
		for(Keycard k : keycards)
		{
			if(k.getId() == id)
				return k;
		}
		
		return null;
	}
	
	// Returnerer siste/høyeste id i lista
	public int getLastId()
	{
		if(keycards.isEmpty())
			return -1;
		
		int index = keycards.size() - 1;
		return keycards.get(index).getId();
	}
	
	// Skriver ut informasjon om registrerte keycard
	public String toString()
	{
		String ut = "";
		for(Keycard k : keycards)
			ut += k.toString() + "\n";
		if(ut.equals(""))
			ut = "Ingen registrerte keycards";
		
		return ut;
	}
	
	// Sender ut en String-array med keycards
	public String[] toStringArray()
	{
		if(keycards.size() == 0)
			return null;
		
		String ut[] = new String[keycards.size()];
		int i = 0;
		for(Keycard k : keycards)
		{
			ut[i] = k.toString();
			i++;
		}

		return ut;
	}
}
