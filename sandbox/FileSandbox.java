import java.io.File;
import java.io.IOException;


public class FileSandbox {

  public static void main(String args[]) throws IOException{
    File newfile = new File("./re3/test/dsajlf/jdlkas/");

    if (newfile.mkdirs()){
      System.out.println("ディレクトリの作成に成功しました");
      System.out.println(newfile.getPath());
      System.out.println(newfile.getName());
    }else{
      System.out.println("ディレクトリの作成に失敗しました");
    }
  }

}
