package localizationsim.localization;

/**
 * 
 * 
 * @author TOkamoto
 * 
 */
public enum LocalizatinerType {
  MCL("mcl"), DEADRECKONING("dr"), EKF("ekf");

  private String value;

  private LocalizatinerType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public static LocalizatinerType create(String strType) throws CreateException {
    for(LocalizatinerType type :LocalizatinerType.values()){
      if(type.value.equals(strType))
        return type;
    }
    
    throw new CreateException("TYPE" + strType + " dose not exist."
        + " :There is no type such like that");

  }

}
