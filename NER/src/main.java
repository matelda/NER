import java.util.List;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MADAFeatureExtracion instance =  new MADAFeatureExtracion();
		GazetteersImp gz = new GazetteersImp();
		
		
		String text = "لما نكون سوا يا حياتي انا";
		List<Token> tok ;
		tok = instance.extract(text);
		gz.compareWithGazettees(tok);
		
		
		for (int i = 0 ; i < tok.size() ;i++)
		{
			tok.get(i).printToken();
		}
	}
	

}
