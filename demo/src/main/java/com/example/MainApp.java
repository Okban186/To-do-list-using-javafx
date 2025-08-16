package com.example;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.FileLock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainApp extends Application {
  public List<To_Do> todos = new LinkedList<>();
  private double widthRoot = 800;
  private double heightRoot = 600;
  private Image imageAddNewToDoDo ;
  private Image imageAddNewToDoLazy;
  private ImageView mainImageAddNewToDo = new ImageView();
  private Pane paneForImageAddNewToDO = new Pane();
  private double heightvBox = heightRoot;
  private Pane root = new Pane();
  private ImageCache cache = new ImageCache();
  private  ScheduledExecutorService upDateData = Executors.newScheduledThreadPool(2);
  private ThreadPoolExecutor pools = new ThreadPoolExecutor(1, 1, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
  private LocalDate currentDay = LocalDate.now();
  private boolean dataChange = false;
  private inputForm form = new inputForm();
  private StackPane formPane = new StackPane();
  private double size = 0;
  private ReadAndWriteContentData contentData = new ReadAndWriteContentData();
  private List<ToDoPropertise> listContentData = new LinkedList<>();
  private boolean click = true;
  private int spacing = 10;
  private int bottom_padding = 50;
  private int top_padding = 100;
  private double LocationOfVBox = 0;
   private static FileLock lock;
    private static FileOutputStream lockFileStream;
  private Image backgroundRoot = new Image(getClass().getResource(
      "/HD wallpaper_ Anime, Nekopara, Chocola (Nekopara), Vanilla (Nekopara).jpg").toString());

  public static void main(String[] args) throws Exception {
       File lockFile = new File(System.getProperty("java.io.tmpdir"), "To Do List.lock");

        try {
            lockFileStream = new FileOutputStream(lockFile);
            lock = lockFileStream.getChannel().tryLock(); // Cố gắng khóa file

            if (lock == null) {
                System.exit(0); // Thoát nếu không thể khóa file
            }

            // Chạy JavaFX
            launch(args);

        } catch (IOException e) {
            System.exit(1);
        } finally {
            // Giải phóng file lock khi thoát ứng dụng
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (lock != null) lock.release();
                    if (lockFileStream != null) lockFileStream.close();
                    lockFile.delete(); // Xóa file khi thoát
                } catch (IOException ignored) {}
            }));
        }

  }

  @Override
  public void stop() throws Exception {
    // TODO Auto-generated method stub
    super.stop();
    contentData.writeData(listContentData);
    upDateData.shutdown();
    pools.shutdown();
    System.exit(0);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Scene scene = new Scene(createContians());
    scene.setFill(null);
    stage.setScene(scene);
    stage.getIcons().add(cache.getImage(getClass().getResource("/meuuuu.jpg").toString()));
    stage.setResizable(false);
    stage.setTitle("TO DO LIST");
    // stage.initStyle(StageStyle.UNDECORATED);
    stage.show();
  }

  public Pane createContians() {
    contentData.check();
    
    VBox vBox = new VBox();
    vBox.setPrefSize(widthRoot, heightvBox);
    vBox.setPadding(new Insets(top_padding, 0, bottom_padding, 0));
    vBox.setSpacing(spacing);
    vBox.setAlignment(Pos.TOP_CENTER);

    ScrollBar scrollOfVbox = new ScrollBar();
    
    // ds
    createToDo(vBox, scrollOfVbox);
    addAction(vBox, scrollOfVbox);
    setUpPaneForm();
    setUpImage();
    scrollSetUp(vBox,scrollOfVbox);
    vBox.setStyle("-fx-background-color: transparent;");
    vBox.setBackground(null);
    root.setBackground(new Background(new BackgroundImage(
        backgroundRoot,
        BackgroundRepeat.NO_REPEAT, // Lặp lại theo trục X
        BackgroundRepeat.NO_REPEAT, // Lặp lại theo trục Y
        BackgroundPosition.CENTER, // Căn giữa
        new BackgroundSize(
            BackgroundSize.AUTO, // Kích thước rộng tự động
            BackgroundSize.AUTO, // Kích thước cao tự động
            false, // Không phủ toàn bộ theo chiều ngang
            false, // Không phủ toàn bộ theo chiều dọc
            true, // Co giãn theo tỉ lệ
            true // Không phủ toàn bộ vùng chứa
        ))));
    root.setPrefSize(widthRoot, heightRoot);
    root.getChildren().addAll(vBox, scrollOfVbox, paneForImageAddNewToDO);
    updatePerCycle();
    root.setOnMouseClicked(e -> {
      if (!click)
        return;
        
      doFetueOfOptions(vBox, scrollOfVbox);
     

    });
    return root;
  }

  public void createToDo(VBox vBox, ScrollBar scrollOfVbox) {
    listContentData = contentData.readData();

    for (ToDoPropertise data : listContentData) {
      To_Do t = new To_Do();
      t.setToDoPropertise(data);
      t.setImageData(cache);
      t.build();
      String dateString[] = t.tPropertise.getDate().split("/");
      if((Integer.parseInt(dateString[2]) != currentDay.getYear() || 
      Integer.parseInt(dateString[1]) != currentDay.getMonthValue() || 
      Integer.parseInt(dateString[0]) != currentDay.getDayOfMonth()))
      t.overDate();
      size += t.getMaxHeight() + spacing;
      todos.add(t);
      vBox.getChildren().add(t);
      t = null;
    }
    if (size > heightvBox - (top_padding + bottom_padding)) {
      heightvBox = size + (top_padding + bottom_padding) ;
      vBox.setPrefHeight(heightvBox);
    }


    //scrollbar
    scrollOfVbox.setOrientation(Orientation.VERTICAL); // Chiều của bar
    scrollOfVbox.setMin(0); // giá trị nhỏ nhất của thanh cuộn
    scrollOfVbox.setPrefHeight(heightRoot); // chiều cao của thanh cuộn
    scrollOfVbox.setUnitIncrement(20); //toc độ cuộn mỗi lần kéo thanh trượt hoặc cuộn chuột
    scrollOfVbox.setBlockIncrement(50);// tốc độ cuộn mỗi lần nhất vào thanh trượt
    scrollOfVbox.setMax(heightvBox - heightRoot);// xét giá trị lớn nhất của thanh cuộn
    scrollOfVbox.setVisibleAmount((heightvBox-heightRoot)/2); // xét đô lơn của thanh trượt
    
    //scroll by mouse
    


  }
  public void setUpImage(){
    imageAddNewToDoDo = cache.getImage(getClass().getResource("/wantnewtodo.png").toString());
    imageAddNewToDoLazy = cache.getImage(getClass().getResource("/notthingtodo.png").toString());
    paneForImageAddNewToDO.getChildren().add(mainImageAddNewToDo);
    paneForImageAddNewToDO.setBackground(null);
    mainImageAddNewToDo.setImage(imageAddNewToDoLazy);
    paneForImageAddNewToDO.setLayoutX(widthRoot - 120);
    paneForImageAddNewToDO.setLayoutY(heightRoot - 130);
    mainImageAddNewToDo.setFitWidth(120);
    mainImageAddNewToDo.setFitHeight(100);
    

  }
  public void doFetueOfOptions(VBox vBox, ScrollBar scrollOfVbox) {
    for (int i = 0; i < todos.size(); i++) {
      To_Do t = todos.get(i);
      if (t.countClick == 1) {
        t.countClick += 1;
    
      } else {
        if (t.Feture.wantDelete()) {
          todos.remove(t);
          listContentData.remove(t.tPropertise);
          vBox.getChildren().remove(t);
          updateUI(vBox, scrollOfVbox,-(t.getMaxHeight()+spacing));
          t.clickHandle();
          dataChange = true;
          t = null;
          System.gc();
          break;
        }
        if (t.Feture.wantEdit() && !t.isDone.isSelected() && !t.isOver) {
          t.Feture.editClicked = false;
          root.getChildren().add(formPane);
          click = false;
          t.clickHandle();
          form.setContent(t.tPropertise.getContent());
          EditContent(t, vBox, scrollOfVbox);
          
          t.countClick = 0;
          t = null;
          break;
        } else {
          t.countClick = 0;
          t.clickHandle();
        }
      }
      t = null;

    }
  

  }

  public void addAction(VBox vBox, ScrollBar scrollOfVbox) {

    paneForImageAddNewToDO.setOnMouseClicked(e -> {
      root.getChildren().add(formPane);
      click = false;
      for (To_Do t : todos)
        t.clickHandle();
      addNewToDoContent(vBox, scrollOfVbox);

    });
    paneForImageAddNewToDO.setOnMouseEntered(e -> {
      
      mainImageAddNewToDo.setImage(imageAddNewToDoDo);
    });
    paneForImageAddNewToDO.setOnMouseExited(e -> {
      
      mainImageAddNewToDo.setImage(imageAddNewToDoLazy);
    });
  }

  public void EditContent(To_Do idx, VBox vBox, ScrollBar scrollOfVbox) {
    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        if (wantNewContent()) {
          synchronized (this) {
            if(!idx.tPropertise.getContent().equals(form.getContent()))
            idx.setContentMission(form.getContent());
            
          }
          Platform.runLater(() -> {
            form.setContent("");
            idx.onUpdate();
            root.getChildren().remove(formPane);
            updateUI(vBox, scrollOfVbox,idx.sizeIsChange);
            idx.sizeIsChange = 0;
            System.gc();
          });

        }
        
        return null;
      }
    };
    pools.submit(task);
  }
