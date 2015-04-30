import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;


/**
 * @author matelda
 *
 */
public class J48SingleClassifierImpl {

	/**
	 * @param args
	 */
	final private int TRAIN = 0 ;
	final private int CALSSIFY = 1;
	final private int NUM_OF_ATTRIBUTES = 19 ;
	double[] vals ;
	Instances instances;
	Classifier classifier;
	String modelFileName = "model";
	StringToWordVector filter;
	
	public J48SingleClassifierImpl()
	{
//		loadModel();
	}
	public void train(List<Token> tokens)
	{
		try
		{
			constructARFF(TRAIN,tokens);		
			filter = new StringToWordVector();
			filter.setAttributeIndices("last");
			filter.setInputFormat(instances);
			Instances filteredData  = Filter.useFilter(instances, filter);
			classifier = new J48();
			classifier.buildClassifier(filteredData);
			//saving the model
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(modelFileName));
            out.writeObject(classifier);
            out.close();
		}
		catch(IOException x)
		{
			
		}
		catch(Exception ex)
		{
			
		}
	}
	public void classify(List<Token> tokens)
	{
		constructARFF(CALSSIFY,tokens);
		double pred;
		try
		{
			for(int i =0 ; i < instances.size();i++)
			{
				pred = classifier.classifyInstance(instances.instance(i));
				tokens.get(i).setNERClass(instances.classAttribute().value((int) pred));
			}
		}
			catch(Exception ex)
			{
				
			}
		
	}
	private void loadModel()
	{
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(modelFileName));
            Object tmp = in.readObject();
			classifier = (J48) tmp;
            in.close();
 			System.out.println("===== Loaded model: " + modelFileName + " =====");
       } 
		catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			System.out.println("Problem found when reading: " + modelFileName);
		}
	}
	public void constructARFF(int task,List<Token> tokens)
	{
		int numInstances = tokens.size();
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		double[] vals ;
		
		List<String> YNNominalValues = new ArrayList<>();
		YNNominalValues.addAll(Arrays.asList("n","y"));
		
		List<String> classNominalValues = new ArrayList<>();
		classNominalValues.addAll(Arrays.asList("O","B-PERS","I-PERS","B-LOC","I-LOC","B-ORG","I-ORG"));
		
		List<String> genNominalValues = new ArrayList<>();
		genNominalValues.addAll(Arrays.asList("f","m","na"));
		atts.add(new Attribute("gen",genNominalValues));
			
		List<String> casNominalValues = new ArrayList<>();
		casNominalValues.addAll(Arrays.asList("n","a","g","na","u"));
		atts.add(new Attribute("cas",casNominalValues));
			
		List<String> sttNominalValues = new ArrayList<>();
		sttNominalValues.addAll(Arrays.asList("i","d","na","u"));
		atts.add(new Attribute("stt",sttNominalValues));
			
		List<String> numNominalValues = new ArrayList<>();
		numNominalValues.addAll(Arrays.asList("s","p","d","na","u"));
		atts.add(new Attribute("num",numNominalValues));
		
		//other features
		
		atts.add(new Attribute("cap",YNNominalValues));
		atts.add(new Attribute("length",YNNominalValues));
		
		//gazetteers features
		
		atts.add(new Attribute("persGazetteer",YNNominalValues));
		atts.add(new Attribute("locGazetteer",YNNominalValues));
		atts.add(new Attribute("orgGazetteer",YNNominalValues));
		
		atts.add(new Attribute("persGazetteerR",YNNominalValues));//right neighbor(the next index)
		atts.add(new Attribute("locGazetteerR",YNNominalValues));
		atts.add(new Attribute("orgGazetteerR",YNNominalValues));
		
		atts.add(new Attribute("persGazetteerL",YNNominalValues));//left neighbor(the previous)
		atts.add(new Attribute("locGazetteerL",YNNominalValues));
		atts.add(new Attribute("orgGazetteerL",YNNominalValues));
		
		//Rule Based prediction
		
		atts.add(new Attribute("ruleBasedpre",classNominalValues));
		atts.add(new Attribute("ruleBasedpreL",classNominalValues));
		atts.add(new Attribute("ruleBasedpreR",classNominalValues));
		
		//class
		
		atts.add(new Attribute("class",classNominalValues));
		
		//create dataset
		
		instances = new Instances("Dataset", atts, numInstances);
		instances.setClassIndex(NUM_OF_ATTRIBUTES-1);
		vals = new double[NUM_OF_ATTRIBUTES];
		
		//add instances
		  
		for(int i = 0 ; i < numInstances ; i++)
		{ 
			vals[0] = genNominalValues.indexOf(tokens.get(i).getGen());
			vals[1] = casNominalValues.indexOf(tokens.get(i).getCas());
			vals[2] = sttNominalValues.indexOf(tokens.get(i).getStt());
			vals[3] = numNominalValues.indexOf(tokens.get(i).getNum());
			vals[4] = tokens.get(i).isCapitalLetter() ? 1 : 0 ;
			vals[5] = tokens.get(i).getWord().length()>3 ? 1 : 0;
			vals[6] = tokens.get(i).isPerGazetter() ? 1 : 0;
			vals[7] = tokens.get(i).isLocGazetter() ? 1 : 0;
			vals[8] = tokens.get(i).isOrgGazetter() ? 1 : 0;
			if(i == numInstances-1)
			{
				vals[9] = vals[10] = vals[11] = 0 ;
			}
			else
			{
				vals[9] = tokens.get(i+1).isPerGazetter() ? 1 : 0;
				vals[10] = tokens.get(i+1).isLocGazetter() ? 1 : 0;
				vals[11] = tokens.get(i+1).isOrgGazetter() ? 1 : 0;
			}
			if(i == 0)
			{
				vals[12] = vals[13] = vals[14] = 0 ;
			}
			else
			{
				vals[12] = tokens.get(i-1).isPerGazetter() ? 1 : 0;
				vals[13] = tokens.get(i-1).isLocGazetter() ? 1 : 0;
				vals[14] = tokens.get(i-1).isOrgGazetter() ? 1 : 0;
			}
			
			vals[15] = classNominalValues.indexOf(tokens.get(i).getRulePrediction());
			if(i == 0)
				vals[16] = 0 ;
			else
				vals[16] = classNominalValues.indexOf(tokens.get(i-1).getRulePrediction());
			if(i == numInstances-1)
				vals[17] = 0;
			else
				vals[17] = classNominalValues.indexOf(tokens.get(i+1).getRulePrediction());
			
			if(task == TRAIN)
				vals[NUM_OF_ATTRIBUTES-1] = classNominalValues.indexOf(tokens.get(i).getNERClass());
			
			//creating an instance
			 Instance inst = new DenseInstance(1.0, vals);
			 if(task == CALSSIFY)
				 inst.setMissing(NUM_OF_ATTRIBUTES-1);
			 
			instances.add(inst);
			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		J48SingleClassifierImpl classifier = new J48SingleClassifierImpl();
		MADAFeatureExtracion fx = new MADAFeatureExtracion();
		List<Token> tok ;
		String st = "محمد";
		tok = fx.extract(st);
		classifier.constructARFF(classifier.CALSSIFY, tok);
		System.out.println(classifier.instances);
	}

}
