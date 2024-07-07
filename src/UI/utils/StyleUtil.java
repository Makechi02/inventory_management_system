package UI.utils;

import javafx.scene.Scene;

import java.util.Objects;

public class StyleUtil {
    public static void applyStyles(Scene scene) {
        scene.getStylesheets().add(Objects.requireNonNull(StyleUtil.class.getResource("/UI/views/style.css")).toExternalForm());
    }
}
