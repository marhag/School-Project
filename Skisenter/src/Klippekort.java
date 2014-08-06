/*
 * Klassen inneholder:
 *  - kort-id, salgsdato, antall klipp og pris
 * 
 * lagd av Martin
 * 
 * Siste endring: 25/04/13
 */

import java.util.Date;

/* Klassen representerer et klippekort. Klippekort
   ligger i registeret over aktive/gyldige heiskort. */
public class Klippekort extends Korttype
{
	private static final long serialVersionUID = 1003L;
	
	private int klipp;
	
	// Oppretter et klippekort. Den setter pris, salgsdato, id og antall klipp
	public Klippekort(double pris, Date salgdato,int id, int klipp) 
	{
		super(pris, salgdato, id);
		this.klipp = klipp;	
	}
	
	// Henter id
	public int getId()
	{
		return super.getId();
	}
	
	// Henter pris
	public double getPris()
	{
		return super.getPris();
	}
	
	// Henter salgsdato
	public Date getSalgDato()
	{
		return super.getSalgDato();
	}
	
	// Henter antall klipp
	public int getKlipp()
	{
		return klipp;
	}
	
	// Fjerner et enkelt-klipp. Skjer bare når kortet brukes
	public void fjernKlipp()
	{
		klipp--;
	}
	
	// Skriver ut informasjon om klippekortet
	public String toString()
	{
		String ut = "";
		ut = "Id " + getId() + " - Klippekort - Antall klipp: "+ getKlipp() + " - Solgt: "+ super.formatDate(getSalgDato());
		return ut;
	}
}
