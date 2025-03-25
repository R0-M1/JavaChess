import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessView extends JFrame {
    void build() {
        JPanel jp = new JPanel(new GridLayout(8, 8));
        setContentPane(jp);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel jl = new JLabel();
                jl.setOpaque(true);
                if ((i + j) % 2 == 0) {
                    jl.setBackground(Color.WHITE);
                } else {
                    jl.setBackground(Color.BLACK);
                }
                jp.add(jl);
                final int row = i;
                final int col = j;
                jl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Coordonnées: (" + row + ", " + col + ")");
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        ChessView mf = new ChessView();
        mf.build();
        mf.setSize(400, 400);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setVisible(true);
    }
}