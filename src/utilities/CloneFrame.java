package utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CloneFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel colorPanel;
    private JPanel idPanel;
    private JButton submit_btn;
    private JButton chooseColor_btn;
    private JButton setID;
    private JTextField id_txtfield;
    private int id;
    private Color color;
    private ArrayList<String> data;
    CloneFrame(ArrayList<String> data) {
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        data = data;
        mainPanel = new JPanel(new BorderLayout());
        chooseColor_btn = new JButton("Choose Color");
        submit_btn = new JButton("Submit");
        idPanel = createIDPanel();
        colorPanel = createColorPanel();
//        initChooseColor();
        mainPanel.add(idPanel, BorderLayout.CENTER);
        mainPanel.add(colorPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
    private JPanel createIDPanel() {
        JPanel idPanel = new JPanel(new FlowLayout());
        idPanel.add(new JLabel("Enter ID :"));
        id_txtfield = new JTextField(10);
        idPanel.add(id_txtfield);
        setID = new JButton("Set ID");
        setID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = id_txtfield.getText();
                inputID.
                CloneFrame.this.data.add(inputID);
            }
        });
        idPanel.add(setID);

        return idPanel;
    }


    private void initChooseColor() {
        chooseColor_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the color chooser dialog
                JFrame colorFrame = new JFrame("Color Chooser");
//                colorFrame.setSize(400, 400);
                JPanel colorPanel = new JPanel();
                Color chosenColor = JColorChooser.showDialog(colorPanel, "Choose a Color", Color.WHITE);
                colorFrame.add(colorPanel);
                // If a color was chosen, update the preview panel
//                if (chosenColor != null) {
//                    colorPreview.setBackground(chosenColor)

            }
        });

    }
    private JPanel createColorPanel() {
        JPanel buttons = new JPanel(new BorderLayout());
        initChooseColor();

        buttons.add(chooseColor_btn, BorderLayout.SOUTH);
        return buttons;
    }


    public static void main(String[] args){
        CloneFrame frame = new CloneFrame(new ArrayList<>());
        frame.setVisible(true);
    }
}
