package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 模糊处理器
 * 使用3x3均值模糊滤波器
 */
public class BlurProcessor extends AbstractImageProcessor {

    public BlurProcessor() {
        super("模糊", "3x3均值模糊滤波");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 3x3均值模糊核
        float[][] kernel = {
            { 1f/9f, 1f/9f, 1f/9f },
            { 1f/9f, 1f/9f, 1f/9f },
            { 1f/9f, 1f/9f, 1f/9f }
        };

        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] result = ImageUtils.applyKernel(input, x, y, kernel, 1f);
                ImageUtils.setPixel(output, x, y, result[0], result[1], result[2]);
            }
        }
    }
}
