package com.github.romainbrenguier.sedmoy.app;

import java.io.File;
import java.io.IOException;

public class HtmlToMobi {

    public File convert(File htmlFile) throws IOException, InterruptedException {
        File tmpMobi = File.createTempFile("tmpMobi", ".mobi");
        Process process =
                new ProcessBuilder("ebook-convert", htmlFile.toString(), tmpMobi.toString())
                        .start();
        int result = process.waitFor();
        if (result != 0) {
            System.out.println("Process exited with unexpected code " + result);
        }
        return tmpMobi;
    }
}
