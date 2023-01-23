package frc.utils;

import java.io.FileWriter;
import java.io.IOException;

public class JSON {

  public FileWriter file;

  public String data;

  public JSON(String fName) {

    try {
      file = new FileWriter(fName);
    }
    catch (IOException e) {
      System.out.println("IO exception");
      e.printStackTrace();
    }

    data = "[";
  }

  public void put(String key, String rawData) {
    data += "\"" + key + "\":\"" + rawData + "\",";
  }

  public String getData() {
    return data + "]";
    
  }

  public void newLevel() {
    data += "{";
  }

  public void closeLevel() {
    data += "}";
  }
}
