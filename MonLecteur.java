   //package lecteur;
   import composantsFSR.*;
   import javafx.application.Application;
   import javafx.scene.input.MouseEvent;
   import javafx.event.EventHandler;
   import javafx.stage.WindowEvent;
   import javafx.scene.image.*;
   import javafx.scene.layout.*;
   import javafx.scene.media.*;
   import javafx.beans.value.*;
   import javafx.util.Duration;
   import javafx.scene.Group;
   import javafx.scene.Scene;
   import javafx.stage.Stage;
   import javafx.animation.*;
   import javafx.scene.control.*;


   public class MonLecteur extends Application {
      static MediaView mediaView;
      static MediaBar mediaBar;
      static SearchBar searchBar;
      static ProgressBarFSR starProgress;
      private ImageView captureImage = new ImageView("./img/capture.png");


   //Modifier la methode start
      public void start(Stage st) {
         Group racine = new Group();      
         final Scene scene = new Scene(racine, 800, 500);
         st.getIcons().add(new Image("./img/logo.png"));
         st.setTitle("FSRPlayer");      
         st.setScene(scene);
         scene.getStylesheets().add("MonStyle.css");                                            // * * * * * * Insertion Style
         
      //Creation BorderPane
         BorderPane border = new BorderPane();
         border.setStyle("-fx-background-color: black;");
         st.setMinHeight(280);
         st.setMinWidth(380);
         scene.setRoot(border);
      
      //Placer la Vue au centre du BorderPane
         mediaView = new MediaView(null);
         border.setCenter(mediaView);
     

      //Placer le SearchBar au-dessous du Menu (hauteur de 24)
         searchBar = new SearchBar(scene);
         border.getChildren().add(searchBar);
         searchBar.relocate(0,24);
/*
      //Placer starProgres au-dessous du SearchBar (hauteur de 24+30)   
         starProgress = new ProgressBarFSR("star" , 5);
         starProgress.setValeur(5);
         border.getChildren().add(starProgress);*/
         // starProgress.relocate(0,54); 

      //Placer le Menubar en Haut du BorderPane
         MenuLecteur menubar = new MenuLecteur(scene);
         border.setTop(menubar);

      //Placer le mediaBar en bas du BorderPane //je dois changer le width sur Mediacontrol fonction setOnReady
        mediaBar = new MediaBar(scene.getWidth()-50);                                // * * * * * * Insertion MediaBar (Slider, Nom_Media & Temps_Ecoul�_Media)
        border.setBottom(mediaBar);

      //Placer MaList sur la droite du BorderPane
            // * * * * * * 

      //Ajouter icon de capture d'image
         captureImage.setFitWidth(44);
         captureImage.setFitHeight(36);
         double x = (scene.getWidth()-captureImage.getFitWidth())/2;
         captureImage.relocate(x,60);
         border.getChildren().add(captureImage);
 
      //Ajouter Listner sur Bordre (Effet Transition)
         final TranslateTransition ttMediaBar = new TranslateTransition(Duration.millis(400), mediaBar);   // * * * * * * Effet Transition pour MediaBar
         ttMediaBar.setToY(80);
         ttMediaBar.play();

         final TranslateTransition ttSearchBar = new TranslateTransition(Duration.millis(400), searchBar);   // * * * * * * Effet Transition pour MediaBar
         ttSearchBar.setToY(-30);
         ttSearchBar.play();


         border.setOnMouseEntered(                                                                 // * * * * * * Effet Transition pour MediaBar
                                 new EventHandler<MouseEvent>(){
                                    public void handle(MouseEvent e) {
                                    ttMediaBar.stop(); 
                                    ttMediaBar.setDelay(Duration.millis(0));
                                    ttMediaBar.setDuration(Duration.millis(200));
                                    ttMediaBar.setToY(0);
                                    ttMediaBar.play();
                                    ttSearchBar.stop();
                                    ttSearchBar.setDelay(Duration.millis(0));
                                    ttSearchBar.setDuration(Duration.millis(200));
                                    ttSearchBar.setToY(0); 
                                    ttSearchBar.play();
                                    }
                                 });
         border.setOnMouseExited(                                                                  // * * * * * * Effet Transition pour MediaBar
                                 new EventHandler<MouseEvent>(){
                                    public void handle(MouseEvent e) {
                                      ttMediaBar.stop();
                                      ttMediaBar.setDelay(Duration.millis(1000));
                                      ttMediaBar.setDuration(Duration.millis(500));
                                      ttMediaBar.setToY(80);
                                      ttMediaBar.play();
                                      ttSearchBar.stop();
                                      ttSearchBar.setDelay(Duration.millis(1000));
                                      ttSearchBar.setDuration(Duration.millis(500));
                                      ttSearchBar.setToY(-30);                                       
                                      ttSearchBar.play();
                                    }
                                 });
      captureImage.setOnMousePressed(
                                    new EventHandler<MouseEvent>(){
                                    public void handle(MouseEvent e) {
                                       captureImage.setOpacity(0.2);
                                       CaptureCoverFSR.captureImage(mediaView);
                                    }
                                 });
      captureImage.setOnMouseReleased(
                                    new EventHandler<MouseEvent>(){
                                    public void handle(MouseEvent e) {
                                       captureImage.setOpacity(1);
                                    }
                                 });

      //Ajouter Listner sur window (Effet Zoom)
         scene.widthProperty().addListener(new ChangeListener<Number>() {  
             public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                     final double largeurApp = scene.getWidth();
                     mediaView.setFitWidth(largeurApp);

                     MonLecteur.mediaBar.setLargeurSlider(largeurApp);                    //* * * * * * * Adaptation Taille MediaBar au Media
                     MonLecteur.searchBar.setLargeurSearchBar(largeurApp);                //* * * * * * * Adaptation Taille SearchBar au Media
                     double x = (scene.getWidth()-captureImage.getFitWidth())/2;          //* * * * * * * Adaptation Taille CaptureImage
                     captureImage.relocate(x,60);

             }
            });
         scene.heightProperty().addListener(new ChangeListener<Number>() { 
             public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                     mediaView.setFitHeight(scene.getWindow().getHeight());
             }
            });
         CaptureCoverFSR.correctionBD();
         scene.getWindow().setOnCloseRequest( new EventHandler<WindowEvent>() {                
               public void handle(WindowEvent we) {                                          
                     CaptureCoverFSR.saveID();
             }
            });              
         st.show();
      }        
   
      public static void main(String[] args) {       
         launch(args);
      }  
   }