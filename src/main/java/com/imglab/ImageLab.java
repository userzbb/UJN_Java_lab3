package com.imglab;

import com.imglab.processor.*;
import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 图像处理实验室 - TUI主菜单
 *
 * 展示Java面向对象编程核心概念:
 * 1. 接口与多态
 * 2. extends继承
 * 3. super关键字
 * 4. 匿名内部类
 * 5. Lambda表达式
 */
public class ImageLab {

    /** 处理器注册表 - 使用Map存储所有处理器 */
    private static final Map<Integer, ImageProcessor> processors = new LinkedHashMap<>();

    /** 当前加载的图像 */
    private static BufferedImage currentImage;

    /** 当前图像路径 */
    private static String currentImagePath;

    static {
        // 注册所有处理器
        processors.put(1, new GrayscaleProcessor());
        processors.put(2, new BrightnessProcessor(30));
        processors.put(3, new ContrastProcessor(1.5));
        processors.put(4, new BlurProcessor());
        processors.put(5, new SharpenProcessor());
        processors.put(6, new EdgeDetectorProcessor());
        processors.put(7, new InvertProcessor());
        processors.put(8, new FlipProcessor(true));   // 水平翻转
        processors.put(9, new FlipProcessor(false));  // 垂直翻转
        processors.put(10, new RotateProcessor(90));
        processors.put(11, new ScaleProcessor(1.5));
        processors.put(12, new ThresholdProcessor(128));
        processors.put(13, new ErosionProcessor());
        processors.put(14, new DilationProcessor());
    }

    public static void main(String[] args) {
        System.out.println("=== 图像处理实验室 ===");
        System.out.println();

        // 支持直接通过命令行参数加载图像
        if (args.length > 0) {
            try {
                loadImage(args[0]);
                System.out.println("已加载图像: " + currentImagePath);
                System.out.println("尺寸: " + currentImage.getWidth() + "x" + currentImage.getHeight());
                System.out.println();
            } catch (IOException e) {
                System.out.println("加载图像失败: " + e.getMessage());
                System.exit(1);
            }
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();

            System.out.print("请选择操作 (0-14): ");
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);

                if (choice == 0) {
                    System.out.println("感谢使用，再见！");
                    break;
                }

                if (choice == 99) {
                    // 展示匿名内部类用法
                    demonstrateAnonymousClass();
                    continue;
                }

                if (choice == 98) {
                    // 展示Lambda表达式用法
                    demonstrateLambda();
                    continue;
                }

                ImageProcessor processor = processors.get(choice);
                if (processor == null) {
                    System.out.println("无效选择，请重新输入！");
                    continue;
                }

                // 确保已加载图像
                if (currentImage == null) {
                    System.out.print("请输入图像路径: ");
                    String path = scanner.nextLine().trim();
                    try {
                        loadImage(path);
                    } catch (IOException e) {
                        System.out.println("加载失败: " + e.getMessage());
                        continue;
                    }
                }

                // 执行处理
                System.out.println("\n执行: " + processor.getName());
                System.out.println("描述: " + processor.getDescription());

                long startTime = System.currentTimeMillis();
                BufferedImage result = processor.process(currentImage);
                long endTime = System.currentTimeMillis();

                System.out.println("处理完成! 耗时: " + (endTime - startTime) + "ms");

                // 保存结果
                System.out.print("保存结果路径 (直接回车跳过): ");
                String savePath = scanner.nextLine().trim();
                if (!savePath.isEmpty()) {
                    try {
                        ImageUtils.saveImage(result, savePath);
                        System.out.println("已保存到: " + savePath);
                    } catch (IOException e) {
                        System.out.println("保存失败: " + e.getMessage());
                    }
                }

                // 更新当前图像
                currentImage = result;

            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n--- 主菜单 ---");
        System.out.println("当前图像: " + (currentImage != null ? currentImagePath + " (" + currentImage.getWidth() + "x" + currentImage.getHeight() + ")" : "未加载"));
        System.out.println();
        System.out.println("  1. 灰度转换");
        System.out.println("  2. 亮度调整 (+30)");
        System.out.println("  3. 对比度调整 (1.5x)");
        System.out.println("  4. 模糊");
        System.out.println("  5. 锐化");
        System.out.println("  6. 边缘检测 (Sobel)");
        System.out.println("  7. 颜色反转");
        System.out.println("  8. 水平翻转");
        System.out.println("  9. 垂直翻转");
        System.out.println(" 10. 旋转 90°");
        System.out.println(" 11. 缩放 1.5x");
        System.out.println(" 12. 阈值二值化");
        System.out.println(" 13. 腐蚀");
        System.out.println(" 14. 膨胀");
        System.out.println(" 98. 演示 Lambda 表达式");
        System.out.println(" 99. 演示 匿名内部类");
        System.out.println("  0. 退出");
    }

