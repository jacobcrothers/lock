package binar.box.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class ImageUtils {

    public static final String PNG = "png";
    public static final String JPG = "jpg";

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

//    public static void addTextToMultiartFileImage(MultipartFile multipartFile, String pathToSaveFiles, String message) throws IOException {
//
//        BufferedImage file2buffer = ImageUtils.convertToImage(multipartFile);
//
//        BufferedImage result = addTextToBufferedImage(file2buffer, message);
//
//        ImageIO.write(result, "png", new File(
//                pathToSaveFiles + File.separator + "New" + multipartFile.getOriginalFilename()));
//    }



    public static InputStream addTextToImage(InputStream file, String message) throws IOException {
        BufferedImage file2buffer = ImageIO.read(file);
        BufferedImage resultBuffer = addTextToBufferedImage(file2buffer,
                message,
                new Font("Segoe Script", Font.BOLD, 25),
                Color.BLACK);

        return BufferedImageToInputStream(resultBuffer,PNG);
    }
    //Arial ok
    //Candara
    //Calibri
    //Gadugi
    //Microsoft JHENGHEI
    //Nirmala UI
    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();

        List<BufferedImage> lockz = new ArrayList<>();
        for (int i=1; i<17; i++)
        lockz.add(readImage(ImageUtils.returnPathToImages() +
                File.separator +
                "fontTest" +
                File.separator +
                i +
                ".png"));

        String message = "i Love you";
        for (int i=0; i<lockz.size(); i++) {
            BufferedImage resultBuffer = addTextToBufferedImage(lockz.get(i),
                    message,
                    new Font("Candara", Font.BOLD, 25),
                    Color.BLACK);
            int j = i+1;
            writeImage(resultBuffer, ImageUtils.returnPathToImages() +
                    File.separator +
                    "fontTestResult" +
                    File.separator + j + ".png",
                    "PNG");
        }


        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);
    }

    public static BufferedImage addTextToBufferedImage(BufferedImage file2buffer, String message, Font font, Color color) {
        Graphics2D w = (Graphics2D) file2buffer.getGraphics();
        w.drawImage(file2buffer, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        w.setComposite(alphaChannel);
        w.setColor(color);
        w.setFont(font);
        FontMetrics fontMetrics = w.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(message, w);

        // calculate center of the image
        int centerX = (file2buffer.getWidth() - (int) rect.getWidth()) / 2 - 10;
        int centerY = file2buffer.getHeight() / 2 + 70;

        // add text overlay to the image
        w.drawString(message, centerX + 20, centerY);

        w.dispose();

        return file2buffer;
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

    public static InputStream BufferedImageToInputStream(BufferedImage bridgePicBuffered, String imageExtension) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bridgePicBuffered, imageExtension, os);
        return new ByteArrayInputStream(os.toByteArray());
    }
}