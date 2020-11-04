import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Setup {

        Filler filler = new Filler();
        FileLister lister = new FileLister();


        protected Image defParent;

        protected Image search;

        protected Image yes;

        protected Image no;

        {
            try {
                defParent = ImageIO.read(getClass().getResource("parent.png"));
                search = ImageIO.read(getClass().getResource("search.png"));
                yes = ImageIO.read(getClass().getResource("yesIcon.png"));
                no = ImageIO.read(getClass().getResource("noIcon.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected Image parentFolder = defParent.getScaledInstance(25,25, Image.SCALE_SMOOTH);
        protected Image searchIcon = search.getScaledInstance(25,25, Image.SCALE_SMOOTH);
        protected Image yesIcon = yes.getScaledInstance(100,100, Image.SCALE_SMOOTH);
        protected Image noIcon = no.getScaledInstance(100,100, Image.SCALE_SMOOTH);

        protected String focusedPath = null;

        // default left and right path
        protected final String defaultLeft = "C:\\";
        protected final String defaultRight = "D:\\";

        // paths to modify
        protected String[] currentPathLeft = {"C:\\"};
        protected String[] currentPathRight = {"D:\\"};

        // TABLE ELEMENTS
        protected String[] columns = {"type", "name", "date"};

        /* SWING ELEMENTS */

        protected JFrame frame = new JFrame("Files Manager");

        protected DefaultTableModel model = new DefaultTableModel();


        // TABLES
        protected JTable mainTable = new JTable(filler.fillData(defaultLeft), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        protected TableColumnModel columnModel = mainTable.getColumnModel();

        protected JTable mainTable2 = new JTable(filler.fillData(defaultRight), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //SCROLL PANES
        protected JScrollPane scrollPane = new JScrollPane(mainTable);
        protected JScrollPane scrollPane2 = new JScrollPane(mainTable2);

        //TEXT FIELDS
        protected JTextField leftPath = new JTextField(currentPathLeft[0]);
        protected JTextField rightPath = new JTextField(currentPathRight[0]);

        //BUTTONS
        protected JButton leftParent = new JButton();
        protected JButton rightParent = new JButton();

        protected JButton leftTButton = new JButton();
        protected JButton rightTButton = new JButton();

        protected JButton delButton = new JButton("DELETE - F8");
        protected JButton createButton = new JButton("MAKE FOLDER - F7");
        protected JButton refreshButton = new JButton("REFRESH - F9");

        protected JButton yesButton = new JButton();
        protected JButton noButton = new JButton();

        // LABELS
        protected JLabel signature = new JLabel("Kajetan Gałęziowski - kajgal - final study project");
        protected JLabel dialogLabel;


        // RENDERER - ROW COLORS
        protected DefaultTableCellRenderer renderer = new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                ArrayList<Integer> toPaint = getRows(table);
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(toPaint.contains(row)) {
                    setBackground(Color.YELLOW.darker());
                }
                else {
                    setBackground(Color.LIGHT_GRAY);
                }
                if(isSelected) {
                    this.setBackground(Color.RED);
                }
                return c;
            }
        };

        // TRANSFERHANDLERS

        TransferHandler transferHandler = new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                if(!support.isDrop()) {
                    return false;
                }

                if(!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    return false;
                }
                // nie dziala jak zaznaczone jest cos w dwoch tabelach
                if(mainTable.getSelectedRow() != -1 && mainTable2.getSelectedRow() != - 1)
                    return false;

                return true;
            }

            public boolean importData(TransferSupport support) {
                if(!canImport(support)) {
                    return false;
                }

                JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();

                int row = dropLocation.getRow();


                if(focusedPath.equals(leftPath.getText()))
                    focusedPath = rightPath.getText();
                else {
                    focusedPath = leftPath.getText();
                }

                DefaultTableModel modelek = new DefaultTableModel(filler.fillData(focusedPath),columns);

                ArrayList<File> focused = getSelectedItems();
                Object[] dataToDrag = new Object[3];
                String type = "";
                for(int k = 0; k < focused.size(); k++) {
                    if(getSelectedItems().get(k).isFile()) {
                        type = "Plik";
                    }
                    else {
                        type = "Folder";
                    }
                    dataToDrag[0] = type;
                    dataToDrag[1] = getSelectedItems().get(k).getName();
                    dataToDrag[2] = lister.getCreationDateTime(getSelectedItems().get(k));
                    // kopiowanie
                    System.out.println("KOPIUJE Z :" + getSelectedItems().get(k).getPath());
                    File fileToCopy = new File(getSelectedItems().get(k).getPath());
                    System.out.println("DO: " + Paths.get(Paths.get(focusedPath) + fileToCopy.getName()));
                    try {
                        copyDir(Paths.get(fileToCopy.getPath()), Paths.get(Paths.get(focusedPath) + "\\" + fileToCopy.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    modelek.insertRow(row, dataToDrag);
                }
                if(focusedPath.equals(leftPath.getText()))
                    mainTable.setModel(modelek);
                else {
                    mainTable2.setModel(modelek);
                }
                System.out.println(focusedPath);
                refreshTables();
                return true;
            }
        };

        TransferHandler defaultHandler = mainTable.getTransferHandler();

        public Setup() {
            allListeners();
        }

    public void allListeners() {
        TransferHandler TH = mainTable.getTransferHandler();
        TransferHandler TH2 = mainTable2.getTransferHandler();

        leftParent.addActionListener(e -> goToParent(currentPathLeft, mainTable, leftPath));
        rightParent.addActionListener(e -> goToParent(currentPathRight, mainTable2, rightPath));

        leftTButton.addActionListener(e -> searcher(leftPath, mainTable, currentPathLeft));
        rightTButton.addActionListener( e -> searcher(rightPath, mainTable2, currentPathRight));

        delButton.addActionListener( e -> {
            try {
                tryToDel();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        createButton.addActionListener( e-> {
            folderCreationSequence();
        });

        refreshButton.addActionListener( e-> {
            refreshTables();
        });

        mainTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainTable2.getSelectionModel().clearSelection();
                tablesMovement(e, currentPathLeft, mainTable, leftPath);
                focusedPath = currentPathLeft[0];
                mainTable2.setDropMode(DropMode.INSERT_ROWS);
                mainTable2.setTransferHandler(transferHandler);
                mainTable.setTransferHandler(defaultHandler);
                System.out.println(focusedPath);
            }
            public void mouseReleased(MouseEvent e) {
                focusedPath = currentPathLeft[0];
            }
            public void mousePressed(MouseEvent e) {
                mainTable2.setTransferHandler(transferHandler);
                mainTable.setTransferHandler(defaultHandler);
            }
        });

        scrollPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                focusedPath = currentPathLeft[0];
                mainTable2.getSelectionModel().clearSelection();
            }
        });

        scrollPane2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                focusedPath = currentPathRight[0];
                mainTable.getSelectionModel().clearSelection();
            }
        });

        mainTable2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainTable.getSelectionModel().clearSelection();
                tablesMovement(e, currentPathRight, mainTable2, rightPath);
                focusedPath = currentPathRight[0];
                mainTable.setDropMode(DropMode.INSERT_ROWS);
                mainTable.setTransferHandler(transferHandler);
                mainTable2.setTransferHandler(defaultHandler);
            }

            public void mouseReleased(MouseEvent e) {
                focusedPath = currentPathRight[0];
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mainTable.setTransferHandler(transferHandler);
                mainTable2.setTransferHandler(defaultHandler);
            }
        });

        mainTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {


            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 119) {
                    try {
                        tryToDel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });


        mainTable2.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 119) {
                    try {
                        tryToDel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        frame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainTable.getSelectionModel().clearSelection();
                mainTable2.getSelectionModel().clearSelection();
                focusedPath = null;
            }
        });
    }

    public void searcher(JTextField path, JTable table, String[] currentPath) {
            String nextPath = path.getText();
            File file = new File(nextPath);
            if(file.exists()) {
                DefaultTableModel data = new DefaultTableModel(filler.fillData(nextPath), columns);
                table.setModel(data);
                currentPath[0] = nextPath;
                focusedPath = currentPath[0];
            }
    }

    public void tablesMovement(MouseEvent e, String[] currentPath, JTable table, JTextField path) {
        String furtherPath;
        File tempFile = null;
        if(e.getClickCount() == 2) {
            JTable focused = (JTable) e.getSource();
            int row = focused.getSelectedRow();
            String folderName = (String) focused.getValueAt(row, 1);
            if(currentPath[0].equals("C:\\") || currentPath[0].equals("D:\\")) {
                furtherPath = currentPath[0] + folderName;
            } else {
                furtherPath = currentPath[0] + "\\" + folderName;
                }
            try {
                tempFile = new File(furtherPath);
            }
            catch(Exception ek) {
                System.out.println("Nie moge wejsc");
            }
            if(tempFile.isDirectory()) {
                DefaultTableModel data = new DefaultTableModel(filler.fillData(furtherPath), columns);
                table.setModel(data);
                currentPath[0] = furtherPath;
                path.setText(currentPath[0]);
                focusedPath = currentPath[0];
            }
        }
    }

    public void tryToDel() throws IOException {
        if(mainTable.getSelectedRow() != -1 || mainTable2.getSelectedRow() != -1) {
            if(drawAlertBox("DeleteAlertBox", "Usunąć zawartość?")) {
                Delete deleter = new Delete();
                deleter.pureDelete(getSelectedItems());
                refreshTables();
            }
        }
    }

    public void goToParent(String[] currentPath, JTable table, JTextField path) {
        if(!currentPath[0].equals("D:") && !currentPath[0].equals("D:\\") && !currentPath[0].equals("C:") && !currentPath[0].equals("C:\\")) {
            File file = new File(currentPath[0]);
            if(!file.getParent().equals(null)) {
                DefaultTableModel data = new DefaultTableModel(filler.fillData(file.getParent()),columns);
                table.setModel(data);
                currentPath[0] = file.getParent();
                path.setText(currentPath[0]);
                focusedPath = currentPath[0];
            }
        }
    }

    public ArrayList getRows(JTable table) {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            if(table.getValueAt(i, 0).equals("Folder"))
                rows.add(i);
        }
        return rows;
    }

    public ArrayList<File> getSelectedItems() {
        int[] rowsFromFirst = mainTable.getSelectedRows();
        File[] filesFromFirst = new File[rowsFromFirst.length];
        int[] rowsFromSecond = mainTable2.getSelectedRows();
        File[] filesFromSecond = new File[rowsFromSecond.length];
        ArrayList<File> filesToDel = new ArrayList<>();

        for(int i = 0; i < rowsFromFirst.length; i++) {
            String pathToFile = currentPathLeft[0] + "\\" + (String) mainTable.getValueAt(rowsFromFirst[i],1);
            File tempFile = new File(pathToFile);
            filesToDel.add(tempFile);
        }
        for(int i = 0; i < rowsFromSecond.length; i++) {
            String pathToFile = currentPathRight[0] + "\\" + (String) mainTable2.getValueAt(rowsFromSecond[i],1);
            System.out.println(pathToFile);
            File tempFile = new File(pathToFile);
            filesToDel.add(tempFile);
        }
        return filesToDel;
    }

    public boolean drawAlertBox(String titleOfWindow, String textToShow) {
        boolean[] returnState = new boolean[1];
        JDialog dialog = new JDialog(frame, titleOfWindow, true);
        dialogLabel = new JLabel(textToShow);
        dialogLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        dialog.setSize(400,200);
        yesButton.setBounds(90,80,60,60);
        noButton.setBounds(230,80,60,60);
        dialogLabel.setBounds(45,20,360,40);
        yesButton.setIcon(new ImageIcon(yesIcon));
        yesButton.addActionListener( e -> {
            returnState[0] = true;
            dialog.dispose();
        });
        noButton.setIcon(new ImageIcon(noIcon));
        noButton.addActionListener( e -> {
            returnState[0] = false;
            dialog.dispose();
        });
        dialog.add(noButton);
        dialog.add(dialogLabel);
        dialog.add(yesButton);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);
        dialog.setVisible(true);
        return returnState[0];
    }

    public void folderCreationSequence() {
        if(focusedPath != null) {
            String name = JOptionPane.showInputDialog(frame, focusedPath);
            String path = focusedPath + "\\" + name;
            File file = new File(path);
            if(!file.exists()) {
                file.mkdir();
                JOptionPane.showMessageDialog(frame, "Pomyślnie utworzono folder " + name);
                refreshTables();
            }
            else {
                JOptionPane.showMessageDialog(frame, "Folder o takiej nazwie już istnieje!");
            }
        }
    }

    public void refreshTables() {
        searcher(leftPath, mainTable, currentPathLeft);
        searcher(rightPath, mainTable2, currentPathRight);
    }

    public void copyDir(Path source, Path dest) throws IOException {
        Files.copy(source,dest,StandardCopyOption.REPLACE_EXISTING);
    }

}
