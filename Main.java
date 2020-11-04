import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;
import javax.swing.text.html.ImageView;

public class Main {


    public static void main(String[] args) throws IOException {
        Components components = new Components();
        components.compsDesign();
        new Setup();
        components.compsAdd();
    }

}