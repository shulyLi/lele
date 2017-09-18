package org.newbee.lele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.newbee.lele.common.tool.StringTool;
import org.newbee.lele.data.SimpleData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DataCenter {
    public final static String ObjectHead = "词频统计";
    public final static String[] TABLE_HEAD = {"单词", "词频"};

    public static Stage primaryStage;                           // 主
    public static final TableView<SimpleData.Item> table = new TableView<>();      // table
    public static TextField fNameFld    = new TextField("");    // 文件名字 框
    public static TextField fPathFld    = new TextField("");    // 文件路径 框

    public static TextField queryPath   = new TextField();      // 查询的 文件入框

    public static TextArea  queryData   = new TextArea();       // 查询的 内容输入框



    public static SimpleData store      = new SimpleData();     // 存储
    public static List<SimpleData.Item> data = new ArrayList<>();



    private static Alert alertBox = new Alert(Alert.AlertType.ERROR);
    // 读取文件 填充

    /**
     * 读取问价内容
     * @param file            获取到的文件
     * @throws IOException    可抛出的 IO 异常
     *
     *
     * 1. 获取到文件后 清空 上个文本读取的内容， 填充文件名字和 文件路径.
     *
     * 2. 根据文件名字的后缀 去拼配 读取处理方式
     *     1) txt
     *         生成 {@link RandomAccessFile} 每次 读取一行，
     *        分割成{@link String[]}　分割流程看{@link StringTool#splitLineWord(String)}
     *        然后存储数据　
     *     2) doc
     *          word 07以前的版本
     *          利用 文件流 去生成 {@link WordExtractor} 把整个文章转换成 {@link String}
     *          先分割成段
     *          每个段的处理流程和 1) 内的处理行的流程一样
     *     3）docx
     *          word 07 以及以后的版本
     *          大体和 2）的基本一致, 都是利用 org.apache 的api 转换成 String
     *          下面的处理就和2的一样了。
     * 3. 填充 表格
     *
     */
    public static synchronized void start(File file) throws IOException {
        System.out.println("start reading " + file.getName());
        // 清除存储结构
        store.clear();
        String name = file.getName();
        // 填充名字
        fNameFld.setText(name);
        // 填充字段
        fPathFld.setText(file.getAbsolutePath());
        queryPath.setText("");
        // txt
        if (name.endsWith(".txt")) {
            RandomAccessFile readFile = new RandomAccessFile(file, "r");
            String line;
            while ((line = readFile.readLine()) != null) {
                dealSegment(line);
            }
        } else if(name.endsWith(".doc") || name.endsWith(".wps")) { // doc
            InputStream is = new FileInputStream(file);
            WordExtractor ex = new WordExtractor(is);
            String data = ex.getText();
            String[] segments = data.split("\n");
            for(String segment : segments) {
                dealSegment(segment);
            }
        }else if (name.endsWith(".docx") ) { // docx
            OPCPackage opcPackage = POIXMLDocument.openPackage(file.getAbsolutePath());
            POIXMLTextExtractor extractor = null;
            try {
                extractor = new XWPFWordExtractor(opcPackage);
            } catch (XmlException | OpenXML4JException ignored) {
                DataCenter.alert("打开文件错误Error");
            }
            String data = extractor.getText();
            String[] segments = data.split("\n");
            for(String segment : segments) {
                dealSegment(segment);
            }
        }
        // 默认 吧全部的都放进去
        fullTable(store.queryAll());
        System.out.println("end reading" + file.getName());
    }

    /**
     * 查询的主要逻辑
     *
     *
     */
    public synchronized static void query() {
        String queryValue = queryData.getText();
        if (queryValue != null && queryValue.length() > 0) {
            String[] words = StringTool.splitLineWord(queryValue);
            List<SimpleData.Item> data = new ArrayList<>();
            for(String word : words) {
                int cnt = store.query(word);
                SimpleData.Item item = new SimpleData.Item();
                data.add(item);
                item.cnt    = cnt;
                item.word   = word;
            }
            DataCenter.fullTable(data);
        } else {
            DataCenter.fullTable(store.queryAll());
        }
    }


    /**
     * 填充table
     * @param data 填充的数据
     *
     *   1. 清空 {@link #data}
     *   2. 填充 表格
     *   hint ： {@link #data} 和表格内的数据展示 一致， 只是在顺序上会有差异；
     */
    private static synchronized void fullTable(List<SimpleData.Item> data) {
        DataCenter.data = data;
        final ObservableList<SimpleData.Item> list = FXCollections.observableList(data);
        table.setItems(list);
    }

    /**
     * 导出文件内容
     * @param fileName 导出的文件名字
     *
     *  说白了整个 流程 就是 把 {@link #data} 的数据 处理到excel里面
     *
     *  也是利用 org.apache 的api
     *
     *
     *  生成 HSSFWorkbook  再把 HSSFWorkbook 写到文件绑定的 FileOutputStream
     *
     *
     */
    public static synchronized void exportExcel(String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 生成一个sheet
        HSSFSheet sheet = workbook.createSheet("结果sheet");
        // head 头部
        HSSFRow head = sheet.createRow(0);
        for(int i = 0; i < TABLE_HEAD.length ; ++i) {
            head.createCell(i).setCellValue(TABLE_HEAD[i]);
        }
        // body data 内容
        for(int i = 0; i < data.size(); ++i) {
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(data.get(i).word);
            row.createCell(1).setCellValue(data.get(i).cnt);
        }
        // save 存储
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(fileName));
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dealSegment(String segment) {
        String[] what = StringTool.splitLineWord(segment);
        for (String it : what) {
            if (it == null || it.length() == 0) continue;
            store.addWord(it);
        }
    }
    public static void alert(String e) {
        alertBox.setContentText(e);
        alertBox.initOwner(primaryStage);
        Optional<ButtonType> _buttonType = alertBox.showAndWait();
    }
}
