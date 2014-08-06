/*
 * Klassen inneholder:
 *  - GUI-elementer: panel, knapper, textfield og combobox
 *  - Prisliste, database, informasjonsvinduet
 *  - Metoder for å lage GUI
 *  - Metoder for å sende, endre og gjennopprette priser
 * 
 * Skrevet av Martin og Sondre
 * 
 * Sist endret: 05/05/13
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/* PrisPanel inneholder panelet for "endre priser", og dets metoder
   Oppdaterer også priser i informasjonsvinduet/nettsiden */
public class PrisPanel implements ActionListener 
{
	private JPanel endrePriser;
	private JButton tilbakePriser, lagrePriser, gjennopprett;
	private JTextField endreBarnKlipp, endreVoksenKlipp, endreHonnorKlipp, 
						endreBarnDag, endreVoksenDag, endreHonnorDag, 
						endreBarnSesong, endreVoksenSesong, endreHonnorSesong, 
						endreKc, endreKlipp,endreRabattHalvdag;
	private JComboBox<String> typeDag, typeKlipp, rabattSes, rabattDag,rabattKlipp;
	private Dimension sideKnapp;
	private FocusListener focuslistener;
	
	private Priser pris;
	private Database database;
	private InfoVindu infoOppdate;
	
	private String[] type = {"Voksen","Barn","Honnør"};
	private String[] rabatt = {"0%","-10%","-20%","-30%","-40%","-50%"};
	
