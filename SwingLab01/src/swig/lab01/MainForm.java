package swig.lab01;

import javax.swing.*;
import java.awt.*;

public class MainForm {

    private JPanel mainPanel;
    private JLabel helloLabel;
    private JTextField nameTextField;
    private JCheckBox upperCaseCheckBox;
    private JComboBox<String> languageComboBox;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private JButton kliknijMnieButton;

    public MainForm() {
        // Inicjalizacja komponentów
        mainPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        helloLabel = new JLabel("Podaj swoje imię", SwingConstants.CENTER);
        nameTextField = new JTextField();
        upperCaseCheckBox = new JCheckBox("Dużymi literami");
        languageComboBox = new JComboBox<>(new String[]{"Polski", "English", "Deutsch"});
        studentRadioButton = new JRadioButton("Student");
        teacherRadioButton = new JRadioButton("Prowadzący");
        kliknijMnieButton = new JButton("Kliknij mnie");

        // Grupa dla JRadioButton – tylko jeden może być zaznaczony
        ButtonGroup group = new ButtonGroup();
        group.add(studentRadioButton);
        group.add(teacherRadioButton);

        // Domyślne ustawienia
        languageComboBox.setSelectedIndex(0);
        studentRadioButton.setSelected(true);

        // Panel na przyciski radiowe
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rolePanel.add(studentRadioButton);
        rolePanel.add(teacherRadioButton);

        // Dodanie komponentów do głównego panelu
        mainPanel.add(helloLabel);
        mainPanel.add(nameTextField);
        mainPanel.add(upperCaseCheckBox);
        mainPanel.add(languageComboBox);
        mainPanel.add(rolePanel);
        mainPanel.add(kliknijMnieButton);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Obsługa kliknięcia przycisku
        kliknijMnieButton.addActionListener(e -> {
            String name = nameTextField.getText().trim();

            if (name.isBlank()) {
                JOptionPane.showMessageDialog(mainPanel, "Podaj imię");
                return;
            }

            String lang = (String) languageComboBox.getSelectedItem();
            String greeting;

            if ("English".equals(lang)) {
                greeting = "Hello";
            } else if ("Deutsch".equals(lang)) {
                greeting = "Hallo";
            } else {
                greeting = "Cześć";
            }

            String message = greeting + ", " + name + "!";

            if (studentRadioButton.isSelected()) {
                message += " Powitanie studenta.";
            } else if (teacherRadioButton.isSelected()) {
                message += " Powitanie prowadzącego.";
            }

            if (upperCaseCheckBox.isSelected()) {
                message = message.toUpperCase();
            }

            JOptionPane.showMessageDialog(mainPanel, message);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Moje pierwsze okno Swing");
            frame.setContentPane(new MainForm().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