public void scrollSetUp(VBox vBox,ScrollBar scrollOfVbox){
  scrollOfVbox.valueProperty().addListener((obs, oldValue, newValue) -> { // logic để trượt
    vBox.setLayoutY(-newValue.doubleValue());
    LocationOfVBox = -newValue.doubleValue();
  });
  vBox.setOnScroll(event -> {
    double deltaY = event.getDeltaY(); // Giá trị cuộn dọc (âm hoặc dương)
    double currentValue = scrollOfVbox.getValue();
    double newValue = currentValue - deltaY / 2; // Điều chỉnh tốc độ cuộn (chia cho 10)

    // Giới hạn giá trị trong khoảng min và max của ScrollBar
    if (newValue < scrollOfVbox.getMin()) {
      newValue = scrollOfVbox.getMin();
    } else if (newValue > scrollOfVbox.getMax()) {
      newValue = scrollOfVbox.getMax();
    }

    scrollOfVbox.setValue(newValue); // Cập nhật giá trị ScrollBar
  });

}
  public void addNewToDoContent(VBox vBox, ScrollBar scrollOfVbox) {
    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        if (wantNewContent()) {
          To_Do t = new To_Do();
          t.setToDoPropertise(new ToDoPropertise());
          t.setDateNow();
          t.setImageData(cache);
          t.build();
          t.setContentMission(form.getContent());
          form.setContent("");
          listContentData.add(t.tPropertise);
          Platform.runLater(() -> {
            todos.add(t);
            t.onUpdate();
            vBox.getChildren().add(t);
            updateUI(vBox, scrollOfVbox,t.getMaxHeight()+spacing);
            root.getChildren().remove(formPane);
            System.gc();
          });
          
        }
        return null;
      }
    };
    pools.submit(task);
  }

  public boolean wantNewContent() {
   
    while (true) {
      try{
      Thread.sleep(20);
      if (form.pushSave()) {
        form.isPushSave = false;
        if(form.getContent().isBlank()) continue;
        click = true;
        dataChange = true;
        return true;
      }
      if (form.pushCancle()) {
        form.isPushCancle = false;
        click = true;
        form.setContent("");
        Platform.runLater(() -> root.getChildren().remove(formPane));
        return false;
      }
    }catch(Exception e){

    }
    }
  }

  private void setUpPaneForm() {
    formPane.setAlignment(Pos.CENTER);
    formPane.setPrefSize(widthRoot, heightRoot);
    formPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    formPane.getChildren().add(form);
  }
  public void updateUI(VBox vBox, ScrollBar scrollOfVbox, double sizeOfNodeToDo) {
    heightvBox = heightRoot;
    size+=sizeOfNodeToDo;
    if (size > heightvBox - (top_padding + bottom_padding)) {
      heightvBox = size + (top_padding + bottom_padding) ;
    }
    if (LocationOfVBox - (heightvBox - vBox.getPrefHeight()) < 0){
      vBox.setLayoutY(LocationOfVBox -= heightvBox - vBox.getPrefHeight());
    scrollOfVbox.setValue(-LocationOfVBox);
    }
    if(heightRoot >= heightvBox){
      LocationOfVBox = 0;
      vBox.setLayoutY(LocationOfVBox);
    scrollOfVbox.setValue(-LocationOfVBox);
    }
    vBox.setPrefHeight(heightvBox);
    scrollOfVbox.setMax(heightvBox - heightRoot);
    scrollOfVbox.setVisibleAmount((heightvBox-heightRoot)/2);
  }
  public void updatePerCycle(){
     Runnable updateStateTask = () -> {
            for(To_Do t : todos){
              if(t.isDone.isSelected()) t.isDone.setDisable(true);
              else t.overDate();
            }
        };

        // Tính thời gian còn lại cho đến 00:00 (midnight)
        LocalTime now = LocalTime.now();
        LocalTime midnight = LocalTime.MIDNIGHT;
        long initialDelay = Duration.between(now, midnight).getSeconds();
        if(initialDelay < 0) initialDelay+=24 * 60 * 60;
        // Lên lịch công việc 1 (in Hello) vào 00:00 mỗi ngày
        upDateData.scheduleAtFixedRate(updateStateTask, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS); // Lặp lại mỗi 24 giờ

        // Công việc 2: Chạy công việc khác mỗi 6 phút
        Runnable periodicTask = () -> {
          
          if(dataChange){
            contentData.writeData(listContentData);
            dataChange = false;
          }
         

        };

        // Lên lịch công việc 2 (mỗi 6 phút)
        /*  ở phần initialDelay la 6*60 tuc la sau 6 phut moi chay va sau do thi chu trinh period 6 sẽ lặp lại
        mỗi 6 phút nếu để initialDelay là 0 thì sẽ thực hiện ngay tức khắc*/
        upDateData.scheduleAtFixedRate(periodicTask, 6, 6, TimeUnit.MINUTES); // Lặp lại mỗi 6 phút
    }
}
