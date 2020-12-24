import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import javax.swing.JOptionPane;

public class ImageEditorNilayGUI {

    private static JMenuBar menuBar;
    private static JMenu menuOne;
    private static JMenu menuTwo;
    private static JMenuItem open = new JMenuItem("  Open  ");
    private static JMenuItem save = new JMenuItem("  Save As  ");
    private static JMenuItem exit = new JMenuItem("  Exit  ");
    private static JMenuItem restore = new JMenuItem("  Restore to Original  ");
    private static JMenuItem horizontalFlip = new JMenuItem("  Horizontal Flip  ");
    private static JMenuItem verticalFlip = new JMenuItem("  Vertical Flip  ");
    private static JMenuItem greyScale = new JMenuItem("  Grey Scale  ");
    private static JMenuItem sepiaTone = new JMenuItem("  Sepia Tone  ");
    private static JMenuItem invertColour = new JMenuItem("  Invert Colour  ");
    private static JMenuItem gaussianBlur = new JMenuItem("  Gaussian Blur  ");
    private static JMenuItem buldgeEffect = new JMenuItem("  Buldge Effect  ");
    private static JFileChooser chooser;
    private static JLabel imageLabel;
    private static JPanel panel = new JPanel();
    private static BufferedImage image = null;
    private static BufferedImage pic = null;
    private static JFrame frame = new JFrame("ImageEditor");

