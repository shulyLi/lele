package org.newbee.lele.event.hander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.newbee.lele.DataCenter;

/**
 * @author shuly
 * @date 2017/9/17.
 */
public class QueryClickHandler implements EventHandler<ActionEvent> {
    @Override
    synchronized public void handle(ActionEvent event) {
        DataCenter.query();
    }
}