	/* Oppretter klassen og GUI. Parametre: Database og informasjonsvindu 
	   blir satt, og Selgervindu brukes som actionListener */
	public PrisPanel(Database dataBase,SelgerVindu parent, InfoVindu infovindu)
	{
		// markerer all tekst i tekstfelt dersom tekstfeltet blir trykket på
		focuslistener = (new FocusListener()
		{
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

		database = dataBase;
		this.pris = database.getPriser();
		this.infoOppdate = infovindu;
		
		tilbakePriser = new JButton("Tilbake");
		lagrePriser = new JButton("Lagre");
		gjennopprett = new JButton("Gjennopprett priser");
		
		sideKnapp = new Dimension(220, 25);
		tilbakePriser.setPreferredSize(sideKnapp);
		lagrePriser.setPreferredSize(sideKnapp);
		gjennopprett.setPreferredSize(sideKnapp);
		
		tilbakePriser.addActionListener(parent);
		lagrePriser.addActionListener(this);
		gjennopprett.addActionListener(this);
		
		endreBarnKlipp = new JTextField(pris.getKLIPPBARN()+"", 6);
		endreVoksenKlipp = new JTextField(pris.getKLIPPVOKSEN()+"", 6);
		endreHonnorKlipp = new JTextField(pris.getKLIPPHONNOR()+"", 6);
		endreBarnDag = new JTextField(pris.getDAGBARN()+"", 6);
		endreVoksenDag = new JTextField(pris.getDAGVOKSEN()+"", 6);
		endreHonnorDag = new JTextField(pris.getDAGHONNOR()+"", 6);
		endreBarnSesong = new JTextField(pris.getSESBARN()+"", 6);
		endreVoksenSesong = new JTextField(pris.getSESVOKSEN()+"", 6);
		endreHonnorSesong = new JTextField(pris.getSESHONNOR()+"", 6);
		endreKc = new JTextField(pris.getKEYCARD()+"",6);
		endreKlipp = new JTextField(pris.getKlipp() +"",6);
		endreRabattHalvdag = new JTextField(100.00-pris.getRABATTHALVDAG()*100+"",6);
		endreBarnKlipp.addFocusListener(focuslistener);
		endreVoksenKlipp.addFocusListener(focuslistener);
		endreHonnorKlipp.addFocusListener(focuslistener);
		endreBarnDag.addFocusListener(focuslistener);
		endreVoksenDag.addFocusListener(focuslistener);
		endreHonnorDag.addFocusListener(focuslistener);
		endreBarnSesong.addFocusListener(focuslistener);
		endreVoksenSesong.addFocusListener(focuslistener);
		endreHonnorSesong.addFocusListener(focuslistener);
		endreKc.addFocusListener(focuslistener);
		endreKlipp.addFocusListener(focuslistener);
		endreRabattHalvdag.addFocusListener(focuslistener);
		
		endrePriser = new JPanel();
		endrePriser.setPreferredSize(new Dimension(900,500));
		
		JPanel omriss = new JPanel();
		BorderLayout endrePriserL = new BorderLayout(5,5);
		omriss.setLayout(endrePriserL);
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		omriss.setBorder(title);
		
		rabattSes = new JComboBox<String>();
		typeDag = new JComboBox<String>();
		rabattDag = new JComboBox<String>();
		typeKlipp = new JComboBox<String>();
		rabattKlipp = new JComboBox<String>();

		//Fyller inn navn på JComboBox
		for(int j = 0;j<3; j++)
		{
			typeDag.addItem(type[j]);
			typeKlipp.addItem(type[j]);

		}
		
		for(int k = 0; k<6;k++)
		{
			rabattSes.addItem(rabatt[k]);
			rabattDag.addItem(rabatt[k]);
			rabattKlipp.addItem(rabatt[k]);
		}

		JPanel total = new JPanel();
		total.setPreferredSize(new Dimension(430,400));
		
		JPanel knapperPriser = new JPanel();
		GridLayout inni = new GridLayout(4,4,1,1);
		knapperPriser.setLayout(inni);
		knapperPriser.add(new JLabel("Type"));
		knapperPriser.add(new JLabel("Barn"));
		knapperPriser.add(new JLabel("Voksen"));
		knapperPriser.add(new JLabel("Honnør"));
		knapperPriser.add(new JLabel("Klippekort"));
		knapperPriser.add(endreBarnKlipp);
		knapperPriser.add(endreVoksenKlipp);
		knapperPriser.add(endreHonnorKlipp);
		knapperPriser.add(new JLabel("Dagskort"));
		knapperPriser.add(endreBarnDag);
		knapperPriser.add(endreVoksenDag);
		knapperPriser.add(endreHonnorDag);
		knapperPriser.add(new JLabel("Sesongkort"));
		knapperPriser.add(endreBarnSesong);
		knapperPriser.add(endreVoksenSesong);
		knapperPriser.add(endreHonnorSesong);
		
		JPanel kcKlipp = new JPanel();
		GridLayout inniKcK = new GridLayout(3,2,1,1);
		kcKlipp.setLayout(inniKcK);
		kcKlipp.add(new JLabel("Keycard pris"));
		kcKlipp.add(endreKc);
		kcKlipp.add(new JLabel("Antall klipp"));
		kcKlipp.add(endreKlipp);
		
		JPanel rabatt = new JPanel();
		GridLayout rabattInni = new GridLayout(2,2,1,1);
		rabatt.setLayout(rabattInni);
		rabatt.add(new JLabel("Rabatt klippekort"));
		rabatt.add(rabattKlipp);
		rabatt.add(new JLabel("Rabatt dagskort"));
		rabatt.add(rabattDag);
		rabatt.add(new JLabel("Rabatt sesongkort"));
		rabatt.add(rabattSes);
		JPanel space = new JPanel();
		space.add(Box.createRigidArea(new Dimension(300,20)));
		total.add(knapperPriser);
		total.add(rabatt);
		total.add(kcKlipp);
		total.add(space);
		total.add(lagrePriser);
		total.add(gjennopprett);
		total.add(tilbakePriser);
		
		omriss.add(total,BorderLayout.CENTER);
		endrePriser.add(omriss);
	}
	
	// Oppretter panelet for "Endre priser"
	public JPanel endrePriser()
	{
		endrePriser = new JPanel();
		endrePriser.setPreferredSize(new Dimension(900,500));
		
		JPanel omriss = new JPanel();
		BorderLayout endrePriserL = new BorderLayout(5,5);
		omriss.setLayout(endrePriserL);
		TitledBorder title;
		title = BorderFactory.createTitledBorder("");
		omriss.setBorder(title);
		
		JPanel total = new JPanel();
		total.setPreferredSize(new Dimension(430,400));
		
		JPanel knapperPriser = new JPanel();
		GridLayout inni = new GridLayout(4,4,1,1);
		knapperPriser.setLayout(inni);
		knapperPriser.add(new JLabel("Type"));
		knapperPriser.add(new JLabel("Barn"));
		knapperPriser.add(new JLabel("Voksen"));
		knapperPriser.add(new JLabel("Honnør"));
		knapperPriser.add(new JLabel("Klippekort"));
		knapperPriser.add(endreBarnKlipp);
		knapperPriser.add(endreVoksenKlipp);
		knapperPriser.add(endreHonnorKlipp);
		knapperPriser.add(new JLabel("Dagskort"));
		knapperPriser.add(endreBarnDag);
		knapperPriser.add(endreVoksenDag);
		knapperPriser.add(endreHonnorDag);
		knapperPriser.add(new JLabel("Sesongkort"));
		knapperPriser.add(endreBarnSesong);
		knapperPriser.add(endreVoksenSesong);
		knapperPriser.add(endreHonnorSesong);
		
		JPanel kcKlipp = new JPanel();
		GridLayout inniKcK = new GridLayout(3,2,1,1);
		kcKlipp.setLayout(inniKcK);
		kcKlipp.add(new JLabel("Keycard pris"));
		kcKlipp.add(endreKc);
		kcKlipp.add(new JLabel("Antall klipp"));
		kcKlipp.add(endreKlipp);
		kcKlipp.add(new JLabel("Rabatt halvdag"));
		kcKlipp.add(endreRabattHalvdag);
		
		JPanel rabatt = new JPanel();
		GridLayout rabattInni = new GridLayout(3,2,1,1);
		rabatt.setLayout(rabattInni);
		rabatt.add(new JLabel("Rabatt klippekort"));
		rabatt.add(rabattKlipp);
		rabatt.add(new JLabel("Rabatt dagskort"));
		rabatt.add(rabattDag);
		rabatt.add(new JLabel("Rabatt sesongkort"));
		rabatt.add(rabattSes);
		JPanel space = new JPanel();
		space.add(Box.createRigidArea(new Dimension(300,20)));
		total.add(knapperPriser);
		total.add(rabatt);
		total.add(kcKlipp);
		total.add(space);
		total.add(lagrePriser);
		total.add(gjennopprett);
		total.add(tilbakePriser);
		
		omriss.add(total,BorderLayout.CENTER);
		endrePriser.add(omriss);
		
		return endrePriser;
	}

	// Henter priser fra prislista og skriver dem ut i textfelt'ene
	public void setDefault() 
	{
		endreBarnKlipp.setText(pris.getKLIPPBARN() + "");
		endreVoksenKlipp.setText(pris.getKLIPPVOKSEN()+"");
		endreHonnorKlipp.setText(pris.getKLIPPHONNOR()+"");
		endreBarnDag.setText(pris.getDAGBARN()+"");
		endreVoksenDag.setText(pris.getDAGVOKSEN()+"");
		endreHonnorDag.setText(pris.getDAGHONNOR()+"");
		endreBarnSesong.setText(pris.getSESBARN()+"");
		endreVoksenSesong.setText(pris.getSESVOKSEN()+"");
		endreHonnorSesong.setText(pris.getSESHONNOR()+"");
		endreKc.setText(pris.getKEYCARD()+"");
		endreKlipp.setText(pris.getKlipp()+"");	
		endreRabattHalvdag.setText((100.00-pris.getRABATTHALVDAG()*100+""));
		
		rabattKlipp.setSelectedIndex(setRabattIndex(pris.getRABATTKLIPPKORT()));
		rabattDag.setSelectedIndex(setRabattIndex(pris.getRABATTDAGKORT()));
		rabattSes.setSelectedIndex(setRabattIndex(pris.getRABATTSESONGKORT()));
	}
	
	// Setter priser fra innparameter
	public void setPris(Priser p)
	{
		pris = p;
	}
	
	/* Endrer priser med å hente info fra textfelt'ene
	 * NumberFormatException kan oppstå, brukeren isåfall en feilmelding */
	public void endre()
	{
		try
		{
			double barnKlipp = Double.parseDouble(endreBarnKlipp.getText()); 
			double voksenKlipp = Double.parseDouble(endreVoksenKlipp.getText()); 
			double honnorKlipp = Double.parseDouble(endreHonnorKlipp.getText()); 
			double barnDag = Double.parseDouble(endreBarnDag.getText()); 
			double voksenDag = Double.parseDouble(endreVoksenDag.getText());
			double honnorDag = Double.parseDouble(endreHonnorDag.getText());
			double barnSes = Double.parseDouble(endreBarnSesong.getText());
			double voksenSes = Double.parseDouble(endreVoksenSesong.getText());
			double honnorSes = Double.parseDouble(endreHonnorSesong.getText());
			double rabattHalv = Double.parseDouble(endreRabattHalvdag.getText());
			double kcPris = Double.parseDouble(endreKc.getText());
			int antKlipp = Integer.parseInt(endreKlipp.getText());
			int rabattKlippValg = rabattKlipp.getSelectedIndex();
			int rabattDagValg = rabattDag.getSelectedIndex();
			int rabattSesValg = rabattSes.getSelectedIndex();
			
			// Setter rabatt, dersom rabatt-verdiene er endret
			if(rabattKlippValg!=0)
				pris.setRABATTKLIPPKORT(setRabatt(rabattKlippValg));
			if(rabattDagValg!=0)
				pris.setRABATTDAGKORT(setRabatt(rabattDagValg));
			if(rabattSesValg!=0)
				pris.setRABATTSESONGKORT(setRabatt(rabattSesValg));
			
			rabattHalv = (100-rabattHalv)/100;
			
			pris.setSESVOKSEN(voksenSes);
			pris.setSESBARN(barnSes);
			pris.setSESHONNOR(honnorSes);
			pris.setDAGVOKSEN(voksenDag);
			pris.setDAGBARN(barnDag);
			pris.setDAGHONNOR(honnorDag);
			pris.setKLIPPVOKSEN(voksenKlipp);
			pris.setKLIPPBARN(barnKlipp);
			pris.setKLIPPHONNOR(honnorKlipp);
			pris.setKEYCARD(kcPris);
			pris.setKlipp(antKlipp);
			pris.setRABATTHALVDAG(rabattHalv);
			
			double array[] = new double[13];
			array[0]=voksenKlipp;array[1]=barnKlipp;array[2]=honnorKlipp;array[3]=voksenDag;
			array[4]=barnDag;array[5]=honnorDag;array[6]=voksenSes;
			array[7]=barnSes;array[8]=honnorSes;array[9]=kcPris;
			array[10] = setRabatt(rabattKlippValg); array[11] = setRabatt(rabattDagValg);
			array[12] = setRabatt(rabattSesValg);
			infoOppdate.oppdatePriser(array);
			setDefault();
		}
		catch ( NumberFormatException e )
	    {
			JOptionPane.showMessageDialog(null,"Kan kun lese inn tall");
			return;
	    }
	}
	
	// Sender prisene til informasjonsvinduet/nettsida
	public void sendPrisTilInfo()
	{
		double array[] = new double[13];
		
		pris.getKEYCARD();
		pris.getKlipp();
		array[0]=pris.getKLIPPVOKSEN();array[1]=pris.getKLIPPBARN();array[2]=pris.getKLIPPHONNOR();array[3]=pris.getDAGVOKSEN();
		array[4]=pris.getDAGBARN();array[5]=pris.getDAGHONNOR();array[6]=pris.getSESVOKSEN();
		array[7]=pris.getSESBARN();array[8]=pris.getSESHONNOR();array[9]=pris.getKEYCARD();
		array[10] = pris.getRABATTKLIPPKORT(); array[11] = pris.getRABATTDAGKORT();
		array[12] = pris.getRABATTSESONGKORT();
		infoOppdate.oppdatePriser(array);
	}
	
	// Setter rabatt utifra inn-parameteren
	private double setRabatt(int index)
	{
		if(index==1)
			return 0.9;
		else if(index==2)
			return 0.8;
		else if(index==3)
			return 0.7;
		else if(index==4)
			return 0.6;
		else if(index==5)
			return 0.5;
		return 1;
	}
	
	//Setter rabatt-index utifra inn-parameter
	private int setRabattIndex(double inn)
	{
		if(inn==0.90)
			return 1;
		else if(inn == 0.80)
			return 2;
		else if(inn == 0.70)
			return 3;
		else if(inn == 0.60)
			return 4;
		else if(inn == 0.50)
			return 5;
		return 0;
	}
	
	/* Henter "tilbake"-knappen, fordi actionListeneren
	   dens er i en annen klasse, SelgerVindu */
	public JButton getTilbakePriserKnapp() 
	{
		return tilbakePriser;
	}
	
	// ActionListener med til klassen
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == gjennopprett) 
		{
			pris.gjennopprettPriser();
			infoOppdate.resetPriser();
			setDefault();
		}
		if (e.getSource() == lagrePriser) 
		{
			endre();
		}
	}
}