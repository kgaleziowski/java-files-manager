import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class Components extends Setup {

    public void compsDesign() {

        mainTable.setAutoCreateRowSorter(true);
        mainTable2.setAutoCreateRowSorter(true);

        mainTable.setFillsViewportHeight(true);
        mainTable2.setFillsViewportHeight(true);

        mainTable.setDefaultRenderer(Object.class, renderer);
        mainTable2.setDefaultRenderer(Object.class, renderer);

        mainTable.setDragEnabled(true);
        mainTable2.setDragEnabled(true);

        scrollPane.setBounds(20, 80, 400, 600);
        scrollPane2.setBounds(660, 80, 400, 600);

        leftPath.setBounds(50, 50, 329, 27);
        rightPath.setBounds(691, 50, 329, 27);

        leftParent.setBounds(20,50,25,25);
        leftParent.setIcon(new ImageIcon(parentFolder));
        rightParent.setBounds(661, 50, 25, 25);
        rightParent.setIcon(new ImageIcon(parentFolder));


        leftTButton.setBounds(383, 50, 35, 25);
        leftTButton.setIcon(new ImageIcon(searchIcon));

        rightTButton.setBounds(1023, 50, 35, 25);
        rightTButton.setIcon(new ImageIcon(searchIcon));

        delButton.setBounds(450, 500, 180, 50);
        createButton.setBounds(450, 420, 180, 50);
        refreshButton.setBounds(450, 580, 180, 50);

        createButton.setFont(new Font("Arial", Font.BOLD, 15));
        delButton.setFont(new Font("Arial", Font.BOLD, 15));
        refreshButton.setFont(new Font("Arial", Font.BOLD, 15));

        signature.setBounds(125,690,1000,50);
        signature.setFont(new Font("Verdana", Font.BOLD, 31));
    }

    public void compsAdd() {
        frame.add(signature);
        frame.add(leftParent);
        frame.add(rightParent);
        frame.add(leftTButton);
        frame.add(rightTButton);
        frame.add(createButton);
        frame.add(refreshButton);
        frame.add(delButton);
        frame.add(leftPath);
        frame.add(rightPath);
        frame.add(scrollPane);
        frame.add(scrollPane2);
        frame.setSize(1100, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
    }

}
