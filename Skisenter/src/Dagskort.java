/*
 * Klassen inneholder:
 *  - kort-id, salgsdato, utløpstid, pris
 * 
 * lagd av Martin
 * 
 * Siste endring: 25/04/13
 */
import java.util.Calendar;
import java.util.Date;

/* Klassen representerer et dagskort. Dagskort 
   ligger i registeret over aktive/gyldige heiskort. */
public class Dagskort extends Tidsbasertkort
{
	private static final long serialVersionUID = 1001L;

	// Oppretter et dagskort. Den setter pris, salgsdato, utløpstid og id
	public Dagskort(double pris, Date salgdato,int id) 
	{
		super(pris, salgdato,id);
		leggTilDag();
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
	
	// Henter utløpstid
	public Date getUtløp()
	{
		return super.getUtløp();
	}
	
	// Setter utløpstid
	public void setUtløp(Date n)
	{
		super.setUtløp(n);
	}
	
	// Legger til en enkelt-dag på utløpstid
	public void leggTilDag()
	{
		Calendar cal = Calendar.getInstance();
        cal.setTime(getSalgDato());
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date ny = cal.getTime();
        setUtløp(ny);
	}
	
	// Skriver ut informasjon om dagskortet
	public String toString()
	{
		String ut = "";
		ut = "Id " + getId() + " - Dagskort - Utløper: "+ super.formatDate(getUtløp())+" - Solgt: "+ super.formatDate(getSalgDato());
		return ut;
	}
}
