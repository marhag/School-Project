/*
 * Klassen inneholder:
 *  - GUI-elementer; paneler, knapper, textfelt, 
 *    textarea og JList
 *  - Kundeliste og liste over aktive-kort
 *  - Datafelter for kunder som vises i JList, 
 *    og enkelt-kunde som er valgt
 *  - Metoder for oppretting av GUI
 *  - Metoder for søking, endring og sletting av kunde
 *  - 
 *  
 *  Skrevet av Sondre og Martin
 * 
 * 		Sist endret: 07/05/13
 * 
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* Klassen oppretter GUI til kundebehandling-vinduet,
   og inneholder metodene til dette vinduet,
   som "søk", "endre", "fjern" osv. */
public class KundeBehandlingPanel implements ActionListener 
{
	private JPanel kundePanel, endreKundePanel;	
	private JButton finnKundeNavn, finnKundeId, endreKunde, slettKunde, 
					oppdaterKunde, autoSlettKunde, tilbakeKunde;
	private JTextField finnFelt, endreFNavn, endreENavn, endreFDato;
	private JTextArea info;
	private JList<String> kundeliste;
	
	private KundeListe kunder;
	private AktiveKort aktivekortreg;
	
	private boolean admin = false;
	
	private int valgtindex;
	private Kunde valgtkunde;
	Kunde[] kundertemp;
	String[] kundenavn;

	/* Oppretter klassen, lager GUI, og henter kundeliste
	   og aktive-kort registeret fra Database. 
	   ButtonMaker brukes til å generereknapper,
	   Selgervindu brukes som actionlistener */
	public KundeBehandlingPanel(Database database, ButtonMaker bm, SelgerVindu parent)
	{
		kunder = database.getKunde();
		aktivekortreg = database.getAktive();
		
		JPanel kundeknappPanel = new JPanel();
		JPanel finnkundeknappPanel = new JPanel();
		JPanel tilbakePanel = new JPanel();
		JPanel space = new JPanel();
		JPanel spaceMellom = new JPanel();
		JPanel spaceNede = new JPanel();
		
		space.add(Box.createRigidArea(new Dimension(180,10)));
		spaceMellom.add(Box.createRigidArea(new Dimension(180,5)));
		spaceNede.add(Box.createRigidArea(new Dimension(180,20)));
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		kundeknappPanel.setBorder(title);
		kundeknappPanel.setPreferredSize(new Dimension(200,400));
		
		kundeknappPanel.add(space);
		finnFelt = new JTextField("",13);
		kundeknappPanel.add(finnFelt);
		finnKundeNavn = bm.generateButton("Søk navn", finnkundeknappPanel, ButtonMaker.halvknapp, this);
		finnKundeId = bm.generateButton("Søk ID", finnkundeknappPanel, ButtonMaker.halvknapp, this);
		kundeknappPanel.add(finnkundeknappPanel);
		info = new JTextArea(6,15);
		info.setEditable(false);
		kundeknappPanel.add(info);
		endreKunde = bm.generateButton("Endre", kundeknappPanel, ButtonMaker.helknapp, this);
		slettKunde = bm.generateButton("Slett", kundeknappPanel, ButtonMaker.helknapp, this);		
		kundeknappPanel.add(spaceNede);
		oppdaterKunde = bm.generateButton("Oppdater Liste", kundeknappPanel, ButtonMaker.helknapp, this);
		autoSlettKunde = bm.generateButton("Auto-Fjern Ugyldige", kundeknappPanel, ButtonMaker.helknapp, this);
 		
		JPanel kundeKnapper = new JPanel();
		kundeKnapper.add(kundeknappPanel);
		
		tilbakeKunde = bm.generateButton("Tilbake", tilbakePanel, ButtonMaker.helknapp, parent);
		
		kundeliste = new JList<String>();
		kundeliste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		kundeliste.addListSelectionListener( new ListSelectionListener() {
		      public void valueChanged( ListSelectionEvent e) // Listelytter
		      {
		        if ( e.getValueIsAdjusting() && kundeliste == e.getSource())
		        {
		          int index = kundeliste.getSelectedIndex();
		          if(index > -1 && index < kundertemp.length)
		        	  visKunde(index);
		        }
		      }
		    });
		
		JPanel kundeListePanel = new JPanel();
		kundeListePanel.setPreferredSize(new Dimension(500,400));

		JScrollPane scrollkunde = new JScrollPane(kundeliste);
		scrollkunde.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollkunde.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollkunde.setPreferredSize(new Dimension(385,400));
		kundeListePanel.add(scrollkunde);
		
		JPanel kundePanelInni = new JPanel();
		BorderLayout kundeLayout = new BorderLayout();
		kundePanelInni.setLayout(kundeLayout);
		kundePanelInni.add(tilbakePanel, BorderLayout.PAGE_END);
		kundePanelInni.add(kundeListePanel, BorderLayout.CENTER);
		kundePanelInni.add(kundeKnapper, BorderLayout.LINE_END);
		kundePanelInni.setBorder(title);
		kundePanelInni.setPreferredSize(new Dimension(600,450));
		
		kundePanel = new JPanel();
		kundePanel.setPreferredSize(new Dimension(900,500));
		kundePanel.add(kundePanelInni);
		
		endreKundePanel();
	}
	
