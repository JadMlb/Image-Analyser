import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Main class that runs the app
 * 
 * @author Jad Malaeb - 201911093
 */
public class Main
{
	
	public static void main (String[] args)
	{
		Scanner s = new Scanner (System.in);

        System.out.println();
        System.out.println ("    #####     #   #      ###       ####     #####");
        System.out.println ("      #       ## ##     #   #     #         #");
        System.out.println ("      #       # # #     #####     #  ##     ##### ");
        System.out.println ("      #       #   #     #   #     #   #     #");
        System.out.println ("    #####     #   #     #   #      ###      #####");
        System.out.println();
        System.out.println();
        System.out.println (" ####     #####      ###      ####       ####    #   #");
        System.out.println ("#         #         #   #     #   #     #        #   #");
        System.out.println (" ###      #####     #####     ####      #        #####");
        System.out.println ("    #     #         #   #     #   #     #        #   #");
        System.out.println ("####      #####     #   #     #   #      ####    #   #");
        
		HashMap<String, String> cmd;
		
		do
		{
			System.out.println();
			System.out.println();
			System.out.println ("Enter command, \"?\" for help, or \"x\" to exit");
			System.out.print ("> ");
			
			String entry = s.nextLine();
			cmd = getCmdArgs (entry);
			
			switch (cmd.get ("p"))
			{
				case "localhost":
					{
						// Check the integrity of the command in order not to face errors. Make sure that the:
						// Provided path to search into exists and is a directory
						// Parameter representing the number of the output is an integer, if provided
						if (!new File(cmd.get ("-l")).exists() || !new File(cmd.get ("-l")).isDirectory() || (cmd.get ("-c") != null && !isInt (cmd.get ("-c"))))
						{
							System.out.println ("Invalid command localhost");
							break;
						}

						System.out.println ("Please wait a moment...");
						
						HashSet<String> keywords = getKeywords (cmd.get ("-k"));
						String dirSep = System.getProperty("os.name").toLowerCase().contains ("windows") ? "\\" : "/";
						ArrayList<LocalImage> results = new LocalImageSearcher().search (cmd.get ("-l") + (cmd.get("-l").endsWith (dirSep) ? "" : dirSep), keywords, (cmd.get ("-c") != null) ? Integer.parseInt (cmd.get ("-c")) : Searcher.MAX_RES);

						displayResults (cmd, results, keywords);
					}
					break;

				case "twitter":
					{
						// Check the integrity of the command in order not to face errors. Make sure that the:
						// Parameter representing the number of the output is an integer, if provided
						// Keywords are included
						if ((cmd.get ("-c") != null && !isInt (cmd.get ("-c"))) || cmd.get ("-k") == null)
						{
							System.out.println ("Invalid command twitter");
							break;
						}

						System.out.println ("Please wait a moment...");

						HashSet<String> keywords = getKeywords (cmd.get ("-k"));
						if (cmd.get("-l").startsWith ("@"))
							cmd.put ("-l", cmd.get("-l").substring (1));
						ArrayList<SocialMediaImage> results = new TwitterSearcher().search (cmd.get ("-l"), keywords, (cmd.get ("-c") != null) ? Integer.parseInt (cmd.get ("-c")) : Searcher.MAX_RES);
						
						displayResults (cmd, results, keywords);
					}
					break;

				case "?":
					System.out.println ("<platform>\n\tMANDATORY and FIRST argument.\n\tCan be \"localhost\" or any of the supported social media platforms (currently none)");
					System.out.println ("-l <location>\n\tMANDATORY for \"localhost\": directory to search into.\n\tOPTIONAL for other platforms: account tag (@john_doe)");
					System.out.println ("-c <number-of-results>\n\tNumber of resulting images. Can vary between 0 and the limit set");
					System.out.println ("-k <keywords>\n\tOPTIONAL for \"localhost\", otherwise MANDATORY\n\tKeywords to filter images in location, separated by a comma only");
					break;

				case "X", "x": System.out.println ("Closing the programme..."); break;
				
				default: System.out.println ("Unknown command!"); break;
			}
		}
		while (!cmd.get("p").equals ("x"));

		s.close();
	}

	/**
	 * Checks if string is a representation of an integer
	 * 
	 * @param s String to check if is an integer
	 * @return true if string is an integer
	 */
	private static boolean isInt (String s)
	{
		try
		{
			Integer.parseInt (s);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}

	/**
	 * Splits the command passed in the CLI and specifies the value of each field
	 * 
	 * @param cmd The command to be analyzed
	 * @return HashMap from String to String mapping the elements as passed in by the user
	 */
	private static HashMap<String, String> getCmdArgs (String cmd)
	{
		// Splits the string using positive lookahead or lookbehind in order to keep the separator (which are in this case " -l ", " -c ", " -k ")
		String[] args = cmd.split ("((?= -l | -c | -k )|(?<= -l | -c | -k ))");
		
		HashMap<String, String> hmArgs = new HashMap<>();
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].startsWith (" ") && args[i].endsWith (" ")) // Remove space at the beginning and end of separator
				args[i] = args[i].substring (1, args[i].length() - 1);

			if (i == 0)
				hmArgs.put ("p", args[0]);
			else if (args[i].equals ("-l"))
				hmArgs.put ("-l", args[++i]);
			else if (args[i].equals ("-c") && isInt (args[i + 1]))
				hmArgs.put ("-c", args[++i]);
			else if (args[i].equals ("-k"))
				hmArgs.put ("-k", args[++i]);
			else
				continue;
		}
		
		return hmArgs;
	}

	/**
	 * Splits the keywords in string form and removes redundancy
	 * 
	 * @param s The keywords all in one string
	 * @return HashSet of String including all keywords appearing each one time
	 */
	private static HashSet<String> getKeywords (String s)
	{
		HashSet<String> keywords = new HashSet<>();
		
		if (s != null)
		{
			String[] words = s.split (",");

			for (int i = 0; i < words.length; i++)
				keywords.add (words[i]);
		}

		return keywords;
	}

	/**
	 * Displays results
	 * 
	 * @param cmd The command passed in after being analysed by {@link Main#getCmdArgs(String)}
	 * @param results The ArrayList containing the results
	 * @param keywords The list of keywords that were the criteria
	 */
	private static void displayResults (HashMap<String, String> cmd, ArrayList<? extends Image> results, HashSet<String> keywords)
	{
		System.out.print ("\nFound " + results.size() + " images with keyword(s)");
		for (Iterator<String> it = keywords.iterator(); it.hasNext();)
			System.out.print (" " + it.next());
		if (cmd.get("p").equals ("localhost"))
			System.out.print (" in " + cmd.get ("-l") + " on " + cmd.get ("p") + "\n");
		else
			System.out.println (" in " + cmd.get ("p") + " database\n");

		if (!results.isEmpty())
			for (Image img : results)
				System.out.println (img);
	}
}

/*
!For instagram api
Call the methods in igAPItest.java to get the search results for the keywords.
for the n first accounts, use the api to retrieve the latest image containing those keywords, in hashtags or the image
display
*/