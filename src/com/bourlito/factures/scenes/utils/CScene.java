package com.bourlito.factures.scenes.utils;

import com.bourlito.factures.JavaFX;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CScene extends Scene {

    public CScene(Parent root) {
        super(root, 800, 500);
        this.getStylesheets().add(JavaFX.STYLE);
    }
}
