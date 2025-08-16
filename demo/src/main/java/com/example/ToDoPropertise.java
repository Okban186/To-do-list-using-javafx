
package com.example;

import java.io.Serializable;

public class ToDoPropertise implements Serializable {
  private String content;
  private String Date = "";
  private boolean isDone;

  public ToDoPropertise() {

  }

  public ToDoPropertise(String content, String Date, boolean isDone) {
    this.content = content;
    this.Date = Date;
    this.isDone = isDone;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setDate(String Date) {
    this.Date = Date;
  }

  public void setDone(boolean isDone) {
    this.isDone = isDone;
  }

  public String getContent() {
    return content;
  }

  public String getDate() {
    return Date;
  }

  public boolean getDone() {
    return isDone;
  }

  
}
