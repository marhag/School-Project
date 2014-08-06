/*
 * Klassen inneholder:
 *  - pris, salgsdato, id
 *  - get-metoder for pris, salgsdato og id
 *  
 * Skrevet av Martin og Sondre
 * 
 * Siste endring: 25/04/13
 */

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Denne klassen er super-klassen til alle kort-typer.
   Dags-, Klipp-, og Sesongkort, arver alle metodene fra Korttype */
public class Korttype  implements Serializable
{
	private static final long serialVersionUID = 10025L;
	
	private double pris;
	private Date salgDato;
	private int id;

	// Oppretter klassen og setter pris, salgsdato, og id til parametrene
	public Korttype(double pris, Date salgsdato, int id)
	{
		this.pris = pris;
		this.salgDato = salgsdato;
		this.id = id;
	}
	
	// Henter id
	public int getId()
	{
		return id;
	}
	
	// Henter pris
	public double getPris()
	{
		return pris;
	}
	
	// Henter salgsdato
	public Date getSalgDato()
	{
		return salgDato;
	}
	
	// Formaterer dato til et enklere og mer leslig format
	public String formatDate(Date date)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String ut = df.format(date);
		return ut;
	}
}
