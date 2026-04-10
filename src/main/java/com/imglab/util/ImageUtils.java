package com.imglab.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像工具类
 * 提供常用的图像处理辅助方法
 */
public final class ImageUtils {

    private ImageUtils() {
        // 工具类不允许实例化
    }

    /**
     * 从文件加载图像
     */
    public static BufferedImage loadImage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + path);
        }
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IOException("无法读取图像: " + path);
        }
        return img;
    }

    /**
     * 保存图像到文件
     */
    public static void saveImage(BufferedImage image, String path) throws IOException {
        String format = getFormat(path);
        File file = new File(path);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("无法保存图像格式: " + format);
        }
    }

    /**
     * 获取文件格式
     */
    public static String getFormat(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".png")) return "PNG";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "JPG";
        if (lower.endsWith(".gif")) return "GIF";
        if (lower.endsWith(".bmp")) return "BMP";
        return "PNG";
    }

    /**
     * 获取像素RGB值
     */
    public static int[] getPixel(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);
        return new int[] {
            (rgb >> 16) & 0xFF, // R
            (rgb >> 8) & 0xFF,  // G
            rgb & 0xFF         // B
        };
    }

    /**
     * 设置像素RGB值
     */
    public static void setPixel(BufferedImage image, int x, int y, int r, int g, int b) {
        int rgb = (r << 16) | (g << 8) | b;
        image.setRGB(x, y, rgb);
    }

    /**
     * 限制值在0-255范围内
     */
    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    /**
     * 限制值在0-255范围内（float）
     */
    public static int clamp(float value) {
        return clamp((int) value);
    }

    /**
     * 应用卷积核到像素
     * @param image 图像
     * @param x 像素x坐标
     * @param y 像素y坐标
     * @param kernel 卷积核
     * @param divisor 除数
     * @return 计算后的像素值
     */
    public static int[] applyKernel(BufferedImage image, int x, int y, float[][] kernel, float divisor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int kSize = kernel.length;
        int kHalf = kSize / 2;

        float rSum = 0, gSum = 0, bSum = 0;

        for (int i = 0; i < kSize; i++) {
            for (int j = 0; j < kSize; j++) {
                int px = clamp(x + i - kHalf, 0, width - 1);
                int py = clamp(y + j - kHalf, 0, height - 1);
                int[] pixel = getPixel(image, px, py);
                float k = kernel[i][j];

                rSum += pixel[0] * k;
                gSum += pixel[1] * k;
                bSum += pixel[2] * k;
            }
        }

        return new int[] {
            clamp(rSum / divisor),
            clamp(gSum / divisor),
            clamp(bSum / divisor)
        };
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
