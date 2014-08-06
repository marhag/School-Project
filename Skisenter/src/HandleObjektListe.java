/*
 * Klassen inneholder:
 *  - LinkedList av HandleObjekter
 *  - metoder for innsetting, søking, endring 
 *    og fjerning av HandleObjekter
 *  - metoder for
 *  
 *  Sist endret: 04/05/13
 */

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/* Klassen inneholder alle handleobjektene.
   Denne klassen er selve handlelista. */
public class HandleObjektListe 
{
	private List<HandleObjekt> handleliste = new LinkedList<>();
	
	/* Setter inn et HandleObjekt i lista. Parametere er:
	   id, korttype, fornavn, etternavn, fødselsdato og pris */
	public void settInn(int id, Korttype kort, String fornavn, String etternavn, Date fodselsdato,double pris)
	{
		handleliste.add(new HandleObjekt(id, kort, fornavn, etternavn, fodselsdato,pris));
	}
	
	// Finner og returnerer et handleobjekt med gitt id
	public HandleObjekt finnHandleObjekt(int id)
	{		
		for(HandleObjekt o : handleliste)
		{
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	// Sletter handleobjekt med gitt id
	public boolean slett(int id)
	{
		for(HandleObjekt o : handleliste)
		{
			if(o.getId() == id)
				return handleliste.remove(o);
		}
		return false;
	}
	
	// Sletter første handleobjekt i lista
	public HandleObjekt slettForste()
	{
		if(handleliste.size() < 1)
			return null;
		
		return handleliste.remove(0);
	}
	
	// Returnerer lengden av handlelista
	public int getLength()
	{
		return handleliste.size();
	}
	
	// Returnerer index'en til det siste handleobjektet i lista
	public int getLastIndex()
	{
		return handleliste.size() - 1;
	}
	
	// Returnerer id'en til det siste handleobjektet i lista
	public int getLastId()
	{
		if(handleliste.size() == 0)
			return -1;

		return handleliste.get(getLastIndex()).getId();
	}
	
	// Sender ut en String-array av alle handleobjekter i lista
	public String[] toStringArray()
	{
		String[] ut = new String[handleliste.size()];		
		for(int i = 0; i < ut.length; i++)
			ut[i] = handleliste.get(i).toString();
		
		return ut;
	}
	
	// Henter handleobjektet fra innkommende liste-index
	public HandleObjekt getElementFromIndex(int index)
	{
		if(getLastIndex() == -1 || index > getLastIndex())
			return null;
		
		return handleliste.get(index);
	}
	
	// Returnerer LinkedList'en
	public List<HandleObjekt> getHandleliste()
	{
		return handleliste;
	}
	
	// Fjerner elementet på indexen til innkommende tallverdi
	public void removeElementOnIndex(int index)
	{
		handleliste.remove(index);
	}
	
	// Sjekker om handleobjektet har en eier med gitt id
	public boolean harEier(int id) 
	{
		HandleObjekt h = finnHandleObjekt(id);
		if(h == null || h.getFornavn().equals("") || h.getEtternavn().equals(""))
			return false;

		return true;
	}
	
	// Skriver ut informasjon fra handlelista
	public String toString()
	{
		String ut = "";
		for(HandleObjekt o : handleliste)
			ut += o.toString() + "\n";
		
		return ut;
	}
}
