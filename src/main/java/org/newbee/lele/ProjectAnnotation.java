package org.newbee.lele;

import javafx.event.ActionEvent;
import org.newbee.lele.event.hander.FileChooseSaveHandler;
import org.newbee.lele.view.MenuView;
import org.newbee.lele.view.BodyView;
import org.newbee.lele.event.hander.FileChooseHandler;
import org.newbee.lele.common.tool.StringTool;
import java.io.File;
import java.util.List;


public class ProjectAnnotation {

    /**
     *  文件加载 处理流程
     *  1. 点击按钮触发 {@link MenuView#createMenu()} 中绑定的 点击事件 也就是这个 {@link FileChooseHandler#handle(ActionEvent)}
     *
     *  2. {@link FileChooseHandler} 会把 File传递给 {@link DataCenter#start(File)}  具体的过程看 {@link DataCenter#start(File)}的注释
     *
     *  3. 上边的 {@link DataCenter#start(File)} 最后会调用一次 {@link DataCenter#fullTable(List)} 把所有的结果填充到table
     *
     *  查询流程。
     *  1. 点击按钮触发 {@link BodyView#initQueryButton} 他的点击事件会去 {@link DataCenter#store} 中查询出结果
     *
     *  2. 上个步骤的结果 充到 表格内 看这个 {@link DataCenter#fullTable(List)}
     *
     *
     *  导出流程
     *  1. 点击按钮触发 {@link BodyView#initOutExcel 中给那个按钮 绑定的事件 {@link FileChooseSaveHandler#handle(ActionEvent)}}
     *
     *  2. 上边的事件会 把选择的绝对路径传递给 {@link DataCenter#exportExcel(String)} 距离的流程 看这个的流程
     *
     *  hint 放心 整个过程有 对于关键数据的额访问比如 {@link DataCenter#store} , {@link DataCenter#data} ,  {@link DataCenter#start(File)} 有 互斥锁.也就是说 同一时间 只会有一个方法在运行

     *
     */
    private   static void  main(){}


    /**
     * 注意的问题
     *
     * １）对于　俩个不同的String 当作一个单词去统计的时候需要在　classPath 里面的　dataConvert内添加数据,
     *          一行内的数据都被当作　第一个word统计,
     *
     * ２）查询　的结果
     *          如果是全部查询，那么对于 比如　foot, feet 是一个词的问题, 会展示　foot cnt,
     *          如过查询的是单个　那么对于比如　foot, feet 查询　feet 则展示的是 feet cnt
     * 3) 对与导出功能
     *          导出的和界面的table内容一致
     *
     * 4) 字符串分割我自行写了个工具　{@link StringTool#splitLineWord(String)}
     */
}
