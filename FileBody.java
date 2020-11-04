import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class FileBody {
    protected String type;
    protected String fileName;
    protected String date;

    public FileBody(String type, String fileName, String date)  {
        super();
        this.type = type;
        this.fileName = fileName;
        this.date = date;
    }

    public FileBody()  {
        super();
    }

    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDate() {
        return date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
