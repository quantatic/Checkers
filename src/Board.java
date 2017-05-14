import com.sun.glass.ui.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

/**
 * Created by Aidan on 5/13/2017.
 */
public class Board extends JPanel{

    private final int SQUARE_SIZE = 64;
    private final int CHECKER_SIZE = 40;
    private final Piece[][] BOARD = new Piece[8][8]; //access as board[y][x]. 0 for none, -1 for black, 1 for white
    private final int WIDTH = BOARD[0].length;
    private final int HEIGHT = BOARD.length;

    private final Color WHITE_SQUARE = new Color(0xFFFACD);
    private final Color BLACK_SQUARE = new Color(0xA0522D);
    private final Color WHITE_CHECKER = new Color(0xF5F5DC);
    private final Color BLACK_CHECKER = Color.BLACK;

    private final Color SELECTED_COLOR = new Color(0, 0, 0, 127);

    private int selectedX = -1;
    private int selectedY = -1;
    private Piece currentMover = Piece.WHITE;

    private final int FPS = 60;

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
        assert SQUARE_SIZE >= CHECKER_SIZE; //make sure that the size of our squares is larger than the size of a checker so we don't have overlap

        setPreferredSize(new Dimension(WIDTH * SQUARE_SIZE, HEIGHT * SQUARE_SIZE));
        setDoubleBuffered(true);
        setFocusable(true);

        for(Piece[] i : BOARD){ //use a for each and Arrays.fill to initialize the BOARD with the empty Enum
            Arrays.fill(i, Piece.EMPTY);
        }

        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                if((x + y) % 2 == 1){
                    if(y < 3){
                        BOARD[y][x] = Piece.BLACK;
                    }else if(y > 4){
                        BOARD[y][x] = Piece.WHITE;
                    }
                }
            }
        }
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                if(selectedX == -1 && selectedY == -1){
                    selectedX = e.getX() / SQUARE_SIZE;
                    selectedY = e.getY() / SQUARE_SIZE;
                    if(BOARD[selectedY][selectedX] != currentMover){
                        selectedX = -1;
                        selectedY = -1;
                    }
                }else{
                    int moveToX = e.getX() / SQUARE_SIZE;
                    int moveToY = e.getY() / SQUARE_SIZE;
                    attemptMove(selectedX, selectedY, moveToX, moveToY);
                    selectedX = -1;
                    selectedY = -1;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
//                selectedX = -1;
//                selectedY = -1;
            }
        });
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

        if(selectedX >= 0 && selectedY >= 0){
            Rectangle.Double selection = new Rectangle.Double(selectedX * SQUARE_SIZE, selectedY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            g2d.setColor(SELECTED_COLOR);
            g2d.fill(selection);
            g2d.draw(selection);
        }
    }

    private void renderCheckers(Graphics2D g2d){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                Ellipse2D.Double circle = new Ellipse2D.Double(1.0 * x * SQUARE_SIZE + 0.5 * SQUARE_SIZE - 0.5 * CHECKER_SIZE, 1.0 * y * SQUARE_SIZE + 0.5 * SQUARE_SIZE - 0.5 * CHECKER_SIZE, CHECKER_SIZE, CHECKER_SIZE);
                switch(BOARD[y][x]){
                    case WHITE:
                        g2d.setColor(WHITE_CHECKER);
                        break;
                    case BLACK:
                        g2d.setColor(BLACK_CHECKER);
                        break;
                    default:
                        assert BOARD[y][x] == Piece.EMPTY;
                        break;
                }

                if(BOARD[y][x] != Piece.EMPTY){
                    g2d.fill(circle);
                }
            }
        }
    }

    private boolean attemptMove(int startX, int startY, int finalX, int finalY){
        boolean isValidMove = false;
        if(BOARD[finalY][finalX] == Piece.EMPTY && (finalX + finalY) % 2 == 1){
            switch(BOARD[startY][startX]){
                case WHITE:
                    if(finalY < startY && currentMover == Piece.WHITE){
                        if(startY - finalY == 1 && Math.abs(startX - finalX) == 1){
                            isValidMove = true;
                        }else if(startY - finalY == 2 && Math.abs(startX - finalX) == 2){
                            if(BOARD[startY - 1][(startX + Integer.signum(finalX - startX))] == Piece.BLACK){
                                BOARD[startY - 1][(startX + Integer.signum(finalX - startX))] = Piece.EMPTY; //clear the piece we just jumped over
                                isValidMove = true;
                            }
                        }
                    }
                    break;
                case BLACK:
                    if(finalY > startY && currentMover == Piece.BLACK){
                        if(startY - finalY == -1 && Math.abs(startX - finalX) == 1){
                            isValidMove = true;
                        }else if(startY - finalY == -2 && Math.abs(startX - finalX) == 2){
                            if(BOARD[startY + 1][(startX + Integer.signum(finalX - startX))] == Piece.WHITE){
                                BOARD[startY + 1][(startX + Integer.signum(finalX - startX))] = Piece.EMPTY; //clear the piece we just jumped over
                                isValidMove = true;
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Check attemptMove(). You are trying to move from a square with no piece?");
            }
        }

        if(isValidMove){
            BOARD[finalY][finalX] = BOARD[startY][startX];
            BOARD[startY][startX] = Piece.EMPTY;
            currentMover = (currentMover == Piece.BLACK ? Piece.WHITE : Piece.BLACK);
        }
        return isValidMove;
    }
}
