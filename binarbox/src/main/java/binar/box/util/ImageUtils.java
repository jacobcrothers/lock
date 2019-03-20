package binar.box.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class ImageUtils {

    public static final String PNG = "png";

    public static byte[] convert(MultipartFile file) throws IOException {
        validateFile(file);
        return file.getBytes();
    }

    private static void validateFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!Objects.equals(contentType, MediaType.IMAGE_JPEG.toString()) && !Objects.equals(contentType, MediaType.IMAGE_PNG.toString()))
            throw new IOException("Invalid media type");
    }

    public static BufferedImage convertToImage(MultipartFile file) throws IOException {
        validateFile(file);
        InputStream in = new ByteArrayInputStream(file.getBytes());
        return ImageIO.read(in);
    }

    public static String returnPathToImages() {
        return new File("").getAbsolutePath() + "\\images";
    }

    public static void addTextToMultiartFileImage(MultipartFile multipartFile, String pathToSaveFiles, String message) throws IOException {

        BufferedImage file2buffer = ImageUtils.convertToImage(multipartFile);

        BufferedImage result = addTextToBufferedImage(file2buffer, message);

        ImageIO.write(result, "png", new File(
                pathToSaveFiles + File.separator + "New" + multipartFile.getOriginalFilename()));
    }

    private static BufferedImage addTextToBufferedImage(BufferedImage file2buffer, String message) {
//        Graphics graphics = file2buffer.getGraphics();
//        graphics.setColor(Color.LIGHT_GRAY);
//        graphics.fillRect(0, 0, 50, 50);
//        graphics.setColor(Color.BLUE);
//        graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
//        graphics.drawString(message, 10, 25);
//
//        graphics.setFont(graphics.getFont().deriveFont(30f));
//        graphics.drawString(message, 100, 100);
//        graphics.dispose();

        Graphics2D w = (Graphics2D) file2buffer.getGraphics();
        w.drawImage(file2buffer, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        w.setComposite(alphaChannel);
        w.setColor(Color.LIGHT_GRAY);
        w.setFont(new Font("Segoe Script", Font.BOLD, 15));
        FontMetrics fontMetrics = w.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(message, w);

        // calculate center of the image
        int centerX = (file2buffer.getWidth() - (int) rect.getWidth()) / 2 - 10;
        int centerY = file2buffer.getHeight() / 2;

        // add text overlay to the image
        w.drawString(message, centerX + 20, centerY);

        w.dispose();

        return file2buffer;
    }

    public static InputStream addTextToImage(InputStream file, String message) throws IOException {
        BufferedImage file2buffer = ImageIO.read(file);
        BufferedImage resultBuffer = addTextToBufferedImage(file2buffer, message);

        return BufferedImageToInputStream(resultBuffer);
    }

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        BufferedImage lock = readImage(ImageUtils.returnPathToImages() + File.separator + "lock.png");
        BufferedImage bridge = readImage(ImageUtils.returnPathToImages() + File.separator + "bridge.jpg");

        Image lock1= lock.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        BufferedImage l1 = readImage(ImageUtils.returnPathToImages() + File.separator + "l1.png");
        BufferedImage l2 = readImage(ImageUtils.returnPathToImages() + File.separator + "l2.png");
        BufferedImage l3 = readImage(ImageUtils.returnPathToImages() + File.separator + "l3.png");
        BufferedImage l4 = readImage(ImageUtils.returnPathToImages() + File.separator + "l4.png");
        BufferedImage l5 = readImage(ImageUtils.returnPathToImages() + File.separator + "l5.png");

        Image li1= l1.getScaledInstance(420, 300, Image.SCALE_SMOOTH);
        Image li2= l2.getScaledInstance(420, 300, Image.SCALE_SMOOTH);
        Image li3= l3.getScaledInstance(420, 300, Image.SCALE_AREA_AVERAGING);
        Image li4= l4.getScaledInstance(420, 300, Image.SCALE_AREA_AVERAGING);

//        BufferedImage newImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
//        Graphics2D g = newImg.createGraphics();

        Graphics2D g = bridge.createGraphics();
        // Draw the background image
//        g.setComposite(AlphaComposite.SrcOver);
//        g.drawImage(bridge, 100, 100, null);

        // Draw the overlay image
//        float alpha = 0.25f;
//        g.setComposite(AlphaComposite.SrcOver.derive(alpha));

        g.drawImage(lock1, 410, 780, null);

        g.drawImage(li1, 2200, 816, null);
        g.drawImage(li2, 2350, 816, null);
        g.drawImage(li3, 2500, 816, null);
        g.drawImage(li4, 2650, 816, null);

//        g.drawImage(li1, 2242, 928, null);
//        g.drawImage(li2, 2238, 922, null);
//        g.drawImage(li4, 2684, 922, null);
        g.drawImage(l5, 3160, 915, null);


//        for (int i=0; i<400;i++)
//        {
//            g.drawImage(lock1, 30*i, 560, null);
//        }

        g.dispose();

        writeImage(bridge, ImageUtils.returnPathToImages() + File.separator + "lockBridge.png","PNG");

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);
    }

    public static void writeImage(BufferedImage img, String fileLocation,
                                  String extension) {
        try {
            BufferedImage bi = img;
            File outputfile = new File(fileLocation);
            ImageIO.write(bi, extension, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static InputStream BufferedImageToInputStream(BufferedImage bridgePicBuffered) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bridgePicBuffered, PNG, os);
        return new ByteArrayInputStream(os.toByteArray());
    }
}