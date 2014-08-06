/*
 * Klassen inneholder:
 *  - Main-metoden
 *  - Registere: database, statistikk, aktive kort, keycards, kunder, priser
 *  - Metoder for oppretting av DesktopPane, InternalFrame og MenuBar
 *  
 *
 * Skrevet av Martin og Sondre
 * 		
 * Siste endring: 09/05/13
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

/* Skisenter er main-klassen, og herfra startes programmet. 
   Her opprettes registerene, vinduene og klassene */
public class Skisenter extends JFrame
{
	private static final long serialVersionUID = 1000L;
	
	private AdminStat adminstatistikk;
	private AktiveKort aktivekort;
	private KeycardListe keycardliste;
	private KundeListe kundeliste;
	private Priser prisliste;
	private Database database;
	
	private static SelgerVindu selgervindu;
	private InfoVindu infoVindu;
	private ArrayList<KontrollVindu> kontrollere;
	private JDesktopPane desktopp;
	private JInternalFrame internvinduS, internvinduK, internvinduI;
	private OmProgFil infoOm;
	
	private int teller = 2;
	private int HOYDEKONTROLLER = 50;
	
	// Starter programmet, og lagrer til fil når programmet avsluttes
	public static void main(String[]args)
	{
		Skisenter skisenter = new Skisenter();
		skisenter.addWindowListener(
				new WindowAdapter() 
				{
					public void windowClosing( WindowEvent e )
			        {
			          selgervindu.getSelgerMetoder().skrivFil();
			          System.exit( 0 );
			        }
				});
	}
	
	// Oppretter SkiSenteret, med dets alle registere og vinduer
	public Skisenter()
	{
		super("Hobøl Skisenter");
		adminstatistikk = new AdminStat();
		aktivekort = new AktiveKort(adminstatistikk);
		keycardliste = new KeycardListe();
		kundeliste = new KundeListe();
		prisliste = new Priser();
		infoOm = new OmProgFil();
		
		kontrollere = new ArrayList<KontrollVindu>();

		database = new Database(adminstatistikk, aktivekort, keycardliste, kundeliste, prisliste);
		
		infoVindu = new InfoVindu(database);
		selgervindu = new SelgerVindu(database,kontrollere, infoVindu);
		
		if(!selgervindu.getSelgerMetoder().hentFil(false))
			new Script(database,selgervindu);
		
		infoMeny();
		
		desktopp = new JDesktopPane();
		getContentPane().add(desktopp, BorderLayout.CENTER);
		
		/*Dimension skjermstørrelse = 
	          Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,skjermstørrelse.width,skjermstørrelse.height);*/
		setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );// maksimerer hovedvinduet
		setVisible(true);
		addInfo();
		addKontroller("Heiskontroll");
		addSelgerVindu();
		
