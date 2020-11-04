import java.io.File;
import java.util.ArrayList;

public class Delete {

    public void deleteRecursively(File fileToDel) {
        File[] files = fileToDel.listFiles();
        if(files != null) {
            for (File file : files) {
                deleteRecursively(file);
            }
        }
        fileToDel.delete();
    }

    public void pureDelete(ArrayList<File> files) {
        for (File file : files) {
            deleteRecursively(file);
        }
    }
}
