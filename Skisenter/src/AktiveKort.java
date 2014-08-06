/*
 * Klassen innholder:
 *  - liste over aktive/gyldige kort
 *  - objekt for statistikk
 *  - metoder som sjekker om diverse ski-kort er gyldige
 *  - metoder som fjerner ugyldige skikort
 *  - metode som fjerner enkelt-klipp fra klippekort
 *  - metoder som skriver ut informasjon om skikort
 * 
 * Skrevet av Sondre og Martin
 */

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/* Klassen holder styr på alle aktive/gyldige 
   skikort og oppdaterer heistur statistikk*/
public class AktiveKort implements Serializable
{
	private static final long serialVersionUID = 10020L;
	
	private List<Korttype> kort = new LinkedList<>();
	private AdminStat stat;
		
	public AktiveKort(AdminStat statistikk)
	{
		stat = statistikk;
	}
	
	// legger inn kort, av superklassen "Korttype", sortert i lista
	public void leggTil(Korttype kcard)
	{
		for(Korttype k : kort)
		{
			if(k.getId() > kcard.getId())
			{
				kort.add(kort.indexOf(k), kcard);
				return;
			}
		}
		kort.add(kcard);
	}
	
	// fjerner innkommende element fra lista
	public void fjern(Korttype kc)
	{
		kort.remove(kc);
	}
	
	// fjerner element med innkommende id fra lista
	public void fjern(int id) 
	{
		for(Korttype k : kort)
		{
			if(k.getId() == id)
			{
				kort.remove(k);
				return;
			}
		}
	}	
	
	// Sjekker om det fins et aktivt/gyldig kort med
	// den innkommende id'en
	public boolean keycardFins(int id)
	{
		for(Korttype k : kort)
		{
			if(k.getId() == id)
				return true;
		}
		return false;
	}
	
	// henter korttype med gitt id
	public Korttype getKorttype(int id)
	{
		for(Korttype k : kort)
		{
			if(k.getId() == id)
				return k;
		}
		
		return null;
	}
	
	// fjerner alle kort som ikke lenger er gyldige
	public void oppdater()
	{
		try
		{
			for(Korttype k : kort)
			{
				if(k instanceof Klippekort)
				{
					if(sjekkKlipp(k) != null)
						fjern(k);
				}
				else if(k instanceof Dagskort)
				{
					if(sjekkDag(k) != null)
						fjern(k);
				}
				
				else if(k instanceof Sesongkort)
				{
					if(sjekkSes(k) != null)
						fjern(k);
				}
			}
		}
	    catch (ConcurrentModificationException concEx)
	    {
	      oppdater();
	    }
	}

	// sjekker om klippekort er gyldig
	private Korttype sjekkKlipp(Korttype kt) 
	{
		Klippekort ny = (Klippekort) kt;
		if(ny.getKlipp()==0)
			return kt;
		else return null;
		
	}

	// sjekker om dagskort er gyldig
	private Korttype sjekkDag(Korttype kt) 
	{
		Dagskort ny = (Dagskort) kt;
		
		Date naa = new Date();
		Date utlop = ny.getUtløp();
		
		boolean ok = naa.after(utlop);
		if(ok)
			return kt;
		else return null;
	}

	// sjekker om sesongkort er gyldig
	public Korttype sjekkSes(Korttype kt) 
	{
		Sesongkort ny = (Sesongkort) kt;
		
		Date naa = new Date();
		Date utlop = ny.getUtløp();
		
		boolean ok = naa.after(utlop);
		if(ok)
			return kt;
		else return null;
		
	}
	
	// fjerner et klipp fra klippekort
	public void fjernKlipp(int nr)
	{
		Korttype k = getKorttype(nr);
		if(k.getId() == nr && k instanceof Klippekort)
		{
			Klippekort ny = (Klippekort) k;
			if(ny.getKlipp()==1)
				fjern(k);
			ny.fjernKlipp();
		}
	}
	
	/* sjekker om kort med innkommende id'nr 
	   er gyldig, og oppdaterer statistikk */
	public boolean sjekkGyldig(int nr)
	{
		for(Korttype k : kort)
		{
			if(k.getId() == nr)
			{
				if(k instanceof Klippekort)
				{
					Klippekort ny = (Klippekort) k;
					if(ny.getKlipp()>=1)
						stat.setAntallKlippTurer();
					else 
						return false;
				}
				else if(k instanceof Dagskort)
					stat.setAntallDagTurer();
				else if(k instanceof Sesongkort)
					stat.setAntallSesTurer();
				
				stat.setTotalKontroller();
				return true;
			}
		}
		return false;
	}
	
	/* sender ut kort-info, av kort med gitt 
	   id, dersom det er gyldig/aktivt */
	public String toStringVisKunde(int id)
	{
		String ut = "Keycard id: " + id + "\nIngen aktive kort";
		Korttype kort = getKorttype(id);
		if(kort == null)
			return ut;
		if(kort instanceof Sesongkort)
		{
			Sesongkort k = (Sesongkort) kort;
			ut = "Type: Sesongkort - Id: " + k.getId() +"\nSolgt: " + k.formatDate(k.getSalgDato()) +"\nUtløper: " + k.formatDate(k.getUtløp());
		}
		else if(kort instanceof Klippekort)
		{
			Klippekort k = (Klippekort) kort;
			ut = "Type: Klippekort - Id: " + k.getId() +"\nSolgt: " + k.formatDate(k.getSalgDato()) + "\nKlipp igjen: " + k.getKlipp();
		}
		else if(kort instanceof Dagskort)
		{
			Dagskort k = (Dagskort) kort;
			ut = "Type: Dagskort - Id: " + k.getId() + "\nSolgt: " + k.formatDate(k.getSalgDato()) + "\nUtløper: " + k.formatDate(k.getUtløp());
		}
		return ut;
	}
	
	// sender ut kort-info i en String-array
	public String[] toStringArray()
	{
		if(kort.size() == 0)
			return null;
		
		String ut[] = new String[kort.size()];
		int i = 0;
		for(Korttype k : kort)
		{
			ut[i] = k.toString();
			i++;
		}

		return ut;
	}
	
	// sender ut all kort-info i en String
	public String toString()
	{
		String ut = "";
		for(Korttype k : kort)
			ut += k.toString() + "\n";
		
		if(ut.equals(""))
			return "Ingen registrerte";
		
		return ut;
	}
}