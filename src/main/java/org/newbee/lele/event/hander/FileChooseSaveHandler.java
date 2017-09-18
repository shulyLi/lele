package org.newbee.lele.event.hander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import org.newbee.lele.DataCenter;

import java.io.File;


public class FileChooseSaveHandler implements EventHandler<ActionEvent> {
    @Override
    synchronized public void handle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出结果");
        fileChooser.setInitialFileName("result.xls");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS Files", "*.xls"));
        File file = fileChooser.showSaveDialog(DataCenter.primaryStage);
        if (file != null) {
            DataCenter.exportExcel(file.getAbsolutePath());
        }
    }
}