    private static void loadImage(String path) throws IOException {
        currentImage = ImageUtils.loadImage(path);
        currentImagePath = path;
    }

    /**
     * 演示匿名内部类用法
     * 使用匿名内部类创建一个自定义处理器
     */
    private static void demonstrateAnonymousClass() {
        System.out.println("\n=== 匿名内部类演示 ===");
        System.out.println("创建一个自定义的'棕褐色'处理器...\n");

        // 使用匿名内部类创建自定义处理器
        // 匿名内部类：直接new接口并实现其方法，无需单独创建类文件
        ImageProcessor sepiaProcessor = new AbstractImageProcessor("棕褐色", "复古棕褐色调处理") {
            @Override
            public void process(BufferedImage input, BufferedImage output) {
                int width = input.getWidth();
                int height = input.getHeight();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int[] pixel = ImageUtils.getPixel(input, x, y);
                        int r = (int) (pixel[0] * 0.393 + pixel[1] * 0.769 + pixel[2] * 0.189);
                        int g = (int) (pixel[0] * 0.349 + pixel[1] * 0.686 + pixel[2] * 0.168);
                        int b = (int) (pixel[0] * 0.272 + pixel[1] * 0.534 + pixel[2] * 0.131);
                        ImageUtils.setPixel(output, x, y,
                            ImageUtils.clamp(r), ImageUtils.clamp(g), ImageUtils.clamp(b));
                    }
                }
            }
        };

        System.out.println("匿名内部类创建的处理器: " + sepiaProcessor.getName());
        System.out.println("描述: " + sepiaProcessor.getDescription());

        if (currentImage != null) {
            System.out.println("\n执行棕褐色处理...");
            BufferedImage result = sepiaProcessor.process(currentImage);
            currentImage = result;
            System.out.println("处理完成！");
        } else {
            System.out.println("\n请先加载一张图像！");
        }
    }

    /**
     * 演示Lambda表达式用法
     * 使用Lambda过滤和查找处理器
     */
    private static void demonstrateLambda() {
        System.out.println("\n=== Lambda表达式演示 ===");

        // 获取所有处理器名称 - 使用Lambda表达式
        System.out.println("\n所有处理器 (使用stream + lambda):");
        processors.values().stream()
            .map(ImageProcessor::getName)  // Lambda: p -> p.getName()
            .forEach(name -> System.out.println("  - " + name));

        // 使用Lambda过滤：查找包含"边缘"的处理器
        System.out.println("\n包含'边缘'的处理器 (filter + lambda):");
        processors.values().stream()
            .filter(p -> p.getName().contains("边缘"))  // Lambda表达式
            .map(ImageProcessor::getName)
            .forEach(name -> System.out.println("  - " + name));

        // 使用Lambda统计
        long count = processors.values().stream()
            .filter(p -> p.getName().startsWith("阈"))
            .count();
        System.out.println("\n包含'阈'的处理器数量: " + count);

        // 使用Lambda转换
        System.out.println("\n处理器描述列表 (map + lambda):");
        List<String> descriptions = processors.values().stream()
            .map(ImageProcessor::getDescription)  // Lambda表达式
            .collect(Collectors.toList());
        descriptions.forEach(desc -> System.out.println("  - " + desc));
    }
}
