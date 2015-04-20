import java.util.List;

public interface Token {

	public abstract void setTokenized(List<String> tokenized);

	public abstract boolean hasPrefix();

	public abstract String getWord();

	public abstract int getOffset();

	public abstract int getLength();

	public abstract String getCas();

	public abstract String getStt();

	public abstract String getNum();

	public abstract String getGen();

	public abstract String getPos();

	public abstract String getStem();

	public abstract String getDiacFreeStem();

	public abstract String getGloss();

	public abstract List<String> getTokenized();

	public abstract String getNormalizedWord();

	public abstract String getRulePrediction();

	public abstract void setRulePrediction(String rulePrediction);

	public abstract boolean isPerGazetter();

	public abstract void setPerGazetter(boolean perGazetter);

	public abstract boolean isLocGazetter();

	public abstract void setLocGazetter(boolean locGazetter);

	public abstract boolean isOrgGazetter();

	public abstract void setOrgGazetter(boolean orgGazetter);

	public abstract boolean isCompleteName();

	public abstract void setCompleteName(boolean completeName);

	public abstract boolean isFirstName();

	public abstract void setFirstName(boolean firstNames);

	public abstract boolean isLastName();

	public abstract void setLastName(boolean lastNames);

	public abstract boolean isHonorific();

	public abstract void setHonorific(boolean honorific);

	public abstract boolean isOrgCompleteName();

	public abstract void setOrgCompleteName(boolean orgCompleteName);

	public abstract boolean isBusinessType();

	public abstract void setBusinessType(boolean businessType);

	public abstract boolean isCompanyFollowingIndicator();

	public abstract void setCompanyFollowingIndicator(
			boolean companyFollowingIndicator);

	public abstract boolean isCompanyPrecedingIndicator();

	public abstract void setCompanyPrecedingIndicator(
			boolean companyPrecedingIndicator);

	public abstract boolean isPerfixBusiness();

	public abstract void setPerfixBusiness(boolean perfixBusiness);

	public abstract boolean isDirection1();

	public abstract void setDirection1(boolean direction1);

	public abstract boolean isDirection2();

	public abstract void setDirection2(boolean direction2);

	public abstract boolean isDirection3();

	public abstract void setDirection3(boolean direction3);

	public abstract boolean isDirection4();

	public abstract void setDirection4(boolean direction4);

	public abstract boolean isDirection5();

	public abstract void setDirection5(boolean direction5);

	public abstract boolean isCityName();

	public abstract void setCityName(boolean cityName);

	public abstract boolean isCountryName();

	public abstract void setCountryName(boolean countryName);

	public abstract boolean isStateName();

	public abstract void setStateName(boolean stateName);

	public abstract boolean isCapitalName();

	public abstract void setCapitalName(boolean capitalName);

	public abstract boolean isAdministrativeDivision();

	public abstract void setAdministrativeDivision(
			boolean administrativeDivision);

	public abstract boolean isCountryPreceedingIndicator();

	public abstract void setCountryPreceedingIndicator(
			boolean countryPreceedingIndicator);

	public abstract boolean isCountryPostIndicator();

	public abstract void setCountryPostIndicator(boolean countryPostIndicator);

	public abstract boolean isCityPreceedingIndicator();

	public abstract void setCityPreceedingIndicator(
			boolean cityPreceedingIndicator);

	public abstract boolean isCityPostIndicator();

	public abstract void setCityPostIndicator(boolean cityPostIndicator);

	public abstract boolean isContinent();

	public abstract void setContinent(boolean continent);

	public abstract boolean isMountain();

	public abstract void setMountain(boolean mountain);

	public abstract boolean isRiver();

	public abstract void setRiver(boolean river);

	public abstract boolean isMonuments();

	public abstract void setMonuments(boolean monuments);

	public abstract boolean isOceanOrSea();

	public abstract void setOceanOrSea(boolean oceanOrSea);

	public abstract boolean isPreceedingPlace();

	public abstract void setPreceedingPlace(boolean preceedingPlace);

	public abstract void setNormalizedWord(String normalizedWord);
	
	public abstract boolean isTitle();
	
	public abstract void setTitle(boolean title);
	
	public abstract String getNERClass();
	
	public abstract void setNERClass(String nERClass);

	public abstract void printToken();

}