package binar.box.util;

import com.xuggle.mediatool.*;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IAudioResampler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;

import static binar.box.util.ImageUtils.readImageFromURL;

public class ModifyMediaExample {

    private static final String inputFilename = "C:\\Users\\Andrei\\Desktop\\pod14test.mp4";
    private static final String outputFilename = "C:\\Users\\Andrei\\Desktop\\pod14Text.mp4";
    private static final String imageFilename = "C:\\Users\\Andrei\\Desktop\\lacatTest.png";

    private IAudioResampler audioResampler = null;

    public static void main(String[] args) {

        // create a media reader
        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);

        // configure it to generate BufferImages
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        IMediaWriter mediaWriter =
                ToolFactory.makeWriter(outputFilename, mediaReader);

        IMediaTool imageMediaTool = new StaticImageMediaTool(imageFilename);
        IMediaTool audioVolumeMediaTool = new VolumeAdjustMediaTool(0.1);

        // create a tool chain:
        // reader -> addStaticImage -> reduceVolume -> writer
        mediaReader.addListener(imageMediaTool);
        imageMediaTool.addListener(audioVolumeMediaTool);
        audioVolumeMediaTool.addListener(mediaWriter);

        while (mediaReader.readPacket() == null) ;

    }

    private static class StaticImageMediaTool extends MediaToolAdapter {

        private BufferedImage logoImage;

//        AtomicInteger frameNumber= new AtomicInteger(0);
        int frameNumber= 0;

        public StaticImageMediaTool(String imageFile) {

            try {
                logoImage = ImageIO.read(new File(imageFile));
            }
            catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not open file");
            }

        }

        @Override
        public void onVideoPicture(IVideoPictureEvent event) {
//            frameNumber.getAndIncrement();
            frameNumber++;
//            System.out.println(frameNumber.get());


            /// COPY THE SAME IMAGE AND RETURN IT EVERY TIME

            System.out.println(frameNumber);
            BufferedImage image = event.getImage();

            String resourceURL="http://localhost:8080/api/v1/generateText?font=Arial&fontSize=11&message=Love you Jenn{LINE_END}Great type What&color=%23000000";
            InputStream lockWithTextFromURL= null;
            try {
                lockWithTextFromURL = readImageFromURL(resourceURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (frameNumber.get()>2755) {
            if (frameNumber>240) {
                // get the graphics for the image

                // add PNG instead of text
//                ImageUtils.addTextToBufferedImage(
//                        image,
//                        "Love U",
//                        new Font("Candara", Font.BOLD, 45),
//                        Color.BLUE);
                Graphics2D g = image.createGraphics();
                try {
                    g.drawImage(ImageIO.read(lockWithTextFromURL), image.getWidth()/2, image.getHeight()/2,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // call parent which will pass the video onto next tool in chain
            super.onVideoPicture(event);

        }

    }

    private static class VolumeAdjustMediaTool extends MediaToolAdapter {

        // the amount to adjust the volume by
        private double mVolume;

        public VolumeAdjustMediaTool(double volume) {
            mVolume = volume;
        }

        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {

            // get the raw audio bytes and adjust it's value
            ShortBuffer buffer =
                    event.getAudioSamples().getByteBuffer().asShortBuffer();
            for (int i = 0; i < buffer.limit(); ++i) {
                buffer.put(i, (short) (buffer.get(i) * mVolume));
            }

            // call parent which will pass the audio onto next tool in chain
            super.onAudioSamples(event);
        }
    }
}
