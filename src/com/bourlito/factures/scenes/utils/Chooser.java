package com.bourlito.factures.scenes.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.bourlito.factures.utils.MotsCles;

import java.io.File;

public class Chooser {
    public static void configureFileChooser(FileChooser fileChooser) {
        File folder = new File(MotsCles.DOSSIER);
        if (folder.isDirectory()) {
            fileChooser.setInitialDirectory(folder);
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLS", "*.xls")
        );
    }

    public static void configureFolderChooser(DirectoryChooser directoryChooser) {
        File folder = new File(MotsCles.DOSSIER);
        if (folder.isDirectory()) {
            directoryChooser.setInitialDirectory(folder);
        }
    }
}
