package rysowanie;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.util.ArrayList;
    import java.util.logging.Level;
    import java.util.logging.Logger;

public class Main extends JFrame
{

    private JPanel buttonPanel = new JPanel();
    private AnimationPanel animationPanel = new AnimationPanel();
    
    
    public Main()
    {
        this.setTitle("Bouncing ball");
        this.setBounds(300,300,300,300);
        animationPanel.setBackground(Color.GRAY);
        this.getContentPane().add(animationPanel);
            JButton bStrat = (JButton)buttonPanel.add(new JButton("Start animation"));
                bStrat.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startAnimation();
                }
                });
             JButton bStop = (JButton)buttonPanel.add(new JButton("Stop animation"));   
                bStop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animationPanel.stopAnimation();
                }
                });
                
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void startAnimation()
    {
        animationPanel.addAnimatedObject();
    }
    
    class AnimationPanel extends JPanel
    {
        
        Thread theThread;
        ThreadGroup theThreadGroup = new ThreadGroup("The Thread Group");
        
        public void addAnimatedObject()
        {
            animatedObjectsList.add(new AnimatedObject());
            theThread = new Thread(theThreadGroup, new AnimatedObjectRunnable((AnimatedObject)animatedObjectsList.get(animatedObjectsList.size()-1)));
            theThread.start();
            
            theThreadGroup.list();
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for (int i = 0; i < animatedObjectsList.size(); i++)
            {
                g.drawImage(AnimatedObject.getImage(), ((AnimatedObject)animatedObjectsList.get(i)).x,((AnimatedObject)animatedObjectsList.get(i)).y, null);
            }
            
        }
        
        ArrayList animatedObjectsList = new ArrayList();
        public class AnimatedObjectRunnable implements Runnable
        {
            public AnimatedObjectRunnable(AnimatedObject animatedObject)
            {
                this.animatedObject = animatedObject;
            }
            @Override
            public void run() 
            {
                try
                { 
                    while (!Thread.currentThread().isInterrupted())
                    {
                        this.animatedObject.moveAnimatedObject(animationPanel);
                        repaint();
                        Thread.sleep(1);

                    }
                }
                catch (InterruptedException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            AnimatedObject animatedObject;
        }
        
        public void stopAnimation()
        {
            theThreadGroup.interrupt();
            animatedObjectsList.clear();
        }
    }
    
    public static void main(String[] args) {

        new Main().setVisible(true);
        
    }
    
}

class AnimatedObject
{
    
    public static Image getImage()
    {
        return AnimatedObject.animatedObject;
    }
    
    public void moveAnimatedObject(JPanel animationPanel)
    {
        Rectangle animationPanelBorder = animationPanel.getBounds();
      
        x += dX;
        y += dY;
        
        if( y + yAnimatedObject >= animationPanelBorder.getMaxY())
        {
            y = (int)(animationPanelBorder.getMaxY()-yAnimatedObject);
            dY = - dY;
        }
        
        if( x + xAnimatedObject >= animationPanelBorder.getMaxX())
        {
            x = (int)(animationPanelBorder.getMaxX()-xAnimatedObject);
            dX = - dX;
        }
        
        if( y < animationPanelBorder.getMinY())
        {
            y = (int)(animationPanelBorder.getMinY());
            dY = - dY;
        }
        
         if( x < animationPanelBorder.getMinX())
        {
            x = (int)(animationPanelBorder.getMinX());
            dX = - dX;
        }
        
      
    }
    
    public static Image animatedObject = new ImageIcon("animatedObject.png").getImage();

    int x = 0;
    int y = 0;
    int dX = 1;
    int dY = 1;
    int xAnimatedObject = animatedObject.getWidth(null);
    int yAnimatedObject = animatedObject.getHeight(null);
}
