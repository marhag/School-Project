/*
 * Klassen inneholder:
 *  - Metode som skriver ut informasjon
 * 
 * Lagd av Martin
 * 
 * Sist endret: 12/05/13
 */

// Klassen inneholder informasjon som sendes til menyen i JDeskTop
public class OmProgFil 
{
	// Skriver ut informasjon "Om oss"
	public String toStringOmOss()
	{
		String ut ="";
		ut  = "Dette programmet er laget som prosjektoppgaven i \n" +
				"Programutvikling våren 2013 \n\n" +
				"Programmet er laget av\n" +
				" Sondre Sparby Boge, s1881130, Klasse 1AA\n"
				+ "og Martin Hagen, s188098, Klasse 1AA\n\n" +
				"Gruppe nr. 16";

		return ut;
	}
	
	// Skriver ut informasjon "Om programmet"
	public String toStringOmProg()
	{
		String ut ="";
		ut = "Dette programmet er et program til et fiktivt\n" +
				"skisenter, Hobøl Skisenter.\n\n" +
				"De tre vinduene inne i dette panelet er \n" +
				"et Selgervindu,et Kontrollvindu og et \n" +
				"Infovindu. Alle vinduene kan lukkes, og så \n" +
				"åpnes/sette fokus på ved hjelp av knappene\n" +
				"i menyen over.\n\n" +
				"Selgervindu er stedet en ansatt som skal selge heiskort\n" +
				"eller sjekke statistikk logger inn. Hvis selger logger inn\n" +
				"vil den møte begrenset tilgang flere steder, det er \n" +
				"kun admin som har muligheten til å endre på \n" +
				"kort, kunder eller priser i selve systemet.\n\n" +
				"Kontrollvindu simulerer heiskontrollen ved heisene.\n" +
				"Hvis det er behov for å utvide anlegget er det en \n" +
				"<add>-knapp under <Kontrollvindu> i menyen over.\n\n" +
				"Infovindu er kun en infoside, som viser priser og \n" +
				"diverse om senteret. Dette skal forestille en nettside.\n\n" +
				"Hvis det er behov for noe hjelp finnes det i \n" +
				"vår Brukerveiledning.";
		return ut;
	}
}
