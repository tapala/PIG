package swing.lab02;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainForm {

    private JPanel mainPanel;
    private JLabel timeLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JLabel mouseLabel;

    // logika stopera
    private Timer timer;
    private long startTimeMillis;
    private long elapsedMillis;
    private boolean running;

    private Color defaultPanelBackground;

    public MainForm() {
        // Inicjalizacja GUI – pomiń, jeśli używasz formularza .form
        mainPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        timeLabel = new JLabel("00:00.0", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 32));

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(resetButton);

        mouseLabel = new JLabel("Mysz: -");

        mainPanel.add(timeLabel);
        mainPanel.add(buttonsPanel);
        mainPanel.add(mouseLabel);

        running = false;
        elapsedMillis = 0;
        defaultPanelBackground = mainPanel.getBackground();

        timer = new Timer(100, e -> onTimerTick());

        startButton.addActionListener(e -> onStart());
        stopButton.addActionListener(e -> onStop());
        resetButton.addActionListener(e -> onReset());

        mainPanel.setFocusable(true);

        // Obsługa klawiatury
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_SPACE) {
                    toggleStartStop();          // SPACJA = Start/Stop
                } else if (code == KeyEvent.VK_R) {
                    onReset();                  // R = Reset
                } else if (code == KeyEvent.VK_S) {
                    onStop();                   // Zadanie 1: S = Stop
                } else if (code == KeyEvent.VK_ESCAPE) {
                    confirmClose();             // Zadanie 1: ESC = zamknięcie z potwierdzeniem
                }
            }
        });

        // Obsługa myszy
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Zadanie 3: podwójny klik = reset
                    onReset();
                    mouseLabel.setText("Podwójny klik - reset");
                } else if (e.getClickCount() == 1) {
                    mouseLabel.setText("Klik: x=" + e.getX() + ", y=" + e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Zadanie 2: kursor wchodzi na panel
                mainPanel.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Zadanie 2: kursor opuszcza panel
                mainPanel.setBackground(defaultPanelBackground);
            }
        });

        // Obsługa ruchu i przeciągania myszy
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseLabel.setText("Mysz: x=" + e.getX() + ", y=" + e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Zadanie 4: przeciąganie myszy
                mouseLabel.setText("Przeciąganie: x=" + e.getX() + ", y=" + e.getY());
            }
        });
    }

    private void updateTimeLabel() {
        long total = elapsedMillis;

        if (running) {
            long now = System.currentTimeMillis();
            total += (now - startTimeMillis);
        }

        int totalSeconds = (int) (total / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        int tenths = (int) (total % 1000) / 100;

        timeLabel.setText(String.format("%02d:%02d.%d", minutes, seconds, tenths));
    }

    private void onTimerTick() {
        updateTimeLabel();
    }

    private void onStart() {
        if (running) {
            return;
        }
        running = true;
        startTimeMillis = System.currentTimeMillis();
        timer.start();
        updateTimeLabel();
    }

    private void onStop() {
        if (!running) {
            return;
        }
        running = false;
        timer.stop();

        long now = System.currentTimeMillis();
        elapsedMillis += (now - startTimeMillis);
        updateTimeLabel();
    }

    private void onReset() {
        running = false;
        timer.stop();
        elapsedMillis = 0;
        updateTimeLabel();
    }

    private void toggleStartStop() {
        if (running) {
            onStop();
        } else {
            onStart();
        }
    }

    private void confirmClose() {
        Window window = SwingUtilities.getWindowAncestor(mainPanel);

        int result = JOptionPane.showConfirmDialog(
                window,
                "Na pewno zamknąć stoper?",
                "Zamykanie",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            if (window != null) {
                window.dispose();
            } else {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm form = new MainForm();

            JFrame frame = new JFrame("SwingLab02 - Stoper");
            frame.setContentPane(form.mainPanel);
            frame.setSize(400, 250);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    form.confirmClose();
                }
            });

            frame.setVisible(true);
            form.mainPanel.requestFocusInWindow();
        });
    }
}