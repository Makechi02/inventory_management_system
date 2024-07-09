package com.makechi.inventory_management_system.utils;

import javafx.scene.Scene;

import java.util.Objects;

public class StyleUtil {
    public static void applyStyles(Scene scene) {
        scene.getStylesheets().add(Objects.requireNonNull(StyleUtil.class.getResource("/com/makechi/inventory_management_system/css/style.css")).toExternalForm());
    }
}
