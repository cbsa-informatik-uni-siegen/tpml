package de.unisiegen.tpml.ui;


/**
 * this class first checks the javaversion. If ist is under 1.5 it shows an
 * errormessage. Otherwise it starts the main.class. This class should be
 * compiled with target 1.1.
 * 
 * @author michael
 * @version $Id$
 */
public class Start
{

  /**
   * The needed Java-version is represented by the MASTERN and the SLAVEN.
   * Example: Java 1.4.2: MASTERN: 1, SLAVEN 4 Java 1.5.0: MASTERN: 1, SLAVEN 5
   * all digits after the second dot will be ignored.
   */
  // Here you can definde the needed JAVA-Version MASTERN.SLAVAN
  // It is MASTERN = 1;
  // SLAVEN = 5;
  // for JAVA 1.5 needed. The Programm will start with all JAVA 1.5 and above
  // versions
  final static int MASTERN = 1;


  /**
   * The needed Java-version is represented by the MASTERN and the SLAVEN.
   * Example: Java 1.4.2: MASTERN: 1, SLAVEN 4 Java 1.5.0: MASTERN: 1, SLAVEN 5
   * all digits after the second dot will be ignored.
   */
  final static int SLAVEN = 5;


  /**
   * implementation of split function because String.split dose not exist bevore
   * Java 1.4. The implementation is different. Argument isn't a regular
   * expression, it is only a char
   * 
   * @param toDiv String which shold be split
   * @param div at all chars of this typ will be split
   * @return String [] with split parts
   */
  public static String [] split ( String toDiv, char div )
  {
    // TO work with String put it into an array
    char [] toDivC = toDiv.toCharArray ();
    // length of array that will be returned
    int lengthWihtoutDiv = toDivC.length;
    // lets count the split-char
    int countdiv = 0;
    for ( int i = 0 ; i < toDivC.length ; i++ )
    {
      // div-char is found
      if ( toDivC [ i ] == div )
      {
        // because the div-chars will not be returned
        lengthWihtoutDiv-- ;
        countdiv++ ;
      }
    }
    // build result
    String result[] = new String [ countdiv + 1 ];
    // these strings will be added to the result[]
    String tmp1 = ""; //$NON-NLS-1$
    // second count, at j the tmp will be added
    int j = 0;
    for ( int i = 0 ; i < toDivC.length ; i++ )
    {
      // only chars != to div will be added
      if ( toDivC [ i ] != div )
      {
        tmp1 = tmp1 + toDivC [ i ];
      }
      else
      {
        result [ j ] = ( tmp1 );
        j++ ;
        tmp1 = "";//$NON-NLS-1$
      }
      if ( i == toDivC.length - 1 )
      {
        result [ j ] = ( tmp1 );
      }
    }
    return result;
  }


  /**
   * checks, if javaversion is equal or higher than given version
   * 
   * @param neededMaster int, in 1.5 neededMaster ist the 1
   * @param neededSlave int, in 1.5 neededSlave ist the 5
   * @return boolean, true, if version ist equal or higher
   */
  public static boolean isJavaRightVersion ( int neededMaster, int neededSlave )
  {
    int first = 0;
    int second = 0;
    // String contrains version like 1.5.0_006
    String version = System.getProperty ( "java.version" );//$NON-NLS-1$
    // Regular expression [.] for ., get 1 and 5 from 1.5, only this part will
    // be tested
    // the split function only exists from 1.4, so self implementatet will be
    // uses
    // String[] versionA = version.split("[.]");
    String [] versionA = split ( version, '.' );

    try
    {
      first = Integer.parseInt ( versionA [ 0 ] );
      second = Integer.parseInt ( versionA [ 1 ] );
    }
    catch ( NumberFormatException e )
    {
      new MsgFrame (
          "unknown JAVA version",//$NON-NLS-1$
          "The result of the java-version check is not valid.\n"//$NON-NLS-1$
              + versionA [ 0 ]
              + "."//$NON-NLS-1$
              + versionA [ 1 ]
              + "\n"//$NON-NLS-1$
              + "to start TPML please call it using -f. \"java -jar de.unisiegen.tpml.ui-1.2.0.jar -f\n"//$NON-NLS-1$
              + "node: de.unisiegen.tpml.ui-1.2.0.jar stands for the actual version." );//$NON-NLS-1$
    }

    // lexografik order: in 2.5, the 5 isn't important
    if ( first > neededMaster )
    {
      return true;
    }
    if ( ( first == neededMaster ) && ( second >= neededSlave ) )
    {
      return true;
    }
    return false;
  }


