import java.util.List;

/**
 * <h1>Gazetteers</h1>
 * This class loads a total of 18 gazetteers from .txt files
 * searches them for tokens and sets the value of the 
 * attribute to true if the token's word is found in the corresponding gazetteer
 * 
 *
 */
public interface Gazetteers {

	/**
	 * searches for token's word  in all of the gazetteers and setes
	 * the value of the corresponding attribute to true if the word is found 
	 * 
	 * @param tokens
	 */
	public abstract void compareWithGazettees(List<Token> tokens);

}