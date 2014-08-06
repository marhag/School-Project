/*
 * Klassen inneholder:
 *  - kort-id, salgsdato og pris
 * 
 * lagd av Martin
 * 
 * Siste endring: 25/04/13
 */

import java.util.Calendar;
import java.util.Date;

/* Klassen representerer et Sesongkort. Sesongkort
   ligger i registeret over aktive/gyldige heiskort. */
public class Sesongkort extends Tidsbasertkort
{
	private static final long serialVersionUID = 1005L;

	// Oppretter Sesongkort, og setter utløpsdato. Parametere: pris, salgsdato, id
	public Sesongkort(double p, Date s,int i) 
	{
		super(p, s,i);
		leggTilAr();
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
	
	// Henter utløpsdato
	public Date getUtløp()
	{
		return super.getUtløp();
	}
	
	// Setter utløpsdato
	public void setUtløp(Date n)
	{
		super.setUtløp(n);
	}
	
	// Legger til et år på utløpsdato
	public void leggTilAr()
	{
		Calendar cal = Calendar.getInstance();
        cal.setTime(getSalgDato());
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.add(Calendar.DATE, 364);
        Date ny = cal.getTime();
        setUtløp(ny);
	}
	
	// Skriver ut informasjon om sesongkortet
	public String toString()
	{
		String ut = "";
		ut = "Id " + getId() + " - Sesongkort - Utløper: "+ super.formatDate(getUtløp()) + " - Solgt: "+ super.formatDate(getSalgDato());
		return ut;
	}
}
