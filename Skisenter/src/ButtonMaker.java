/*
 * Klassen inneholder:
 *  - alle knappe-dimensjoner i programmet
 *  - metoder for å generere knapper
 * 
 * lagd av Martin
 * 
 * Siste endring: 01/05/13
 */

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

// Klassen genererer knapper, og legger dem i forskjellige vinduer/paneler
public class ButtonMaker 
{
	public static final Dimension topKnapp = new Dimension(150, 30);
	public static final Dimension sideKnapp = new Dimension(220, 25);
	public static final Dimension regKnapp = new Dimension(170, 30);
	public static final Dimension betalKnapp = new Dimension(80, 30);
	public static final Dimension helknapp = new Dimension(190,30);
	public static final Dimension halvknapp = new Dimension(90, 30);
	public static final Dimension infoKnapper = new Dimension(110,25);
	public static final Dimension byttKnappDim = new Dimension(100,100);
	public static final Dimension loginKnapp = new Dimension(70, 30);
	
	private ActionListener al;
	
	// setter en felles actionlistener i klassen
	public ButtonMaker(ActionListener al)
	{
		this.al = al;
	}

	/* Lager og returnerer en knapp med parametre for: 
       navn, panel, størrelse, actionlistener. Knappen 
	   legges i gitt panel, og bruker innkommende actionlistener */
	public JButton generateButton(String name, JPanel panelet, Dimension size, ActionListener lytter)
	{
		JButton knapp = new JButton(name);
		knapp.setPreferredSize(size);
		knapp.addActionListener(lytter);
		panelet.add(knapp);
		return knapp;
	}
	
	// lager knapp med parametre for navn og størrelse.
	// den felles actionlisteneren for denne klassen brukes
	public JButton generateButton(String name, Dimension size)
	{
		JButton knapp = new JButton(name);
		knapp.setPreferredSize(size);
		knapp.addActionListener(al);
		return knapp;
	}
}