  /**
   * checks the version, runs given programm if version ist valid with -f the
   * execution can be forced
   * 
   * @param args Format: version programm [-f]
   */
  public static void main ( String args[] )
  {
    int countArgs = args.length;
    int neededMaster = MASTERN;
    int neededSlave = SLAVEN;
    boolean force = false;

    if ( countArgs > 0 )
    {
      for ( int i = 0 ; i < args.length ; i++ )
      {
        if ( args [ i ].equalsIgnoreCase ( "-f" ) )//$NON-NLS-1$
        {
          force = true;
        }
      }
    }
    // if -f is set- programm starts wihtout any test
    if ( force )
    {
      // build the other arguments to provide open files
      String arguments[] = new String [ args.length - 1 ];
      int j = 0;
      for ( int i = 0 ; i < args.length ; i++ )
      {
        // the -f will be ignored because the main.class dose not use it
        if ( args [ i ].equalsIgnoreCase ( "-f" ) )//$NON-NLS-1$
        {
          // nothing to do, do not copy to Arguments
        }
        else
        {
          arguments [ j ] = args [ i ];
          j++ ;
        }
      }
      Main.main ( arguments );
    }
    else
    // no force, check
    {
      try
      {
        // for errormessages bevor 1.2
        // for nicer errormessages
        boolean is12 = isJavaRightVersion ( 1, 2 );
        // boolean is12 = false;

        // checks version
        boolean isRight = isJavaRightVersion ( neededMaster, neededSlave );

        if ( isRight )
        {
          try
          {
            Main.main ( args );
          }
          catch ( Exception e )
          {
            String message = "The programm could not be started!";//$NON-NLS-1$
            String title = "Error while running TPML";//$NON-NLS-1$
            if ( is12 )
            {
              new JOpFrame ( title, message );
              // new MsgFrame(title, message);
              System.exit ( 1224 );
            }
            else
            {
              new MsgFrame ( title, message );
              System.exit ( 1224 );
            }
          }
        }
        else
        {
          String message = "Java "//$NON-NLS-1$
              + neededMaster
              + "."//$NON-NLS-1$
              + neededSlave
              + " not founded! Install this version or above. To sidestep the version test and force TPML to start\r"//$NON-NLS-1$
              + "please run \"java -jar de.unisiegen.tpml.ui-1.2.0.jar -f\".";//$NON-NLS-1$
          String title = "Wrong JAVA version";//$NON-NLS-1$
          if ( is12 )
          {
            new JOpFrame ( title, message );
            // new MsgFrame(title, message);
            System.exit ( 55 );
          }
          else
          {
            new MsgFrame ( title, message );
            System.exit ( 55 );
          }
        }
      }
      catch ( IndexOutOfBoundsException e )
      {
        // errormessages for developer...
        System.out.println ( "The needed Version number must be given" );//$NON-NLS-1$
        System.out.println ( "JavaTest version" );//$NON-NLS-1$
        System.out.println ( "z.B.: JavaTest 1.4" );//$NON-NLS-1$
        System.exit ( 125 );
      }
      catch ( NumberFormatException e )
      {
        // Fehlermeldungen für Entwickler
        System.out
            .println ( "The version must be given correctly. The given Version was:"//$NON-NLS-1$
                + args [ 0 ] );
        System.out.println ( "JavaTest version" );//$NON-NLS-1$
        System.out.println ( "z.B.: JavaTest 1.4" );//$NON-NLS-1$
        System.exit ( 125 );
      }
    }
  }
}
