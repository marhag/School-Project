/*
 * Klassen inneholder:
 *  - Metoder som oppretter kort og kunder
 * 
 * Skrevet av Sondre
 * 		Martin har lagt til oppdatering av 
 * 		dagsoppgjør, og metoder som hører til
 * Sist endret: 14/05/13
 */

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/* Klassen oppretter et gitt antall med kunder og skikort,
   dersom programmet ikke henter kort- og kunde-data fra fil */
public class Script 
{
	private int antallKlipp = 100;
	private int antallSesong = 150;
	
	private int id = 1;
	
	private String[] fornavn = {"Sondre", "Martin", "Lars", "Erik", "Joakim", "Eva", "Ole", "Markus", "Jenny", "Martine", "Guro", "Line", "Harry", "Mikkel"};
	private String[] etternavn = {"Bogen", "Hage", "Larsen", "Vihovde", "Sturlason", "Void", "Hansen", "Persson", "Hansen", "Lillemark", "Hole", "Potter"};
	
	private AktiveKort aktive;
	private KundeListe kunder;
	private KeycardListe keycards;
	private Priser pris;
	private SelgerVindu selger;
	private AdminStat admin;
	
	/* Oppretter klassen, henter klasse-registere fra database
	   og kjører metoder for oppretting av kort/kunder */
	public Script(Database database,SelgerVindu selgerV)
	{
		aktive = database.getAktive();
		kunder = database.getKunde();
		keycards = database.getKeycard();
		pris = database.getPriser();
		selger = selgerV;
		admin = database.getAdmin();
		
		opprettKlipp();
		opprettSesong();
	}
	
	// Oppretter et gitt antall klippekort, og legger informasjon inn i registerne
	private void opprettKlipp()
	{
		for(int i = 0; i < antallKlipp; i++)
		{
			Keycard kc = new Keycard(id++);
			int klipp = randomTall(1,10);
			Date dato = randomDate(2009, 2013);
			Klippekort kk = new Klippekort(pris.getKLIPPVOKSEN(), dato, kc.getId(), klipp);
			
			admin.setAntallKlipp();
			admin.setTotalKlipp(pris.getKLIPPVOKSEN());
			admin.setTotalAntall();
			admin.setKeycardAntall();
			admin.setKeycardPris(pris.getKEYCARD());
			admin.setTotalPris(pris.getKLIPPVOKSEN()+pris.getKEYCARD());
			keycards.leggTil(kc);
			aktive.leggTil(kk);
			//for hvert 50'ene kort lagres det som en dag med salg
			if(((i+1)%25==0)&&(i!=0))
			{
				//simulerer 3 transakjoner
				admin.setAntallTransaksjoner();
				admin.setAntallTransaksjoner();
				admin.setAntallTransaksjoner();
				selger.oppgjorStart();
			}
		}
	}
	
	// Oppretter et gitt antall Sesongkort, og legger informasjon inn i registerne
	private void opprettSesong()
	{
		for(int i = 0; i < antallSesong; i++)
		{
			Keycard kc = new Keycard(id++);
			Kunde k = opprettKunde(kc);
			Sesongkort sk;
			Date solgt = randomDate(2012, 2013);
			sk = new Sesongkort(pris.getSESVOKSEN(), solgt, kc.getId());
			
			admin.setAntallASes();
			admin.setTotalSes(pris.getSESVOKSEN());
			admin.setTotalAntall();
			admin.setKeycardAntall();
			admin.setKeycardPris(pris.getKEYCARD());
			admin.setTotalPris(pris.getSESVOKSEN()+pris.getKEYCARD());
			keycards.leggTil(kc);
			aktive.leggTil(sk);
			kunder.settInnKunde(k);
			if(((i+1)%25==0)&&(i!=0))
			{
				//simulerer 3 transakjoner
				admin.setAntallTransaksjoner();
				admin.setAntallTransaksjoner();
				admin.setAntallTransaksjoner();
				selger.oppgjorStart();
			}
		}
	}
	
	// Oppretter og returnerer en kunde, med keycard kc
	private Kunde opprettKunde(Keycard kc)
	{
		String fnavn, enavn;
		Date dato;

		fnavn = fornavn[randomTall(0, fornavn.length-1)];
		enavn = etternavn[randomTall(0, etternavn.length-1)];
		dato = randomDate(1940, 2002);
		
		Kunde kunde = new Kunde(fnavn, enavn, dato, kc);
		return kunde;
	}
	
	// Genererer en tilfeldig dato, mellom årstall "fra" og "til"
	private Date randomDate(int fra, int til)
	{
		Date dato;
		do
		{
			int år = randomTall(fra, til);
			int dagIÅret = randomTall(1, 365);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, år);
			calendar.set(Calendar.DAY_OF_YEAR, dagIÅret);
			dato = calendar.getTime();
		} while(!dato.before(new Date()));
		
		return dato;
	}
	
	// Genererer et tilfeldig tall, mellom "fra" og "til"
	private int randomTall(int fra, int til)
	{
		Random rand = new Random(); 
		int tall = rand.nextInt(til-fra+1) + fra; 
		return tall;
	}
}