/*
 * Klassen inneholder:
 *  - Registere: database, statistikk, aktive kort
 *    keycard, kundeliste, priser og metoderegister
 *  - ArrayList av KontrollVindu
 *  - Liste av handleobjekter og midlertidige keycard
 *  - Skriving og henting til/fra fil
 *  - Metoder for å betale, endre og slette handleobjekter
 *
 * Skrevet av Sondre og Martin
 * 
 * 		Siste endring: 14/05/13
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/* SelgerMetoder håndterer salgsoperasjoner, og sender
   betalte handleobjekter til de riktige registerene */
public class SelgerMetoder 
{
	private SelgerVindu vindu;
	private PrisPanel prispanel;
	
	private Database database;
	private AdminStat adminstat;
	private AktiveKort aktivkortreg;
	private KeycardListe keycardreg;
	private KundeListe kundereg;
	private Priser prisliste;
	private MetodeRegister metoder;
	
	private ArrayList<KontrollVindu> kontrollere;
	private HandleObjektListe tempOldKeycard, handleliste;
	
	private int antallTempNewKeycard;
	private int valgtindex;
	private HandleObjekt valgt;
	private double total;
	
	private boolean open;
	
	/* Oppretter klassen, registrerer parametre: selgervindu, database,
	   kontrollvinduer og informasjonsvinduet/nettside 
	   Andre registere blir også opprettet */
	public SelgerMetoder(SelgerVindu v, Database database, ArrayList<KontrollVindu> kontroll, InfoVindu infoVindu)
	{
		vindu = v;
		this.database = database;
		adminstat = database.getAdmin();
		aktivkortreg = database.getAktive();
		keycardreg = database.getKeycard();
		kundereg = database.getKunde();
		prisliste = database.getPriser();
		prispanel = new PrisPanel(database, vindu, infoVindu);
		
		metoder = new MetodeRegister(database);
		kontrollere = kontroll;
		
		open = false;
		
		tempOldKeycard = new HandleObjektListe();
		handleliste = new HandleObjektListe();
	}
	
	/*******Metoder for skriving og henting til og fra fil*****/
	// Henter database-klassen fra fil
	public boolean hentFil(boolean melding)
	{
		try (ObjectInputStream inn = new ObjectInputStream(new FileInputStream("database.dta")))
		{
			database = (Database) inn.readObject();
			redefinerRegister(database);
			return true;
		}
		catch(ClassNotFoundException cnfe)
		{
			if(melding)
				JOptionPane.showMessageDialog(vindu, "Database ble ikke funnet!\nNy database vil bli opprettet!",
			         "Database ikke funnet", JOptionPane.PLAIN_MESSAGE );
		}
		catch(FileNotFoundException fne)
		{
			if(melding)
				JOptionPane.showMessageDialog(vindu, "Finner ikke fil database-fil.\nOppretter en ny database.","Feilmelding", JOptionPane.PLAIN_MESSAGE );
		}
		catch(IOException ioe)
		{
			if(melding)
				JOptionPane.showMessageDialog(vindu, "Problem med innlesning. Register er tomt.","Feilmelding", JOptionPane.ERROR_MESSAGE );
		}
		return false;
	}
	
