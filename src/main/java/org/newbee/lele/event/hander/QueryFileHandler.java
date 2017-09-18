package org.newbee.lele.event.hander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import org.newbee.lele.DataCenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.newbee.lele.DataCenter.primaryStage;


public class QueryFileHandler implements EventHandler<ActionEvent> {
    private final FileChooser fileChooser = new FileChooser();
    public QueryFileHandler() {
        super();
        fileChooser.setTitle("选取 统计文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Word Files", "*.doc", "*.docx")
        );
    }
    @Override
    synchronized public void handle(ActionEvent event) {
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) return;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                sb.append(line).append('\n');
            }
            DataCenter.queryData.setText(sb.toString());
            DataCenter.queryPath.setText(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            DataCenter.alert("文件不存在" + file.getAbsolutePath());
        } catch (IOException e) {
            DataCenter.alert("文件读取出错" + file.getAbsolutePath());
        }
    }
}
