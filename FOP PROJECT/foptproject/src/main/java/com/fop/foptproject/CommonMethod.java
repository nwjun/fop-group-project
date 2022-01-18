package com.fop.foptproject;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author jun
 */
public class CommonMethod {

    public void setHChildrenPriority(Pane root, Priority priority) {
        ObservableList<Node> nodes = root.getChildren();
        nodes.forEach(node -> {
            HBox.setHgrow(node, priority);
        });
    }

    public void setVChildrenPriority(Pane root, Priority priority) {
        ObservableList<Node> nodes = root.getChildren();
        nodes.forEach(node -> {
            VBox.setVgrow(node, priority);
        });
    }

    public String getPathToResources(String relPath) {
        /**
         * @param relPath eg:assets/banner/banner2.jpg.
         */
        String path = getClass().getResource(relPath).toString();

        return path;
    }

    public static String getOperatingSystem() {
        String os = System.getProperty("os.name");
        return os;
    }
}
