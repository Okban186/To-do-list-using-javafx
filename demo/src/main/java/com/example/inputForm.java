package com.example;

import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class inputForm extends Pane {
  private TextField inputToDo = new TextField();
  private Button save = new Button("Save");
  private Button cancle = new Button("Cancle");
  private double widthForm = 300;
  private double heightForm = 100;

  public inputForm() {
    setStyle(
        "-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: lightgray;");
    setMaxSize(widthForm, heightForm);
    setUpInputToDo();
    setUpSave();
    setUpCancle();
    pushCancleHandle();
    pushSaveHandle();
    getChildren().addAll(inputToDo, save, cancle);

  }

  public void setUpInputToDo() {
    inputToDo.setLayoutX(widthForm - 280);
    inputToDo.setLayoutY(heightForm - 80);
    inputToDo.setPrefWidth(widthForm - 40);
    inputToDo.setStyle(
        "-fx-border-width: 0 0 1px 0; " + // Xóa viền trên, trái và phải, chỉ giữ đường kẻ dưới
            "-fx-border-color: black; " + // Màu đường kẻ dưới là đen
            "-fx-background-color: transparent;" // Làm nền trong suốt để không có viền nền
    );
    inputToDo.setPromptText("Nhap viec can lam: ");
    inputToDo.setFont(new Font(15));
  }

  public void setUpSave() {
    save.setLayoutX(widthForm - 280);
    save.setLayoutY(heightForm - 40);
    
  }

  public boolean isPushSave = false;

  public boolean pushSave() {
    return isPushSave;
  }
public void pushSaveHandle(){
  save.setOnAction(e -> {
    isPushSave = true;
  });
}
  public boolean isPushCancle = false;

  public boolean pushCancle() {
    return isPushCancle;
  }
  public void pushCancleHandle(){
    cancle.setOnAction(e -> {
      isPushCancle = true;
    });
  }
  public String getContent() {
    return inputToDo.getText().toString();
  }

  public void setContent(String s) {
    inputToDo.setText(s);
  }

  public void setUpCancle() {
    cancle.setLayoutX(widthForm - 70);
    cancle.setLayoutY(heightForm - 40);
  }

  public void setTileOfSave(String s) {
    save.setText(s);
  }

  public void saveDisable(boolean avaiable) {
    save.setDisable(avaiable);
  }
}
