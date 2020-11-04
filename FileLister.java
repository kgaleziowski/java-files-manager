import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileLister {
    protected File folder;
    protected File[] listOfFiles;

    public FileLister(String folderName) {
        this.folder = new File(folderName);
        this.listOfFiles = orderFiles();
    }

    public FileLister() {

    }

    public File[] returnList() {
        return listOfFiles;
    }

    public String getCreationDateTime(File file) {
        BasicFileAttributes attribute = null;
        try {
            attribute = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert attribute != null;
        FileTime time = attribute.creationTime();

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = new Date(time.toMillis());

        String formatedDate = simpleDateFormat.format(date);
        return formatedDate;
    }

    public File[] orderFiles() {
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        File[] returedList = new File[listOfFiles.length];

        int howManyDirs = 0;
        int howManyFiles = 0;

        for(int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isDirectory())
                howManyDirs++;
            if(listOfFiles[i].isFile())
                howManyFiles++;
        }
        File[] directories = new File[howManyDirs];
        File[] files = new File[howManyFiles];

        int k = 0;
        int l = 0;
        for(int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isDirectory()) {
                directories[k] = listOfFiles[i];
                k++;
            }
            else {
                files[l] = listOfFiles[i];
                l++;
            }
        }

        for(int i = 0; i < howManyDirs; i++)
            returedList[i] = directories[i];
        l = 0;
        for(int i = howManyDirs; i < listOfFiles.length; i++) {
            returedList[i] = files[l];
            l++;
        }

        return returedList;
    }

}
