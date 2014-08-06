import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class Linjeteller
{
	private int javafiles = 0;
	private int totallines = 0;
	
	private JFileChooser fc;

	private String currentDirectory = "C:/Users/Sondre/Documents/GitHub/Project/Skisenter/src"; //Skriv inn ønsket default mappe her

	public Linjeteller()
	{
		fc = new JFileChooser(currentDirectory);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(fc);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String text = fc.getSelectedFile().getAbsolutePath();
            sjekkFil(text);
        }
	}

	public void sjekkFil(String file)
	{
		String output = "";
		File navn = new File(file);

		if(navn.isDirectory())
		{
			String[] dir = navn.list();
			output += navn.getAbsolutePath() + "\n";

			for(int i = 0; i < dir.length; i++)
			{
				String[] ord;
				ord = dir[i].split("\\.");
				if(ord[ord.length-1].equals("java"))
				{
					@SuppressWarnings("unused")
					int linjer = tell(navn.getPath() + "/" + dir[i]);
					//output += dir[i] + " - kodelinjer: " + linjer + "\n";
					javafiles++;
				}
			}
			output += "\nAntall javafiler: " + javafiles + "\n";
			output += "Totalt antall kodelinjer: " + totallines;
		}
		JOptionPane.showMessageDialog(null, output);
	}

	public int tell(String fil)
	{
		int teller = 0;

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fil));
			while (reader.readLine() != null)
				teller++;

			totallines += teller;
			reader.close();
		}
		catch(IOException e)
		{
			System.out.println("IOException");
		}
		return teller;
	}

	public static void main(String[] args)
	{
		new Linjeteller();
	}
}