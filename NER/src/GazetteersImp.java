import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;


public class GazetteersImp implements Gazetteers {
	
	private static final Object instance = new Object();
	
	private HashSet<?>[] gazetters ;
	private String[] fileNames;
	final private int ARRAY_LENGTH = 18 ;
	protected  GazetteersImp()
	{
		loadGazetters();
	}

	/**
	 * 
	 * @return an instance of the singlton object
	 */
	public static Object getInstance() {
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private void loadGazetters()
	{
		gazetters = new HashSet<?>[ARRAY_LENGTH];
		for (int i = 0; i < gazetters.length; ++i)
			gazetters[i] = new HashSet<String>();
		fileNames = new String[ARRAY_LENGTH];
		
		fileNames[0] = "perGazetteer_1";
		fileNames[1] = "locGazetteer_1";
		fileNames[2] = "locGazetteer_2";
		fileNames[3] = "locGazetteer_3";
		fileNames[4] = "orgGazetteer_1";
		fileNames[5] = "orgGazetteer_2";
		fileNames[6] = "orgGazetteer_3";
		fileNames[7] = "orgGazetteer_4";
		fileNames[8] = "orgGazetteer_5";
		fileNames[9] = "honorifics_1";
		fileNames[10] = "titles_1";
		fileNames[11] = "nameWithdefiniteArticle_1";
		fileNames[12] = "introductoryVerb_1";
		fileNames[13] = "companyFollowingIndicators_1";
		fileNames[14] = "companyPrecedingIndicator_1";
		fileNames[15] = "locPreceedingIndicator_1";
		fileNames[16] = "locPostIndicator_1";
		fileNames[17] = "preceedingPlace_1";
		
		File openF ;
		String str;
		BufferedReader in ;
		
        try {
       	for(int i = 0 ; i < fileNames.length ; i++)
       	{
       			openF = new File("gazetters/"+fileNames[i]+".txt");
    			in = new BufferedReader( new InputStreamReader( new FileInputStream(openF), "UTF8"));
    			str = "";
    			while ((str = in.readLine()) != null) {
    				if (!str.isEmpty())
    				{
    					((HashSet<String>)gazetters[i]).add(str.trim());
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
	
	/* (non-Javadoc)
	 * @see Gazetteers#compareWithGazettees(java.util.List)
	 */
	@Override
	public void compareWithGazettees(List<Token> tokens)
	{
		int i ;
		String oneWord;
		String twoWords = "";
		String threeWords = "";
		String fourWords = "";
		String fiveWords = "";
		
		for(i = 0 ; i < tokens.size(); i++)
		{
			//matching with one word lists
			oneWord = tokens.get(i).getNormalizedWord();
			if(gazetters[0].contains(oneWord))
				tokens.get(i).setPerGazetter(true);
			if(gazetters[1].contains(oneWord))
				tokens.get(i).setLocGazetter(true);
			if(gazetters[4].contains(oneWord))
				tokens.get(i).setOrgGazetter(true);
			if(gazetters[9].contains(oneWord))
				tokens.get(i).setHonorific(true);
			if(gazetters[10].contains(oneWord))
				tokens.get(i).setTitle(true);
			if(gazetters[11].contains(oneWord))
				tokens.get(i).setNameWithdefiniteArticle(true);
			if(gazetters[12].contains(oneWord))
				tokens.get(i).setIntroductoryVerb(true);
			if(gazetters[13].contains(oneWord))
				tokens.get(i).setCompanyFollowingIndicator(true);
			if(gazetters[14].contains(oneWord))
				tokens.get(i).setCompanyPrecedingIndicator(true);
			if(gazetters[15].contains(oneWord))
				tokens.get(i).setLocPreceedingIndicator(true);
			if(gazetters[16].contains(oneWord))
				tokens.get(i).setLocPostIndicator(true);
			if(gazetters[17].contains(oneWord))
				tokens.get(i).setPreceedingPlace(true);
			
			
			if(i < tokens.size()-1)
			{
				//matching with two words lists
				twoWords = oneWord + " " + tokens.get(i+1).getWord();
				if(gazetters[2].contains(twoWords))
				{
					tokens.get(i).setLocGazetter(true);
					tokens.get(i+1).setLocGazetter(true);
				}
				if(gazetters[5].contains(twoWords))
				{
					tokens.get(i).setOrgGazetter(true);
					tokens.get(i+1).setOrgGazetter(true);
				}
			}
			
			if(i < tokens.size()-2)
			{
				//matching with three words lists
				threeWords = twoWords + " " + tokens.get(i+2).getWord();
				if(gazetters[3].contains(threeWords))
				{
					tokens.get(i).setLocGazetter(true);
					tokens.get(i+1).setLocGazetter(true);
					tokens.get(i+2).setLocGazetter(true);
				}
				if(gazetters[6].contains(threeWords))
				{
					tokens.get(i).setOrgGazetter(true);
					tokens.get(i+1).setOrgGazetter(true);
					tokens.get(i+2).setOrgGazetter(true);
				}
			}
			
			if(i < tokens.size()-3)
			{
				//matching with four words lists
				fourWords = threeWords + " " + tokens.get(i+3).getWord();
				if(gazetters[7].contains(fourWords))
				{
					tokens.get(i).setOrgGazetter(true);
					tokens.get(i+1).setOrgGazetter(true);
					tokens.get(i+2).setOrgGazetter(true);
					tokens.get(i+3).setOrgGazetter(true);
				}
			}
			
			if(i < tokens.size()-4)
			{
				//matching with four words lists
				fiveWords = fourWords + " " + tokens.get(i+4).getWord();
				if(gazetters[8].contains(fiveWords))
				{
					tokens.get(i).setOrgGazetter(true);
					tokens.get(i+1).setOrgGazetter(true);
					tokens.get(i+2).setOrgGazetter(true);
					tokens.get(i+3).setOrgGazetter(true);
					tokens.get(i+4).setOrgGazetter(true);
				}
			}
		}
	}
}
