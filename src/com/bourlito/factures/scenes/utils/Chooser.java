package com.bourlito.factures.scenes.utils;

import javafx.stage.DirectoryChooser;
import com.bourlito.factures.utils.Constants;

import java.io.File;

public class Chooser {

    /**
     * methode de configuration du directory chooser
     * @param directoryChooser le directory chooser a configurer
     */
    public static void configureDirectoryChooser(DirectoryChooser directoryChooser) {
        File folder = new File(Constants.DOSSIER);
        if (folder.isDirectory()) {
            directoryChooser.setInitialDirectory(folder);
        }
    }
}
