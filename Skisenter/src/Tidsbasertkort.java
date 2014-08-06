/*
 * Klassen inneholder:
 *  - Utløpsdato
 * 
 * Lagd av Martin
 * 
 * Sist endret: 25/04/13
 */

import java.util.Date;

// En super-klasse til dag- og sesongkort, men arver korttype.
public class Tidsbasertkort extends Korttype
{
	private static final long serialVersionUID = 1006L;
	
	private Date utløp;
	
	// Oppretter objekt. Parametere: pris, salgsdato, id
	public Tidsbasertkort(double p, Date s,int i) 
	{
		super(p, s, i);
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
		return utløp;
	}
	
	// Setter utløpsdato til dato n
	public void setUtløp(Date n)
	{
		utløp = n;
	}
}