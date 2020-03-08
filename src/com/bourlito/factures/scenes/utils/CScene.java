package com.bourlito.factures.scenes.utils;

import com.bourlito.factures.JavaFX;
import com.bourlito.factures.utils.Constants;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CScene extends Scene {

    public CScene(Parent root) {
        super(root, Constants.PANE_WIDTH, Constants.PANE_HEIGHT);
        this.getStylesheets().add(JavaFX.STYLE);
    }

    public CScene(Parent root, double width, double height) {
        super(root, width, height);
        this.getStylesheets().add(JavaFX.STYLE);
    }
}
