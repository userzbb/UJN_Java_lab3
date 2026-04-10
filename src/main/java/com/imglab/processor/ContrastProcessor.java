package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 对比度调整处理器
 * 通过扩展像素值范围来调整对比度
 */
public class ContrastProcessor extends AbstractImageProcessor {

    /** 对比度因子，1.0表示不变，大于1增加对比度，小于1减少对比度 */
    private final double contrastFactor;

    public ContrastProcessor(double contrastFactor) {
        super("对比度调整", "调整图像对比度 (因子: " + contrastFactor + ")");
        this.contrastFactor = contrastFactor;
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixel = ImageUtils.getPixel(input, x, y);
                // 对比度调整公式: (pixel - 128) * factor + 128
                int r = ImageUtils.clamp((int) ((pixel[0] - 128) * contrastFactor + 128));
                int g = ImageUtils.clamp((int) ((pixel[1] - 128) * contrastFactor + 128));
                int b = ImageUtils.clamp((int) ((pixel[2] - 128) * contrastFactor + 128));
                ImageUtils.setPixel(output, x, y, r, g, b);
            }
        }
    }
}
