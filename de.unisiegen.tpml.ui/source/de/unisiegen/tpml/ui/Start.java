package de.unisiegen.tpml.ui;

/**
 * this class first checks the javaversion. If ist is under 1.5 it shows an errormessage. Otherwise it starts the
 * main.java. This class should be compiled with target 1.1.
 * 
 * @author Feivel
 * 
 */
public class Start
{
	/**
	 * The needed Java-version is represented by the MSTERN and the SLAVEN. Example: Java 1.4.2: MASTERN: 1, SLAVEN 4
	 * Java 1.5.0: MASTERN: 1, SLAVEN 5 all digits after the second dot will be ignored.
	 */
	// Here you can definde the needed JAVA-Version MASTERN.SLAVAN
	// It is MASTERN = 1;
	// SLAVEN = 5;
	// for JAVA 1.5 needed. The Programm will start with all JAVA 1.5 and above versions
	final static int MASTERN = 1;

	/**
	 * The needed Java-version is represented by the MSTERN and the SLAVEN. Example: Java 1.4.2: MASTERN: 1, SLAVEN 4
	 * Java 1.5.0: MASTERN: 1, SLAVEN 5 all digits after the second dot will be ignored.
	 */
	final static int SLAVEN = 5;

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
		// TO work with String put it into an array
		char[] toDivC = toDiv.toCharArray();
		// length of array that will be returned
		int lengthWihtoutDiv = toDivC.length;
		// lets count the split-char
		int countdiv = 0;
		// walk through the array
		for (int i = 0; i < toDivC.length; i++)
		{
			// div-char is found
			if (toDivC[i] == div)
			{
				// because the div-chars will not be returned
				lengthWihtoutDiv--;
				countdiv++;
			}
		}
		// build result
		String result[] = new String[countdiv + 1];
		// these strings will be added to the result[]
		String tmp1 = "";
		// second count, at j the tmp will be added
		int j = 0;
		for (int i = 0; i < toDivC.length; i++)
		{
			// only chars != to div will be added
			if (toDivC[i] != div)
			{
				tmp1 = tmp1 + toDivC[i];
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
			new MsgFrame("Unbekannte Version", "Das Ergebnis der Java-versionspr�fung ist ung�ltig! Es wurde "
					+ versionA[0] + "." + versionA[1] + "als Version zur�ckgegeben.");
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
		int neededMaster = MASTERN;
		int neededSlave = SLAVEN;
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
		// if -f is set- programm starts wihtout any test
		if (force)
		{
			// alle Argumente werden gebaut, damit Dateien gestartet ge�ffnet werden k�nnen
			String Arguments[] = new String[args.length - 1];
			int j = 0;
			for (int i = 0; i < args.length; i++)
			{
				// wenn das Argument das -f ist, wird es nicht mitgereicht
				if (args[i].equalsIgnoreCase("-f"))
				{
				}
				else
				{
					Arguments[j] = args[i];
					j++;
				}
			}
			Main.main(Arguments);
		}
		else
		// no force
		{
			try
			{
				// Problem: Split gibt es fr�her noch nicht 1.4
				// String [] neededVersionA = neededVersion.split("[.]");
				// String[] actualVersionA = split(actualVersion, '.');
				// actualMaster = Integer.parseInt(actualVersionA[0]);
				// actualSlave = Integer.parseInt(actualVersionA[1]);
				// Testausgabe f�r die Java-Version
				// System.out.println(System.getProperty("java.version"));
				// JavaTest javaTest = new JavaTest();

				// For errormessages bevor 1.2
				// For nicer errormessages
				boolean is12 = isJavaRightVersion(1, 2);
				// boolean is12 = false;

				// checks fersion
				boolean isRight = isJavaRightVersion(neededMaster, neededSlave);

				if (isRight)
				{
					try
					{
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
							System.exit(1);
						}
					}
				}
				else
				{
					String message = "Java " + neededMaster + "." + neededSlave
							+ " NICHT erkannt! Installieren Sie bitte die benötigte Verion.";
					String title = "Falsche Java-Version";
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
			catch (IndexOutOfBoundsException e)
			{
				// Fehlermeldungen f�r Entwickler
				System.out.println("Die benötigte Versionsnummer und das zu startende Programm m�ssen �bergeben werden.");
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
