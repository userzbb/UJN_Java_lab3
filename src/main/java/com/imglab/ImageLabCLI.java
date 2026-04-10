package com.imglab;

import com.imglab.processor.*;
import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 图像处理实验室 - CLI命令行接口
 *
 * 用法:
 *   java com.imglab.ImageLabCLI <输入图像> <输出图像> <操作> [参数]
 *   java com.imglab.ImageLabCLI --batch <输入目录> <输出目录> <操作1,参数1> <操作2,参数2> ...
 *
 * 示例:
 *   java com.imglab.ImageLabCLI input.png output.png grayscale
 *   java com.imglab.ImageLabCLI input.png output.png brightness 50
 *   java com.imglab.ImageLabCLI input.png output.png rotate 90
 *   java com.imglab.ImageLabCLI --batch input/ output/ brightness,30 blur
 */
public class ImageLabCLI {

    /** 操作名称到处理器构造函数的映射 */
    private static final java.util.Map<String, java.util.function.Function<String[], ImageProcessor>> OPERATIONS =
        new java.util.LinkedHashMap<>();

    static {
        // 注册所有支持的操作
        OPERATIONS.put("grayscale", args -> new GrayscaleProcessor());
        OPERATIONS.put("gray", args -> new GrayscaleProcessor());

        OPERATIONS.put("brightness", args -> {
            int value = args.length > 0 ? Integer.parseInt(args[0]) : 30;
            return new BrightnessProcessor(value);
        });

        OPERATIONS.put("contrast", args -> {
            double factor = args.length > 0 ? Double.parseDouble(args[0]) : 1.5;
            return new ContrastProcessor(factor);
        });

        OPERATIONS.put("blur", args -> new BlurProcessor());
        OPERATIONS.put("sharpen", args -> new SharpenProcessor());
        OPERATIONS.put("edge", args -> new EdgeDetectorProcessor());
        OPERATIONS.put("invert", args -> new InvertProcessor());

        OPERATIONS.put("flip-h", args -> new FlipProcessor(true));
        OPERATIONS.put("flip-v", args -> new FlipProcessor(false));

        OPERATIONS.put("rotate", args -> {
            int degrees = args.length > 0 ? Integer.parseInt(args[0]) : 90;
            return new RotateProcessor(degrees);
        });

        OPERATIONS.put("scale", args -> {
            double factor = args.length > 0 ? Double.parseDouble(args[0]) : 1.5;
            return new ScaleProcessor(factor);
        });

        OPERATIONS.put("threshold", args -> {
            int value = args.length > 0 ? Integer.parseInt(args[0]) : 128;
            return new ThresholdProcessor(value);
        });

        OPERATIONS.put("erode", args -> new ErosionProcessor());
        OPERATIONS.put("dilate", args -> new DilationProcessor());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        // 批处理模式
        if ("--batch".equals(args[0])) {
            runBatch(args);
            return;
        }

        // 单文件模式
        if (args.length < 3) {
            printUsage();
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String operation = args[2];
        String[] operationArgs = args.length > 3 ? Arrays.copyOfRange(args, 3, args.length) : new String[0];

        processImage(inputPath, outputPath, operation, operationArgs);
    }

    private static void processImage(String inputPath, String outputPath, String operation, String[] args) {
        try {
            System.out.println("加载图像: " + inputPath);
            BufferedImage input = ImageUtils.loadImage(inputPath);
            System.out.println("尺寸: " + input.getWidth() + "x" + input.getHeight());

            ImageProcessor processor = createProcessor(operation, args);

            System.out.println("执行: " + processor.getName());
            long startTime = System.currentTimeMillis();
            BufferedImage output = processor.process(input);
            long endTime = System.currentTimeMillis();

            System.out.println("处理完成! 耗时: " + (endTime - startTime) + "ms");

            System.out.println("保存图像: " + outputPath);
            ImageUtils.saveImage(output, outputPath);
            System.out.println("完成!");

        } catch (IOException e) {
            System.err.println("错误: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("参数错误: " + e.getMessage());
            System.exit(1);
        }
    }

    private static ImageProcessor createProcessor(String operation, String[] args) {
        java.util.function.Function<String[], ImageProcessor> factory = OPERATIONS.get(operation.toLowerCase());
        if (factory == null) {
            System.err.println("未知操作: " + operation);
            System.err.println("支持的操作:");
            OPERATIONS.keySet().forEach(op -> System.err.println("  - " + op));
            System.exit(1);
        }
        return factory.apply(args);
    }

    private static void runBatch(String[] args) {
        if (args.length < 4) {
            System.err.println("批处理模式用法: --batch <输入目录> <输出目录> <操作1,参数1> <操作2,参数2> ...");
            System.exit(1);
        }

        String inputDir = args[1];
        String outputDir = args[2];
        String[] operations = Arrays.copyOfRange(args, 3, args.length);

        File inDir = new File(inputDir);
        File outDir = new File(outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        File[] files = inDir.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".bmp");
        });

        if (files == null || files.length == 0) {
            System.out.println("在 " + inputDir + " 中未找到图像文件");
            System.exit(1);
        }

        System.out.println("找到 " + files.length + " 个图像文件");
        System.out.println();

        for (File file : files) {
            System.out.println("=== 处理: " + file.getName() + " ===");
            try {
                String outputPath = new File(outDir, file.getName()).getAbsolutePath();
                processImage(file.getAbsolutePath(), outputPath, "grayscale", new String[0]);
            } catch (Exception e) {
                System.err.println("处理失败: " + e.getMessage());
            }
            System.out.println();
        }

        System.out.println("批处理完成!");
    }

    private static void printUsage() {
        System.out.println("图像处理实验室 CLI");
        System.out.println();
        System.out.println("用法:");
        System.out.println("  单文件处理:");
        System.out.println("    java com.imglab.ImageLabCLI <输入> <输出> <操作> [参数]");
        System.out.println();
        System.out.println("  批处理:");
        System.out.println("    java com.imglab.ImageLabCLI --batch <输入目录> <输出目录> <操作1> <操作2> ...");
        System.out.println();
        System.out.println("支持的操作:");
        for (String op : OPERATIONS.keySet()) {
            System.out.println("  - " + op);
        }
        System.out.println();
        System.out.println("示例:");
        System.out.println("  java com.imglab.ImageLabCLI input.png output.png grayscale");
        System.out.println("  java com.imglab.ImageLabCLI input.png output.png brightness 50");
        System.out.println("  java com.imglab.ImageLabCLI input.png output.png rotate 90");
        System.out.println("  java com.imglab.ImageLabCLI input.png output.png scale 2.0");
    }
}
