import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
  
public class crop extends JFrame {
   private Image image;
   int startx, starty;
   int stopx, stopy;
   int currentx, currenty;
    
   public crop(String file_add) throws Exception {
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent we) {
            System.exit(1);
         }
      });
  
      getContentPane().setLayout(new BorderLayout());
      image = new javax.swing.ImageIcon(file_add).getImage();
      JOptionPane.showMessageDialog(this,"select region of image to crop");
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent me) {
            startx = me.getX();
            starty = me.getY();
            stopx = stopy = 0;
            repaint();
         }
  
         @Override
         public void mouseReleased(MouseEvent me) {
            stopx = me.getX();
            stopy = me.getY();
            currentx = currenty = 0;
            repaint();
            constructFrame(getCroppedImage(image, startx, starty,
                                           stopx-startx, stopy-starty));
         }
      });
  
      this.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent me) {
            currentx = me.getX();
            currenty = me.getY();
            repaint();
         }
      });
   }
   
   public void constructFrame(final Image img) {
       crop2 ob=new crop2(img);
       ob.setVisible(true);
       this.setVisible(false);
    }
   @Override
   public void paint(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
  
      g2d.drawImage(image, 0, 0, this);
  
      if (startx != 0 && currentx != 0) {
         g2d.setColor(Color.white);
         BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                            5, new float[] {10}, 0);
         g2d.setStroke(bs);
         g2d.drawRect(startx, starty, currentx-startx, currenty-starty);
      }
  
   }
  
   public Image getCroppedImage(Image img, int x, int y, int w, int h) {
      CropImageFilter cif = new CropImageFilter(x, y, w, h);
      FilteredImageSource fis = new FilteredImageSource(img.getSource(), cif);
      Image croppedImage = getToolkit().createImage(fis);
  
      MediaTracker tracker = new MediaTracker(this);
      tracker.addImage(croppedImage, 0);
      try {
         tracker.waitForID(0);
      } catch (Exception e) {}
          
      return croppedImage;
   }  
}