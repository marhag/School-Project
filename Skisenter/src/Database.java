/*
 * Klassen inneholder:
 *  - Administrator-statistikk
 *  - Liste av priser, kunder, aktivekort og keycards
 *  - get-metoder for alle objektene
 * 
 * Sondre skrev denne klassen
 * 
 * Sist endret: 02/05/13
 */
import java.io.Serializable;

/* Database inneholder all informasjon som lagres om skikort,
   kunder og salgsstatistikk. Denne klassen blir lagret på disk    */
public class Database implements Serializable
{
	private static final long serialVersionUID = 10010L;

	private AdminStat adminstat;
	private AktiveKort aktivekort;
	private KeycardListe keycardliste;
	private KundeListe kundeliste;
	private Priser prisliste;
	
	/* Oppretter en database og definerer parametrene for:
	   - administratorstatistikk
	   - listene over aktive-kort, keycards, kunder og priser */
	public Database(AdminStat adminstatistikk, AktiveKort aktivekort,
			KeycardListe keycardliste, KundeListe kundeliste, Priser prisliste)
	{
		this.adminstat = adminstatistikk;
		this.aktivekort = aktivekort;
		this.keycardliste = keycardliste;
		this.kundeliste = kundeliste;
		this.prisliste = prisliste;
	}
	
	// henter administrator-statistikk
	public AdminStat getAdmin()
	{
		return adminstat;
	}
	
	// henter liste over aktive kort
	public AktiveKort getAktive()
	{
		return aktivekort;
	}
	
	// henter liste over keycards
	public KeycardListe getKeycard()
	{
		return keycardliste;
	}
	
	// henter kundeliste
	public KundeListe getKunde()
	{
		return kundeliste;
	}	
	
	// henter priser
	public Priser getPriser()
	{
		return prisliste;
	}
}