	// Lager panelet for endring av kunde
	private void endreKundePanel()
	{
		endreKundePanel = new JPanel();
		endreKundePanel.setPreferredSize(new Dimension(200,170));
		
		endreFNavn = new JTextField("",13);
		endreENavn = new JTextField("",13);
		endreFDato = new JTextField("dd/mm/yyyy",13);
		JLabel fnavnlabel = new JLabel("Fornavn:");
		JLabel enavnlabel = new JLabel("Etternavn:");
		JLabel fdatolabel = new JLabel("Fødselsdato:");
		endreKundePanel.add(fnavnlabel);
		endreKundePanel.add(endreFNavn);
		endreKundePanel.add(enavnlabel);
		endreKundePanel.add(endreENavn);
		endreKundePanel.add(fdatolabel);
		endreKundePanel.add(endreFDato);
	}
	
	/* Henter tilbake-knappen. ActionListener for 
	   "tilbake"-knappen ligger i en annen klasse */
	public JButton getTilbakeKnapp() 
	{
		return tilbakeKunde;
	}
	
	// Henter kundepanelet
	public JPanel getPanel()
	{
		return kundePanel;
	}
	
	// Finner kunde med id fra textfeltet finnFelt
	private void finnKundeId() 
	{
		kundertemp = new Kunde[1];
		kundertemp[0] = kunder.finnEierMedIdString(finnFelt.getText());
		String[] ut = {""};
		if(kundertemp[0] == null)
		{
			ut[0] = "Ingen kunder med gitt id";
			kundeliste.setListData(ut);
			info.setText("");
		}
		else
		{
			ut[0] = kundertemp[0].toString();
			kundeliste.setListData(ut);
			visKunde(0);
		}
		
		kundeliste.requestFocus();
		kundeliste.setSelectedIndex(0);
	}
	
	//Finner kunde med navn fra textfeltet finnFelt. 
	private void finnKundeNavn()
	{
		kundertemp = kunder.finnKunderNavn(finnFelt.getText());
		kundeListe();

		if(kundertemp.length == 0)
		{
			String[] ut = new String[1];
			ut[0] = "Ingen kunder med gitt navn";
			kundeliste.setListData(ut);
			info.setText("");
		}
		else
			visKunde(0);
		
		kundeliste.requestFocus();
		kundeliste.setSelectedIndex(0);
	}
	
	// Viser informasjon om kunde på index fra parameteren
	private void visKunde(int index)
	{
		valgtindex = index;
		valgtkunde = kundertemp[valgtindex];

		if(valgtkunde == null)
			return;
	
		String ut = "";
		
		ut = valgtkunde.toString() + "\nFødselsdato: " + formatDate(valgtkunde.getDato()) + "\n*****************************" + "\n" + aktivekortreg.toStringVisKunde(valgtkunde.getKeycard().getId());
		info.setText(ut);
	}
	
	/* Åpner et panel i JOptionPane, der brukeren kan endre valgt 
	   kunde. Brukeren kan kun endre kunde om han er admin. 
	   Feilmelding blir gitt dersom brukeren ikke er admin */
	private void endreKunde()
	{
		if(!admin)
		{
			JOptionPane.showMessageDialog(null, "Du har ikke rettigheter til å gjøre endringer!");
			return;
		}

		if(valgtkunde == null)
			return;
		
		endreFNavn.setText(valgtkunde.getFornavn());
		endreENavn.setText(valgtkunde.getEtternavn());
		endreFDato.setText(formatDate(valgtkunde.getDato()));
		
		int svar = JOptionPane.showConfirmDialog(null, endreKundePanel,
		        "Endre kunde", JOptionPane.OK_CANCEL_OPTION);
		
		if(svar == JOptionPane.OK_OPTION)
		{
			try
			{
				String fnavn = endreFNavn.getText();
				String enavn = endreENavn.getText();

				DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
				String datoB = endreFDato.getText();
				Date fdato = sourceFormat.parse(datoB);
				
				if((kunder.finnKunderNavn(fnavn + " " + enavn).length != 0) && valgtkunde.getDato().equals(fdato))
					return;
				
				kunder.endreEier(valgtkunde, fnavn, enavn, fdato);
			}
			catch ( NumberFormatException e )
		    {
				JOptionPane.showMessageDialog(null, "Feil i tallformat");
				return;
		    } 
			catch (ParseException e) 
		    {
				JOptionPane.showMessageDialog(null, "Feil i datoformat");
				return;
			}
		}
		
		info.setText("");
		oppdater();
	}
	
