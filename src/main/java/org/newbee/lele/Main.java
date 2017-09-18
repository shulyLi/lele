package org.newbee.lele;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.newbee.lele.view.MenuView;
import org.newbee.lele.view.BodyView;

public class Main extends Application {
    private MenuView menuView = new MenuView();
    private BodyView newBodyView = new BodyView();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(DataCenter.ObjectHead);
        DataCenter.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        // 菜单
        {
            root.setTop(menuView.createMenu());
        }
        // 主体积
        {
            root.setCenter(newBodyView.getGridPane());
        }
        Scene scene = new Scene(root, 600, 400, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        Main.launch();
    }
}