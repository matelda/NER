import java.util.List;

/**
 * <h1>Morphological analyser</h1>
 * This class extracts morphological features of a text of type String throght the use of madamira.
 * in addition to tokenizing the string into single word tokens
 */
public interface MorphologicalAnalyser {

	/**
	 * converts the text from a String to a list of Tokens (instances of the Tonken class)
	 * with their morphological features set
	 * 
	 * @param inputText - raw arabic text String
	 * @return list of Tokens 
	 */
	public abstract List<Token> extract(String inputText);

}