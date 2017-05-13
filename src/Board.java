import com.sun.glass.ui.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 * Created by Aidan on 5/13/2017.
 */
public class Board extends JPanel{

    private final int SQUARE_SIZE = 64;
    private final int CHECKER_SIZE = 40;
    private final int[][] BOARD = new int[8][8]; //access as board[y][x]. 0 for none, -1 for black, 1 for white
    private final int WIDTH = BOARD[0].length;
    private final int HEIGHT = BOARD.length;

    private final Color WHITE_SQUARE = new Color(0xFFFACD);
    private final Color BLACK_SQUARE = new Color(0xA0522D);
    private final Color WHITE_CHECKER = new Color(0xF5F5DC);
    private final Color BLACK_CHECKER = Color.BLACK;

    private final int FPS = 60;
    private final double TWO_PI = 2 * Math.PI;

    public Board(){
        init();
        Timer timer = new Timer(1000 / FPS, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                step();
            }
        });
        timer.start();
    }

    private void init(){
        assert SQUARE_SIZE > CHECKER_SIZE; //make sure that the size of our squares is larger than the size of a checker so we don't have overlap
        setPreferredSize(new Dimension(WIDTH * SQUARE_SIZE, HEIGHT * SQUARE_SIZE));
        setDoubleBuffered(true);

        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                if((x + y) % 2 == 1){
                    if(y < 3){
                        BOARD[y][x] = -1;
                    }else if(y > 4){
                        BOARD[y][x] = 1;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        renderBoard(g2d);
        renderCheckers(g2d);
    }

    private void step(){

        repaint();
    }

    private void renderBoard(Graphics2D g2d){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                if((x + y) % 2 == 0){
                    g2d.setColor(WHITE_SQUARE);
                }else{
                    g2d.setColor(BLACK_SQUARE);
                }
                Rectangle.Double square = new Rectangle.Double(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                g2d.fill(square);
                g2d.setColor(BLACK_SQUARE);
                g2d.draw(square);
            }
        }
    }

    private void renderCheckers(Graphics2D g2d){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                Ellipse2D.Double circle = new Ellipse2D.Double(1.0 * x * SQUARE_SIZE + 0.5 * SQUARE_SIZE - 0.5 * CHECKER_SIZE, 1.0 * y * SQUARE_SIZE + 0.5 * SQUARE_SIZE - 0.5 * CHECKER_SIZE, CHECKER_SIZE, CHECKER_SIZE);
                switch(BOARD[y][x]){
                    case 1:
                        g2d.setColor(WHITE_CHECKER);
                        break;
                    case -1:
                        g2d.setColor(BLACK_CHECKER);
                        break;
                    default:
                        assert BOARD[y][x] == 0;
                        break;
                }

                if(BOARD[y][x] != 0){
                    g2d.fill(circle);
                }


            }
        }
    }
}
