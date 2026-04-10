package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 锐化处理器
 * 使用拉普拉斯锐化滤波器增强边缘
 */
public class SharpenProcessor extends AbstractImageProcessor {

    public SharpenProcessor() {
        super("锐化", "拉普拉斯锐化滤波");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 拉普拉斯锐化核
        float[][] kernel = {
            {  0f, -1f,  0f },
            { -1f,  5f, -1f },
            {  0f, -1f,  0f }
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
