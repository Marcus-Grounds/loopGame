package unsw.loopmania;

import unsw.loopmania.BasicItems.*;
import unsw.loopmania.Buildings.*;
import unsw.loopmania.Cards.*;
import unsw.loopmania.Enemies.*;
import unsw.loopmania.GameMode.*;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {
    // TODO = possibly add other menus?

    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainController;
    //private BattleEnemyController battleEnemyController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        //load battle screen
        BattleEnemyController battleEnemyController = new BattleEnemyController();
        FXMLLoader battleLoader = new FXMLLoader(getClass().getResource("Battle.fxml"));
        battleLoader.setController(battleEnemyController);
        Parent battleRoot = battleLoader.load();

        //shop sell screen
        ShopSellController shopSellController = new ShopSellController();
        FXMLLoader shopSellLoader = new FXMLLoader(getClass().getResource("ShopSellView.fxml"));
        shopSellLoader.setController(shopSellController);
        Parent shopSellRoot = shopSellLoader.load();
        

        Scene scene = new Scene(mainMenuRoot);
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("world_with_twists_and_turns.json", battleEnemyController, null);
    

        //LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("basic_world_with_player.json");
        mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();
        

        mainController.setGameRoot(gameRoot);

        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        mainController.setMainMenuSwitcher(() -> {
            mainController.pause();
            switchToRoot(scene, mainMenuRoot, primaryStage);
            });
        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        mainController.setBattleSwitcher(() -> {  
            switchToRoot(scene, battleRoot, primaryStage);
            mainController.pause();
            battleEnemyController.startTimer();
        });
         mainController.setShopSellSwitcher(() -> {  
            switchToRoot(scene, shopSellRoot, primaryStage);
            mainController.pause();
            shopSellController.startTimer(mainController);
        });
       
        battleEnemyController.setGameSwitcher(() -> {  
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        shopSellController.setGameSwitcher(() -> {  
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        
        // deploy the main onto the stage
        gameRoot.requestFocus();
        //battleRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop(){
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    public static void switchToRoot(Scene scene, Parent root, Stage stage){
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
