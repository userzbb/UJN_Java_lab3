package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 模糊处理器
 * 使用5x5高斯模糊滤波器
 */
public class BlurProcessor extends AbstractImageProcessor {

    public BlurProcessor() {
        super("模糊", "5x5高斯模糊滤波");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 5x5高斯模糊核 (sigma≈1.4)
        float[][] kernel = {
            { 1f/256f,  4f/256f,  6f/256f,  4f/256f,  1f/256f },
            { 4f/256f, 16f/256f, 24f/256f, 16f/256f,  4f/256f },
            { 6f/256f, 24f/256f, 36f/256f, 24f/256f,  6f/256f },
            { 4f/256f, 16f/256f, 24f/256f, 16f/256f,  4f/256f },
            { 1f/256f,  4f/256f,  6f/256f,  4f/256f,  1f/256f }
        };

        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] result = ImageUtils.applyKernel(input, x, y, kernel, 2);
                ImageUtils.setPixel(output, x, y, result[0], result[1], result[2]);
            }
        }
    }
}
