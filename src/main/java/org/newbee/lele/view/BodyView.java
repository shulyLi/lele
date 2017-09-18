package org.newbee.lele.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.newbee.lele.DataCenter;
import org.newbee.lele.data.SimpleData;
import org.newbee.lele.event.hander.FileChooseSaveHandler;
import org.newbee.lele.event.hander.QueryClickHandler;
import org.newbee.lele.event.hander.QueryFileHandler;


public class BodyView {
    // body 布局
    private GridPane gridPane  = null;
    private TextField fNameFld  = DataCenter.fNameFld;
    private TextField fPathFld  = DataCenter.fPathFld;
    private TextField queryPath = DataCenter.queryPath;
    private TextArea  queryData = DataCenter.queryData;

    private TableView<SimpleData.Item> table = DataCenter.table;

    public BodyView() {
        createGrid();
        initTable();
        initFileName();
        initFilePath();
        initOutExcel();
        initQueryButton();
    }


    public GridPane getGridPane() {
        return gridPane;
    }

    private void createGrid(){
        this.gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }
    private void initTable(){
        table.setEditable(true);
        TableColumn<SimpleData.Item, String> firstNameCol = new TableColumn<>(DataCenter.TABLE_HEAD[0]);

        TableColumn<SimpleData.Item, Integer> lastNameCol = new TableColumn<>(DataCenter.TABLE_HEAD[1]);

        table.setPrefWidth(220);
        firstNameCol.setPrefWidth(130);
        lastNameCol.setPrefWidth(90);

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("cnt"));
        table.getColumns().setAll(firstNameCol, lastNameCol);
        // 类似于 x,y轴 也就是 (x, y, x 占多少， y 站多少) （x,y） 是开始的点， 这样就生成一个矩形。
        gridPane.add(table, 0, 0, 1, 6);
    }
    private void initFileName(){
        Label fileName = new Label("文件名字");
        GridPane.setHalignment(fileName, HPos.LEFT);
        gridPane.add(fileName, 1, 0);
        GridPane.setHalignment(fNameFld, HPos.RIGHT);
        //fNameFld.setDisable(true);
        fNameFld.setEditable(false);
        fNameFld.setPrefWidth(200);
        gridPane.add(fNameFld, 2, 0);
    }
    private void initFilePath(){
        Label filePath = new Label("文件路径");
        GridPane.setHalignment(filePath, HPos.LEFT);
        gridPane.add(filePath, 1, 1);
        GridPane.setHalignment(fPathFld, HPos.RIGHT);
        fPathFld.setEditable(false);
        gridPane.add(fPathFld, 2, 1);
    }


    private void initQueryButton() {
        // 查询按钮 触发事件；
        Button button = new Button("查询");
        button.setOnAction(new QueryClickHandler());

        GridPane.setHalignment(button, HPos.LEFT);
        gridPane.add(button, 1, 2);
        HBox hBox = new HBox();
        GridPane.setHalignment(hBox, HPos.RIGHT);
        Button fileButton = new Button("File");

        fileButton.setOnAction(new QueryFileHandler());
        HBox.setHgrow(fileButton, Priority.ALWAYS);
        HBox.setHgrow(queryPath, Priority.ALWAYS);
        queryPath.setEditable(false);
        hBox.getChildren().addAll(fileButton, queryPath);
        gridPane.add(hBox, 2, 2);
        gridPane.add(queryData, 1, 3 , 2, 1);
    }

    private void initOutExcel() {
        // 导出按钮
        Button button2 = new Button("导出");
        GridPane.setHalignment(button2, HPos.LEFT);
        gridPane.add(button2, 1, 4);
        button2.setDefaultButton(true);
        button2.setOnAction(new FileChooseSaveHandler());
    }
}
