/*
 * Klassen inneholder:
 *  - Statistikk for administrator
 *  - set- og get-metoder for statistikk
 *  - toString for statistikk
 * 
 * Lagd av Martin og Sondre
 */

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

// Klassen lagrer statistikk som vises i statistikkvindu'et
public class AdminStat implements Serializable
{
	private int antallTransaksjoner = 0;
	private int totalAntall = 0;
	private int TotalantallKlipp = 0;
	private int TotalantallDag = 0;
	private int TotalantallSes = 0;
	private int TotalantallKort = 0;
	private int TotalKeycard = 0;
	private int keycardTotal = 0;
	private int antallKlipp = 0;
	private int antallDag = 0;
	private int antallSes = 0;
	private int antallKontroller = 0;
	private int antallKlippTurer = 0;
	private int antallDagTurer = 0;
	private int antallSesTurer = 0;
	private int toaltAntallTrans = 0;
	private int totaltAntallVarer = 0;
	private double totalPris = 0.00;
	private double totalKlipp = 0.0;
	private double totalDag = 0.0;
	private double totalSes = 0.0;
	private double keycardPris = 0.0;
	private double totalt = 0;
	private double totalKlippTotal = 0;
	private double totalDagTotal = 0;
	private double totalSesTotal = 0;
	private double totalKcTotal = 0;
	
	private List<String> dagsreg;
	private int dag = 0;
	private Date sisteDato;
	
	private static final long serialVersionUID = 10020L;
	
	// Kontruktør til klassen
	public AdminStat()
	{
		dagsreg = new LinkedList<>();
		//gir kunstige startverider til turer
		if(dagsreg.isEmpty())
		{
			antallKlippTurer = 55;
			antallDagTurer = 19;
			antallSesTurer = 20;
			antallKontroller = 94;
		}	
	}
	
	// Sjekker om sisteDato er idag
	public Boolean sjekkDate()
	{
		Date naa = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String naaDate = df.format(naa);
		
		if(sisteDato!=null)
		{
			String sisteDate = df.format(sisteDato);
			if(naaDate.equals(sisteDate))
				return false;
		}
		return true;
	}
	
	// Skriver ut statistikk-informasjon
	public String toString()
	{
		DecimalFormat decim = new DecimalFormat("0.00");
		String ut = "";
		ut+="\tSalgstall idag\n\n" +
				"********************************************************************\n";
		ut+=" Antall transaksjoner: "+ antallTransaksjoner+" \tTotalt: " + toaltAntallTrans+antallTransaksjoner +" \n\n";
		ut+=" Antall solgte varer: " + totalAntall + "\t Total sum kjøpt for: "+decim.format( totalPris) + " ,-" +
				"\n Totalt varer: "+ (totaltAntallVarer+totalAntall)+"\t Totalt alle dager: "+decim.format(totalt+totalPris) + " ,-\n\n\n";
		ut+=" Salg:\n\n";
		ut+=" Klippekort: " + decim.format(totalKlipp) + " ,-\t Totalt: "+decim.format(totalKlippTotal+totalKlipp) + " ,-\n";
		ut+=" Dagskort: " + decim.format(totalDag) + " ,-\t Totalt: "+decim.format(totalDagTotal+totalDag) + " ,-\n";
		ut+=" Sesongkort: " + decim.format(totalSes) + " ,-\t Totalt: "+decim.format(totalSesTotal+totalSes) + " ,-\n";
		ut+=" Keycard: " + decim.format(keycardPris) + " ,-  \t Totalt: "+decim.format(totalKcTotal+keycardPris) + " ,-\n" +
				"********************************************************************\n";
		return ut;
	}
	
	// Skriver ut informasjon for neste dag
	public String toStringNesteDag()
	{
		String ut = "";
		DecimalFormat decim = new DecimalFormat("0.00");
		ut+=" Antall transaksjoner: "+ antallTransaksjoner+" \n\n";
		ut+=" Antall solgte varer: " + totalAntall + "\t Total sum kjøpt for: "+ decim.format(totalPris) + " ,-\n\n\n";
		ut+=" Salg:\n\n";
		ut+=" Klippekort: " + decim.format(totalKlipp) + " ,-" + "\tAntall: " +antallKlipp +"\n";
		ut+=" Dagskort: " + decim.format(totalDag) + " ,-" + "\tAntall: " +antallDag +"\n";
		ut+=" Sesongkort: " + decim.format(totalSes) + " ,-" + "\tAntall: " +antallSes +"\n";
		ut+=" Keycard: " + decim.format(keycardPris) + " ,-\n"
				+"********************************************************************\n";
		dagsreg.add(ut);
		stilltall();
		return ut;
	}
	
