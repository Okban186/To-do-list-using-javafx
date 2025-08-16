package com.example;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.time.LocalDate;
import javafx.scene.layout.Pane;


public class To_Do extends Pane {
  private Text missionField;
  private TextFlow mission = new TextFlow();
  private Text options = new Text("⋮");
  private Text date = new Text();
  public ToDoPropertise tPropertise;
  private boolean state = false;
  public boolean isOver = false;
  public double sizeIsChange = 0;
  public int countClick = 0;
  private  LocalDate currentDate = LocalDate.now();
  private ImageView stateImage = new ImageView();
  private Image cat1;
  private Image cat2;
  public CheckBox isDone = new CheckBox();
  private Circle circle = new Circle(20);
  private double widthRe = 600;
  private double heightReOriginal = 60;
  private double heightRe = 60;
  private Rectangle rec = new Rectangle(widthRe, heightRe);
  public feture Feture = new feture();

  public void build() {
    
    
    Feture.setLayoutX(widthRe - 150);
    Feture.setLayoutY(heightRe - 85);
    setMaxHeight(heightRe);
    setMaxWidth(widthRe);
    setRec();
    setDate();
    setUpImage();
    setUpCheckBox();
    setMission(tPropertise.getContent());
    getChildren().addAll(isDone,rec, mission, date, circle, options,stateImage);
    setOutlineOfOptions();
    setOptionsButt();
    installEventHandle();
    onUpdate();
    
    setStyle("-fx-background-color: transparent;");
  }
  public void setUpCheckBox(){
    isDone.setLayoutX(widthRe-(widthRe+30));
    isDone.setStyle("-fx-font-size: 16px; -fx-pref-width: 50px; -fx-pref-height: 50px;");
    isDone.setSelected(tPropertise.getDone());
    isDone.getStylesheets().add(getClass().getResource("/UI/ok.css").toExternalForm());
  }
  public void setUpImage(){
  stateImage.setLayoutX(widthRe-300);
  stateImage.setLayoutY(-75);
  
}
  public void setRec() {
    rec.setFill(Color.LIGHTYELLOW);
    rec.setStrokeWidth(5);
    rec.setArcHeight(30);

    rec.setArcWidth(30);
  }

  public void setDate() {
    date.setText(tPropertise.getDate());
    date.setFont(new Font(15));
    date.setLayoutX(10);
    date.setLayoutY(heightRe - 10);
  }
  
  public void setContentMission(String s) {
    Platform.runLater(() -> {
      tPropertise.setContent(s);
      missionField.setText(s);
      missionField.getBoundsInParent().getWidth();
    });
  }
  public void setMission(String missionText) {
    missionField = new Text(missionText);
    missionField.setFont(new Font(15));
    missionField.setFill(Color.web("#3E2723"));
    missionField.setStrikethrough(isDone.isSelected());
    mission.getChildren().add(missionField);
    mission.setLayoutX(10);
    mission.setLayoutY(10);
    mission.setPrefWidth(widthRe - 50);
  }
  public void onUpdate() {
    requestFocus();
   layout();

 double mmm = mission.getBoundsInLocal().getHeight();
 
 if(heightReOriginal+mmm == heightRe){sizeIsChange = 0; return;}
 if(heightReOriginal+mmm < heightRe) sizeIsChange = -heightRe+(heightReOriginal+mmm); 
 else sizeIsChange = (heightReOriginal+mmm)-heightRe;
 heightRe = heightReOriginal + mmm;
    rec.setHeight(heightRe);
    isDone.setLayoutY((heightRe/2)-25);
    circle.setLayoutY(heightRe / 2);
    options.setLayoutY((heightRe / 2) + 10);
    date.setLayoutY(heightRe - 10);
    Feture.setLayoutY((heightRe / 2) - 85);
    setMaxHeight(heightRe);
    setMaxWidth(widthRe);
  
  }

  public void setOutlineOfOptions() {
    circle.setLayoutX(widthRe - 24);
    circle.setLayoutY(heightRe - 30);
    circle.setFill(null);
    circle.setOpacity(0.6);
  }

  public void setOptionsButt() {
    options.setLayoutX(widthRe - 30);
    options.setLayoutY(heightRe - 20);
    options.setFont(new Font(30));
  }

  public void installEventHandle() {
    LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
    new Stop(0, Color.web("#3E2723")), // Chocolate (màu nâu đậm)
    new Stop(1, Color.web("#FFF8E1")));
    setPickOnBounds(false);
    options.setOnMouseEntered(e -> {
      if (state == true)
        return;
      circle.setFill(Color.LIGHTGRAY);
      options.setOpacity(0.7);
      options.setCursor(Cursor.HAND);

    });
    options.setOnMouseClicked(e -> {
      if (state == true)
        return;
     
      countClick++;
      state = true;
      circle.setFill(Color.LIGHTGRAY);
      options.setOpacity(0.7);
      getChildren().add(Feture);
    });
    options.setOnMouseExited(e -> {
      if (state == true)
        return;
      circle.setFill(null);
      options.setOpacity(1);
    });
    setOnMouseEntered(e -> {
      if(isOver)
      stateImage.setImage(cat2);
      else
      stateImage.setImage(cat1);
      rec.setStroke(gradient);
    });
    
    setOnMouseExited(e -> {
      stateImage.setImage(null);
      rec.setStroke(null);
    });
    isDone.setOnAction(e ->{
      if(isOver) return;
      missionField.setStrikethrough(isDone.isSelected());
      tPropertise.setDone(isDone.isSelected());
    
    });
   }

  public void clickHandle() {
    if (state == false)
      return;
    state = false;
    circle.setFill(null);
    options.setOpacity(1);
    getChildren().remove(Feture);
  }

  public void overDate(){
      if(!isDone.isSelected()){
        isOver = true;
        missionField.setFill(Color.RED);
      }
      isDone.setDisable(true);

    
     
  }
  public void setImageData(ImageCache cache){
    cat1 = cache.getImage(getClass().getResource("/missiondoneandnew.png").toString());
    cat2 = cache.getImage(getClass().getResource("/UALyeHBt.png").toString());
  }
  public void setDateNow() {
    tPropertise.setDate(currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue() + "/" + currentDate.getYear());
  }
  public void setToDoPropertise(ToDoPropertise toDoPropertise){
    tPropertise = toDoPropertise;
  }
}
