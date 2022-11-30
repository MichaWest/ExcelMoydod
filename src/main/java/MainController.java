import exceptions.LoadFileException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private final Desktop desktop = Desktop.getDesktop();
    private final FileChooser fileChooser = new FileChooser();
    private File tabelle;
    private final int h = 122;
    private final int w = 343;

    @FXML
    private Text path;
    @FXML
    private Pane pane;
    final Canvas canvas = new Canvas(w,h);

    @FXML
    void create() {
        try{
            if(tabelle==null) throw new LoadFileException();
            Excel excel = new Excel(tabelle);
            openFile(excel.createAntwort());
        } catch (FileAlreadyExistsException e){
            printError("Файл уже открыт");
        } catch(IOException e){
            e.printStackTrace();
            printError(e.getMessage());
        }
    }

    @FXML
    void load() {
        Stage stage = new Stage();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            tabelle = file;
            path.setText("Файл: "+file.getName());
            path.setFill(Color.BLACK);
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Выберите таблицу");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xlsx"));
    }

    private void printError(String err){
        int b = err.length()*7;
        int a = 20;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(13));
        gc.setFill(new Color(0.4, 0.4, 0.4, 0.4));
        gc.fillRect((w/2 - (b+10)/2), h-a, b+10, a);
        gc.setFill(new Color(0.85, 0.05, 0.05, 0.8));
        gc.fillText(err, (double)(w/2 - b/2), (double)(h-a/2));
        pane.getChildren().add(canvas);
        pane.setOnMousePressed(event1 -> pane.getChildren().remove(canvas));
        pane.setOnKeyPressed(event1 -> pane.getChildren().remove(canvas));
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

}
