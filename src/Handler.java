import javax.swing.*;
import java.awt.*;

/**
 * Created by Aidan on 5/13/2017.
 */
public class Handler extends JFrame{

    public Handler(){
        init();
    }

    private void init(){
        setTitle("A simple Checkers Game :)");

        add(new Board());
        setResizable(false);
        pack();

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                Handler myHandler = new Handler();
                myHandler.setVisible(true);
            }
        });
    }


}
