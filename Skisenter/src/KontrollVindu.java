/*
 * Klassen innholder: 
 *  - GUI elementer; textarea, textfield, knapp
 *  - Liste over aktive-kort, og SelgerVindu
 *  - Metoder som sjekker om skisenteret er åpent
 *  - Metoder som sjekker om skikort er gyldig
 *  - Timer som åpner skikontrollen noen sekunder
 * 
 * Skrevet av Martin og Sondre
 * 
 * Sist endret: 07/05/13
 */

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* Klassen forestiller en heiskontroll, for 
   å kontrollere om keycard er gyldig  */
public class KontrollVindu extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1004L;

	private JTextArea info;
	private JButton sjekk;
	private JTextField kortid;
	
	private AktiveKort aktivkortreg;
	private SelgerVindu vindu;
	
	private boolean open;
	private boolean wait;
	private Timer timer;
	private int timersec = 3;
	
	/* Oppretter kontroll-vinduet, og GUI-elementer og timer.
	   Database og Selgervindu kobles internt i klassen. */
	public KontrollVindu(String title, Database reg, SelgerVindu Svindu)
	{
		aktivkortreg = reg.getAktive();
		vindu = Svindu;
		open = vindu.getSelgerMetoder().getOpen();
		
		info = new JTextArea(6, 10);
		info.setEditable(false);
		info.setFont(new Font("Serif", Font.ROMAN_BASELINE, 20));
		kortid = new JTextField(15);
		kortid.addActionListener(this);
		sjekk = new JButton("Kontroller");
		sjekk.addActionListener(this);
		
		BorderLayout layout = new BorderLayout(5,5); 
		setLayout(layout);
		
		add(info,BorderLayout.PAGE_START);
		add(kortid,BorderLayout.CENTER);
		add(sjekk,BorderLayout.PAGE_END);
		
		setSize(300, 300);
		setVisible(true);
		
		timer = new Timer();
		wait = false;
		
		setOpen(open);
		
	    resetFocus();
	}
	
	// setter selgervindu i klassen, til den innkommende parameteren
	public void setSalgVindu(SelgerVindu selgervindu)
	{
		vindu = selgervindu;
	}
	
	// setter kontrollvinduet til åpent/stengt, utifra inn-parameteren
	public void setOpen(boolean o)
	{
		open = o;
		if(open)
		{
			info.setText("\n\n              Heisene er åpne");
			return;
		}
		info.setText("\n\n              Heisene er stengt");
		return;
	}
	
	// setter aktivekort-lista i klassen, til den fra inn-parameteren
	public void setAktive(AktiveKort aktiv)
	{
		aktivkortreg = aktiv;
	}
	
	/* Setter i gang en timer når kort blir skanna.
	   Kontrollvindu'et blir utilgjengelig helt til timeren
	   har holdt på en gitt stund.
	   Inn-parameteren er om kortet er gyldig/ugyldig */
	private boolean scan(boolean gyldig)
	{
		if(wait)
		{
			info.setText("\n\n                 Vennlist vent...");
			info.setBackground(Color.WHITE);
			return false;
		}
		
		if(gyldig)
		{
			wait = true;
			timer.schedule(new Tick(), timersec*1000);
			return true;
		}
		else
		{
			wait = true;
			timer.schedule(new Tick(), timersec*1000);
			return false;
		}
	}
	
	// Klasse dannes når timer har kjørt etter en gitt tid 
	class Tick extends TimerTask 
	{
		// Gjør kontrollvindu'et tilgjengelig igjen
	    public void run() 
	    {
	    	wait = false;
	    	info.setText("\n\n              Heisene er åpne");
	    	info.setBackground(Color.WHITE);
	    	this.cancel();
	    }
	}

	// Sjekker om skisenteret er åpent, og kjører sjekk()-metoden
	private void sjekkOpen()
	{
		if(vindu == null)
		{
			if(open)
				sjekk();
			else
			{
				info.setText("\n\n              Heisene er stengt");
				kortid.setText("");
			}
		}
		else
		{
			setOpen( vindu.getSelgerMetoder().getOpen());
			if(open)
				sjekk();
			else
			{
				info.setText("\n\n              Heisene er stengt");
				kortid.setText("");
			}
		}
	}
	
	/* Sjekker om scannet kort finnes og om det er gyldig,
	   og skriver ut om scanningen var gyldig/ugyldig */
	private void sjekk() 
	{
		vindu.getSelgerMetoder().setAktivTilKontroll();
		try
		{
			int nr = Integer.parseInt(kortid.getText());
			Boolean ok = aktivkortreg.sjekkGyldig(nr);
			if(ok)
			{
				info.setText("\n\n                       Gyldig");
				info.setBackground(Color.GREEN);
				if(scan(ok))
					aktivkortreg.fjernKlipp(nr);
				
				kortid.setText("");
				kortid.selectAll();
			    resetFocus();
			}
			else
			{
				info.setText("\n\n                    Ikke gyldig");
				info.setBackground(Color.RED);
				scan(ok);
				kortid.setText("");
				kortid.selectAll();
			    resetFocus();
			}
			
			aktivkortreg.oppdater();
			vindu.oppdaterInfoPanel();
		}
		catch ( NumberFormatException e )
		{
			info.setText("\n\n                    Kun tall...");
			kortid.setText("");
			timer.schedule(new Tick(), timersec*500);
			return;
		}
		
	}
	
	// Setter fokus i vinduet til textfeltet
	private void resetFocus() 
	{
		kortid.requestFocusInWindow();
	}
	
	// Setter fokus i vinduet til textfeltet. Brukes i "Skisenter"
	public void setFocus()
	{
		kortid.requestFocusInWindow();
	}
	
	// ActionListener for KontrollVindu
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == sjekk) 
			sjekkOpen();
	}
}