    public static void main (String[] args) {
        JMenuBar menuBar = new JMenuBar();
        chooser = new JFileChooser();

        open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        save.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        exit.setAccelerator(KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        restore.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        horizontalFlip.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        verticalFlip.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        greyScale.setAccelerator(KeyStroke.getKeyStroke('G', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        sepiaTone.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        invertColour.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        gaussianBlur.setAccelerator(KeyStroke.getKeyStroke('U', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        buldgeEffect.setAccelerator(KeyStroke.getKeyStroke('B', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

        menuOne = new JMenu("File");
        menuTwo = new JMenu("Options");
        menuBar.add(menuOne);
        menuOne.add(open);
        menuOne.add(save);
        menuOne.add(new JSeparator());
        menuOne.add(exit);

        open.addActionListener(new SelectFile());
        save.addActionListener(new SelectFile());
        exit.addActionListener(new SelectFile());
        horizontalFlip.addActionListener(new EditFile());
        verticalFlip.addActionListener(new EditFile());
        greyScale.addActionListener(new EditFile());
        sepiaTone.addActionListener(new EditFile());
        restore.addActionListener(new EditFile());
        invertColour.addActionListener(new EditFile());
        buldgeEffect.addActionListener(new EditFile());
        gaussianBlur.addActionListener(new EditFile());

        menuBar.add(menuTwo);
        menuTwo.add(restore);
        menuTwo.add(new JSeparator());
        menuTwo.add(horizontalFlip);
        menuTwo.add(verticalFlip);
        menuTwo.add(greyScale);
        menuTwo.add(sepiaTone);
        menuTwo.add(invertColour);
        menuTwo.add(gaussianBlur);
        menuTwo.add(buldgeEffect);

        frame.setVisible(true);
        frame.setSize(400, 170);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setJMenuBar(menuBar);
        frame.setContentPane(panel);
    }
    public static double[][] matrix(int r, double strength) {
        double[][] density = new double[r][r];
        double sum = 0;
        for (int row = 0; row < density.length; row++) {
            for (int col = 0; col < density[row].length; col++) {
                density[row][col] = gaussianFormula(row - r / 2, col - r / 2, strength);
                sum += density[row][col];
            }
        }
        for (int row = 0; row < density.length; row++) {
            for (int col = 0; col < density[row].length; col++) {
                density[row][col] /= sum;
            }
        }
        return density;
    }
    public static double gaussianFormula(double x, double y, double strength) {
        return (1 / (2 * Math.PI * Math.pow(strength, 2)) * Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(strength, 2))));
    }
    public static int colourValue (double[][] wColour) {
        double addition = 0;
        for (int row = 0; row < wColour.length; row++) {
            for (int col = 0; col < wColour[row].length; col++) {
                addition += wColour[row][col];
            }
        }
        return (int)addition;
    }
    public static void nImage(BufferedImage x) {
        image = x;
        panel.removeAll();
        imageLabel = new JLabel(new ImageIcon(x));
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public static class SelectFile implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // action listener for the 3 menu items
            if (e.getSource() == exit) {
                System.exit(0);
            }
            if (e.getSource() == open) {
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        panel.removeAll();
                        File file = chooser.getSelectedFile();
                        String path = file.getAbsolutePath();
                        image = ImageIO.read(new File(path));
                        pic = ImageIO.read(new File(path));
                        imageLabel = new JLabel(new ImageIcon(image));
                        panel.add(imageLabel, BorderLayout.CENTER);
                        panel.revalidate();
                        panel.repaint();
                        JOptionPane.showMessageDialog(frame, "File Successfully opened");
                    }
                    catch (IOException a){
                        JOptionPane.showMessageDialog(frame, "File did not open correctly try again");
                    }
                } else {
                    //status.setText("Open command cancelled");
                }
            } else if (e.getSource() == save) {
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fileSave = chooser.getSelectedFile();
                        ImageIO.write(image, "png", fileSave);
                        JOptionPane.showMessageDialog(frame, "File was successfully saved");
                    }
                    catch (IOException c) {
                        JOptionPane.showMessageDialog(frame, "File did not save correctly try again");
                    }
                }
            }
        }
    }
    public static class EditFile implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            if (e.getSource() == restore) {
                nImage(pic);
            }
            if (e.getSource() == horizontalFlip) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        img.setRGB(x, height - y - 1, image.getRGB(x, y));
                    }
                }
                image = img;
                nImage(img);
            }
            if (e.getSource() == verticalFlip) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        img.setRGB(width - x - 1, y, image.getRGB(x, y));
                    }
                }
                nImage(img);
            }
            if (e.getSource() == greyScale) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color grey = new Color(image.getRGB(x, y));
                        int red = (int) (grey.getRed() * 0.299);
                        int green = (int) (grey.getGreen() * 0.587);
                        int blue = (int) (grey.getBlue() * 0.114);
                        Color nColor = new Color(red + green + blue, red + green + blue, red + green + blue);
                        img.setRGB(x, y, nColor.getRGB());
                    }
                }
                nImage(img);
            }
            if (e.getSource() == sepiaTone) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int pixel = image.getRGB(x,y);
                        int alpha = (pixel>>24)&0xff;
                        int red = (pixel>>16)&0xff;
                        int green = (pixel>>8)&0xff;
                        int blue = pixel&0xff;
                        int tr = (int)(0.393*red + 0.769*green + 0.189*blue);
                        int tg = (int)(0.349*red + 0.686*green + 0.168*blue);
                        int tb = (int)(0.272*red + 0.534*green + 0.131*blue);
                        //check condition
                        if(tr > 255){
                            red = 255;
                        }else{
                            red = tr;
                        }
                        if(tg > 255){
                            green = 255;
                        }else{
                            green = tg;
                        }
                        if(tb > 255){
                            blue = 255;
                        }else{
                            blue = tb;
                        }
                        //set new RGB value
                        pixel = (alpha<<24) | (red<<16) | (green<<8) | blue;
                        img.setRGB(x, y, pixel);
                    }
                }
                nImage(img);
            }
            if (e.getSource() == invertColour) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Color invert = new Color(image.getRGB(x, y));
                        int red = (int) (255 - invert.getRed());
                        int green = (int) (255 - invert.getGreen());
                        int blue = (int) (255 - invert.getBlue());
                        Color nColor = new Color(red, green, blue);
                        img.setRGB(x, y, nColor.getRGB());
                    }
                }
                nImage(img);
            }
            if (e.getSource() == buldgeEffect) {
                for(int x = 0; x < width; x++){
                    for(int y = 0; y < height; y++){
                        int sumX = x;
                        int sumY = y;
                        int nX = x - (width/2);
                        int nY = y - (height/2);
                        double radius =  Math.sqrt(Math.pow(nX, 2) + Math.pow(nY, 2)) / 250;
                        //atan2 gives four-quadrant inverse tan
                        double angle = Math.atan2(nX, nY);
                        double newRadius = Math.pow(radius, 0.65) * Math.sqrt(Math.pow(nX, 2) + Math.pow(nY, 2));
                        double newX = newRadius * Math.sin(angle) + (width/2);
                        double newY = newRadius * Math.cos(angle) + (height/2);
                        sumX += (newX - x);
                        sumY += (newY - y);
                        if (sumX >= 0 && sumX < width && sumY >= 0 && sumY < height) {
                            int rgb = image.getRGB(sumX, sumY);
                            img.setRGB(x, y, rgb);
                        }
                    }
                }
                nImage(img);
            }
            if (e.getSource() == gaussianBlur) {
                int r = 5;
                double strength = 2;
                double[][] density = matrix(r, strength);
                for (int x = 0; x < width - r; x++) {
                    for (int y = 0; y < height - r; y++) {
                        double[][] cRed = new double[r][r];
                        double[][] cBlue = new double[r][r];
                        double[][] cGreen = new double[r][r];
                        for (int densityX = 0; densityX < density.length; densityX++) {
                            for (int densityY = 0; densityY < density[densityX].length; densityY++) {
                                try {
                                    int samX = x + densityX - (density.length / 2);
                                    int samY = y + densityY - (density.length / 2);
                                    double currentDensity = density[densityX][densityY];
                                    Color sColor = new Color(image.getRGB(samX, samY));
                                    cRed[densityX][densityY] = currentDensity * sColor.getRed();
                                    cBlue[densityX][densityY] = currentDensity * sColor.getBlue();
                                    cGreen[densityX][densityY] = currentDensity * sColor.getGreen();
                                } catch (Exception b) {

                                }
                            }
                        }
                        img.setRGB(x, y, new Color(colourValue(cRed), colourValue(cGreen), colourValue(cBlue)).getRGB());
                    }
                }
                nImage(img);
            }
        }
    }
}
