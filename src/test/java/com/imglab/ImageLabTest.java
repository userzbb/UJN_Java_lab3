package com.imglab;

import com.imglab.processor.*;
import com.imglab.util.ImageUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ImageLab 集成测试
 * 扫描images/目录中的所有图片，应用14种图像处理操作，输出到image_output/目录
 */
public class ImageLabTest {

    private static final String[] SUPPORTED_EXTENSIONS = {"png", "jpg", "jpeg", "gif", "bmp"};
    private static final String INPUT_DIR = "images";
    private static final String OUTPUT_DIR = "image_output";

    @TempDir
    static Path tempDir;

    private static List<File> inputImages = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        File imagesDir = new File(INPUT_DIR);
        if (imagesDir.exists() && imagesDir.isDirectory()) {
            File[] files = imagesDir.listFiles((dir, name) -> {
                String lower = name.toLowerCase();
                for (String ext : SUPPORTED_EXTENSIONS) {
                    if (lower.endsWith("." + ext)) {
                        return true;
                    }
                }
                return false;
            });
            if (files != null) {
                for (File f : files) {
                    inputImages.add(f);
                }
            }
        }

        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    @Test
    void testAllProcessors() throws Exception {
        if (inputImages.isEmpty()) {
            System.out.println("[WARN] No images found in " + INPUT_DIR + " directory");
            return;
        }

        System.out.println("Found " + inputImages.size() + " image(s) in " + INPUT_DIR);

        for (File inputFile : inputImages) {
            String baseName = inputFile.getName();
            String nameWithoutExt = baseName.substring(0, baseName.lastIndexOf('.'));
            String ext = baseName.substring(baseName.lastIndexOf('.') + 1);

            BufferedImage inputImage = ImageIO.read(inputFile);
            System.out.println("\nProcessing: " + baseName + " (" + inputImage.getWidth() + "x" + inputImage.getHeight() + ")");

            // 1. GrayscaleProcessor
            runProcessor(new GrayscaleProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 2. BrightnessProcessor(30)
            runProcessor(new BrightnessProcessor(30), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 3. ContrastProcessor(1.5)
            runProcessor(new ContrastProcessor(1.5), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 4. BlurProcessor
            runProcessor(new BlurProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 5. SharpenProcessor
            runProcessor(new SharpenProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 6. EdgeDetectorProcessor
            runProcessor(new EdgeDetectorProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 7. InvertProcessor
            runProcessor(new InvertProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 8. FlipProcessor(true) - horizontal
            runProcessor(new FlipProcessor(true), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 9. FlipProcessor(false) - vertical
            runProcessor(new FlipProcessor(false), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 10. RotateProcessor(90)
            runProcessor(new RotateProcessor(90), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 11. ScaleProcessor(1.5)
            runProcessor(new ScaleProcessor(1.5), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 12. ThresholdProcessor(128)
            runProcessor(new ThresholdProcessor(128), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 13. ErosionProcessor
            runProcessor(new ErosionProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);

            // 14. DilationProcessor
            runProcessor(new DilationProcessor(), inputImage, nameWithoutExt, ext, OUTPUT_DIR);
        }

        System.out.println("\n[Test Complete] All processed images saved to " + OUTPUT_DIR);
    }

    private void runProcessor(ImageProcessor processor, BufferedImage input, String nameWithoutExt, String ext, String outputDir) {
        try {
            String outputName = nameWithoutExt + "_" + processor.getName() + "." + ext;
            String outputPath = outputDir + "/" + outputName;

            BufferedImage output = processor.process(input);
            ImageUtils.saveImage(output, outputPath);

            System.out.println("  [OK] " + processor.getName() + " -> " + outputName);
        } catch (Exception e) {
            System.err.println("  [FAIL] " + processor.getName() + ": " + e.getMessage());
        }
    }
}