		ImageIcon bilde = new ImageIcon(
				getClass().getResource("bilder/logo.gif")); 
	    Image ikon = bilde.getImage();
		setIconImage(ikon);   
	}
	
	// Oppretter menybaren øverst i desktopPane
	private void infoMeny()
	{
		JMenuBar menylinje = new JMenuBar();
		JMenu salgmeny = new JMenu("Salgsvindu");
	    salgmeny.setMnemonic('S');
	    JMenuItem visSalg = new JMenuItem("Vis");
	    visSalg.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) {
				if(internvinduS.isClosed())
				{
					addSelgerVindu();
				}
				else
				{
					try {
						internvinduS.setSelected(rootPaneCheckingEnabled);
					} catch (PropertyVetoException e1) {
					}	
				}
			}
	    });
	    salgmeny.add(visSalg);
	   
	    JMenu kontrollmeny = new JMenu("Kontroll");
	    kontrollmeny.setMnemonic('K');
	    JMenuItem visKontroll = new JMenuItem("Vis");
	    JMenuItem addKon = new JMenuItem("Add");
	    visKontroll.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) {
				if(internvinduK.isClosed())
				{
					addKontroller("Heikontroll");
				}
				else
				{
					try {
						internvinduK.setSelected(rootPaneCheckingEnabled);
						kontrollere.get(0).setFocus();
					} catch (PropertyVetoException e1) {
					}	
				}
			}
	    });
	    addKon.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) {
				addKontrollere();
			}
	    });
	    kontrollmeny.add(visKontroll);
	    kontrollmeny.add(addKon);
	    
	    JMenu infomeny = new JMenu("Infovindu");
	    infomeny.setMnemonic('I');
	    JMenuItem visInfo = new JMenuItem("Vis");
	    visInfo.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) {
				if(internvinduI.isClosed())
				{
					addInfo();
				}
				else
				{
					try {
						internvinduI.setSelected(rootPaneCheckingEnabled);
					} catch (PropertyVetoException e1) {
					}	
				}
			}
	    });
	    infomeny.add(visInfo);
	    JMenu ommeny = new JMenu("Om");
	    ommeny.setMnemonic('O');
	    JMenuItem avslutt = new JMenuItem("Avslutt");
	    avslutt.setMnemonic('A');
	    avslutt.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e) {
				selgervindu.getSelgerMetoder().skrivFil();
		        System.exit( 0 );
			}
	    });
	    
	    JMenuItem omOss = new JMenuItem("Om oss");
	    omOss.setMnemonic('m');
	    omOss.addActionListener(new ActionListener()
	            {
	              public void actionPerformed(ActionEvent e)
	              {
	                addOmOss();
	              }
	            });
	    JMenuItem omProg = new JMenuItem("Om Programmet");
	    omProg.setMnemonic('P');
	    omProg.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              addOmProg();
            }
          });
	    ommeny.add(omOss);
	    ommeny.add(omProg);
	    ommeny.add(avslutt);
	    
	    menylinje.add(ommeny);
	    menylinje.add(salgmeny);
	    menylinje.add(kontrollmeny);
	    menylinje.add(infomeny);
	    //menylinje.add(avslutt);
	    
	    setJMenuBar(menylinje);
	}
	
	// Oppretter informasjonsvinduet/nettsiden
	public void addInfo()
	{
		internvinduI = new JInternalFrame(
	              "www.hobolskisenter.no",
	              false, //kan endre størrelse
	              true, //kan lukkes
	              true, //kan maksimeres
	              true); //kan ikonifiseres
	      Container container = internvinduI.getContentPane();
	      container.add(infoVindu, BorderLayout.CENTER);
	      internvinduI.setDefaultCloseOperation(
	              JInternalFrame.DISPOSE_ON_CLOSE);
	      internvinduI.setLocation(0,0);

	      desktopp.add(internvinduI);
	      internvinduI.setVisible(true);
	      internvinduI.setFocusable(true);
	      Dimension skjermstørrelse = 
		            Toolkit.getDefaultToolkit().getScreenSize();
		   internvinduI.setBounds(0, 0, (int)(skjermstørrelse.width-18),
		            skjermstørrelse.height-120);
	    // internvinduI.setSize(300,250);
	}
	
	// Legger til knapp "Om oss" i menybar'en
	public void addOmOss()
	{

		JInternalFrame internvindu = new JInternalFrame(
	              "Om oss",
	              true, //kan endre størrelse
	              true, //kan lukkes
	              true, //kan maksimeres
	              true); //kan ikonifiseres
		Container container = internvindu.getContentPane();
	    JTextArea omOss = new JTextArea();
	    omOss.setPreferredSize(new Dimension(400,400));
	    omOss.setEditable(false);
	    omOss.setText(infoOm.toStringOmOss());
	    container.add(omOss, BorderLayout.CENTER);
	    internvindu.setDefaultCloseOperation(
	    		  JInternalFrame.DISPOSE_ON_CLOSE);
	    internvindu.pack();
	    desktopp.add(internvindu);
	    internvindu.setLocation(450,100);
	    internvindu.setVisible(true);
	    internvindu.pack();
	}
	
	// Legger til knapp "Om Programmet" i menybar'en
	public void addOmProg()
	{
		JInternalFrame internvindu = new JInternalFrame(
	              "Om programmet",
	              true, //kan endre størrelse
	              true, //kan lukkes
	              true, //kan maksimeres
	              true); //kan ikonifiseres
	    Container container = internvindu.getContentPane();
	    JTextArea omProg = new JTextArea();
	    omProg.setPreferredSize(new Dimension(400,400));
	    omProg.setEditable(false);
	    omProg.setText(infoOm.toStringOmProg());
	    container.add(omProg, BorderLayout.CENTER);
	    internvindu.setDefaultCloseOperation(
	              JInternalFrame.DISPOSE_ON_CLOSE);
	    internvindu.pack();
	    desktopp.add(internvindu);
	    internvindu.setVisible(true);
	    internvindu.setLocation(450,100);
	    internvindu.pack();
	}
	
	// Oppretter SelgerVindu
	public void addSelgerVindu()
	{
		  internvinduS = new JInternalFrame(
	              "Selgervindu",
	              true, //kan endre størrelse
	              true, //kan lukkes
	              true, //kan maksimeres
	              true); //kan ikonifiseres

	      Container container = internvinduS.getContentPane();
	      container.add(selgervindu, BorderLayout.CENTER);
	      internvinduS.setDefaultCloseOperation(
	              JInternalFrame.DISPOSE_ON_CLOSE);

	      desktopp.add(internvinduS);
	      internvinduS.setVisible(true);
	      internvinduS.setFocusable(true);
	      internvinduS.pack();
	      internvinduS.setMinimumSize(new Dimension(900,530));
	}
	
	// Oppretter et Heiskontroll-vindu
	public void addKontroller(String tittel)
	{
		internvinduK = new JInternalFrame(
	              tittel,
	              false, //kan endre størrelse
	              true, //kan lukkes
	              false, //kan maksimeres
	              true); //kan ikonifiseres
	      Container container = internvinduK.getContentPane();
	      kontrollere.add(new KontrollVindu("Heiskontroll", database,selgervindu));
	      container.add(kontrollere.get(kontrollere.size()-1), BorderLayout.CENTER);
	      internvinduK.setDefaultCloseOperation(
	              JInternalFrame.DISPOSE_ON_CLOSE);
	      internvinduK.setLocation(960,0);

	      desktopp.add(internvinduK);
	      internvinduK.setVisible(true);
	      internvinduK.setFocusable(true);
	      internvinduK.setSize(300,250);
	}
	
	// Oppretter et nytt Heiskontroll-vindu
	public void addKontrollere()
	{
		JInternalFrame internvindu = new JInternalFrame(
				  "Heiskontroll " + teller,
	              false, //kan endre størrelse
	              true, //kan lukkes
	              false, //kan maksimeres
	              true); //kan ikonifiseres
	      Container container = internvindu.getContentPane();
	      kontrollere.add(new KontrollVindu("Heiskontroll " + teller++, database, selgervindu));
	      container.add(kontrollere.get(kontrollere.size()-1), BorderLayout.CENTER);
	      internvindu.setDefaultCloseOperation(
	              JInternalFrame.DISPOSE_ON_CLOSE);
	      internvindu.setLocation(960,HOYDEKONTROLLER);

	      desktopp.add(internvindu);
	      internvindu.setVisible(true);
	      internvindu.setFocusable(true);
	      internvindu.setSize(300,250);
	      HOYDEKONTROLLER+=50;
	}
}