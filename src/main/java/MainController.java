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
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private final Desktop desktop = Desktop.getDesktop();
    private final FileChooser fileChooser = new FileChooser();
    private File tabelle;
    private Set<File> tabelles = new TreeSet<>();
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
            for(File f: tabelles){
                excel.add(f);
            }
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
            tabelles = new TreeSet<>();
            path.setText("Файл: "+file.getName());
            path.setFill(Color.BLACK);
        }
    }

    @FXML
    void add(){
        Stage stage = new Stage();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            tabelles.add(file);
            StringBuilder t = new StringBuilder();
            for(File f: tabelles){
                t.append(", ").append(f.getName());
            }
            path.setText("Файл: "+tabelle.getName()+t);
            path.setFill(Color.BLACK);
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Выберите таблицу");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel", "*.xlsx", "*.xls"));
    }

    private void printError(String err){
        int b = err.length()*7;
        int a = 20;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(13));
        gc.setFill(new Color(0.4, 0.4, 0.4, 0.4));
        gc.fillRect((w/2.f - (b+10)/2.f), h-a, b+10, a);
        gc.setFill(new Color(0.85, 0.05, 0.05, 0.8));
        gc.fillText(err, (w/2.f - b/2.f), (h-a/2.f));
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
