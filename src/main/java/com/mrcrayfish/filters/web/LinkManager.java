package com.mrcrayfish.filters.web;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkManager {
    public static boolean openLink(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openLink(URL url) {
        try {
            return openLink(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean openCurseforgeLink() {
        try {
            return openLink(new URL("https://www.curseforge.com/minecraft/mc-mods/filters-reborn"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
