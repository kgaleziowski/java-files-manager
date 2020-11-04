import java.io.File;
import java.io.IOException;

public class Filler {

    public Object[][] fillData(String filePath) {
        FileLister fileLister = new FileLister(filePath);
        File[] listOfFiles = fileLister.returnList();
        Object[][] data = new Object[listOfFiles.length][3];
        for(int i = 0; i < listOfFiles.length; i++) {
            String type;

            if(listOfFiles[i].isDirectory())
                type = "Folder";
            else
                type = "File";

            try {
                FileBody tempBody = new FileBody(type, listOfFiles[i].getName(), fileLister.getCreationDateTime(listOfFiles[i]));
                data[i][0] = tempBody.getType();
                data[i][1] = tempBody.getFileName();
                data[i][2] = tempBody.getDate();
            }
            catch (Exception e) {

            }
        }
        return data;
    }
}
