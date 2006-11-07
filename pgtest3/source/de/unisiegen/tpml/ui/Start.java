package de.unisiegen.tpml.ui ;

/**
 * @author Feivel
 * 
 */
public class Start
{
	final static int masterN = 1;
	final static int slaveN = 5;

	/**
	 * implementation of split function because String.split dose not exist bevore Java 1.4 implementation ist
	 * different. Argument isn't a regular expression, it is only a char
	 * 
	 * @param toDiv
	 *          String which shold be split
	 * @param div
	 *          at all chars of this typ will be split
	 * @return String [] with split parts
	 */
	public static String[] split(String toDiv, char div)
	{
		char[] toDivC = toDiv.toCharArray();
		int lengthWihtoutDiv = toDivC.length;
		int countdiv=0;
		for (int i = 0; i < toDivC.length; i++)
		{
			if (toDivC[i] == div)
			{
				lengthWihtoutDiv--;
				countdiv++;
			}
		}
		
		String result[] = new String [countdiv+1];
		String tmp1 = "";
		int j = 0;
		for (int i = 0; i < toDivC.length; i++)
		{
			if (toDivC[i] != div)
			{
				tmp1 = tmp1+toDivC[i];
			}
			else
			{
				result[j] = (tmp1);
				j++;
				tmp1 = "";
			}
			if (i == toDivC.length - 1)
			{
				result[j] = (tmp1);
			}
		}
		return result;
	}



	/**
	 * checks, if javaversion is equal or higher than given version
	 * 
	 * @param neededMaster
	 *          int, in 1.5 neededMaster ist the 1
	 * @param neededSlave
	 *          int, in 1.5 neededSlave ist the 5
	 * @return boolean, true, if version ist equal or higher
	 */
	public static boolean isJavaRightVersion(int neededMaster, int neededSlave)
	{
		int first = 0;
		int second = 0;
		// String contrains version like 1.5.0_006
		String version = System.getProperty("java.version");
		// Regular expression [.] for ., get 1 and 5 from 1.5, only this part will be tested
		// the split function only existf from 1.4, so self implementatet will be uses
		// String[] versionA = version.split("[.]");
		String[] versionA = split(version, '.');

		try
		{
			first = Integer.parseInt(versionA[0]);
			second = Integer.parseInt(versionA[1]);
		}
		catch (NumberFormatException e)
		{
			new MsgFrame( "Unknown version", "Das Ergebnis der Java-versionsprüfung ist ungültig!");
		}
		
		// lexografik order: in 2.5, the 5 isn't important
		if (first > neededMaster)
		{
			return true;
		}
		if (first == neededMaster && second >= neededSlave)
		{
			return true;
		}
		return false;
	}

	/**
	 * checks the version, runs given programm if version ist valid with -f the execution can be forced
	 * 
	 * @param args
	 *          Format: version programm [-f]
	 */
	public static void main(String args[])
	{
	
		int countArgs = args.length;
		int neededMaster = masterN;
		int neededSlave = slaveN;
		//String programmToStart = "";
		boolean force = false;
		int forceInt = -1;

		if (countArgs > 0)
		{
			for (int i = 0; i < args.length; i++)
			{
				if (args[i].equalsIgnoreCase("-f"))
				{
					forceInt = i;
					force = true;
				}
			}
		}
		if (force)
		{
			//alle Argumente werden gebaut, damit Dateien gestartet geöffnet werden können
			String Arguments[] = new String[args.length-1];
			int j = 0;
			for (int i = 0; i < args.length; i++)
			{
				// wenn das Argument das -f ist, wird es nicht mitgereicht
				if (i == forceInt)
				{
				}
				else
				{
					j++;
					Arguments[j] = args[i];
				}
			}
			Main.main(Arguments);
		}
		else //no force
		{
			try
			{				
				// Problem: Split gibt es früher noch nicht 1.4
				// String [] neededVersionA = neededVersion.split("[.]");
				//String[] actualVersionA = split(actualVersion, '.');
				//actualMaster = Integer.parseInt(actualVersionA[0]);
				//actualSlave = Integer.parseInt(actualVersionA[1]);
				// Testausgabe für die Java-Version
				// System.out.println(System.getProperty("java.version"));
				//JavaTest javaTest = new JavaTest();
				//For nicer errormessages
				boolean is12 = isJavaRightVersion(1, 2);
				//boolean is12 = false;
				//For errormessages bevor 1.2
				boolean isRight = isJavaRightVersion(neededMaster, neededSlave);
				if (isRight)
				{
					try
					{
						//System.out.println(programmToStart);
						// TODO Programm soll später in der gleichen Klasse stehen und dort einfach die Mainmethode starten
						Main.main(args);
					}
					catch (Exception e)
					{
						String message = "Das Programm konnte nicht gestartet werden!";
						String title = "Programmstart fehlgeschlafen.";
						if (is12)
						{
							new JOpFrame(title, message);
							System.exit(1);
						}
						else
						{
							new MsgFrame(title, message);	
						}
					}
				}
				else
				{
					if (is12)
					{
						String title = "Java "+neededMaster+"."+neededSlave+ " NICHT erkannt! Installieren Sie die	benötigte Verion";
						String message = "Falsche Java-Version";
						new JOpFrame(title, message);
						System.exit(1);
					}
					else
					{
						new MsgFrame("Falsche Java-Version", "Java " + neededMaster + "." + neededSlave	+ " NICHT erkannt! Installieren Sie die benötigte Verion");
					}
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				// Fehlermeldungen für Entwickler
				System.out.println("Die benötigte Versionsnummer und das zu startende Programm müssen übergeben werden.");
				System.out.println("JavaTest version programm");
				System.out.println("z.B.: JavaTest 1.4 main");
				System.exit(125);
			}
			catch (NumberFormatException e)
			{
				// Fehlermeldungen für Entwickler
				System.out.println("Die Version muss korrekt übergeben werden! Es wurde " + args[0]
						+ " als Version übergeben!");
				System.out.println("JavaTest version programm");
				System.out.println("z.B.: JavaTest 1.4 main");
				System.exit(125);
			}
		}
	}
}
