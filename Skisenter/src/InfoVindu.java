/*
 * Klassen inneholder:
 *  - GUI elementer; paneler og knapper
 *  - Prisliste
 *  - metoder for å sette priser
 * 
 * Lagd av Martin
 * 
 * Sist endret: 08/05/13
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;

// Klassen representerer nettsiden til Skiheisen
public class InfoVindu extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1005L;
	private ButtonMaker buttonMaker = new ButtonMaker(this);
	private JPanel innhold,panel,startSide, nyhetPanel, omOssPanel, priserPanel
		,parkenPanel,kontaktPanel,nyTabell;
	private JButton forside,nyheter,omOss,priser,parken,kontakt;
	private Priser prisliste;
	private double prisKlippVoksen,prisBarnKlipp, prisKlippHonnor, prisDagVoksen, prisDagBarn,
	prisDagHonnor, prisVoksenSes,prisBarnSes,prisHonnorSes,rabattSes,rabattDag,rabattKlipp,keycard;
	
	/* Oppretter rammen til nettsiden og setter i gang
	   andre metoder for å lage panelene til nettsiden.
	   Prislista hentes fra inn-parameteren "database"  */
	public InfoVindu(Database database)
	{
		prisliste = database.getPriser();
		
		JPanel omriss = new JPanel();
		innhold = new JPanel();
		omriss.setBackground(Color.LIGHT_GRAY);
		omriss.setPreferredSize(new Dimension(1600,800));
		
		JPanel siden = new JPanel();
		siden.setPreferredSize(new Dimension(800,620));
		siden.setBackground(Color.black);
		BorderLayout sideLayout = new BorderLayout(1,1);
		siden.setLayout(sideLayout);
		omriss.add(siden);
		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(785,165));
		topPanel.setBackground(Color.black);
		
		JPanel logoPanel = new JPanel();
		logoPanel.setPreferredSize(new Dimension(785,100));
		logoPanel.setBackground(Color.black);
		ImageIcon icon = new ImageIcon(
				getClass().getResource("bilder/logoStor.gif")); 
		JLabel label = new JLabel(); 
		label.setIcon(icon);
		logoPanel.add(label);
		
		JPanel knapperPanel = new JPanel();
		knapperPanel.setPreferredSize(new Dimension(785,50));
		knapperPanel.add(knapper());
		
		prisKlippVoksen = prisliste.getKLIPPVOKSEN();
		prisBarnKlipp = prisliste.getKLIPPBARN();
		prisKlippHonnor = prisliste.getKLIPPHONNOR();
		prisDagVoksen = prisliste.getDAGVOKSEN();
		prisDagBarn = prisliste.getDAGBARN();
		prisDagHonnor = prisliste.getDAGHONNOR();
		prisVoksenSes = prisliste.getSESVOKSEN();
		prisBarnSes = prisliste.getSESBARN();
		prisHonnorSes = prisliste.getSESHONNOR();
		keycard = prisliste.getKEYCARD();
		rabattKlipp = prisliste.getRABATTKLIPPKORT();
	    rabattDag = prisliste.getRABATTDAGKORT();
	    rabattSes = prisliste.getRABATTSESONGKORT();
		
		topPanel.add(logoPanel);
		topPanel.add(knapperPanel);
		startSiden();
		nyhetPanel();
		omOss();
		priser();
		parken();
		kontakt();
		Oppdate(startSide);
		siden.add(topPanel,BorderLayout.PAGE_START);
		siden.add(innhold,BorderLayout.CENTER);
			
		add(omriss);
	}
	
	// Henter priser inn-parameteren, av typen double-array
	public void oppdatePriser(double array[])
	{
		prisKlippVoksen = array[0];
		prisBarnKlipp = array[1];
		prisKlippHonnor = array[2];
		prisDagVoksen = array[3];
		prisDagBarn = array[4];
		prisDagHonnor = array[5];
		prisVoksenSes = array[6];
		prisBarnSes = array[7];
		prisHonnorSes = array[8];
		keycard = array[9];
		rabattKlipp = array[10];
	    rabattDag = array[11];
	    rabattSes =array[12];
	}
	
	// Tilbakestiller prisene
	public void resetPriser()
	{
		prisKlippVoksen = prisliste.getKLIPPVOKSEN();
		prisBarnKlipp = prisliste.getKLIPPBARN();
		prisKlippHonnor = prisliste.getKLIPPHONNOR();
		prisDagVoksen = prisliste.getDAGVOKSEN();
		prisDagBarn = prisliste.getDAGBARN();
		prisDagHonnor = prisliste.getDAGHONNOR();
		prisVoksenSes = prisliste.getSESVOKSEN();
		prisBarnSes = prisliste.getSESBARN();
		prisHonnorSes = prisliste.getSESHONNOR();
		keycard = prisliste.getKEYCARD();
		rabattKlipp = 1;
	    rabattDag = 1;
	    rabattSes = 1;
	}
	
	// Oppretter et JTable for priser, og setter det i et JPanel
	public void setPriser()
	{
		nyTabell.removeAll();
		String[] kolonnenavn =
			  {
			    "Type","" , "Voksen", "Barn", "Honnør"
			  };
		 Object[][] celler =
			  {
				{
					"","","","",""
				},
			    {
			      "Klippekort", "",prisKlippVoksen , prisBarnKlipp, prisKlippHonnor
			    },
			    {
			      "Dagskort", "",prisDagVoksen , prisDagBarn, prisDagHonnor
			    },
			    {
			      "Sesonkort", "", prisVoksenSes, prisBarnSes, prisHonnorSes
			    },
			    {
			    	"Keycard","",keycard,keycard,keycard
			    }
			  };
		JTable tabell = new JTable(celler, kolonnenavn);
	    tabell.setEnabled(false);
	    JScrollPane tabellScroll = new JScrollPane(tabell);
	    nyTabell.add(tabellScroll);
	    nyTabell.setPreferredSize(new Dimension(700,120));
	    
	    String[] kolonneRabatt = {"Klippekort","Dagskort","Sesongkort"};
	    Object[][] cellerRabatt =
			{
				{
					"-"+new DecimalFormat("##.##").format((1-rabattKlipp)*100) + "%",
					"-"+new DecimalFormat("##.##").format((1-rabattDag)*100) + "%",
					"-"+new DecimalFormat("##.##").format((1-rabattSes)*100) + "%"
				}
			};
	    panel.removeAll();
	    panel.add(new JLabel("Rabatter:"));
	    JTable tabell2 = new JTable(cellerRabatt, kolonneRabatt);
	    tabell2.setEnabled(false);
	    JScrollPane tabellScroll2 = new JScrollPane(tabell2);
	    tabellScroll2.setPreferredSize(new Dimension(520,40));
	    panel.add(tabellScroll2);
	    panel.setPreferredSize(new Dimension(700,50));
	}
	
	// Oppretter et panel for "Kontakt oss" på nettsiden
	private void kontakt() 
	{
		kontaktPanel = new JPanel();
		kontaktPanel.setPreferredSize(new Dimension(600,500));
		JLabel kontaktOver = new JLabel("Kontakt oss");
		kontaktOver.setFont(new Font("sansserif", Font.BOLD, 25));
		kontaktPanel.add(kontaktOver);
		JPanel ny = new JPanel();
		ny.setPreferredSize(new Dimension(500,50));
		kontaktPanel.add(ny);
		kontaktPanel.add(new JLabel("<html>Hvis du lurer på noe, kontat oss på: <br>" +
				"<br>" +
				"Telefon:  &nbsp &nbsp &nbsp &nbsp &nbsp 69922112<br>" +
				"Epost:   &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp konatk@hobolskisenter.no<br><br>" +
				"Eller besøk oss!<br>" +
				"Adressen< er 1827 Hobøl, DerBorteEtSted<br>" +
				"Vi ligger kun 30 min unna Oslo!</html>"));
		ImageIcon icon = new ImageIcon(
				getClass().getResource("bilder/telefon.gif")); 
		JLabel label = new JLabel(); 
		label.setIcon(icon);
		JPanel ny2 = new JPanel();
		ny2.setPreferredSize(new Dimension(500,50));
		kontaktPanel.add(ny2);
		kontaktPanel.add(label);
	}
	
	// Oppretter et panel for "Parken" på nettsiden
	private void parken() 
	{
		parkenPanel = new JPanel();
		parkenPanel.setPreferredSize(new Dimension(600,500));
		JLabel parkenOver = new JLabel("Parken");
		parkenOver.setFont(new Font("sansserif", Font.BOLD, 25));
		JLabel kartOver = new JLabel("Løypekart for Hobøl Skisenter");
		kartOver.setFont(new Font("sansserif", Font.BOLD, 15));
		parkenPanel.add(parkenOver);
		JPanel ny = new JPanel();
		ny.setPreferredSize(new Dimension(500,50));
		parkenPanel.add(ny);
		parkenPanel.add(kartOver);
		ImageIcon icon = new ImageIcon(
				getClass().getResource("bilder/loypeKart.gif")); 
		JLabel label = new JLabel(); 
		label.setIcon(icon);
		parkenPanel.add(label);
	}
	
	// Oppretter et panel for "Prisliste" på nettsiden
	private void priser() 
	{
		priserPanel = new JPanel();
		priserPanel.setPreferredSize(new Dimension(655,500));
		JLabel prisene = new JLabel("Prisliste Hobøl Skisenter 2013-2014");
		prisene.setFont(new Font("sansserif", Font.BOLD, 25));
		priserPanel.add(prisene);
		nyTabell = new JPanel();
	    JPanel space = new JPanel();
	    space.setPreferredSize(new Dimension(700,50));
	    JPanel space2 = new JPanel();
	    space2.setPreferredSize(new Dimension(700,30));
	    priserPanel.add(space);
	    priserPanel.add(new JLabel("<html>Hos Hobøl Skisenter får du alltid de beste prisene for de beste" +
	    		" opplevelsene!<br><br>" +
	    		"Kjøp i høytider gir 10% rabatt, og skolebesøk før 15% rabatt! Gjelder ikke sesongkort<br>" +
	    		"Ved kjøp av sesonkort lagres personopplysninger, etter kortet er utgått <br>kan det" +
	    		"fylles på med kort av alle typer.<br>Vi anbefaler å ta vare på keycardene" +
	    		" da disse kan brukes flere ganger <br>og er nødvendig for å kjøpe billett.</html>"));
	    space.setPreferredSize(new Dimension(700,50));
	    priserPanel.add(space2);
	    priserPanel.add(nyTabell);
	    JPanel nyPanel = new JPanel();
	    panel = new JPanel();
	    nyPanel.add(panel);
	    priserPanel.add(nyPanel);
	}
	
	// Oppretter et panel for "Om oss" på nettsiden
	private void omOss() 
	{
		omOssPanel = new JPanel();
		omOssPanel.setPreferredSize(new Dimension(600,500));
		JLabel omossOverskrift = new JLabel("Om oss");
		omossOverskrift.setFont(new Font("sansserif", Font.BOLD, 25));
		omOssPanel.add(omossOverskrift);
		JPanel ny = new JPanel();
		ny.setPreferredSize(new Dimension(600,40));
		omOssPanel.add(ny);
		omOssPanel.add(new JLabel("<html>Vi er et nyopprettet skisenter, men har masse av erfaring fra tidligere jobber <br>" +
				"Vi har store abisjoner, og forvetner å bli verdenkjent innen 2014 for våre flotte bakker,<br> høye service og vårt flotte datasystem!<br><br>" +
				"Vårt skisenter tilbyr også gratis langrennsløyper, og dette er noe vi vil fortsette med!<br><br>" +
				"Vi håper dere får en positiv opplevele hos oss, og noen uforglemmelige skiturer! " +
				"<br>Du finner oss i Hobøl kommune, 30 minutter utenfor Oslo. " +
				"Anlegget opner kl 08.00, og <br>stenger kl 17.30 på vanlige dager. Vi har også<br> kveldskjøring i påsken</hmtl>"));
		ImageIcon icon = new ImageIcon(
				getClass().getResource("bilder/omOssBilde.gif")); 
		JLabel label = new JLabel(); 
		label.setIcon(icon);
		JPanel ny2 = new JPanel();
		ny2.setPreferredSize(new Dimension(600,20));
		omOssPanel.add(ny2);
		omOssPanel.add(label);
	}
	
	// Oppretter og returnerer et panel med knappene på nettsiden
	private JPanel knapper()
	{
		JPanel knappene = new JPanel();
		Dimension knapperDim = new Dimension(100,33);
		forside = buttonMaker.generateButton("Forside",knapperDim);
		 nyheter = buttonMaker.generateButton("Nyheter",knapperDim);
		omOss = buttonMaker.generateButton("Om oss",knapperDim);
		priser = buttonMaker.generateButton("Priser",knapperDim);
		parken = buttonMaker.generateButton("Parken",knapperDim);
		kontakt = buttonMaker.generateButton("Kontakt oss",knapperDim);
		knappene.add(forside);
		knappene.add(omOss);
		knappene.add(priser);
		knappene.add(parken);
		knappene.add(kontakt);
		
		return knappene;
	}
	
	// Oppretter et panel for velkomst-siden til nettsiden
	private void startSiden() 
	{
		startSide = new JPanel();
		startSide.setPreferredSize(new Dimension(785,500));
		JLabel velkommen = new JLabel("Velkommen til Hobøl Skisenter");
		velkommen.setFont(new Font("sansserif", Font.BOLD, 32));
		startSide.add(velkommen);
		ImageIcon icon = new ImageIcon(
				getClass().getResource("bilder/skisenterBilde.gif")); 
		JLabel label = new JLabel(); 
		label.setIcon(icon);
		startSide.add(label);
		startSide.add(new JLabel("<html>Vi åpner endelig skisenter i" +
				" Hobøl! Vinter 20|13 åpner vi parken <br>" +
				" &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp" +
				" &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp" +
				" PS: bildet kan lyve litt...</html>"));
	}
	
	// Oppretter et JPanel for "Nyheter" på nettsiden
	private void nyhetPanel()
	{
		nyhetPanel = new JPanel();
		JLabel nyheterOverskrift = new JLabel("Nyheter");
		nyheterOverskrift.setFont(new Font("sansserif", Font.BOLD, 25));
		nyhetPanel.add(nyheterOverskrift);
		
	}
	
	/* Hånterer hvilket panel (underside) som skal vises. Inn-
	   parameteren er panelet som skal vises, alle andre paneler lukkes. */
	public void Oppdate(Component panel)
	{
		innhold.removeAll();
		innhold.add(panel);
		innhold.updateUI();
		revalidate();
	}
	
	// ActionListener som håndterer knappe-trykk på nettsiden
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==forside)
			Oppdate(startSide);
		if(e.getSource()==nyheter)
			Oppdate(nyhetPanel);
		if(e.getSource()==priser)
		{
			Oppdate(priserPanel);
			setPriser();
		}
		if(e.getSource()==omOss)
			Oppdate(omOssPanel);
		if(e.getSource()==parken)
			Oppdate(parkenPanel);
		if(e.getSource()==kontakt)
			Oppdate(kontaktPanel);
		((AbstractButton) e.getSource()).setFocusPainted(false);
	}
}