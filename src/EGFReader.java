import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EGFReader
{
    public static void main(String[] args)
    {
        String egfFilePath = "C:/Users/Honor/Desktop/Endless Online/gfx/gfx023.egf";
        String outputBmpFilePath = "C:/Users/Honor/Desktop/Endless Online/gfx/EXTRACTED.bmp";

        try (FileInputStream fis = new FileInputStream(egfFilePath))
        {
            // Read the EGF file byte by byte
            byte[] buffer = new byte[1024];
            int bytesRead;
            boolean bmpFound = false;

            // Output stream for the BMP file
            FileOutputStream fos = new FileOutputStream(outputBmpFilePath);

            // Loop through the .egf file to find the BMP signature "BM"
            while ((bytesRead = fis.read(buffer)) != -1)
            {
                for (int i = 0; i < bytesRead - 1; i++)
                {
                    // Check for "BM" signature (0x42 0x4D in hex)
                    if (buffer[i] == 0x42 && buffer[i + 1] == 0x4D)
                    {
                        bmpFound = true;
                        fos.write(buffer, i, bytesRead - i);
                        break;
                    }
                }

                // If BMP data has been found, write the remaining bytes
                if (bmpFound)
                {
                    while ((bytesRead = fis.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, bytesRead);
                    }
                    break;
                }
            }

            fos.close();

            if (bmpFound)
            {
                System.out.println("BMP file extracted successfully!");
            }
            
            else
            {
                System.out.println("No BMP data found in the EGF file.");
            }

        }
        
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}