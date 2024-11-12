import javax.swing.*; // для GUI компонентов
import java.awt.*; // для работы с графикой
import java.awt.event.ActionEvent; // для обработки событий кнопок
import java.awt.event.ActionListener;
import java.util.Timer; //для таймера
import java.util.TimerTask;

public class Main extends JFrame {
    private JTextField lengthField; // поле для длины отрезка
    private JTextField frequencyField; // поле для частоты колебаний
    private JButton startButton; // кнопка запуска и остановки
    private JPanel canvas; // панель для анимации

    private Timer timer; // таймер для обновления анимации
    private double q; // длина отрезка
    private double w; // частота
    private double t = 0; // время
    private boolean isRunning = false; // флаг состояния анимации

    public Main() {
        setTitle("Гармонические колебания");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // поля для ввода данных
        lengthField = new JTextField("100", 5);
        frequencyField = new JTextField("1", 5);
        startButton = new JButton("Пуск");
        // панель для ввода данных
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Длина отрезка (q):"));
        inputPanel.add(lengthField);
        inputPanel.add(new JLabel("Частота (w):"));
        inputPanel.add(frequencyField);
        inputPanel.add(startButton);
        // панель для рисования колебаний
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawOscillation(g);
            }
        };
        canvas.setPreferredSize(new Dimension(400, 100));
        // добавление панели
        add(inputPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        // обработчик кнопки Пуск/Стоп
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    stopOscillation(); // остановка
                    startButton.setText("Пуск");
                } else {
                    startOscillation(); // запуск
                    startButton.setText("Стоп");
                }
            }
        });
    }
    // запуск колебаний
    private void startOscillation() {
        if (isRunning) return;
        try {
            q = Double.parseDouble(lengthField.getText());
            w = Double.parseDouble(frequencyField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректные числовые значения.");
            return;
        }
        isRunning = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                t += 0.05; // увеличение времени для плавного колебания
                canvas.repaint();
            }
        }, 0, 20); // обновление каждые 20 мс
    }
    // остановка колебаний
    private void stopOscillation() {
        if (!isRunning) return;
        timer.cancel();
        isRunning = false;
    }
    // расчет и рисование точки
    private void drawOscillation(Graphics g) {
        int x0 = 50; // начальная позиция отрезка
        int x1 = x0 + (int) q; // конечная позиция отрезка
        int x = x0 + (int) (q * (1 + Math.cos(w * t)) / 2); // позиция точки
        int y = canvas.getHeight() / 2; // центр по вертикали
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // очистка
        g.drawLine(x0, y, x1, y); // рисуем отрезок
        g.fillOval(x - 5, y - 5, 10, 10); // рисуем точку
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setVisible(true);
        });
    }
}