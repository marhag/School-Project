/*
 * 
 * Klassen innholder: 
 *  - Metoder for innsetting, endring og sletting av kunder
 *  - metode for å se om eier har aktiv/registrert kort på seg
 * 
 * Lagd av Sondre
 * 
 * Sist endret: 30/04/13
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;

// Klassen har alle kunder i et register
public class KundeListe implements Serializable
{
	private static final long serialVersionUID = 10040L;
	
	private List<Kunde> kunder = new LinkedList<>();
	
	/* Setter inn en kunde i registeret, med parametre for:
	   fornavn, etternavn, fødselsdato og keycard */
	public void settInnKunde(String fn, String en, Date fodselsdato, Keycard keycard)
	{
		String[] fnavn = splitString(fn);
		String[] enavn = splitString(en);
		
		String fornavn = "";
		String etternavn = "";
		
		// Fjerner whitespace om brukeren trykte space
		for(int i = 0; i < fnavn.length; i++) 
		{
			fnavn[i] = fnavn[i].replaceAll("\\s", "");
			if(i == 0)
				fornavn += fnavn[i];
			else
				fornavn += " " + fnavn[i];
		}
		
		for(int i = 0; i < enavn.length; i++)
		{
			enavn[i] = enavn[i].replaceAll("\\s", "");
			if(i == 0)
				etternavn += enavn[i];
			else
				etternavn += " " + enavn[i];
		}
		
		Kunde ny = new Kunde(fornavn, etternavn, fodselsdato, keycard);
		
		for(Kunde k : kunder) // Sørger for at ny kunde blir satt inn sortert
		{	
			if(k.getKeycard().getId() > ny.getKeycard().getId())
			{
				kunder.add(kunder.indexOf(k), ny);
				return;
			}
		}
		kunder.add(ny);
	}
	
	// Setter inn et helt kunde-objekt i registeret
	public void settInnKunde(Kunde k)
	{
		kunder.add(k);
	}
	
	// Splitter inn-parameteren, og returnerer den som String[]
	public String[] splitString(String inn)
	{
		String ut[] = null;
		try 
		{
		    ut = inn.split("\\s+");
		} catch (PatternSyntaxException ex) 
		{
		    JOptionPane.showMessageDialog(null, "Feil med splitting av navn");
		}
		
		return ut;
	}
	
	// Splitter inn-parameteren, gjør den til Lowercase og returnerer den som String[]
	public String[] splitLowerCaseString(String inn)
	{
		String ut[] = null;
		try 
		{
		    ut = inn.toLowerCase().split("\\s+");
		} catch (PatternSyntaxException ex) 
		{
		    JOptionPane.showMessageDialog(null, "Feil med splitting av navn");
		}
		
		return ut;
	}
	
	/* Søker etter kunde på navn fra inn-parameteren, 
	   og returnerer alle søketreff i en Kunde-array */
	public Kunde[] finnKunderNavn(String inn)
	{
		String[] innnavn = splitLowerCaseString(inn);
		
		if(innnavn.length == 0)
			return null;
		
		boolean ok = true;
		
		// Sjekker om input har doble navn, fks "Ole Ole", isåfall er ok = false
		for(int i = 0; i < innnavn.length; i++)
		{
			for(int j = 0; j < innnavn.length; j++)
			{
				if(i != j && innnavn[i].equals(innnavn[j]))
					ok = false;;
			}
		}
		
		// Alle søketreffene legges inn i denne lista
		ArrayList<Kunde> fulltnavn = new ArrayList<Kunde>();
		
		if(ok)
		{
			// Går gjennom alle kunder
			for(Kunde k : kunder)
			{
				ok = true;
				String[] kundenavn = splitLowerCaseString(k.toString());
				// Går gjennom hver enkelt navn fra søke-feltet
				for(int i = 0; i < innnavn.length; i++)
				{
					ok = false;
					/* True dersom navn stemmer, hvis ikke avbrytes 
					   loopen og begynner på neste kunde */
					for(int j = 0; j < kundenavn.length; j++)
					{
						if(innnavn[i].equals(kundenavn[j]))
							ok = true;
					}
					if(!ok)
						break;
				}
				if(ok)
					fulltnavn.add(k);
			}
		}
		
		Kunde[] ut = new Kunde[fulltnavn.size()];
		ut = fulltnavn.toArray(ut);
		
		return ut;
	}
	
	// Finner og returnerer en eier med id'en
	public Kunde finnEierMedId(int id)
	{
		for(Kunde k : kunder)
		{
			if(k.getKeycard().getId() == id)
				return k;
		}
		return null;
	}
	
	// Parser inn-parameteren til int, og finner og returnerer så kunde
	public Kunde finnEierMedIdString(String text)
	{
		int id;
		try
		{
			id = Integer.parseInt(text);
		}
		catch(NumberFormatException nfe)
		{
			return null;
		}
				
		return finnEierMedId(id);
	}
	
	/* Endrer en kunde. Sletter gammel kunde, og setter inn
	   et nytt objekt på samme index. Parametre: gammel er det 
	   gamle kunde-objektet, fornavn, etternavn og fødselsdato */
	public void endreEier(Kunde gammel, String fn, String en, Date fdato)
	{
		String[] fnavn = splitString(fn);
		String[] enavn = splitString(en);
		
		String fornavn = "";
		String etternavn = "";
		
		// Sørger for at fornavn bare har et space mellom seg
		for(int i = 0; i < fnavn.length; i++) 
		{
			fnavn[i] = fnavn[i].replaceAll("\\s", "");
			if(i == 0)
				fornavn += fnavn[i];
			else
				fornavn += " " + fnavn[i];
		}
		
		// Sørger for at etternavn bare har et space mellom seg
		for(int i = 0; i < enavn.length; i++)
		{
			enavn[i] = enavn[i].replaceAll("\\s", "");
			if(i == 0)
				etternavn += enavn[i];
			else
				etternavn += " " + enavn[i];
		}
		
		int i = kunder.lastIndexOf(gammel);
		kunder.remove(i);
		kunder.add(i, new Kunde(fornavn, etternavn, fdato, gammel.getKeycard()));
	}
	
	// Fjerner kunde som ikke har gyldige skikort
	public void fjernUgyldige(AktiveKort aktiv) 
	{
		for(Kunde k : kunder)
		{
			if(!harGyldig(k, aktiv))
			{
				kunder.remove(k);
				fjernUgyldige(aktiv);
				return;
			}
		}
	}
	
	// sjekker om en kunde k, har aktivt skikort
	public boolean harGyldig(Kunde k, AktiveKort aktiv)
	{
		if(aktiv.sjekkGyldig(k.getKeycard().getId()))
			return true;
		
		return false;
	}
	
	// Sjekker om innkommende kunde eksisterer
	public boolean finsKunde(Kunde kunde)
	{
		for(Kunde k : kunder)
		{
			if(k.getKeycard().getId() == kunde.getKeycard().getId())
				return true;
		}
		return false;
	}
	
	// Henter kunde fra innkommende index
	public Kunde getKundeFromIndex(int index) 
	{
		if(index >= 0 && index < kunder.size())
			return kunder.get(index);
		return null;
	}
	
	// Henter lengden på kunderegisteret
	public int getSize()
	{
		return kunder.size();
	}
	
	// Henter selve kundelista
	public List<Kunde> getKunder()
	{
		return kunder;
	}
	
	// Henter informasjon av kundene
	public String toString()
	{
		String ut = "";
		for(Kunde k : kunder)
			ut += k.toString() + "\n";
		return ut;
	}
	
	// henter informasjon av kundene i en array
	public String[] toStringArray()
	{
		String[] ut = new String[kunder.size()];
		int i = 0;
		for(Kunde k : kunder)
		{
			ut[i] = k.toString();
			i++;
		}
		return ut;
	}
}
