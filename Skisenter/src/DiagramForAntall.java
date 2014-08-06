/*
 * Klassen inneholder:
 * - Arrayer av verdier og korttyper som skal vises
 * - Metoder for å lage diagrammer
 * 
 * Lagd av Martin
 * 
 * Sist endret: 03/05/13
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

// Klassen oppretter diagram for statistikk
public class DiagramForAntall extends JPanel 
{
	private static final long serialVersionUID = 1002L;

	private double[] verdier;
	private String[] typeKort;
	private String title;
 
	/* Definerer størrelse og bakgrunn for diagrammet og, definerer 
	   tall-verdier, korttyper og overskrift som skal vises */
	public DiagramForAntall(double[] v, String[] n, String t) 
	{
		typeKort = n;
		verdier = v;
		title = t;
		setPreferredSize(new Dimension(420,375)); 
	    setBackground(Color.white);
	}
	 
	/* Tegner innholdet og selve diagrammet, 
	   utifra verdiene som ligger i klassen */
	public void paintComponent(Graphics g) 
	{
		//  setSize(400, 300);
	    super.paintComponent(g);
	    if (verdier == null || verdier.length == 0)
	    	return;
	    double minValue = 0;
	    double maxValue = 0;
	    for (int i = 0; i < verdier.length; i++) 
	    {
	    	if (minValue > verdier[i])
	    		minValue = verdier[i];
	    	if (maxValue < verdier[i])
	    		maxValue = verdier[i];
	    }
	 
	   // Dimension d = getSize();
	    int bredde = 420;//d.width;
	    int hoyde = 375;//d.height;
	    int barWidth = bredde / verdier.length;
	 
	    Font titleFont = new Font("SansSerif", Font.BOLD, 20);
	    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
	    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
	    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
	 
	    int tittelBredde = titleFontMetrics.stringWidth(title);
	    int y = titleFontMetrics.getAscent();
	    int x = (bredde - tittelBredde) / 2;
	    g.setFont(titleFont);
	    g.drawString(title, x, y);
	 
	    int top = titleFontMetrics.getHeight();
	    int bottom = labelFontMetrics.getHeight();
	    if (maxValue == minValue)
	    	return;
	    double scale = (hoyde - top - bottom) / (maxValue - minValue);
	    y = hoyde - labelFontMetrics.getDescent();
	    g.setFont(labelFont);
	 
	    for (int i = 0; i < verdier.length; i++) 
	    {
	    	int valueX = i * barWidth + 1;
		    int valueY = top;
		    int height = (int) (verdier[i] * scale);
		    if (verdier[i] >= 0)
		    	valueY += (int) ((maxValue - verdier[i]) * scale);
		    else 
		    {
		        valueY += (int) (maxValue * scale);
		        height = -height;
		    }
		    g.setColor(Color.green);//red
		    g.fillRect(valueX, valueY, barWidth - 2, height);
		    g.setColor(Color.black);
		    g.drawRect(valueX, valueY, barWidth - 2, height);
		    int labelWidth = labelFontMetrics.stringWidth(typeKort[i]);
		    x = i * barWidth + (barWidth - labelWidth) / 2;
		    g.drawString(typeKort[i] + " ("+(int)verdier[i] + ")", x, y);
	    }
	}
} 