package com.bourlito.factures.scenes.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.bourlito.factures.utils.Constants;

import java.io.File;

public class Chooser {

    public static void configureFolderChooser(DirectoryChooser directoryChooser) {
        File folder = new File(Constants.DOSSIER);
        if (folder.isDirectory()) {
            directoryChooser.setInitialDirectory(folder);
        }
    }
}
