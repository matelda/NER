import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
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
		loadModel();
	}
	public void train(List<Token> tokens)
	{
		try
		{
			constructARFF(TRAIN,tokens);
//			System.out.print(instances);
			filter = new StringToWordVector();
			filter.setAttributeIndices("last");
			filter.setInputFormat(instances);
			Instances filteredData  = Filter.useFilter(instances, filter);
			classifier = new J48();
			
			//evaluation
			Instances evalins = filteredData ; 
			Evaluation eval = new Evaluation(evalins);
			eval.crossValidateModel(classifier, evalins, 10, new Random(1));
			
			//saving validaion
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("evaluation/"+modelFileName+"_eval")));
			out.write(eval.toSummaryString());
			out.write("\r\n");
			out.write(eval.toClassDetailsString());
			out.write("\r\n");
			out.write(eval.toMatrixString());
			out.write("\r\n");
            
			//classification
			classifier.buildClassifier(filteredData);
			out.write(classifier.toString());
            out.close();
            
			//saving the model
			ObjectOutputStream outclass = new ObjectOutputStream(new FileOutputStream("models/"+modelFileName));
			outclass.writeObject(classifier);
			outclass.close();
		}
		catch(IOException x)
		{
			x.printStackTrace();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void classify(List<Token> tokens)
	{
		constructARFF(CALSSIFY,tokens);
//		System.out.println(instances);
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
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("models/"+modelFileName));
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
	
	
	private void constructARFF(int task,List<Token> tokens)
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
			
			//creating an instance
			 Instance inst = new DenseInstance(1.0, vals);
			 inst.setDataset(instances);
			 if(task == CALSSIFY)
				 inst.setClassMissing();
			 else if(task == TRAIN)
				 inst.setClassValue(classNominalValues.indexOf(tokens.get(i).getNERClass()));
			 
			 
			instances.add(inst);
			
		}
	}

}
