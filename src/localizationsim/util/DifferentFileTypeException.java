package localizationsim.util;

public class DifferentFileTypeException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 6980579212099976452L;
  
  public DifferentFileTypeException(){
    super("File type is differenet from expected file type");
  }
  

}
