import java.util.List;

/**
 * 
 */

/**
 * @author matelda
 *
 */
public class TokenImpl implements Token{

	private String word ; 
	private int offset ;
	private int length ;
	private String cas ;
	private String stt ;
	private String num ;
	private String gen ;
	private String pos ;
	private String stem ;
	private String diacFreeStem ;
	private String gloss ;
	List<String> tokenized ;
	private String normalizedWord;
	private boolean hasPrefix;
	/**
	 * set by rule based comp.
	 */
	private String rulePrediction;//O, B-PERS, I-PERS, B-LOC, I-LOC, B-ORG, I-ORG
	/**
	 * used with ml
	 */
	private boolean perGazetter; 
	private boolean locGazetter;
	private boolean orgGazetter;
	/**
	 * person related gazetters
	 */
	private boolean completeName;
	private boolean firstName;
	private boolean lastName;
	private boolean honorific;
	private boolean title;
	/**
	 * org. related gazetters
	 */
	private boolean orgCompleteName;
	private boolean businessType;
	private boolean companyFollowingIndicator;
	private boolean companyPrecedingIndicator;
	private boolean perfixBusiness;
	/**
	 * loc. related gazetters
	 */
	private boolean direction1;
	private boolean direction2;
	private boolean direction3;
	private boolean direction4;
	private boolean direction5;
	private boolean cityName;
	private boolean countryName;
	private boolean stateName;
	private boolean capitalName;
	private boolean administrativeDivision;
	private boolean countryPreceedingIndicator;
	private boolean countryPostIndicator;
	private boolean cityPreceedingIndicator;
	private boolean cityPostIndicator;
	private boolean continent;
	private boolean mountain;
	private boolean river;
	private boolean monuments;
	private boolean oceanOrSea;
	private boolean preceedingPlace;
	/**
	 * class value used for training as an input and for prediction as an output
	 */
	private String NERClass;
	/**
	 * @param word
	 * @param offset
	 * @param length
	 * @param cas
	 * @param stt
	 * @param num
	 * @param gen
	 * @param pos
	 * @param stem
	 * @param gloss
	 */
	public TokenImpl(String word, int offset, int length, String cas, String stt,
			String num, String gen, String pos, String stem, String gloss) {
		super();
		String temp = word.replace("آ", "ا");
		temp = temp.replace("أ", "ا");
		temp = temp.replace("إ", "ا");
		temp = temp.replace("ٱ", "ا");
		this.word = temp;
		this.offset = offset;
		this.length = length;
		this.cas = cas;
		this.stt = stt;
		this.num = num;
		this.gen = gen;
		if(pos.equals("noun_prop"))
			this.pos = "noun";
		else
			this.pos = pos;
		this.stem = stem;
		this.gloss = gloss;
		if (stem != null)
		{
			this.diacFreeStem = removeDiac(stem);
		}
		else
		{
			this.diacFreeStem = null;
		}
		this.rulePrediction = "O";
		this.perGazetter = false;
		this.locGazetter = false;
		this.orgGazetter = false;
		
		completeName = false;
		firstName = false;
		lastName = false;
		honorific = false;
		title = false;
		orgCompleteName = false;
		businessType = false;
		companyFollowingIndicator = false;
		companyPrecedingIndicator = false;
		perfixBusiness = false;
		direction1 = false;
		direction2 = false;
		direction3 = false;
		direction4 = false;
		direction5 = false;
		cityName = false;
		countryName = false;
		stateName = false;
		capitalName = false;
		administrativeDivision = false;
		countryPreceedingIndicator = false;
		countryPostIndicator = false;
		cityPreceedingIndicator = false;
		cityPostIndicator = false;
		continent = false;
		mountain = false;
		river = false;
		monuments = false;
		oceanOrSea = false;
		preceedingPlace = false;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#setTokenized(java.util.List)
	 */
	@Override
	public void setTokenized(List<String> tokenized) {
		this.tokenized = tokenized;
		int tkNo = tokenized.size()-1 ;
		this.normalizedWord = tokenized.get(tkNo);
		if(tokenized.size() > 1)
		{
			this.hasPrefix = true;
		}
		else
		{
			this.hasPrefix = false;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see Token#hasPrefix()
	 */
	@Override
	public boolean hasPrefix() {
		return hasPrefix;
	}

	
	/* (non-Javadoc)
	 * @see Token#getWord()
	 */
	@Override
	public String getWord() {
		return word;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getOffset()
	 */
	@Override
	public int getOffset() {
		return offset;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getLength()
	 */
	@Override
	public int getLength() {
		return length;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getCas()
	 */
	@Override
	public String getCas() {
		return cas;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getStt()
	 */
	@Override
	public String getStt() {
		return stt;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getNum()
	 */
	@Override
	public String getNum() {
		return num;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getGen()
	 */
	@Override
	public String getGen() {
		return gen;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getPos()
	 */
	@Override
	public String getPos() {
		return pos;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getStem()
	 */
	@Override
	public String getStem() {
		return stem;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getDiacFreeStem()
	 */
	@Override
	public String getDiacFreeStem() {
		return diacFreeStem;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getGloss()
	 */
	@Override
	public String getGloss() {
		return gloss;
	}
	
	
	/* (non-Javadoc)
	 * @see Token#getTokenized()
	 */
	@Override
	public List<String> getTokenized() {
		return tokenized;
	}
	/**
	 * 
	 * @param stem
	 * @return the stem of the token with out any diacritics
	 */
	private String removeDiac(String stem)
	{
		String temp ; 
		temp = stem.replace("ْ", "");
		temp = temp.replace("َ", "");
		temp = temp.replace("ُ", "");
		temp = temp.replace("ّ", "");
		temp = temp.replace("ِ", "");
		temp = temp.replace("آ", "ا");
		temp = temp.replace("أ", "ا");
		temp = temp.replace("إ", "ا");
		temp = temp.replace("ٱ", "ا");
		return temp ;
	}

	
	/* (non-Javadoc)
	 * @see Token#getNormalizedWord()
	 */
	@Override
	public String getNormalizedWord() {
		return normalizedWord;
	}


	/* (non-Javadoc)
	 * @see Token#getRulePrediction()
	 */
	@Override
	public String getRulePrediction() {
		return rulePrediction;
	}

	/* (non-Javadoc)
	 * @see Token#setRulePrediction(java.lang.String)
	 */
	@Override
	public void setRulePrediction(String rulePrediction) {
		this.rulePrediction = rulePrediction;
	}

	/* (non-Javadoc)
	 * @see Token#isPerGazetter()
	 */
	@Override
	public boolean isPerGazetter() {
		return perGazetter;
	}

	/* (non-Javadoc)
	 * @see Token#setPerGazetter(boolean)
	 */
	@Override
	public void setPerGazetter(boolean perGazetter) {
		this.perGazetter = perGazetter;
	}

	/* (non-Javadoc)
	 * @see Token#isLocGazetter()
	 */
	@Override
	public boolean isLocGazetter() {
		return locGazetter;
	}

	/* (non-Javadoc)
	 * @see Token#setLocGazetter(boolean)
	 */
	@Override
	public void setLocGazetter(boolean locGazetter) {
		this.locGazetter = locGazetter;
	}

	/* (non-Javadoc)
	 * @see Token#isOrgGazetter()
	 */
	@Override
	public boolean isOrgGazetter() {
		return orgGazetter;
	}

	/* (non-Javadoc)
	 * @see Token#setOrgGazetter(boolean)
	 */
	@Override
	public void setOrgGazetter(boolean orgGazetter) {
		this.orgGazetter = orgGazetter;
	}

	/* (non-Javadoc)
	 * @see Token#isCompleteName()
	 */
	@Override
	public boolean isCompleteName() {
		return completeName;
	}

	/* (non-Javadoc)
	 * @see Token#setCompleteName(boolean)
	 */
	@Override
	public void setCompleteName(boolean completeName) {
		this.completeName = completeName;
	}

	/* (non-Javadoc)
	 * @see Token#isFirstNames()
	 */
	@Override
	public boolean isFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see Token#setFirstNames(boolean)
	 */
	@Override
	public void setFirstName(boolean firstNames) {
		this.firstName = firstNames;
	}

	/* (non-Javadoc)
	 * @see Token#isLastNames()
	 */
	@Override
	public boolean isLastName() {
		return lastName;
	}

	/* (non-Javadoc)
	 * @see Token#setLastNames(boolean)
	 */
	@Override
	public void setLastName(boolean lastNames) {
		this.lastName = lastNames;
	}

	/* (non-Javadoc)
	 * @see Token#isHonorific()
	 */
	@Override
	public boolean isHonorific() {
		return honorific;
	}

	/* (non-Javadoc)
	 * @see Token#setHonorific(boolean)
	 */
	@Override
	public void setHonorific(boolean honorific) {
		this.honorific = honorific;
	}

	/* (non-Javadoc)
	 * @see Token#isOrgCompleteName()
	 */
	@Override
	public boolean isOrgCompleteName() {
		return orgCompleteName;
	}

	/* (non-Javadoc)
	 * @see Token#setOrgCompleteName(boolean)
	 */
	@Override
	public void setOrgCompleteName(boolean orgCompleteName) {
		this.orgCompleteName = orgCompleteName;
	}

	/* (non-Javadoc)
	 * @see Token#isBusinessType()
	 */
	@Override
	public boolean isBusinessType() {
		return businessType;
	}

	/* (non-Javadoc)
	 * @see Token#setBusinessType(boolean)
	 */
	@Override
	public void setBusinessType(boolean businessType) {
		this.businessType = businessType;
	}

	/* (non-Javadoc)
	 * @see Token#isCompanyFollowingIndicator()
	 */
	@Override
	public boolean isCompanyFollowingIndicator() {
		return companyFollowingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#setCompanyFollowingIndicator(boolean)
	 */
	@Override
	public void setCompanyFollowingIndicator(boolean companyFollowingIndicator) {
		this.companyFollowingIndicator = companyFollowingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#isCompanyPrecedingIndicator()
	 */
	@Override
	public boolean isCompanyPrecedingIndicator() {
		return companyPrecedingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#setCompanyPrecedingIndicator(boolean)
	 */
	@Override
	public void setCompanyPrecedingIndicator(boolean companyPrecedingIndicator) {
		this.companyPrecedingIndicator = companyPrecedingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#isPerfixBusiness()
	 */
	@Override
	public boolean isPerfixBusiness() {
		return perfixBusiness;
	}

	/* (non-Javadoc)
	 * @see Token#setPerfixBusiness(boolean)
	 */
	@Override
	public void setPerfixBusiness(boolean perfixBusiness) {
		this.perfixBusiness = perfixBusiness;
	}

	/* (non-Javadoc)
	 * @see Token#isDirection1()
	 */
	@Override
	public boolean isDirection1() {
		return direction1;
	}

	/* (non-Javadoc)
	 * @see Token#setDirection1(boolean)
	 */
	@Override
	public void setDirection1(boolean direction1) {
		this.direction1 = direction1;
	}

	/* (non-Javadoc)
	 * @see Token#isDirection2()
	 */
	@Override
	public boolean isDirection2() {
		return direction2;
	}

	/* (non-Javadoc)
	 * @see Token#setDirection2(boolean)
	 */
	@Override
	public void setDirection2(boolean direction2) {
		this.direction2 = direction2;
	}

	/* (non-Javadoc)
	 * @see Token#isDirection3()
	 */
	@Override
	public boolean isDirection3() {
		return direction3;
	}

	/* (non-Javadoc)
	 * @see Token#setDirection3(boolean)
	 */
	@Override
	public void setDirection3(boolean direction3) {
		this.direction3 = direction3;
	}

	/* (non-Javadoc)
	 * @see Token#isDirection4()
	 */
	@Override
	public boolean isDirection4() {
		return direction4;
	}

	/* (non-Javadoc)
	 * @see Token#setDirection4(boolean)
	 */
	@Override
	public void setDirection4(boolean direction4) {
		this.direction4 = direction4;
	}

	/* (non-Javadoc)
	 * @see Token#isDirection5()
	 */
	@Override
	public boolean isDirection5() {
		return direction5;
	}

	/* (non-Javadoc)
	 * @see Token#setDirection5(boolean)
	 */
	@Override
	public void setDirection5(boolean direction5) {
		this.direction5 = direction5;
	}

	/* (non-Javadoc)
	 * @see Token#isCityName()
	 */
	@Override
	public boolean isCityName() {
		return cityName;
	}

	/* (non-Javadoc)
	 * @see Token#setCityName(boolean)
	 */
	@Override
	public void setCityName(boolean cityName) {
		this.cityName = cityName;
	}

	/* (non-Javadoc)
	 * @see Token#isCountryName()
	 */
	@Override
	public boolean isCountryName() {
		return countryName;
	}

	/* (non-Javadoc)
	 * @see Token#setCountryName(boolean)
	 */
	@Override
	public void setCountryName(boolean countryName) {
		this.countryName = countryName;
	}

	/* (non-Javadoc)
	 * @see Token#isStateName()
	 */
	@Override
	public boolean isStateName() {
		return stateName;
	}

	/* (non-Javadoc)
	 * @see Token#setStateName(boolean)
	 */
	@Override
	public void setStateName(boolean stateName) {
		this.stateName = stateName;
	}

	/* (non-Javadoc)
	 * @see Token#isCapitalName()
	 */
	@Override
	public boolean isCapitalName() {
		return capitalName;
	}

	/* (non-Javadoc)
	 * @see Token#setCapitalName(boolean)
	 */
	@Override
	public void setCapitalName(boolean capitalName) {
		this.capitalName = capitalName;
	}

	/* (non-Javadoc)
	 * @see Token#isAdministrativeDivision()
	 */
	@Override
	public boolean isAdministrativeDivision() {
		return administrativeDivision;
	}

	/* (non-Javadoc)
	 * @see Token#setAdministrativeDivision(boolean)
	 */
	@Override
	public void setAdministrativeDivision(boolean administrativeDivision) {
		this.administrativeDivision = administrativeDivision;
	}

	/* (non-Javadoc)
	 * @see Token#isCountryPreceedingIndicator()
	 */
	@Override
	public boolean isCountryPreceedingIndicator() {
		return countryPreceedingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#setCountryPreceedingIndicator(boolean)
	 */
	@Override
	public void setCountryPreceedingIndicator(boolean countryPreceedingIndicator) {
		this.countryPreceedingIndicator = countryPreceedingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#isCountryPostIndicator()
	 */
	@Override
	public boolean isCountryPostIndicator() {
		return countryPostIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#setCountryPostIndicator(boolean)
	 */
	@Override
	public void setCountryPostIndicator(boolean countryPostIndicator) {
		this.countryPostIndicator = countryPostIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#isCityPreceedingIndicator()
	 */
	@Override
	public boolean isCityPreceedingIndicator() {
		return cityPreceedingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#setCityPreceedingIndicator(boolean)
	 */
	@Override
	public void setCityPreceedingIndicator(boolean cityPreceedingIndicator) {
		this.cityPreceedingIndicator = cityPreceedingIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#isCityPostIndicator()
	 */
	@Override
	public boolean isCityPostIndicator() {
		return cityPostIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#setCityPostIndicator(boolean)
	 */
	@Override
	public void setCityPostIndicator(boolean cityPostIndicator) {
		this.cityPostIndicator = cityPostIndicator;
	}

	/* (non-Javadoc)
	 * @see Token#isContinent()
	 */
	@Override
	public boolean isContinent() {
		return continent;
	}

	/* (non-Javadoc)
	 * @see Token#setContinent(boolean)
	 */
	@Override
	public void setContinent(boolean continent) {
		this.continent = continent;
	}

	/* (non-Javadoc)
	 * @see Token#isMountain()
	 */
	@Override
	public boolean isMountain() {
		return mountain;
	}

	/* (non-Javadoc)
	 * @see Token#setMountain(boolean)
	 */
	@Override
	public void setMountain(boolean mountain) {
		this.mountain = mountain;
	}

	/* (non-Javadoc)
	 * @see Token#isRiver()
	 */
	@Override
	public boolean isRiver() {
		return river;
	}

	/* (non-Javadoc)
	 * @see Token#setRiver(boolean)
	 */
	@Override
	public void setRiver(boolean river) {
		this.river = river;
	}

	/* (non-Javadoc)
	 * @see Token#isMonuments()
	 */
	@Override
	public boolean isMonuments() {
		return monuments;
	}

	/* (non-Javadoc)
	 * @see Token#setMonuments(boolean)
	 */
	@Override
	public void setMonuments(boolean monuments) {
		this.monuments = monuments;
	}

	/* (non-Javadoc)
	 * @see Token#isOceanOrSea()
	 */
	@Override
	public boolean isOceanOrSea() {
		return oceanOrSea;
	}

	/* (non-Javadoc)
	 * @see Token#setOceanOrSea(boolean)
	 */
	@Override
	public void setOceanOrSea(boolean oceanOrSea) {
		this.oceanOrSea = oceanOrSea;
	}

	/* (non-Javadoc)
	 * @see Token#isPreceedingPlace()
	 */
	@Override
	public boolean isPreceedingPlace() {
		return preceedingPlace;
	}

	/* (non-Javadoc)
	 * @see Token#setPreceedingPlace(boolean)
	 */
	@Override
	public void setPreceedingPlace(boolean preceedingPlace) {
		this.preceedingPlace = preceedingPlace;
	}

	/* (non-Javadoc)
	 * @see Token#setNormalizedWord(java.lang.String)
	 */
	@Override
	public void setNormalizedWord(String normalizedWord) {
		this.normalizedWord = normalizedWord;
	}

	@Override
	public boolean isTitle() {
		return title;
	}

	@Override
	public void setTitle(boolean title) {
		this.title = title;
	}

	@Override
	public String getNERClass() {
		return NERClass;
	}

	@Override
	public void setNERClass(String nERClass) {
		NERClass = nERClass;
	}


	/* (non-Javadoc)
	 * @see Token#printToken()
	 */
	@Override
	public void printToken()
	{
		System.out.println("word "+word+"/lenght "+length+" /offset "+offset);
		System.out.println("cas "+cas+" /stt "+stt+" /num "+num+" /gen "+gen+" /pos "+pos+" /stem "
		+stem+" /gloss "+gloss+" /diacFreeStem "+diacFreeStem);
		String str ;
		if(countryName)
			str = "true";
		else
			str = "false";
		System.out.println("countryName   "+str);
		
		for(int i = 0 ; i < tokenized.size();i++)
		{
			System.out.println(tokenized.get(i));
		}
	}
	
}