	// Tilbakestiller all statistikk for ny dag
	private void stilltall()
	{
		totalt += totalPris;
		toaltAntallTrans+=antallTransaksjoner;
		totaltAntallVarer+=totalAntall;
		totalKlippTotal+=totalKlipp;
		totalDagTotal+=totalDag;
		totalSesTotal+=totalSes;
		totalKcTotal+=keycardPris;
		TotalantallKlipp += antallKlipp;
		TotalantallDag += antallDag;
		TotalantallSes += antallSes;
		TotalantallKort += totalAntall;
		TotalKeycard += keycardTotal;
		antallTransaksjoner = 0;
		totalAntall = 0;
		totalPris = 0.0;
		keycardTotal = 0;
		antallKlipp = 0;
		antallDag = 0;
		antallSes = 0;
		totalKlipp = 0.0;
		totalDag = 0.0;
		totalSes = 0.0;
		keycardPris = 0.0;
	}
	
	// Henter informasjon fra dagsreg
	public String setDagReg()
	{
		String ut ="";
		if(dagsreg!=null)
		{
			for(String o : dagsreg)
			{
				ut += o;
			}
		}
		return ut;
	}
	
	// øker og returnerer dag
	public int getDag()
	{
		dag++;
		return dag;
	}
	
	// Setter siste dag
	public void setsisteDato()
	{
		sisteDato = new Date();
	}
	
	// Øker antall transaksjoner med 1
	public void setAntallTransaksjoner()
	{
		antallTransaksjoner++;
	}
	
	// Øker antall turer med klippekort med 1
	public void setAntallKlippTurer()
	{
		antallKlippTurer++;
	}
	
	// Øker antall turer med dagskort med 1
	public void setAntallDagTurer()
	{
		antallDagTurer++;
	}
	
	// Øker antall turer med sesongkort med 1
	public void setAntallSesTurer()
	{
		antallSesTurer++;
	}
	
	// Øker antall det totale antall turer med 1
	public void setTotalAntall()
	{
		totalAntall++;
	}
	
	public void setAntallKlipp()
	{
		antallKlipp++;
	}
	
	public void setAntallDag()
	{
		antallDag++;
	}
	
	public void setAntallASes()
	{
		antallSes++;
	}
	
	public void setTotalKontroller()
	{
		antallKontroller++;
	}
	
	public void setKeycardAntall()
	{
		keycardTotal++;
	}
	
	public void setTotalPris(double inn)
	{
		totalPris += inn;
	}
	
	public void setTotalKlipp(double inn)
	{
		totalKlipp += inn;
	}
	
	public void setTotalDag(double inn)
	{
		totalDag += inn;
	}
	
	public void setKeycardPris(double inn)
	{
		keycardPris += inn;
	}
	
	public void setTotalSes(double inn)
	{
		totalSes += inn;
	}
	
	public int getTotalAntall()
	{
		return totalAntall;
	}
	
	public int getAntallKlipp()
	{
		return antallKlipp;
	}
	
	public int getAntallDag()
	{
		return antallDag;
	}
	
	public int getAntallASes()
	{
		return antallSes;
	}
	
	public int getTotalKontroller()
	{
		return antallKontroller;
	}
	
	public double getTotalPris()
	{
		return totalPris;
	}
	
	public double getTotalKlipp()
	{
		return totalKlipp;
	}
	
	public double getTotalDag()
	{
		return  totalDag;
	}
	
	public double getTotalSes()
	{
		return totalSes;
	}
	
	public int getAntallKlippTurer()
	{
		return antallKlippTurer;
	}
	
	public int getAntallDagTurer()
	{
		return antallDagTurer;
	}
	
	public int getAntallSesTurer()
	{
		return antallSesTurer;
	}
	
	public int getAntallKc()
	{
		return keycardTotal;
	}
	
	public double getKeycardPris()
	{
		return keycardPris;
	}
	
	public int getAntallTransaksjoner()
	{
		return antallTransaksjoner;
	}
	
	public int getTotalKeycard()
	{
		return TotalKeycard + keycardTotal;
	}
	
	public int getTotalantallKlipp()
	{
		return TotalantallKlipp + antallKlipp;
	}
	
	public int getTotalantallDag()
	{
		return TotalantallDag + antallDag;
	}
	
	public int getTotalantallSes()
	{
		return TotalantallSes + antallSes;
	}
	
	public int getTotalantallKort()
	{
		return TotalantallKort + totalAntall;
	}
}