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
        Graphics graphics = file2buffer.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, 50, 50);
        graphics.setColor(Color.BLUE);
        graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
        graphics.drawString(message, 10, 25);

        graphics.setFont(graphics.getFont().deriveFont(30f));
        graphics.drawString(message, 100, 100);
        graphics.dispose();

        Graphics2D w = (Graphics2D) file2buffer.getGraphics();
        w.drawImage(file2buffer, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        w.setComposite(alphaChannel);
        w.setColor(Color.GREEN);
        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        FontMetrics fontMetrics = w.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(message, w);

        // calculate center of the image
        int centerX = (file2buffer.getWidth() - (int) rect.getWidth()) / 2;
        int centerY = file2buffer.getHeight() / 2;

        // add text overlay to the image
        w.drawString(message, centerX, centerY);

        w.dispose();

        return file2buffer;
    }

    public static InputStream addTextToImage(InputStream file, String message) throws IOException {
        BufferedImage file2buffer = ImageIO.read(file);
        BufferedImage resultBuffer = addTextToBufferedImage(file2buffer, message);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resultBuffer, "jpg", os);

        return new ByteArrayInputStream(os.toByteArray());
    }
}