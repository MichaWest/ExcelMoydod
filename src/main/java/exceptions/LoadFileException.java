package exceptions;

import java.io.IOException;

public class LoadFileException extends IOException {

    public LoadFileException(){
        super("Нет загруженного файла");
    }
}
