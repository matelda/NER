import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;


public class GazetteersImp {
	
	private HashSet<?>[] gazetters ;
	private String[] fileNames;
	final private int ARRAY_LENGTH = 8 ;
	public GazetteersImp()
	{
		loadGazetters();
	}
	@SuppressWarnings("unchecked")
	private void loadGazetters()
	{
		gazetters = new HashSet<?>[ARRAY_LENGTH];
		for (int i = 0; i < gazetters.length; ++i)
			gazetters[i] = new HashSet<String>();
		fileNames = new String[ARRAY_LENGTH];
		fileNames[0] = "firstNames";
		fileNames[1] = "lastNames";
		fileNames[2] = "capitals_1";
		fileNames[3] = "capitals_2";
		fileNames[4] = "capitals_3";
		fileNames[5] = "countries_1";
		fileNames[6] = "countries_2";
		fileNames[7] = "countries_3";
		File openF ;
		String str;
		BufferedReader in ;
		
        try {
       	for(int i = 0 ; i < fileNames.length ; i++)
       	{
       		openF = new File("Gazetters/"+fileNames[i]+".txt");
    			in = new BufferedReader( new InputStreamReader( new FileInputStream(openF), "UTF8"));
    			str = "";
    			while ((str = in.readLine()) != null) {
    				if (!str.isEmpty())
    				{
    					((HashSet<String>)gazetters[i]).add(str);
    				}
    			}
    	                in.close();
       	}
       	
		}
		    catch (UnsupportedEncodingException e) 
		    {
				System.out.println(e.getMessage());
		    } 
		    catch (IOException e) 
		    {
				System.out.println(e.getMessage());
		    }
		    catch (Exception e)
		    {
				System.out.println(e.getMessage());
		    }
	}
	
	public void compareWithGazettees(List<Token> tokens)
	{
		int i ;
		String oneWord;
		String twoWords = "";
		String threeWords = "";
		for(i = 0 ; i < tokens.size(); i++)
		{
			//matching with one word lists
			oneWord = tokens.get(i).getNormalizedWord();
			if(gazetters[0].contains(oneWord))
				tokens.get(i).setFirstName(true);
			if(gazetters[1].contains(oneWord))
				tokens.get(i).setLastName(true);
			if(gazetters[1].contains(oneWord))
				tokens.get(i).setCapitalName(true);
			if(gazetters[5].contains(oneWord))
				tokens.get(i).setCountryName(true);
			
			if(i < tokens.size()-1)
			{
				//matching with two words lists
				twoWords = oneWord + " " + tokens.get(i+1).getWord();
				if(gazetters[3].contains(twoWords))
				{
					tokens.get(i).setCapitalName(true);
					tokens.get(i+1).setCapitalName(true);
				}
				if(gazetters[6].contains(twoWords))
				{
					tokens.get(i).setCountryName(true);
					tokens.get(i+1).setCountryName(true);
				}
			}
			if(i < tokens.size()-2)
			{
				//matching with three words lists
				threeWords = twoWords + " " + tokens.get(i+2).getWord();
				if(gazetters[4].contains(threeWords))
				{
					tokens.get(i).setCapitalName(true);
					tokens.get(i+1).setCapitalName(true);
					tokens.get(i+2).setCapitalName(true);
				}
				if(gazetters[7].contains(threeWords))
				{
					tokens.get(i).setCountryName(true);
					tokens.get(i+1).setCountryName(true);
					tokens.get(i+2).setCountryName(true);
				}
			}
		}
	}
}
