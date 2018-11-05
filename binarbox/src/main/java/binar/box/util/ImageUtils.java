package binar.box.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
}