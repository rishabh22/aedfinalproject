package edu.neu.javachip.aedfinalproject.ui;

import edu.neu.javachip.aedfinalproject.ui.common.Header;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.AbstractMap;
import java.util.Stack;

public abstract class AbstractParentController implements IParentController {
    protected Context app;
    protected Header header;
    private Stack<AbstractMap.SimpleEntry<String,Node>> nodeStack = new Stack<>();

    @FXML
    protected BorderPane mainPane;

    protected void initialize() {
        app = Context.getInstance();

        header = new Header();
        mainPane.setTop(header);
        if(app.getLoggedInUserAccount().getEmployee().getEnterprise()!=null) {
            header.changeEnterpriseName(app.getLoggedInUserAccount().getEmployee().getEnterprise().getName() + " - " + app.getLoggedInUserAccount().getEmployee().getEnterprise().getParentNetwork().getName());
        }
    }



    @Override
    public void setTitle(String title) {
        header.changeHeaderTitle(title);
    }

    @FXML
    private void logout(ActionEvent actionEvent) {
        app.logout();
    }

    @FXML
    public void close() {
        app.closeApp();
    }

    @Override
    public void removeCurrentNodeFromStack() {
        nodeStack.pop();
    }

    @Override
    public AbstractMap.SimpleEntry<String, Node> getCurrentNodeFromStack(){
        return nodeStack.peek();
    }

    @Override
    public void addNodeToStack(AbstractMap.SimpleEntry<String, Node> node){
        nodeStack.push(node);
    }

    public int getNodeStackCount() {
        return nodeStack.size();
    }
}
