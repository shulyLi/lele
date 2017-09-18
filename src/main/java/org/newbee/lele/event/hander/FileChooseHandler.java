package org.newbee.lele.event.hander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import org.newbee.lele.DataCenter;

import java.io.File;
import java.io.IOException;

import static org.newbee.lele.DataCenter.primaryStage;


public class FileChooseHandler implements EventHandler<ActionEvent> {
    private final FileChooser fileChooser = new FileChooser();
    public FileChooseHandler(){
        super();
        fileChooser.setTitle("选取 统计文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Word Files", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("WPS Files", "*.wps")
        );
    }
    @Override
    synchronized public void handle(ActionEvent event) {

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null && file.exists()) {
            try {
                DataCenter.start(file);
            } catch (IOException e1) {
                DataCenter.alert("打开文件出错" + file.getAbsolutePath());
            }
        }
    }
}
