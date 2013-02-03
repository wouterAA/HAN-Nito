package nl.hannito.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class ImageResizer {

    /**
     * Resizes image
     * @param stream image inputstream
     * @return Byte array
     * @throws IOException 
     */
    public static byte[] resize(InputStream stream) throws IOException {
        BufferedImage image = ImageIO.read(stream);
        BufferedImage thumbnail = Scalr.resize(image, Scalr.Mode.AUTOMATIC, 75, 75);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "png", baos);
        return baos.toByteArray();
    }
}