	// Skriver database-klassen til fil
	public void skrivFil()
	{
		try (ObjectOutputStream ut = new ObjectOutputStream(new FileOutputStream("database.dta")))
		{
			ut.writeObject(database);
		}
		catch( NotSerializableException nse )
		{
			JOptionPane.showMessageDialog(vindu, "Objekt ikke serialisert.","Feilmelding", JOptionPane.ERROR_MESSAGE);
		}
		catch( IOException ioe )
		{	
			JOptionPane.showMessageDialog(vindu,"Problem oppsto ved skriving til fil","Feilmelding", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/****************************************************/
	
	
	// Sørger for at alle registere holder seg oppdatert etter innlesning fra fil
	public void redefinerRegister(Database totalreg) 
	{
		adminstat = database.getAdmin();
		aktivkortreg = database.getAktive();
		keycardreg = database.getKeycard();
		kundereg = database.getKunde();
		prisliste = database.getPriser();
		
		metoder.setRegister(database);
		setAktivTilKontroll();
		
		prispanel.setPris(prisliste);
	}
	
	// Åpner et salgsskift, kontrollvinduene og skibakken
	public void åpneSkift()
	{
		if(open)
		{
			open = false;
			for(KontrollVindu k : kontrollere)
				k.setOpen(false);
			vindu.openLightSetColor(open);
			database.getAktive().oppdater();
			return;
		}
		open = true;
		for(KontrollVindu k : kontrollere)
			k.setOpen(true);
		
		vindu.openLightSetColor(open);
		database.getAktive().oppdater();
	}
	
	// Skriver ut info for hjelp
	public void hjelpKnapp()
	{
		JOptionPane.showMessageDialog(vindu,
		"Det er to måter å logge på med: \n" +
		"- Admin: Har tilgang til alle områder i programmet\n" +
		"\tBrukernavn: admin, Passord: 132	 \n" +
		"- Selger: kun tilgang til salg og enkle register metoder, ingen endringer\n" +
		"\tBrukernavn: selger, Passord: 111","Hjelp", JOptionPane.INFORMATION_MESSAGE);
		vindu.resetFocus();
	}
	
	/* Sletter keycard og aktive billetter på keycardet
	   Brukeren må være admin, ellers får han feilmelding */
	public void slettKc(JTextField sletteKc, JList<String> infoRegister) 
	{
		if(!vindu.isAdmin())
		{
			JOptionPane.showMessageDialog(vindu, "Du har ikke rettigheter til denne funksjonen!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try
		{
			int id = Integer.parseInt(sletteKc.getText());
			if(keycardreg.keycardFins(id)) // keycard'et fins
			{
				if(aktivkortreg.keycardFins(id)) // keycard'et er aktivt
				{
					int svar = JOptionPane.showConfirmDialog(vindu, "NB! Dette kortet er i bruk. Om du sletter dette kortet vil det ikke kunne brukes på nytt.\nAll personlig informasjon registret på kortet vil bli slettet!\nVil du slette dette kortet?", 
							"Sletting av keycard", JOptionPane.YES_NO_OPTION);
					if (svar == JOptionPane.YES_OPTION) 
					{
						keycardreg.fjern(id);
						aktivkortreg.fjern(id);
						Kunde k = kundereg.finnEierMedId(id);
						if(k != null)
							kundereg.getKunder().remove(k);
					}
				}
				else
				{
					int svar = JOptionPane.showConfirmDialog(vindu, "NB! Om du sletter dette kortet, vil det ikke kunne brukes på nytt.\nAll personlig informasjon registrert på kortet vil bli slettet!\nVil du slette dette kortet?", 
							"Sletting av keycard", JOptionPane.YES_NO_OPTION);
					if (svar == JOptionPane.YES_OPTION) 
					{
						keycardreg.fjern(id);
						Kunde k = kundereg.finnEierMedId(id);
						if(k != null)
							kundereg.getKunder().remove(k);
					}
				}
				infoRegister.setListData(metoder.toStringInfoArray(keycardreg));
				sletteKc.setText("");
			}
			else
				JOptionPane.showMessageDialog(vindu, "Keycard finnes ikke!","Feilmelding", JOptionPane.ERROR_MESSAGE);
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(vindu, "Skriv inn en gyldig kort-id.\nBare tall.","Feilmelding", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	// Sender register over aktive-kort til kontrollvinduene
	public void setAktivTilKontroll()
	{
		for(KontrollVindu k : kontrollere)
			k.setAktive(aktivkortreg);
	}
		
	// Fjerner nye ubrukt/ikke-betalte keycard fra handlelista
	public void fjernKc()
	{
		if((antallTempNewKeycard <= 0 && valgtindex == 0 && valgt == null) || valgtindex == -1)
			return;
		else if(valgtindex == 0 && valgt == null)
		{
			antallTempNewKeycard--;
		}
		else if(valgt != null)
			tempOldKeycard.removeElementOnIndex(valgtindex);

		valgtindex = 0;
		valgt = null;
		
		vindu.oppdateHandleliste(null);
	}

	/* Registrerer et nytt handleobjekt av typen, Klippekort.
	   Parametre er antallKlipp (antall klippekort), 
	   typeklipp (barn/voksen/honnør), og checkbox for skole-/høytidsrabatt */
	public void regKlippeKort(String antallklipp, JComboBox<String> typeKlipp, JCheckBox skoleCheck, JCheckBox hoytidCheck ) 
	{
		// Sjekker om selger har registrert minst et keycard
		if(antallTempNewKeycard == 0 && tempOldKeycard.getLength()==0)
		{
			JOptionPane.showMessageDialog(vindu, "Du må ha et keycard for å kjøpe biletter!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
		}
		
		if(valgtindex == -1) // Dersom ingenting er valgt, eller en feil skulle skje
			return;
		
		double pris = 0;
		int id = -1;
		int antall = 0;
		try
		{
			antall = Integer.parseInt(antallklipp); // Antall klippekort
			if(antall != 1)
			{
				if((tempOldKeycard.getLength() + antallTempNewKeycard) < antall)
				{
					JOptionPane.showMessageDialog(vindu, "Ikke nok keycards!","Feilmelding", JOptionPane.ERROR_MESSAGE);
					vindu.oppdateHandleliste(null);
					return;
				}
			}
			while(antall!=0)
			{
				String type = (String) typeKlipp.getSelectedItem();
				
				if(type.toUpperCase().equals("VOKSEN"))
					pris = prisliste.getKLIPPVOKSEN();
				else if(type.toUpperCase().equals("BARN"))
					pris = prisliste.getKLIPPBARN();
				else if(type.toUpperCase().equals("HONNØR"))
					pris = prisliste.getKLIPPHONNOR();
				
				if(valgt != null)
					id = tempOldKeycard.getElementFromIndex(valgtindex).getId();
				
				if(antallTempNewKeycard <= 0)
				{
					valgtindex = 0;
					valgt = tempOldKeycard.getElementFromIndex(valgtindex);
					id = valgt.getId();
				}
				
				Date naa = new Date();
				double ekstraRab = 1;
				if(skoleCheck.isSelected()||hoytidCheck.isSelected())
				{
					if(hoytidCheck.isSelected())
						ekstraRab = prisliste.getRABATTHOYTID();
					if(skoleCheck.isSelected())
						ekstraRab = prisliste.getRABATTSKOLE();
				}
				pris  = pris * prisliste.getRABATTKLIPPKORT()*ekstraRab;
				Klippekort ny = new Klippekort(pris, naa, id, prisliste.getKlipp());
				handleliste.settInn(id, ny, "", "", null,pris);
				
				// sjekker om det er et nytt eller gammelt keycard
				if(valgt != null) // gammelt
				{
					valgt = null;
					valgtindex = 0;
					tempOldKeycard.slett(id);
				}
				if(id == -1) // nytt
				{
					antallTempNewKeycard--;
					pris += prisliste.getKEYCARD();
				}
				DecimalFormat decim = new DecimalFormat("0.00");
				total += pris; 
				String idtext = id+"";
				String typekort = "Klippekort";
				if(id == -1)
				{
					idtext = "*";
					typekort += " + kc";
				}	
				
				String[] utKlipp={typekort,idtext,"",decim.format(pris)+""};
				antall--;

				vindu.oppdateHandleliste(utKlipp);
			} // end of while
		} // end of try
		catch ( NumberFormatException e )
	    {
			JOptionPane.showMessageDialog(vindu,"Ingen registrering pga. feil i tallformat!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
	    }
		vindu.setAntallKlipp("1");
	}

	/* Registrerer et nytt handleobjekt av typen, Dagskort.
	   Parametre er antallDag (antall dagskort), 
	   typedag (barn/voksen/honnør), og checkbox for skole-/høytidsrabatt */
	public void regDagsKort(String antallDag, JComboBox<String> typeDag, JCheckBox skoleCheck, JCheckBox hoytidCheck) 
	{
		// Sjekker om selger har registrert minst et keycard
		if(antallTempNewKeycard == 0 && tempOldKeycard.getLength() == 0)
		{
			JOptionPane.showMessageDialog(vindu,"Du må ha et keycard for å kjøpe biletter!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
		}

		if(valgtindex == -1) // Om ingenting er valgt, eller en feil skulle skje
			return;
		
		double pris = 0;
		int id = -1;
		int antall = 0;
		try
		{
			antall = Integer.parseInt(antallDag); // antall dagskort
			if(antall != 1)
			{
				if((antallTempNewKeycard + tempOldKeycard.getLength()) < antall)
				{
					JOptionPane.showMessageDialog(vindu,"Ikke nok keycards\nRegistrer flere keycards!","Feilmelding", JOptionPane.ERROR_MESSAGE);
					vindu.oppdateHandleliste(null);
					return;
				}
			}
			while(antall!=0)
			{
				String type = (String) typeDag.getSelectedItem();
				
				if(type.toUpperCase().equals("VOKSEN"))
					pris = prisliste.getDAGVOKSEN();
				else if(type.toUpperCase().equals("BARN"))
					pris = prisliste.getDAGBARN();
				else if(type.toUpperCase().equals("HONNØR"))
					pris = prisliste.getDAGHONNOR();
				
				// om ingenting er valgt velges et uregistret keycard, om det fins
				if(valgt != null) 
					id = tempOldKeycard.getElementFromIndex(valgtindex).getId();
				
				// sjekker om det fins uregistrerte keycard i handlelista
				if(antallTempNewKeycard <= 0)
				{
					valgtindex = 0;
					valgt = tempOldKeycard.getElementFromIndex(valgtindex);
					id = valgt.getId();
				}
				
				Date naa = new Date();
				
				// Sjekker når på dagen kortet blir kjøpt
				Calendar cal = Calendar.getInstance();
		        cal.setTime(new Date());
		        cal = setCal(15,00,0,0,cal);
		        Date midtDag = cal.getTime();
		        Calendar cal2 = Calendar.getInstance();
		        cal2.setTime(new Date());
		        cal2 = setCal(23,00,0,0,cal2);
		        Date sluttDag = cal2.getTime();
		        
		        // om det er etter midt på dagen
				if(naa.after(midtDag))
				{
					if(naa.after(sluttDag))
					{
						Calendar nesteDag = Calendar.getInstance();
						nesteDag.setTime(naa);
						nesteDag.add(Calendar.DATE, 1);
						nesteDag = setCal(8,00,0,0,nesteDag);
				        naa = nesteDag.getTime();
					}
					else
						pris = pris * prisliste.getRABATTHALVDAG();
				}
				
				double ekstraRab = 1;
				
				// Sjekker skole-/høytids-rabatt
				if(skoleCheck.isSelected()||hoytidCheck.isSelected())
				{
					if(hoytidCheck.isSelected())
						ekstraRab = prisliste.getRABATTHOYTID();
					if(skoleCheck.isSelected())
						ekstraRab = prisliste.getRABATTSKOLE();
				}
				
				pris  = pris * prisliste.getRABATTDAGKORT()*ekstraRab;
				Dagskort ny = new Dagskort(pris, naa, id);
				handleliste.settInn(id, ny, "", "", null,pris);
				
				// sjekker om det er et nytt eller gammelt keycard
				if(valgt != null) //gammelt
				{
					valgt = null;
					valgtindex = 0;
					tempOldKeycard.slett(id);
				}
				else if(id == -1) // nytt
				{
					antallTempNewKeycard--;
					pris += prisliste.getKEYCARD();
				}
				
				DecimalFormat decim = new DecimalFormat("0.00");
				total +=pris; 
				String idtext = id+"";
				String typekort = "Dagskort";
				if(id == -1)
				{
					idtext = "*";
					typekort += " + kc";
				}	
				
				String[] utDag={typekort, idtext,"",decim.format(pris)+""};
				antall--;
				
				vindu.oppdateHandleliste(utDag);
			} // end of while
		} // end of try
		catch ( NumberFormatException e )
	    {
			JOptionPane.showMessageDialog(vindu,"Ingen registrering pga. feil i tallformat!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
	    }
		vindu.setAntallDag("1");
	}

	/* Registrerer et nytt handleobjekt av typen, Sesongkort.
	   Dersom kortet ikke har eier fra før, blir metoden under initialisert.
	   Parametre er fornavn, etternavn og fødselsdato */
	public void regSesongKort(String fn, String en, String dat) 
	{
		// Sjekker om det fins keycard i handlelista
		if(antallTempNewKeycard == 0 && tempOldKeycard.getLength()==0)
		{	
			JOptionPane.showMessageDialog(vindu,"Du må ha et keycard for å kjøpe bilett","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
		}
		
		if(valgtindex == -1) // dersom ingenting er valgt, eller en feil skulle skje
			return;
	
		Kunde eier;
		double pris = 0;
		String enavn;
		int id = -1;
		
		try
		{
			if(valgt != null) // dersom ingenting er valgt, velges et nytt keycard
				id = tempOldKeycard.getElementFromIndex(valgtindex).getId();
			
			// Sjekker om kort har eier, hvis ikke kjøres metoden under.
			if(kundereg.finnEierMedId(id)==null) // ny eier
			{
				regSesongkortNyEier(id, pris, fn, en, dat);
				return;
			}
			else // gammel eier
				eier = kundereg.finnEierMedId(id);
			
			Date fdato = eier.getDato();
			Calendar cal = Calendar.getInstance();
			Calendar calNow = Calendar.getInstance();
			cal.setTime(fdato);
			calNow.setTime(new Date());
			
			int year = cal.get(Calendar.YEAR);
			int yearNow = cal.get(Calendar.YEAR);
			
			// Sjekker alder, og bestemmer pris etter alder
			if((yearNow-year)>18)
				pris = prisliste.getSESVOKSEN();
			else if((yearNow-year)<=18)
				pris = prisliste.getSESBARN();
			else
				pris = prisliste.getSESHONNOR();
			
			String fnavn = eier.getFornavn();
			enavn = eier.getEtternavn();

			// Sjekker om nytt keycard er valgt
			if(valgt != null) // gammelt
				tempOldKeycard.slett(id);
			else // nytt
			{
				antallTempNewKeycard--;
				pris += prisliste.getKEYCARD();
			}
			
			Date naa = new Date();
			Sesongkort ny = new Sesongkort(pris, naa, id);
			pris  = pris * prisliste.getRABATTSESONGKORT();
			handleliste.settInn(id, ny, fnavn, enavn, fdato,pris);
		} // end of try
		catch ( NumberFormatException e )
	    {
			JOptionPane.showMessageDialog(vindu,"Ingen registrering pga. feil i tallformat!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
	    } 
		
		//Skriver ut info til kvitteringsvinduet
		
		DecimalFormat decim = new DecimalFormat("0.00");
		total +=pris; 
		valgt = null;
		valgtindex = 0;

		String idtext = id+"";
		String type = "Sesongkort";
		
		if(id == -1)
		{
			idtext = "*";
			type += " + kc";
		}	
		
		String[] utSes={type, idtext,enavn.toUpperCase(),decim.format(pris)+""};
		vindu.oppdateHandleliste(utSes);
	}
	
	/* Registrerer et nytt handleobjekt av typen, Sesongkort.
	   Initialiseres dersom kortet ikke har eier fra før.
	   Parametre er id, pris, fornavn, etternavn og fødselsdato */
	public void regSesongkortNyEier(int id,double pris, String fn, String en, String dat) 
	{
		String fnavn;
		Date fdato;
		try
		{
			fnavn = fn;
			String enavn = en;
	
			DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			String datoB = dat;
			fdato = sourceFormat.parse(datoB);
			
			Calendar cal = Calendar.getInstance();
			Calendar calNow = Calendar.getInstance();
			cal.setTime(fdato);
			calNow.setTime(new Date());
			
			int year = cal.get(Calendar.YEAR);
			int yearNow = calNow.get(Calendar.YEAR);
			
			if((yearNow-year)>18 && (yearNow-year)<61)
				pris = prisliste.getSESVOKSEN();
			else if((yearNow-year)<=18)
				pris = prisliste.getSESBARN();
			else 
				pris = prisliste.getSESHONNOR();
	
			Date naa = new Date();
			pris  = pris * prisliste.getRABATTSESONGKORT();
			Sesongkort ny = new Sesongkort(pris, naa, id);
			
			handleliste.settInn(id, ny, fnavn, enavn, fdato,pris);
			
			// Sjekker om keycardet er ubrukt eller gammelt
			if(valgt != null) // gammelt
				tempOldKeycard.slett(id);
			else // ubrukt
			{
				antallTempNewKeycard--;
				pris += prisliste.getKEYCARD();
			}
		} // end of try
		catch ( NumberFormatException e )
	    {
			JOptionPane.showMessageDialog(vindu,"Ingen registrering pga. feil i tallformat!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			return;
	    } 
		catch (ParseException e) 
	    {
	    	JOptionPane.showMessageDialog(vindu,"Ingen registrering pga. feil i dato, dd/mm/yyyy","Feilmelding", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Skriver ut info til kvitteringslista
		DecimalFormat decim = new DecimalFormat("0.00");
		total +=pris; 
		valgt = null;
		valgtindex = 0;
		String idtext = id+"";
		String type = "Sesongkort";
		
		if(id == -1)
		{
			idtext = "*";
			type += " + kc";
		}	
		
		String[] utSes={type, idtext,fnavn.toUpperCase(),decim.format(pris)+""};
		vindu.oppdateHandleliste(utSes);
	}

	/* Utfører betaling, og sender data fra handleobjektene
	   til aktivekort-lista, kunde-lista og keycard-lista.
	   Inn-parameteren brukes til å manipulere JTable handlelista */
	public void betal(DefaultTableModel model) 
	{
		if(!open) // sjekker at skift er åpent
		{
			JOptionPane.showMessageDialog(vindu,"Transaksjon kan ikke bli gjennomført ved lukket skift!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
		}
		if(handleliste.getLength()==0) // tom handleliste
		{
			JOptionPane.showMessageDialog(vindu,"Handleliste er tom!","Feilmelding", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
		}
		
		List<HandleObjekt> listeB = handleliste.getHandleliste();
		
		// Kjører for alle objektene i handelista
		for(HandleObjekt o : listeB)
		{
			Keycard kc;
			
			// Om keycard'et fra før
			if(keycardreg.keycardFins(o.getId())) // gammelt
			{
				kc = keycardreg.getKeycard(o.getId());	
			}	
			else // nytt
			{
				kc = new Keycard(keycardreg.getLastId()+1);
				keycardreg.leggTil(kc);
				adminstat.setKeycardAntall();
				adminstat.setKeycardPris(prisliste.getKEYCARD());
				adminstat.setTotalPris(prisliste.getKEYCARD());
			}
			
			if(o != null && (o.getKorttype() instanceof Klippekort))
			{
				adminstat.setAntallKlipp();
				adminstat.setTotalKlipp(o.getPris());
				Korttype kt = o.getKorttype();
				Klippekort kk = new Klippekort(o.getPris(), kt.getSalgDato(), kc.getId(), 10);
				aktivkortreg.leggTil(kk);
			}
			else if(o != null && (o.getKorttype() instanceof Dagskort))
			{
				adminstat.setAntallDag();
				adminstat.setTotalDag(o.getPris());
				Dagskort dk = new Dagskort(o.getPris(), o.getKorttype().getSalgDato(), kc.getId());
				aktivkortreg.leggTil(dk);
			}
			else if(o != null && (o.getKorttype() instanceof Sesongkort))
			{
				adminstat.setAntallASes();
				adminstat.setTotalSes(o.getPris());
				Kunde k = new Kunde(o.getFornavn(), o.getEtternavn(), o.getFodselsdato(), kc);
				
				// setter inn ny kunde, dersom personen er i registeret fra før
				if(!kundereg.finsKunde(k)) 
					kundereg.settInnKunde(o.getFornavn(), o.getEtternavn(), o.getFodselsdato(), kc);
				
				Sesongkort sk = new Sesongkort(o.getPris(), o.getKorttype().getSalgDato(), kc.getId());
				aktivkortreg.leggTil(sk);
			}
			
			adminstat.setTotalAntall();
			adminstat.setTotalPris(o.getPris());
		} // end of for
		
		handleliste = null;
		total = 0;
		handleliste = new HandleObjektListe();
		tempOldKeycard = new HandleObjektListe();
		
		// fjerner radene i handlelista
		for (int i = model.getRowCount() - 1; i > -1; i--)
		{
	        model.removeRow(i);
	    }
		
		JOptionPane.showMessageDialog(vindu, "Transaksjon ble gjennomført!", "Betaling", JOptionPane.INFORMATION_MESSAGE);
		vindu.oppdateHandleliste(null);
		adminstat.setAntallTransaksjoner();
		vindu.setCheckBox(false);
		skrivFil();
	}
	
	/* Sletter alle handleobjekter og gjør handlelista tom
	  Inn-parameteren brukes til å manipulere JTable handlelista */
	public void slettAltHandlevogn(DefaultTableModel model)
	{
		if(handleliste.getLength()==0)
			return;
		
		handleliste = null;
		handleliste = new HandleObjektListe();
		tempOldKeycard = null;
		tempOldKeycard = new HandleObjektListe();
		valgt = null;
		valgtindex = 0;
		total = 0;
		
		// fjerner radene i handlelista
		for (int i = model.getRowCount() - 1; i > -1; i--) 
		{
	        model.removeRow(i);
	    }
		vindu.oppdateHandleliste(null);
		vindu.setCheckBox(false);
	}
	
	/* Sletter det valgte handleobjektet fra handlelista
	   Inn-parameteren model brukes til å manipulere JTable handlelista
	   handleListeT er selve JTable'en */
	public void slettHandlevogn(DefaultTableModel model, JTable handleListeT) 
	{
		if(handleListeT.getSelectedColumn()==-1)
		{
			valgt = null;
			JOptionPane.showMessageDialog(vindu, " Du må velge noe å slette","", JOptionPane.ERROR_MESSAGE);
			vindu.oppdateHandleliste(null);
			return;
		}
		
		int rad = handleListeT.getSelectedRow();
		if(rad == -1)
			return;
		
		double pris = 0;
		// Henter pris fra valgt rad
		try
		{
			String text = (String) model.getValueAt(rad, 3);
			text = text.replace(",", ".");
			pris = Double.parseDouble(text);
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(vindu, "ERROR","", JOptionPane.ERROR_MESSAGE);
			return;
		}

		total -= pris;
		
		handleliste.removeElementOnIndex(handleListeT.getSelectedRow());
		model.removeRow(handleListeT.getSelectedRow());
		vindu.oppdateHandleliste(null);
	}
	
	/* Sjekker om string'en er id'en til et keycard,
	   og legger isåfall keycard'et i tempKeycard-lista */
	public void regKc(String sjekkKc) 
	{
		int id;
		try
		{
			id = Integer.parseInt(sjekkKc);
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(vindu, "Skriv inn en Kort-id.\nBare tall.","Feilmelding", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// sjekker at keycard fins og er ikke aktivt, og registrert i handlelista
		if(keycardreg.keycardFins(id) && !aktivkortreg.sjekkGyldig(id) 
				&& handleliste.finnHandleObjekt(id) == null 
				&& tempOldKeycard.finnHandleObjekt(id) == null)
		{
			Kunde k = kundereg.finnEierMedId(id);
			if(k == null)
				tempOldKeycard.settInn(id, null, "", "", null, prisliste.getKEYCARD());
			else
				tempOldKeycard.settInn(id, null, k.getFornavn(), k.getEtternavn(), k.getDato(), prisliste.getKEYCARD());
		}
		else if(!keycardreg.keycardFins(id))
			JOptionPane.showMessageDialog(vindu, "ID fins ikke","Feilmelding", JOptionPane.ERROR_MESSAGE);
		else if(aktivkortreg.sjekkGyldig(id))
			JOptionPane.showMessageDialog(vindu, "Keycard er fortsatt gyldig","Feilmelding", JOptionPane.ERROR_MESSAGE);
		else if(handleliste.finnHandleObjekt(id) != null || tempOldKeycard.finnHandleObjekt(id) != null)
			JOptionPane.showMessageDialog(vindu, "Keycard allerede lest inn","Feilmelding", JOptionPane.ERROR_MESSAGE);
		else
			JOptionPane.showMessageDialog(vindu, "Noe gikk galt","Feilmelding", JOptionPane.ERROR_MESSAGE);
		
		vindu.oppdateHandleliste(null);
		vindu.setSjekkKc("");
	}

	/* Oppretter nytt keycard. Dersom sjekkKC er en 
	   tallverdi vil så mange nye keycard bli lagt til */
	public void nyttKc(String sjekkKC)
	{
		if(sjekkKC.equals(""))
		{
			antallTempNewKeycard++;
		}
		else
		{
			try
			{
				int tall = Integer.parseInt(sjekkKC);
				if(tall > 0)
				{
					antallTempNewKeycard += tall;
				}
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(vindu,"Kun tall","Feilmelding", JOptionPane.ERROR_MESSAGE);
				vindu.oppdateHandleliste(null);
				return;
			}
		}
		vindu.setSjekkKc("");
		vindu.oppdateHandleliste(null);
	}
	
	/* Legger til info på årsoppgjøret
	   Inn-parameteren er textarea det skrives ut på */
	public JTextArea leggTilArsOppgjor(JTextArea inn)
	{
		JTextArea ut = inn;
		if(!database.getAdmin().sjekkDate())
			return ut;
		
		Date naa = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String naaDate = df.format(naa);
		String neste = "";
		neste += "\tDag:" + naaDate + "\n";
		neste += database.getAdmin().toStringNesteDag();
		database.getAdmin().setsisteDato();
		ut.append(neste);
		return ut;
	}
	
	/* blir brukt som et bindeledd mellom selgervindu og adminstat
	   for å kunne legge verdier i dagsoppgjor fra script-klassen */
	public void leggTilStartDag()
	{
		database.getAdmin().toStringNesteDag();
	}
	
	/* Leser inn det som er lagret i adminstat om dagsoppgjor
	   Inn-parameteren er textarea det skrives ut på */
	public JTextArea startArsOppgjor(JTextArea inn)
	{
		inn.removeAll();
		JTextArea ut = inn;
		ut.setText(database.getAdmin().setDagReg());
		return ut;
	}
	
	// Setter og returnerer verdier i et Calender-objekt
	private Calendar setCal(int timer,int min,int sek,int mili, Calendar cal)
	{
		cal.set(Calendar.HOUR_OF_DAY, timer);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sek);
        cal.set(Calendar.MILLISECOND, mili);
		return cal;
	}
	
	
	/**************get- og set-metoder***************/
	
	// Setter antall nye ubrukte/ikke-betalte keycard til "i"
	public void setAntallTempKc(int i)
	{
		antallTempNewKeycard = i;
	}
	
	// Henter antall nye ubrukte/ikke-betalte keycard
	public int getAntallTempKc()
	{
		return antallTempNewKeycard;
	}
	
	// Henter den totale summen i handlelista
	public double getTotalPris()
	{
		return total;
	}
	
	public void setTotalPris(double i)
	{
		total = i;
	}
	
	public int getValgtindex()
	{
		return valgtindex;
	}
	
	public void setValgtindex(int i)
	{
		valgtindex = i;
	}
	
	public HandleObjekt getValgt()
	{
		return valgt;
	}
	
	public void setValgt(HandleObjekt o)
	{
		valgt = o;
	}
	
	public MetodeRegister getMetoder()
	{
		return metoder;
	}
	
	public boolean getOpen()
	{
		return open;
	}
	
	public HandleObjektListe getTempKeycard()
	{
		return tempOldKeycard;
	}
	
	public KundeListe getKundeListe()
	{
		return kundereg;
	}
	
	public AdminStat getAdminStat()
	{
		return adminstat;
	}
	
	public AktiveKort getAktiveKort()
	{
		return aktivkortreg;
	}
	
	public KeycardListe getKeycardListe()
	{
		return keycardreg;
	}
	
	public PrisPanel getPrisPanel()
	{
		return prispanel;
	}
}