	/* Fjerner valgt kunde dersom brukeren er admin, 
	   ellers får brukeren en feilmelding */
	private void fjernKunde() 
	{
		if(!admin)
		{
			JOptionPane.showMessageDialog(null, "Du har ikke rettigheter til å gjøre endringer!");
			return;
		}
		
		if(valgtkunde == null)
			return;
		
		int svar = JOptionPane.showConfirmDialog(null, "Er du sikker på at du vil slette \"" 
					+ valgtkunde.toString() + 
					"\" fra kunderegisteret?\nNB! Dette vil fjerne alle aktive kort registrert på kunden!", 
					"Sletting av kunde", JOptionPane.YES_NO_OPTION);
		
		if (svar == JOptionPane.YES_OPTION) 
		{
			int id = valgtkunde.getKeycard().getId();
			aktivekortreg.fjern(id);
			kunder.getKunder().remove(kunder.finnEierMedId(id));
			valgtindex = -1;
			valgtkunde = null;
		}
		
		info.setText("");
		oppdater();
	}
	
	/* Fjerner alle kunder som ikke har et aktivt kort
	   Brukeren må være admin, hvis ikke får han feilmelding */
	private void autoFjern()
	{
		if(!admin)
		{
			JOptionPane.showMessageDialog(null, "Du har ikke rettigheter til å gjøre endringer!");
			return;
		}
		int svar = JOptionPane.showConfirmDialog(null, "Dette vil slette ALLE kunder med UGYLDIGE keycards?\n" +
				"Vil du virkelig fortsette?", 
				"Sletting av kunder med ugyldige-heiskort", JOptionPane.YES_NO_OPTION);
		
		if(svar == JOptionPane.YES_OPTION)
			kunder.fjernUgyldige(aktivekortreg);
		
		oppdater();
	}
	
	// Viser kunder som har fått søketreff i JList'en kundeliste
	private void kundeListe()
	{
		kundenavn = new String[kundertemp.length];
		for(int i = 0; i < kundertemp.length; i++)
			kundenavn[i] = kundertemp[i].toString();
		
		kundeliste.setListData(kundenavn);
	}
	
	// Gjennoppretter kundelista. Alle registrerte kunder vises
	private void gjenopprettListe()
	{
		kundenavn = kunder.toStringArray();
		kundertemp = new Kunde[kunder.getSize()];
		
		for(int i = 0; i < kunder.getSize(); i++)
			kundertemp[i] = kunder.getKundeFromIndex(i);
	}
	
	/* Mottar kundelista, liste over aktive kort og admin, 
	   gjennoppretter lista over kunder */
	public void oppdaterData(KundeListe k, AktiveKort a, boolean ad)
	{
		kunder = k;
		aktivekortreg = a;
		admin = ad;
		gjenopprettListe();
	}
	
	// Viser alle kunder i JList'en kundeliste
	public void oppdater()
	{
		gjenopprettListe();
		kundeliste.setListData(kundenavn);
	}
	
	// Formaterer dato, så den blir mer leslig
	public String formatDate(Date date)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String ut = df.format(date);
		return ut;
	}
	
	// Blanker ut søkefeltet
	public void blankUtFinnFelt()
	{
		finnFelt.setText("");
	}

	// Setter JList'en til inn-parameteren
	public void setListData(String[] stringArray) 
	{
		kundeliste.setListData(stringArray);
	}
	
	// ActionListener for knapper i kundebehandling
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == finnKundeNavn)
			finnKundeNavn();
		if(e.getSource() == finnKundeId)
			finnKundeId();
		if(e.getSource() == endreKunde)
			endreKunde();
		if(e.getSource() == slettKunde)
			fjernKunde();
		if(e.getSource() == oppdaterKunde)
			oppdater();
		if(e.getSource() == autoSlettKunde)
			autoFjern();
	}
}