import UI.utils.StyleUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Inventory Management System");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/login-view.fxml"));
        GridPane root = loader.load();
        Scene scene = new Scene(root);

        StyleUtil.applyStyles(scene);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}