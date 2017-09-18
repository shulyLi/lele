package org.newbee.lele.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import org.newbee.lele.event.hander.FileChooseHandler;

import static org.newbee.lele.DataCenter.primaryStage;


public class MenuView {
    // 生成菜单 和 绑定 文件打开的 FileChooser
    public MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu fileMenu = new Menu("文件");
        MenuItem newMenuItem = new MenuItem("打开");
        MenuItem exitMenuItem = new MenuItem("退出");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        // 绑定 出发事件，filChooser;
        EventHandler<ActionEvent> handler = new FileChooseHandler();
        newMenuItem.setOnAction(handler);

        // 生成菜单树
        fileMenu.getItems().addAll(newMenuItem, new SeparatorMenuItem(), exitMenuItem);
        // 绑定 到菜单bar
        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
    }
}
