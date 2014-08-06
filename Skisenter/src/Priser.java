/*
 * Klassen inneholder:
 *  - Priser
 *  - get- og set-metoder for priser
 *  - Metoder for å endre priser
 * 
 * Skrevet av Martin og Sondre
 * 
 * Sist endret: 09/05/13
 */

import java.io.Serializable;

// Prispanel inneholder prisene for skisenteret, som kan endres
public class Priser implements Serializable
{
	private static final long serialVersionUID = 10050L;
	
	private double SESVOKSEN = 3000.00, SESBARN = 2500.00, SESHONNOR=2000.00;
	private double DAGVOKSEN = 350.00, DAGBARN = 200.00, DAGHONNOR=180.00;
	private double KLIPPVOKSEN = 300.00, KLIPPBARN = 230.00, KLIPPHONNOR=200.00;
	private double RABATTKLIPPKORT = 1.00, RABATTDAGKORT = 1.00 , RABATTSESONGKORT=1.00;
	private double RABATTHALVDAG = 0.70;
	private double RABATTSKOLE = 0.85, RABATTHOYTID = 0.90;
	private double KEYCARD = 50.00;
	private int klipp = 10;
	
	// Oppretter klassen og setter prisene
	public Priser()
	{
		setSESVOKSEN(SESVOKSEN);
		setSESBARN(SESBARN);
		setSESHONNOR(SESHONNOR);
		setDAGVOKSEN(DAGVOKSEN);
		setDAGBARN(DAGBARN);
		setDAGHONNOR(DAGHONNOR);
		setKLIPPVOKSEN(KLIPPVOKSEN);
		setKLIPPBARN(KLIPPBARN);
		setKLIPPHONNOR(KLIPPHONNOR);
		setKEYCARD(KEYCARD);
		setKlipp(klipp);
		setRABATTKLIPPKORT(RABATTKLIPPKORT);
		setRABATTDAGKORT(RABATTDAGKORT);
		setRABATTSESONGKORT(RABATTSESONGKORT);
	}
	
	// Gjennoppretter prisene til de originale prisene
	public void gjennopprettPriser() 
	{
		setSESVOKSEN(3000.00);
		setSESBARN(2500.00);
		setSESHONNOR(2000.00);
		setDAGVOKSEN(350.00);
		setDAGBARN(200.00);
		setDAGHONNOR(180.00);
		setKLIPPVOKSEN(300.00);
		setKLIPPBARN(230.00);
		setKLIPPHONNOR(200.00);
		setKEYCARD(50.00);
		setKlipp(10);
		setRABATTKLIPPKORT(1.00);
		setRABATTDAGKORT(1.00);
		setRABATTSESONGKORT(1.00);
		setRABATTHALVDAG(0.70);
	}

	
	/**********GET- OG SET-METODER**********/
	
	public double getRABATTHALVDAG()
	{
		return RABATTHALVDAG;
	}
	
	public void setRABATTHALVDAG(double inn)
	{
		RABATTHALVDAG = inn;
	}
	
	public double getSESVOKSEN() 
	{
		return SESVOKSEN;
	}

	public void setSESVOKSEN(double sESVOKSEN) 
	{
		SESVOKSEN = sESVOKSEN;
	}

	public double getSESBARN() 
	{
		return SESBARN;
	}

	public void setSESBARN(double sESBARN)
	{
		SESBARN = sESBARN;
	}

	public double getSESHONNOR()
	{
		return SESHONNOR;
	}

	public void setSESHONNOR(double sESHONNOR) 
	{
		SESHONNOR = sESHONNOR;
	}

	public double getDAGVOKSEN() 
	{
		return DAGVOKSEN;
	}

	public void setDAGVOKSEN(double dAGVOKSEN) 
	{
		DAGVOKSEN = dAGVOKSEN;
	}

	public double getDAGBARN() 
	{
		return DAGBARN;
	}

	public void setDAGBARN(double dAGBARN)
	{
		DAGBARN = dAGBARN;
	}

	public double getDAGHONNOR() {
		return DAGHONNOR;
	}

	public void setDAGHONNOR(double dAGHONNOR)
	{
		DAGHONNOR = dAGHONNOR;
	}

	public double getKLIPPVOKSEN() 
	{
		return KLIPPVOKSEN;
	}

	public void setKLIPPVOKSEN(double kLIPPVOKSEN)
	{
		KLIPPVOKSEN = kLIPPVOKSEN;
	}

	public double getKLIPPBARN() 
	{
		return KLIPPBARN;
	}

	public void setKLIPPBARN(double kLIPPBARN) 
	{
		KLIPPBARN = kLIPPBARN;
	}

	public double getKLIPPHONNOR() 
	{
		return KLIPPHONNOR;
	}

	public void setKLIPPHONNOR(double kLIPPHONNOR) 
	{
		KLIPPHONNOR = kLIPPHONNOR;
	}

	public double getRABATTKLIPPKORT() 
	{
		return RABATTKLIPPKORT;
	}

	public void setRABATTKLIPPKORT(double rABATTKLIPPKORT) 
	{
		RABATTKLIPPKORT = rABATTKLIPPKORT;
	}

	public double getRABATTDAGKORT() 
	{
		return RABATTDAGKORT;
	}

	public void setRABATTDAGKORT(double rABATTDAGKORT) 
	{
		RABATTDAGKORT = rABATTDAGKORT;
	}

	public double getRABATTSESONGKORT() 
	{
		return RABATTSESONGKORT;
	}

	public void setRABATTSESONGKORT(double rABATTSESONGKORT) 
	{
		RABATTSESONGKORT = rABATTSESONGKORT;
	}
	public double getKEYCARD()
	{
		return KEYCARD;
	}
	
	public double getRABATTSKOLE()
	{
		return RABATTSKOLE;
	}
	
	public void setRABATTSKOLE(double rabattskole)
	{
		RABATTSKOLE = rabattskole;
	}
	
	public double getRABATTHOYTID()
	{
		return RABATTHOYTID;
	}
	
	public void setRABATTHOYTID(double rabatthoytid)
	{
		RABATTHOYTID = rabatthoytid;
	}

	public void setKEYCARD(double kEYCARD) 
	{
		KEYCARD = kEYCARD;
	}

	public int getKlipp()
	{
		return klipp;
	}

	public void setKlipp(int klipp) 
	{
		this.klipp = klipp;
	}
}
