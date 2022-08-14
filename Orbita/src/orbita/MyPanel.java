// 0,11 |-1,12 | -11.2,10 gunDiamenter = 60
package orbita;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener {

    private final int windowsSquareSize = 770;
    private final int windowsHeight = windowsSquareSize; // altura
    private final int windowsWidth = windowsSquareSize; // largura
    private final int windowCenterX = windowsWidth/2;
    private final int windowCenterY = windowsHeight/2;
    private final int scale = 1;    // not in use yet
    
    /** EARTH */
    private final int earthDiameter = 500 ; // 638px proportion: 1 pixel = 20 km
    private final int earthRadius = earthDiameter/2;
    private final int earthCoordenateX = windowCenterX - earthRadius;
    private final int earthCoordenateY = windowCenterY - earthRadius;
    
    private final int earthCenterX = earthCoordenateX + earthRadius;
    private final int earthCenterY = earthCoordenateY + earthRadius;
    private final double GRAVITY_POWER = 0.4; // valor arbitrário
    private final double GRAVITY_FADE_RATE = 0.006; // por pixel de distância à superfície.
    /** GUN BODY */
    private final int gunDiameter = 30; //20
    private final int gunRadius = gunDiameter/2;
    private final int gunCoordenateX = earthCenterX - gunRadius;
    private final int gunCoordenateY = earthCoordenateY -(gunDiameter);
    private final int gunCenterX = gunCoordenateX + gunRadius;
    private final int gunCenterY = gunCoordenateY + gunRadius;
    /** GUN CANNON --------------------------------------------------- */
    private final double cannonAngleInDegrees = 3;      // <------ ANGULO
    private final int cannonSize = gunDiameter;
    private final double cannonAngleInRadians = Math.toRadians(cannonAngleInDegrees);
    private final double cosOfTheCannon = Math.cos(cannonAngleInRadians)*cannonSize;
    private final double sinOfTheCannon = Math.sin(cannonAngleInRadians)*cannonSize;
    private final double cannonCoordenateX = gunCenterX + (cosOfTheCannon);
    private final double cannonCoordenateY = gunCenterY - (sinOfTheCannon);
    /** PROJECTIL AND ANIMATION ------------------------------------ */
    private final double BOOM_POWER = 12;             // <------ TIRO 
    private final int projectilDiameter = 10;
    private final int projectilRadius = projectilDiameter/2;
    private final double cosOfTheProjectil = Math.cos(Math.toRadians(cannonAngleInDegrees))*BOOM_POWER;
    private final double sinOfTheProjectil = Math.sin(Math.toRadians(cannonAngleInDegrees))*BOOM_POWER;
    private double projectilCoordenateX = cannonCoordenateX - projectilRadius;
    private double projectilCoordenateY = cannonCoordenateY - projectilRadius;
    private double velocityX = cosOfTheProjectil; 
    private double velocityY = -sinOfTheProjectil;
    private boolean collided = false;
    private final Timer timer;
    private int i = 0;

    public MyPanel() {
        this.setPreferredSize(new Dimension(windowsWidth,windowsHeight));
        timer = new Timer(1, this);
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        /** erease the hole panel before repainting it.*/
//        g2d.setColor(Color.BLACK);
//        g2d.fillRect(0, 0, windowsWidth, windowsHeight);
   
        /**EARTH */
        g2d.setColor(Color.WHITE);
        g2d.drawOval( earthCoordenateX,earthCoordenateY, earthDiameter, earthDiameter);
        /** GUN */
        g2d.setColor(Color.RED);
        g2d.fillOval( gunCoordenateX, gunCoordenateY, gunDiameter, gunDiameter);
        /** GUN's CANNON */
        g2d.drawLine( gunCenterX, gunCenterY, (int) Math.round(cannonCoordenateX), (int) Math.round(cannonCoordenateY) );
        /** PROJECTIL */
        g2d.setColor(Color.YELLOW);
        g2d.fillOval( (int) Math.round(projectilCoordenateX), (int) Math.round(projectilCoordenateY), projectilDiameter, projectilDiameter);
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        i++;
        if ( i > 20 ) {
            double projectilCenterX = projectilCoordenateX + projectilRadius;
            double projectilCenterY = projectilCoordenateY + projectilRadius;
            
            double distanceX = projectilCenterX - earthCenterX;
            double distanceY = projectilCenterY - earthCenterY;
            double hipothenuse = Math.sqrt(distanceX*distanceX + distanceY*distanceY); // pitágoras
            double cos = distanceX/hipothenuse;
            double sin = distanceY/hipothenuse;
            double angleRadian = ( sin > 0 ) ? Math.acos(cos) : -Math.acos(cos);
            double distanceOfSurface = hipothenuse - earthRadius;
            
            double gravity_fade = ( GRAVITY_POWER * distanceOfSurface * GRAVITY_FADE_RATE)/2;
            
            /** TENTANDO FAZER A PROPORÇÃO CORRETA DE GRAVIDADE */
            double constanteK = 6.67;
            double raioR = earthRadius;
            double massaM = 5.98;
            double effective_gravity = (constanteK*massaM)/Math.pow(raioR + distanceOfSurface, 2);
            // -----------------------------
                    
            /** calculando os componentes X e Y da gravidade */
            
            double gravityComponentX = (gravity_fade >= GRAVITY_POWER)? 0 : Math.cos( angleRadian + 3.14159 ) * (GRAVITY_POWER - gravity_fade );
            double gravityComponentY = (gravity_fade >= GRAVITY_POWER)? 0 : Math.sin( angleRadian + 3.14159 ) * (GRAVITY_POWER - gravity_fade );
            velocityX += gravityComponentX;
            velocityY += gravityComponentY;
            // ----------------
            
            /** projectil COLLIDED with earth! */
            collided = hipothenuse <= earthRadius + projectilRadius;
            if ( collided ) { velocityX = 0; velocityY = 0; }
            // ----------------
            
            /** MAKE THE MOVE (with the updated data)*/
            projectilCoordenateX += velocityX;
            projectilCoordenateY += velocityY;
            repaint();
        }
    }
    
    public static double log(double base, double valor) {
        return Math.log(valor) / Math.log(base);
    }
}