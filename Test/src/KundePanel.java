import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class KundePanel extends JFrame
{	
	private JPanel kundePanel, kundeknappPanel, finnkundeknappPanel;
	private JButton tilbake, finnKundeNavn, finnKundeId, endreKunde, slettKunde, oppdaterKunde, autoSlettKunde;
	private JTextField finnFelt;
	
	private JList kundeliste;
	
	private Dimension helknapp, halvknapp;
	
	public KundePanel()
	{
		super("Kundebehandling");
		
		helknapp = new Dimension(200, 30);
		halvknapp = new Dimension(10, 30);
		
		finnkundeknappPanel = new JPanel();
		GridLayout kundeknappLayout = new GridLayout(1,2,5,5);
		finnkundeknappPanel.setLayout(kundeknappLayout);
		finnkundeknappPanel.setSize(50, 50);
		
		finnKundeNavn = new JButton("Søk navn");
		finnKundeId = new JButton("Søk ID");
		finnKundeNavn.setPreferredSize(halvknapp);
		finnKundeId.setPreferredSize(halvknapp);
		finnkundeknappPanel.add(finnKundeNavn);
		finnkundeknappPanel.add(finnKundeId);
		
		kundeknappPanel = new JPanel();
		GridLayout kundeknapp = new GridLayout(6,1,5,5);
		kundeknappPanel.setLayout(kundeknapp);
		
		finnFelt = new JTextField();
		endreKunde = new JButton("Endre");
		slettKunde = new JButton("Slett");
		oppdaterKunde = new JButton("Oppdater liste");
		autoSlettKunde = new JButton("Auto-fjern ugyldige");
		finnFelt.setPreferredSize(helknapp);
		endreKunde.setPreferredSize(helknapp);
		slettKunde.setPreferredSize(helknapp);
		oppdaterKunde.setPreferredSize(helknapp);
		autoSlettKunde.setPreferredSize(helknapp);
		kundeknappPanel.add(finnFelt);
		kundeknappPanel.add(finnkundeknappPanel);
		kundeknappPanel.add(endreKunde);
		kundeknappPanel.add(slettKunde);
		kundeknappPanel.add(oppdaterKunde);
		kundeknappPanel.add(autoSlettKunde);
		
		
		tilbake = new JButton("Tilbake");
		tilbake.setPreferredSize(helknapp);
		kundeliste = new JList();
		kundeliste.setPreferredSize(new Dimension(300,200));
		
		JScrollPane scrollkunde = new JScrollPane(kundeliste);
		scrollkunde.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		kundePanel = new JPanel();
		BorderLayout kundeLayout = new BorderLayout();
		kundePanel.setLayout(kundeLayout);
		
		kundePanel.add(tilbake, BorderLayout.PAGE_START);
		kundePanel.add(kundeliste, BorderLayout.LINE_START);
		kundePanel.add(kundeknappPanel, BorderLayout.LINE_END);
		
		Container c = getContentPane();
		c.add(kundePanel);
		setSize(600, 500);
		setVisible(true);
	}
	
	/*public static void main(String[] args)
	{
		new KundePanel();
	}*/
}
