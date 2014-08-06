/*
 * Klassen inneholder:
 *  - GUI-elementer
 *  - Metoder for å opprette GUI
 *  - Metoder for å oppdatere info i vinduet
 *  - Registere for kundebehandling og selgermetoder
 *  - Inn- og utloggings-metoder
 * 
 * Skrevet av Martin og Sondre
 * 
 * Siste endring: 12/05/13
 */

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/* SelgerVindu oppretter GUI til salgsvindu, og mesteparten av paneler for
   både selger og administrator. Inn- og utlogging hånteres av denne klassen*/
public class SelgerVindu extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1000L;

	private JTextArea totalSum,salgsTall,salgstallDager;//, infoRegister
	
	private JCheckBox skoleCheck,hoytidCheck;
	
	private JButton byttVindu,byttVindu1,loggAv,loggAvMeny, nyttKC, regRC, slettSiste, betal, slettAlt,
	regSesong, regDag, regKlipp,fjern, openSkift,okButton,kortReg ,
	kundeBeh,endrePri,visStat,tilbake, salgstall,
	oversikt,diagram1,diagram2,antallTurer,tilbakeInfo,
	oppdaterInfo,hjelpButton,visKeycard, visAktiveKort, slettKc,salgstallDag;

	private JTextField sjekkKC, regEierF, regEier,eierDato, antallDag, antallKlipp,inputBruker,
	solgteKlipp, solgteDag,solgteSes, solgtTotal, solgtKc, antallTurerTot,
	antallTurerKlipp, antallTurerDag, antallTurerSes,sletteKc;

	private JPanel topPanel, rightPanel, centerPanel, regPanel, kcPanel, knappPanel,
	betalPanel, adminPanel, loggPanel, droppPanel, sesPanel, dagPanel, klippPanel,totalSumPanel,plassPanel,plass2Panel,handlePanel,openLight,innhold,salgPanel,
	panel, passord,passordPanel,knapper,byttInfoPanel,infoPanel,endrePriser,InfoSidePanel,
	oversiktTurer,oversiktP,framvisingSpace,salgsTallPanel,diagram1Panel,diagram2Panel,
	bilde,kundePanel,diagramForAntall, diagramTurer,salgstallDagerPanel;

	private JLabel bildeAdmin;
	private JList<String> kcBoks, infoRegister;
	private JComboBox<String> typeDag, typeKlipp, rabattSes, rabattDag,rabattKlipp;
	private FocusListener focuslistener;
	private Dimension size;
	private JScrollPane scroll, scrollDD, scrollP;
	private JPasswordField passwordField;
	private DefaultTableModel model;
	private JTable handleListeT;
	
	private BorderLayout layout,layoutPassord;
	private FlowLayout layout11;
	private GridLayout layoutGrid;
	
	private KundeBehandlingPanel kundebehandling;
	private ButtonMaker buttonMaker = new ButtonMaker(this); // Genererer knapper
	private SelgerMetoder selgermetoder;
	private Object openInfoPanel = null;
	
	private String[] type = {"Voksen","Barn","Honnør"};
	private String[] rabatt = {"0%","10%","20%","30%","40%","50%"};
	
	private String correctAdmin = "ADMIN";
	private String correctSelger = "SELGER";
	private String sum;
	private boolean admin = false; 
	
	/* Oppretter klassen, og genererer GUI for SelgerVinduet.
	   Definerer registere fra parametrene
	   Parametere: database, kontrollvindu, informasjonsvindu/nettside */
	public SelgerVindu(Database database, ArrayList<KontrollVindu> kontroll, InfoVindu infoVindu)
	{					
		size = new Dimension(240, 510);
		for(KontrollVindu k : kontroll)
			k.setSalgVindu(this);
		innhold = new JPanel();
		byttInfoPanel = new JPanel();
	
		kundebehandling = new KundeBehandlingPanel(database, buttonMaker, this);
		selgermetoder = new SelgerMetoder(this, database, kontroll, infoVindu);
		
		selgermetoder.hentFil(false);
		selgermetoder.getPrisPanel().setPris(database.getPriser());
		selgermetoder.getPrisPanel().sendPrisTilInfo();
		
		layout = new BorderLayout( 1, 1 ); // 5 pixel gaps
		layoutPassord = new BorderLayout( 1, 1 ); 
	    setSize(930, 530);
		setVisible(true);
		focuslistener = (new FocusListener()
		{
			// Markerer teksten i tekstfelt som ble trykket på
			public void focusGained(FocusEvent e) 
			{
				if(e.getComponent().getClass().getName().equals("javax.swing.JTextField"))
					((JTextField) e.getSource()).selectAll();
			}
			public void focusLost(FocusEvent e) 
			{
				// Tom metode for å tilfredsstille FocusListener	
			}
		});
		Icon slettKnappIcon = new ImageIcon(  
						getClass().getResource("bilder/bareSlett.gif" ));
		slettSiste= new JButton("Slett",slettKnappIcon); 
		slettSiste.setVerticalTextPosition( AbstractButton.BOTTOM );
		slettSiste.setHorizontalTextPosition( AbstractButton.CENTER );
		slettSiste.addActionListener(this);
		Icon betalKnappIcon = new ImageIcon( 
						getClass().getResource("bilder/bareBetal.gif" ));
		betal= new JButton("Betal",betalKnappIcon);
		betal.setVerticalTextPosition( AbstractButton.BOTTOM );
		betal.setHorizontalTextPosition( AbstractButton.CENTER );
		betal.addActionListener(this);
		Icon slettAltKnappIcon = new ImageIcon( 
						getClass().getResource("bilder/bareTom.gif" ));
		slettAlt =  new JButton("Tøm",slettAltKnappIcon);
		slettAlt.setVerticalTextPosition( AbstractButton.BOTTOM );
		slettAlt.setHorizontalTextPosition( AbstractButton.CENTER );
		slettAlt.addActionListener(this);
		Dimension bildeKnapper = new Dimension(65,80);
		slettSiste.setPreferredSize(bildeKnapper);
		betal.setPreferredSize(bildeKnapper);
		slettAlt.setPreferredSize(bildeKnapper);
		
		byttVindu = buttonMaker.generateButton("System", ButtonMaker.topKnapp);
		byttVindu1 = buttonMaker.generateButton("Selgervindu", ButtonMaker.topKnapp);
		loggAv = buttonMaker.generateButton("Logg av", ButtonMaker.topKnapp);
		loggAvMeny = buttonMaker.generateButton("Logg av", ButtonMaker.topKnapp);
		nyttKC = buttonMaker.generateButton("Nytt keycard", ButtonMaker.sideKnapp);
		regRC = buttonMaker.generateButton("Har keycard", ButtonMaker.sideKnapp);
		openSkift = buttonMaker.generateButton("Åpne/Lukke skift", ButtonMaker.sideKnapp);
		regSesong = buttonMaker.generateButton("Sesongkort", ButtonMaker.regKnapp);
		regDag = buttonMaker.generateButton("Dagskort", ButtonMaker.regKnapp);
		regKlipp = buttonMaker.generateButton("Klippekort", ButtonMaker.regKnapp);
		fjern = buttonMaker.generateButton("Fjern", ButtonMaker.betalKnapp);
		tilbake = buttonMaker.generateButton("Tilbake", ButtonMaker.sideKnapp);
		visKeycard = buttonMaker.generateButton("Keycard", ButtonMaker.topKnapp);
		visAktiveKort = buttonMaker.generateButton("Aktive kort", ButtonMaker.topKnapp);
		slettKc = buttonMaker.generateButton("Slett", ButtonMaker.betalKnapp);
		okButton = buttonMaker.generateButton("Ok", ButtonMaker.loginKnapp);
		hjelpButton = buttonMaker.generateButton("Hjelp", ButtonMaker.loginKnapp);
		salgstall = buttonMaker.generateButton("Salgstall", ButtonMaker.infoKnapper);
		salgstallDag = buttonMaker.generateButton("Dager", ButtonMaker.infoKnapper);
		oversikt = buttonMaker.generateButton("Oversikt", ButtonMaker.infoKnapper);
		diagram1 = buttonMaker.generateButton("Salg-diagram", ButtonMaker.infoKnapper);
		diagram2 = buttonMaker.generateButton("Tur-diagram", ButtonMaker.infoKnapper);
		antallTurer = buttonMaker.generateButton("Antall turer", ButtonMaker.infoKnapper);
		tilbakeInfo = buttonMaker.generateButton("Tilbake", ButtonMaker.infoKnapper);
		oppdaterInfo = buttonMaker.generateButton("Oppdater", ButtonMaker.infoKnapper);
		kortReg = buttonMaker.generateButton("Kortregister", null);
		kundeBeh = buttonMaker.generateButton("Kundebehandling", null);
		endrePri = buttonMaker.generateButton("Endre priser", null);
		visStat = buttonMaker.generateButton("Statistikk", null);
		
		sjekkKC = new JTextField("", 5);
		sjekkKC.addFocusListener(focuslistener);
		regEierF= new JTextField("", 7);
		regEierF.addFocusListener(focuslistener);
		regEier= new JTextField("", 7);
		regEier.addFocusListener(focuslistener);
		eierDato = new JTextField("dd/mm/yyyy",7);
		eierDato.addFocusListener(focuslistener);

		infoRegister = new JList<>();
		infoRegister.setSize(20, 30);
		
		selgermetoder.setTotalPris(0);
		sum = "Totalsum" + "t\t\t\t\t\t\t" + "kr: " + selgermetoder.getTotalPris() +",-" ;
		totalSum = new JTextArea(1, 54);
		totalSum.setEditable(false);

		totalSum.setText(sum);

		antallDag = new JTextField("1",4);
		antallDag.addFocusListener(focuslistener);
		antallKlipp = new JTextField("1",4);
		antallKlipp.addFocusListener(focuslistener);

		rabattSes = new JComboBox<String>();
		typeDag = new JComboBox<String>();
		rabattDag = new JComboBox<String>();
		typeKlipp = new JComboBox<String>();
		rabattKlipp = new JComboBox<String>();
		
		//Fyller inn navn på JComboBox
		for(int j = 0;j<3; j++){
			typeDag.addItem(type[j]);
			typeKlipp.addItem(type[j]);
		}
		
		for(int k = 0; k<6;k++)
		{
			rabattSes.addItem(rabatt[k]);
			rabattDag.addItem(rabatt[k]);
			rabattKlipp.addItem(rabatt[k]);
		}
		
		solgteKlipp = new JTextField("",7);
		solgteDag = new JTextField("",7);
		solgteSes = new JTextField("",7);
		solgtTotal = new JTextField("",7);
		solgtKc = new JTextField("",7);
		antallTurerTot = new JTextField("",7);
		antallTurerKlipp = new JTextField("",7);
		antallTurerDag = new JTextField("",7);
		antallTurerSes = new JTextField("",7);
		solgteKlipp.setEditable(false);
		solgteDag.setEditable(false);
		solgteSes.setEditable(false);
		solgtTotal.setEditable(false);
		solgtKc.setEditable(false);
		antallTurerTot.setEditable(false);
		antallTurerKlipp.setEditable(false);
		antallTurerDag.setEditable(false);
		antallTurerSes.setEditable(false);
		
		salgsTall= new JTextArea(23, 34);
		salgsTall.setEditable(false);
		salgstallDager= new JTextArea(23, 34);
		salgstallDager.setEditable(false);
		sletteKc = new JTextField("",6);
		layoutGrid = new GridLayout(2,1,5,0);
	    inputBruker = new JTextField("Brukernavn",10);
	    passwordField = new JPasswordField(10);
	    passwordField.addActionListener(this);
	    innhold = new JPanel();
	    
		addJPanels();
		start();
		loggInnUt(false);
		add(innhold,BorderLayout.CENTER);
		Oppdate(passord);
		resetFocus();
		revalidate();
	}
	
	// Viser panelet som er parameter, og skjuler de andre panelene
	public void Oppdate(Component panel){
		innhold.removeAll();
		innhold.add(panel);
		innhold.updateUI();
		revalidate();
	}

	// Oppretter alle paneler som skal være i salgsvindu
	public void addJPanels()
	{
		salgPanel = new JPanel();
		passord = new JPanel();
		passordPanel = new JPanel();
		knapper = new JPanel();
		new JPanel();
		layout11 = new FlowLayout();
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		passordPanel.setBorder(title);
		panel = new JPanel();
		topPanel();
		rightPanel();
		centerPanel();
		loggpaPanel();
		ByttInfo();
		aktiveKortPanel();
		endrePriser = selgermetoder.getPrisPanel().endrePriser();
		statistikkInfo();
		kundePanel = kundebehandling.getPanel();
	}
	
	// Panel på innloggingsvinduet
	public void start()
	{
		salgPanel.setLayout(layout);
		salgPanel.setPreferredSize(new Dimension(900,500));
		salgPanel.add(topPanel, BorderLayout.PAGE_START);
		salgPanel.add(centerPanel, BorderLayout.CENTER);
		salgPanel.add(rightPanel, BorderLayout.LINE_END);
		
		passordPanel.setLayout(layoutPassord);
		passordPanel.add(passord,BorderLayout.CENTER);
	}
	
	// Panel på innloggingsvindu
	public void loggpa()
	{
		passordPanel.setLayout(layout);
		passordPanel.add(passord,BorderLayout.CENTER);
	}
	


	// Panel for innloggingsvinduet
	public void loggpaPanel()
	{
		panel.setLayout(layoutGrid);
		FlowLayout ny = new FlowLayout();
		JPanel passordWrap = new JPanel();
		passord.setLayout(ny);
		passord.setPreferredSize(new Dimension(900,500));
		panel.add(inputBruker);
		panel.add(passwordField);			
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		panel.setBorder(title);
		
		JPanel knapperPassord = new JPanel();
		knapperPassord.add(okButton);
		knapperPassord.add(hjelpButton);
		knapperPassord.setPreferredSize(new Dimension(70,70));
		JPanel space2 = new JPanel();
		space2.setPreferredSize(new Dimension(900,160));
	
		title = BorderFactory.createTitledBorder("");
		passordWrap.setBorder(title);

		passordWrap.add(panel);
		passordWrap.add(knapperPassord);
		
		passord.add(space2);
		passord.add(passordWrap);
	}

	// Panel som binder andre paneler sammen
	public void topPanel()
	{
		topPanel = new JPanel(); 
		BorderLayout topPanelLayout = new BorderLayout(1,1);
		topPanel.setLayout(topPanelLayout);
		
		JPanel hoyre = new JPanel();
		openLight = new JPanel();
		openLight.setPreferredSize(new Dimension(20,20));
		openLight.setBackground(Color.red);
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		openLight.setBorder(title);

		adminPanel();
		loggPanel();
		/**BILDE**/
		bilde = new JPanel();
		Icon adminBilde = new ImageIcon( 
	      		getClass().getResource("bilder/adminLogoLiten.gif" ));
		bildeAdmin = new JLabel();
		bildeAdmin.setIcon(adminBilde);
		bildeAdmin.setPreferredSize(new Dimension(25,20));
		bilde.setPreferredSize(new Dimension(25,25));
		title = BorderFactory.createTitledBorder("");
		bilde.setBorder(title);
		bilde.setBackground(Color.white);
		bilde.add(bildeAdmin);
		hoyre.add(loggPanel);
		hoyre.add(bilde);
		hoyre.add(openLight);
		
		topPanel.add(adminPanel,BorderLayout.LINE_START);//legger paneler/knapper i dette panelet
		topPanel.add(hoyre,BorderLayout.LINE_END);
	}
	
	// Panel som binder andre paneler sammen
	public void rightPanel()
	{
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(size);
		kcPanel();
		knappPanel();
		betalPanel();
		rightPanel.add(plass2Panel);
		rightPanel.add(plassPanel);
		rightPanel.add(betalPanel);
	}
	
	// Panel som binder andre paneler sammen
	public void centerPanel()
	{
		centerPanel = new JPanel();
		kvitteringPanel();
		regPanel();
		centerPanel.add(regPanel);
		centerPanel.add(handlePanel);
	}
	
	// Panel for meny-vinduet i salgsvinduet
	public void ByttInfo()
	{
		GridLayout layoutInfo = new GridLayout(2,2,20,20);
		BorderLayout layoutBytt = new BorderLayout(5,5);
		byttInfoPanel.setPreferredSize(new Dimension(900,500));
		FlowLayout byttLayout = new FlowLayout();
		
		JPanel topBytt = new JPanel();
		topBytt.setLayout(byttLayout);
		topBytt.setAlignmentX(FlowLayout.RIGHT);
		topBytt.add(byttVindu1);
		topBytt.add(loggAvMeny);
		
		JPanel wrap = new JPanel();
		JPanel space = new JPanel();
		space.setPreferredSize(new Dimension(800,60));
		wrap.setLayout(layout11);
		knapper.setLayout(layoutInfo);
		
		knapper.add(kortReg);
		knapper.add(kundeBeh);
		knapper.add(endrePri);
		knapper.add(visStat);
		knapper.setPreferredSize(new Dimension(500,200));
		wrap.add(space);
		wrap.add(knapper);
		
		byttInfoPanel.setLayout(layoutBytt);
		byttInfoPanel.add(topBytt, BorderLayout.PAGE_START);
		byttInfoPanel.add(wrap, BorderLayout.CENTER);
	}
	
	// Panel for aktive kort
	public void aktiveKortPanel()
	{
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(900,500));
		
		JPanel omriss = new JPanel();
		BorderLayout aktiveRegLayout = new BorderLayout(5,5);
		omriss.setLayout(aktiveRegLayout);
		
		JPanel knapperVis = new JPanel();
		knapperVis.add(visAktiveKort);
		knapperVis.add(visKeycard);
		
		scrollP = new JScrollPane(infoRegister);
		scrollP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollP.setPreferredSize(new Dimension(380,350));

		JPanel knapperInne = new JPanel();
		knapperInne.setPreferredSize(new Dimension(50,150));
		knapperInne.add(slettKc);
		knapperInne.add(sletteKc);
		knapperInne.add(tilbake);

		omriss.add(knapperVis,BorderLayout.PAGE_START);
		omriss.add(scrollP,BorderLayout.CENTER);
		omriss.add(knapperInne,BorderLayout.PAGE_END);
		infoPanel.add(omriss);
		slettKc.setVisible(false);
		sletteKc.setVisible(false);
	}
	
	// Panel for statistikk
	public void statistikkInfo()
	{
		InfoSidePanel = new JPanel();
		InfoSidePanel.setPreferredSize(new Dimension(900,500));
		
		JPanel total= new JPanel();
		total.setPreferredSize(new Dimension(600,400));
		BorderLayout layoutTotal = new BorderLayout(1,1);
		total.setLayout(layoutTotal);
		
		JPanel knapperInfo = new JPanel();
		knapperInfo.setPreferredSize(new Dimension(150,450));
		JPanel space = new JPanel();
		space.add(Box.createRigidArea(new Dimension(100,40)));
		JPanel space2 = new JPanel();
		space2.add(Box.createRigidArea(new Dimension(100,40)));
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		knapperInfo.setBorder(title);
		knapperInfo.add(space2);
		knapperInfo.add(salgstall);
		knapperInfo.add(salgstallDag);
		knapperInfo.add(oversikt);
		knapperInfo.add(antallTurer);
		knapperInfo.add(diagram1);
		knapperInfo.add(diagram2);
		knapperInfo.add(space);
		knapperInfo.add(oppdaterInfo);
		knapperInfo.add(tilbakeInfo);
		
		JPanel framvising = new JPanel();
		framvising.setPreferredSize(new Dimension(450,400));
		framvising.setBorder(title);
		
		salgsTallPanel = new JPanel();
		salgsTallPanel.add(salgsTall); 
		
		salgstallDagerPanel = new JPanel();
		JScrollPane scrollStat = new JScrollPane(salgstallDager);
		scrollStat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		salgstallDagerPanel.add(scrollStat);
		selgermetoder.startArsOppgjor(salgstallDager);
		
		oversiktP = new JPanel();

		GridLayout solgteKort = new GridLayout(5,2,5,5);
		oversiktP.setLayout(solgteKort);
		oversiktP.add(new JLabel("Solgte biletter:"));
		oversiktP.add(solgtTotal);
		oversiktP.add(new JLabel("Solgte Keycard:"));
		oversiktP.add(solgtKc);
		oversiktP.add(new JLabel("Klippekort:"));
		oversiktP.add(solgteKlipp);
		oversiktP.add(new JLabel("Dagskort:"));
		oversiktP.add(solgteDag);
		oversiktP.add(new JLabel("Sesongkort:"));
		oversiktP.add(solgteSes);
		
		oversiktTurer = new JPanel();
		GridLayout turerTatt = new GridLayout(4,2,5,5);
		oversiktTurer.setLayout(turerTatt);
		oversiktTurer.add(new JLabel("Totalt turer:"));
		oversiktTurer.add(antallTurerTot);
		oversiktTurer.add(new JLabel("Turer med Klipperkort:"));
		oversiktTurer.add(antallTurerKlipp);
		oversiktTurer.add(new JLabel("Turer med Dagskort:"));
		oversiktTurer.add(antallTurerDag);
		oversiktTurer.add(new JLabel("Turer med Sesongkort:"));
		oversiktTurer.add(antallTurerSes);
		
	    diagram1Panel = new JPanel();
		diagram2Panel = new JPanel();
			
		framvisingSpace = new JPanel();
		framvisingSpace.add(Box.createRigidArea(new Dimension(400,60)));
		framvising.add(framvisingSpace);
		framvising.add(oversiktP);
		framvising.add(oversiktTurer);
		framvising.add(salgsTallPanel);
		framvising.add(salgstallDagerPanel);
		framvising.add(diagram1Panel);
		framvising.add(diagram2Panel);
		total.add(knapperInfo,BorderLayout.LINE_START);
		total.add(framvising,BorderLayout.CENTER);
		InfoSidePanel.add(total,BorderLayout.CENTER);
		oversiktTurer.setVisible(false);
		oversiktP.setVisible(false);
		framvisingSpace.setVisible(false);
		diagram1Panel.setVisible(false);
		diagram2Panel.setVisible(false);
		salgstallDagerPanel.setVisible(false);
	}
	
	// Panel for kvittering
	@SuppressWarnings("serial")
	public void kvitteringPanel()//et text område til handleliste
	{
		handlePanel = new JPanel();
		GridLayout layout2 = new GridLayout(2,1);
		handlePanel.setLayout(layout2);

		new JPanel();
		totalSumPanel = new JPanel();
		model = new DefaultTableModel();
		model.addColumn("Type");//type
		model.addColumn("Id");//id
		model.addColumn("Navn");//eNavn(SES)
		model.addColumn("kr");//pris
		
		handleListeT = new JTable(model)
		{
			 // Sørger for at man kan markere en rekke, men ikke editere den
			 public boolean isCellEditable(int row,int column)
			 {  
				 return false;  
			 }
		};
		handleListeT.setFillsViewportHeight(true);
		scroll = new JScrollPane(handleListeT);
		scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(100,170));
		totalSumPanel.add(totalSum);
		handlePanel.add(scroll);
		handlePanel.add(totalSumPanel);
	}
	
	// Panel for registrerings-knappene
	public void regPanel()
	{
		regPanel = new JPanel();
		droppPanel();
		sesPanel();
		dagPanel();
		klippPanel();
		regPanel.add(droppPanel);
		regPanel.add(sesPanel);
		regPanel.add(dagPanel);
		regPanel.add(klippPanel);
	}
	
	// Panel for keycard-knapp'ene og textfelt
	public void kcPanel()
	{
		kcPanel = new JPanel();
		plassPanel = new JPanel();
		
		GridLayout layout2 = new GridLayout(3,1,5,5);
		kcPanel.setLayout(layout2);
		plassPanel.setPreferredSize(new Dimension(235,260));

		kcPanel.add(nyttKC);
		kcPanel.add(regRC);
		kcPanel.add(sjekkKC);
		plassPanel.add(kcPanel);
	}
	
	// Panel for knappene under keycard
	public void knappPanel()
	{
		knappPanel = new JPanel();
		plass2Panel = new JPanel();

		GridLayout layout2 = new GridLayout(3,1,5,0);
		knappPanel.setLayout(layout2);
		knappPanel.add(openSkift);

		plass2Panel.add(knappPanel);
	}
	
	// Panel for knappene for betaling, slett og tøm
	public void betalPanel()
	{
		betalPanel = new JPanel();
		GridLayout ny = new GridLayout(1,3,5,5);
		betalPanel.setLayout(ny);
		betalPanel.add(slettAlt);
		betalPanel.add(slettSiste);
		betalPanel.add(betal);
	}
	
	// Panel som vises om du er admin
	public void adminPanel()
	{
		adminPanel = new JPanel();
		adminPanel.add(byttVindu);
	}
	
	// Panel for utloggings-knapp
	public void loggPanel()
	{
		loggPanel = new JPanel();
		loggPanel.add(loggAv);
	}
	
	// Panel for droppdown-menyen
	public void droppPanel() 
	{
		droppPanel = new JPanel();

		kcBoks = new JList<>();
		selgermetoder.setAntallTempKc(0);
		String[] noe = {"Nye kc: " + selgermetoder.getAntallTempKc()};//se med verdier
		kcBoks.setListData( noe );
		kcBoks.setVisibleRowCount( 5 );
		kcBoks.setFixedCellWidth( 80 );
		kcBoks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		kcBoks.addListSelectionListener( new ListSelectionListener() {
		      public void valueChanged( ListSelectionEvent e)
		      {
		        if ( !e.getValueIsAdjusting() && kcBoks == e.getSource())
		        {		          
		          int index = kcBoks.getSelectedIndex();
		          if(index > 0)
		          {
		        	  selgermetoder.setValgtindex(index-1);
		        	  selgermetoder.setValgt(selgermetoder.getTempKeycard().getElementFromIndex(selgermetoder.getValgtindex())); 
		        	  if(selgermetoder.getKundeListe().finnEierMedId(selgermetoder.getValgt().getId()) != null)
		        	  {
		        		  regEierF.setText(selgermetoder.getValgt().getFornavn());
		        		  regEier.setText(selgermetoder.getValgt().getEtternavn());
		        		  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		        		  eierDato.setText(df.format(selgermetoder.getValgt().getFodselsdato()));
		        		  setEierFeltEditable(false);
		        	  }
		          }
		          else
		          {
		        	  // Tekstfelt blankes bare dersom et keycard med eier var valgt før
		        	  if(regEierF.isEditable() && regEier.isEditable() && eierDato.isEditable())
		        		  return;
		        	  
		        	  selgermetoder.setValgt(null);
		        	  selgermetoder.setValgtindex(0);
		        	  regEierF.setText("");
	        		  regEier.setText("");
	        		  eierDato.setText("dd/mm/yyyy");
	        		  setEierFeltEditable(true);
		          }
		        }
		      }
		    });

		scrollDD = new JScrollPane(kcBoks);
		scrollDD.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		droppPanel.setPreferredSize(new Dimension(100,220));
		droppPanel.add(new JLabel("Keycard reg"));
		droppPanel.add(scrollDD);
		droppPanel.add(fjern);
		skoleCheck = new JCheckBox("Skole");
		hoytidCheck = new JCheckBox("Høytid");
		droppPanel.add(skoleCheck);
		droppPanel.add(hoytidCheck);
	}
	
	// Panel for knappene til sesongkort
	public void sesPanel()
	{
		sesPanel = new JPanel();
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		sesPanel.setBorder(title);
		JPanel hjelp = new JPanel();

		sesPanel.add(regSesong);
		sesPanel.setPreferredSize(new Dimension(180,220));

		GridLayout layout2 = new GridLayout(6,1,0,0);
		hjelp.setLayout(layout2);
		hjelp.add(new JLabel("Fornavn:\n"));
		hjelp.add(regEierF);
		hjelp.add(new JLabel("Etternavn:\n"));
		hjelp.add(regEier);
		hjelp.add(new JLabel("Fødselsdag:\n"));
		hjelp.add(eierDato);

		sesPanel.add(hjelp);
	}
	
	// Panel for knappene til dagskort
	public void dagPanel()
	{
		dagPanel = new JPanel();
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		dagPanel.setBorder(title);
		JPanel hjelp = new JPanel();
		dagPanel.add(regDag);
		dagPanel.setPreferredSize(new Dimension(180,220));
		GridLayout layout2 = new GridLayout(4,1,0,0);
		hjelp.setLayout(layout2);

		hjelp.add(new JLabel("Antall kort:"));
		hjelp.add(antallDag);
		hjelp.add(new JLabel("Type:"));
		hjelp.add(typeDag);

		dagPanel.add(hjelp);
	}
	
	// Panel for knappene til klippekort
	public void klippPanel()
	{
		klippPanel = new JPanel();
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		klippPanel.setBorder(title);
		JPanel hjelp = new JPanel();
		klippPanel.add(regKlipp);
		klippPanel.setPreferredSize(new Dimension(180,220));
		GridLayout layout2 = new GridLayout(4,1,0,0);
		hjelp.setLayout(layout2);

		hjelp.add(new JLabel("Antall kort:"));
		hjelp.add(antallKlipp);
		hjelp.add(new JLabel("Type:"));
		hjelp.add(typeKlipp);

		klippPanel.add(hjelp);
	}

	//metode som blir brukt i script for å starta dagsoppgjor med verdier derfra
	//legger til verdier hver gang den er oppkalt og leser også inn det osm blir lagret og satt i registeret
	public void oppgjorStart()
	{
		selgermetoder.leggTilStartDag();
		selgermetoder.startArsOppgjor(salgstallDager);
	}
	// Panel for oppgjør'et
	
	private void oppgjor()
	{
		salgstallDager = selgermetoder.leggTilArsOppgjor(salgstallDager);
		salgstallDagerPanel.removeAll();
		JScrollPane scrollStat = new JScrollPane(salgstallDager);
		scrollStat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		salgstallDagerPanel.add(scrollStat);
	}
	
	// Oppdaterer informasjon i statistikkvinduet
	private void OppdaterInfo() 
	{
		solgteKlipp.setText(selgermetoder.getAdminStat().getTotalantallKlipp()+"");
		solgteDag.setText(selgermetoder.getAdminStat().getTotalantallDag()+"");
		solgteSes.setText(selgermetoder.getAdminStat().getAntallASes()+"");
		solgtTotal.setText(selgermetoder.getAdminStat().getTotalantallKort()+"");
		solgtKc.setText(selgermetoder.getAdminStat().getTotalKeycard()+"");
		antallTurerTot.setText(selgermetoder.getAdminStat().getTotalKontroller()+"");
		antallTurerKlipp.setText(selgermetoder.getAdminStat().getAntallKlippTurer()+"");
		antallTurerDag.setText(selgermetoder.getAdminStat().getAntallDagTurer()+"");
		antallTurerSes.setText(selgermetoder.getAdminStat().getAntallSesTurer()+"");
		salgsTall.setText(selgermetoder.getAdminStat().toString());
		double[] values = new double[3];
		String[] names = new String[3];
		values[0] = (double)selgermetoder.getAdminStat().getTotalantallKlipp();
		names[0] = "Klippekort"; 
		values[1] = (double)selgermetoder.getAdminStat().getTotalantallDag();
		names[1] = "Dagskort";
		values[2]= (double)selgermetoder.getAdminStat().getTotalantallSes();
		names[2] = "Sesongkort";
	    
	    diagramForAntall =  new DiagramForAntall(values, names, "Solgte korttyper");
	    diagram1Panel.removeAll();
	    diagram1Panel.add(diagramForAntall);
	    
		double[] antallTurerVar = new double[3];
		String[] typeTurer = new String[3];
		antallTurerVar[0] = (double)selgermetoder.getAdminStat().getAntallKlippTurer();
		typeTurer[0] = "Klippekort";
		antallTurerVar[1] = (double)selgermetoder.getAdminStat().getAntallDagTurer();
		typeTurer[1] = "Dagskort";
		antallTurerVar[2] = (double)selgermetoder.getAdminStat().getAntallSesTurer();    
		typeTurer[2] = "Sesongkort";
    
	    diagramTurer =  new DiagramForAntall(antallTurerVar, typeTurer, "Totalt turer");
	    diagram2Panel.removeAll();
	    diagram2Panel.add(diagramTurer);
	}
	
	/* Setter infoRegister til å vise informasjon av typen "openInfoPanel" er.
	   Enten keycard eller aktive kort. */
	public void oppdaterInfoPanel() 
	{
		infoRegister.setListData(selgermetoder.getMetoder().toStringInfoArray(openInfoPanel));
	}
	
	/* Oppdaterer lista over keycard i JList'en kcBoks
	   Inn-parameteren er keycard som ikke lenger er aktive,
	   og har blitt scan'et inn for å bli fornya */
	public void oppdateHandleliste(String[] neste)
	{
		String[] tekst = new String[selgermetoder.getTempKeycard().getLength()+1];
		String[] temp = selgermetoder.getTempKeycard().toStringArray();
		tekst[0] = "Nye kc: " + selgermetoder.getAntallTempKc();
		
		for(int i = 1; i < selgermetoder.getTempKeycard().getLength()+1; i++)
			tekst[i] = temp[i-1];
		
		kcBoks.setListData(tekst);
		DecimalFormat decim = new DecimalFormat("0.00");
		totalSum.setText("Totalsum\t\t\t\t\t\tkr: " + decim.format(selgermetoder.getTotalPris()) +"");
		
		if(neste!=null)
			model.addRow(neste);
		
		sjekkKC.setText("");
		regEierF.setText("");
		regEier.setText("");
		eierDato.setText("dd/mm/yyyy");
		antallDag.setText("1");
		antallKlipp.setText("1");
	}
	
	// Setter fokus til brukernavn-tekstfeltet
	public void resetFocus() 
	{
		inputBruker.requestFocusInWindow();
	}
	
	/* Gjør tekstfeltene for fornavn, etternavn og fødselsdato
	   editerbare eller ikke utifra parameteren b */
	private void setEierFeltEditable(boolean b) 
	{
		  regEierF.setEditable(b);
		  regEier.setEditable(b);
		  eierDato.setEditable(b);
	}
	
	// Setter checkbox for skole og høytid lik parameteren ok
	public void setCheckBox(boolean ok)
	{
		skoleCheck.setSelected(ok);
		hoytidCheck.setSelected(ok);
	}
	
	// Setter textfelt lik inn-parameter
	public void setSjekkKc(String inn)
	{
		sjekkKC.setText(inn);
	}
	
	// Setter farge på openLight utifra parameteren ok
	public void openLightSetColor(boolean ok)
	{
		if(ok)
			openLight.setBackground(Color.green);
		else
			openLight.setBackground(Color.red);
	}
	
	// Setter panelene i statistikk-vinduet lik de forskjellige parameterene
	public void setVisibleSalg(Boolean b1, Boolean b2, Boolean b3,
			Boolean b4, Boolean b5, Boolean b6, Boolean b7)
	{
		oversiktP.setVisible(b1);
		oversiktTurer.setVisible(b2);
		framvisingSpace.setVisible(b3);
		salgsTallPanel.setVisible(b4);
		diagram1Panel.setVisible(b5);
		diagram2Panel.setVisible(b6);
		salgstallDagerPanel.setVisible(b7);
	}
	
	// Setter antallKlipp
	public void setAntallKlipp(String inn)
	{
		antallKlipp.setText(inn);
	}
	
	// Setter antallDag
	public void setAntallDag(String inn)
	{
		antallDag.setText(inn);
	}
	
	// Kjører passordsjekk, henter isåfall registererne fra fil
	public void innloggingSjekk()
	{
		if(sjekkPassord())
		{
			selgermetoder.hentFil(true);
			selgermetoder.getAktiveKort().oppdater();
			Oppdate(salgPanel);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Feil passord eller brukernavn");
			resetFocus();
		}
		return;
	}
	
	// Henter passord, og kjører passordsjekk
	public boolean sjekkPassord()
	{
		boolean ok = false;
		char[] input = passwordField.getPassword();
		ok = isPasswordCorrect(input);
		Arrays.fill(input, '0');
	    inputBruker.selectAll();
	    passwordField.setText("");
	    resetFocus();
		return ok;
	}
	
	// Sjekker om passord er riktig
	private boolean isPasswordCorrect(char[] input) 
	{
		boolean isCorrect = false;
	    char[] correctPasswordAdmin = {'1', '3', '2'};
	    char[] correctPasswordSelger = {'1', '1', '1'};
	    String bruker = inputBruker.getText().toUpperCase();
	    if (input.length != correctPasswordAdmin.length || input.length != correctPasswordSelger.length) 
	    	isCorrect = false;
	    if(!bruker.equals(correctAdmin)&& !bruker.equals(correctSelger))
	    	isCorrect = false;
	    if(Arrays.equals(input, correctPasswordAdmin) && bruker.equals(correctAdmin))
	    {
	    	isCorrect = true;
	    	admin = true;
	    	loggInnUt(true);  
	    }
	    if(Arrays.equals(input, correctPasswordSelger) && bruker.equals(correctSelger))
	    {
	        isCorrect = true;
	        openLight.setBackground(Color.red);
	    }
	    Arrays.fill(correctPasswordSelger, '0');
	    Arrays.fill(correctPasswordAdmin,'0');
	    return isCorrect;
	}

	// Logger inn/ut utifra parameteren b
	public void loggInnUt(boolean b)
	{
		bilde.setVisible(b);
		endrePri.setVisible(b);
		visStat.setVisible(b);
	}
	
	// Logger av brukeren
	public void loggAv()
	{
		if(!selgermetoder.getOpen())
		{
			Oppdate(passord);
			loggInnUt(false);
			admin = false;
			resetFocus();
			selgermetoder.getAktiveKort().oppdater();
			oppgjor();
			selgermetoder.skrivFil();
			
			return;
		}
		JOptionPane.showMessageDialog(this, "Du må stenge skift før du kan logge ut","", JOptionPane.ERROR_MESSAGE);
	}
	
	// Om man er admin
	public boolean isAdmin()
	{
		return admin;
	}
	
	// Henter selgermetoder
	public SelgerMetoder getSelgerMetoder()
	{
		return selgermetoder;
	}
	
	// ActionListener for SelgerVindu
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == byttVindu) 
			Oppdate(byttInfoPanel);
		if(e.getSource() == byttVindu1)
			Oppdate(salgPanel);
		if (e.getSource() == loggAv) 
			loggAv();
		if(e.getSource() == loggAvMeny)
			loggAv();
		if (e.getSource() == nyttKC) 
			selgermetoder.nyttKc(sjekkKC.getText());
		if (e.getSource() == regRC) 
			selgermetoder.regKc(sjekkKC.getText());
		if (e.getSource() == slettSiste) 
			selgermetoder.slettHandlevogn(model, handleListeT);
		if (e.getSource() == betal) 
			selgermetoder.betal(model);
		if(e.getSource() == tilbake)
		{
			Oppdate(byttInfoPanel);
			sletteKc.setText("");
		}
		if(e.getSource()==selgermetoder.getPrisPanel().getTilbakePriserKnapp())
			Oppdate(byttInfoPanel);
		if(e.getSource()==kortReg)
		{
			Oppdate(infoPanel);
			infoRegister.setListData(selgermetoder.getMetoder().toStringInfoArray(selgermetoder.getAktiveKort()));
			openInfoPanel = selgermetoder.getAktiveKort();
			slettKc.setVisible(false);
			sletteKc.setVisible(false);
		}
		if(e.getSource()==visAktiveKort)
		{
			infoRegister.setListData(selgermetoder.getMetoder().toStringInfoArray(selgermetoder.getAktiveKort()));
			openInfoPanel = selgermetoder.getAktiveKort();
			slettKc.setVisible(false);
			sletteKc.setVisible(false);
		}
		if(e.getSource()==visKeycard)
		{
			infoRegister.setListData(selgermetoder.getMetoder().toStringInfoArray(selgermetoder.getKeycardListe()));
			openInfoPanel = selgermetoder.getKeycardListe();
			slettKc.setVisible(true);
			sletteKc.setVisible(true);
		}
		if(e.getSource()==slettKc)
			selgermetoder.slettKc(sletteKc, infoRegister);
		if(e.getSource()==kundeBeh)
		{
			Oppdate(kundePanel);
			kundebehandling.setListData(selgermetoder.getKundeListe().toStringArray());
			kundebehandling.oppdaterData(selgermetoder.getKundeListe(), selgermetoder.getAktiveKort(), admin);
		}
		if(e.getSource()==endrePri)
		{
			Oppdate(endrePriser);
			selgermetoder.getPrisPanel().setDefault();
		}
		if(e.getSource()==visStat)
		{
			Oppdate(InfoSidePanel);
			OppdaterInfo();
		}
		if (e.getSource() == slettAlt) 
			selgermetoder.slettAltHandlevogn(model);
		if (e.getSource() == regSesong) 
			selgermetoder.regSesongKort(regEierF.getText(), regEier.getText(), eierDato.getText());
		if (e.getSource() == regDag) 
		{
			selgermetoder.regDagsKort(antallDag.getText(), typeDag, skoleCheck, hoytidCheck);
			skoleCheck.setSelected(false);
			hoytidCheck.setSelected(false);
		}
		if (e.getSource() == regKlipp)
		{ 
			selgermetoder.regKlippeKort(antallKlipp.getText(), typeKlipp, skoleCheck, hoytidCheck);
			skoleCheck.setSelected(false);hoytidCheck.setSelected(false);
		}
		if (e.getSource() == fjern) 
			selgermetoder.fjernKc();
		if(e.getSource()==okButton||e.getSource()==passwordField)
			innloggingSjekk();
		if(e.getSource() == openSkift)
			selgermetoder.åpneSkift();
		if(e.getSource()==salgstall)
			setVisibleSalg(false,false,false,true,false,false,false);
		if(e.getSource()==salgstallDag)
			setVisibleSalg(false, false, false, false,false,false,true);
		if(e.getSource()==oversikt)
			setVisibleSalg(true, false, true, false,false,false,false);
		if(e.getSource()==antallTurer)
			setVisibleSalg(false, true, true, false, false, false,false);
		if(e.getSource()==diagram1)
			setVisibleSalg(false,false,false,false,true,false,false);
		if(e.getSource()==diagram2)
			setVisibleSalg(false,false,false,false,false,true,false);
		if(e.getSource()==oppdaterInfo)
			OppdaterInfo();
		if( e.getSource()==kundebehandling.getTilbakeKnapp())
		{
			Oppdate(byttInfoPanel);
			kundebehandling.blankUtFinnFelt();
		}
		if(e.getSource()==tilbakeInfo)
			Oppdate(byttInfoPanel);
		if(e.getSource()==hjelpButton)
			selgermetoder.hjelpKnapp();
		if(e.getSource()!=passwordField)
			((AbstractButton) e.getSource()).setFocusPainted(false);
	}
}