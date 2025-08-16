package com.example;

import java.io.*;
import java.util.*;


public class ReadAndWriteContentData {
  
  private String userProfile = System.getProperty("user.home");
  public void check(){
    

    userProfile+="\\content.txt";
    File file = new File(userProfile);
    if(!file.exists()){
       try {
                // Tạo tệp mới nếu tệp không tồn tại
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("An error occurred while creating the file: " + e.getMessage());
            }
        
        }
  }

  @SuppressWarnings("unchecked")
  public List<ToDoPropertise> readData() {
    List<ToDoPropertise> data = new LinkedList<>();
    try {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(userProfile)));
      data = (List<ToDoPropertise>) ois.readObject();
      closeStream(ois);
    } catch (Exception e) {
    }
    return data;
  }

  public void writeData(List<ToDoPropertise> data) {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userProfile));
      oos.writeObject(data);
      closeStream(oos);
    } catch (Exception e) {

    }
  }

  public void closeStream(InputStream input) {
    if (input != null) {
      try {
        input.close();
      } catch (Exception e) {

      }
    }
  }

  public void closeStream(OutputStream output) {
    if (output != null) {
      try {
        output.close();
      } catch (Exception e) {

      }
    }
  }
}
