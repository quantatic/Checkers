import com.sun.glass.ui.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

/**
 * Created by Aidan on 5/13/2017.
 */
public class Board extends JPanel{

    private final int SQUARE_SIZE = 64;
    private final int CHECKER_SIZE = 16;
    private final int[][] BOARD = new int[8][8]; //access as board[y][x]. 0 for none, -1 for black, 1 for white
    private final int WIDTH = BOARD[0].length;
    private final int HEIGHT = BOARD.length;
    private final Color WHITE_SQUARE = Color.white;
    private final Color BLACK_SQUARE = Color.black;
    private final Color WHITE_CHECKER = Color.lightGray;
    private final Color BLACK_CHECKER = Color.darkGray;
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
                if(BOARD[y][x] == 1){
                    g2d.setColor(WHITE_CHECKER);
                }else if(BOARD[y][x] == -1){
                    g2d.setColor(BLACK_CHECKER);
                }else{
                    assert (BOARD[y][x] == 0);
                }
                Arc2D.Double circle = new Arc2D.Double(1.5 * x * SQUARE_SIZE - CHECKER_SIZE * 0.5, 1.5 * y * SQUARE_SIZE - CHECKER_SIZE * 0.5, CHECKER_SIZE, CHECKER_SIZE, 0, TWO_PI, Arc2D.Double.);
            }
        }
    }
}
