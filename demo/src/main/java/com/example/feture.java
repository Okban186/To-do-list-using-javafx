package com.example;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class feture extends Pane {
  private double width = 100;
  private double height = 30;
  private Rectangle edit = new Rectangle(width, height);
  private Rectangle delete = new Rectangle(width, height);
  private Text tileEdit = new Text("Edit");
  private Text tileDelete = new Text("Delete");
  private Group nodeEdit = new Group();
  private Group nodeDelete = new Group();

  public feture() {
    setPrefSize(width, height*2);
    setStyle(
        "-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: lightgray;");
    setUpDelete();
    setUpEdit();
    handleDeleteClick();
    handleEditClick();
    getChildren().addAll(nodeEdit,nodeDelete);
  }

  public void setUpEdit() {
    edit.setFill(Color.WHITE);
    edit.setArcHeight(10);
    edit.setArcWidth(10);
    edit.setLayoutY(0);
    tileEdit.setFont(new Font(15));
    tileEdit.setLayoutX((width/2)-13);
    tileEdit.setLayoutY((height/2)+5);
    nodeEdit.getChildren().addAll(edit,tileEdit);
    nodeEdit.setOnMouseEntered(e-> {
      edit.setFill(Color.LIGHTGRAY);
    });
    nodeEdit.setOnMouseExited(e -> {
      edit.setFill(Color.WHITE);

    });

  }

  public void setUpDelete() {
    delete.setFill(Color.WHITE);
    delete.setArcHeight(10);
    delete.setArcWidth(10);
    delete.setLayoutY(30);
    tileDelete.setFont(new Font(15));
    tileDelete.setLayoutX((width/2)-21);
    tileDelete.setLayoutY((height/2)+35);
    nodeDelete.getChildren().addAll(delete,tileDelete); //Group
    nodeDelete.setOnMouseEntered(e -> {
      delete.setFill(Color.LIGHTGRAY);
    });
    nodeDelete.setOnMouseExited(e -> {
      delete.setFill(Color.WHITE);

    });
  }

  public boolean editClicked = false;
  private boolean deleteClicked = false;

  public boolean wantDelete() {
    return deleteClicked;
}

public void handleDeleteClick() {
    nodeDelete.setOnMouseClicked(e -> deleteClicked = true);
}
  public boolean wantEdit() {
    return editClicked;
  }
  public void handleEditClick(){
  nodeEdit.setOnMouseClicked(e -> {
    editClicked = true;
  });
}
}
