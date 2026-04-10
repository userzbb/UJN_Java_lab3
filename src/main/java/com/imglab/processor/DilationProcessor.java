package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 膨胀处理器
 * 使用3x3结构元素的形态学膨胀操作
 */
public class DilationProcessor extends AbstractImageProcessor {

    public DilationProcessor() {
        super("膨胀", "形态学膨胀操作");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        // 先灰度化便于处理
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        GrayscaleProcessor grayscale = new GrayscaleProcessor();
        grayscale.process(input, gray);

        // 3x3结构元素
        int size = 3;
        int half = size / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int maxGray = 0;

                // 在邻域内找最大值
                for (int ky = -half; ky <= half; ky++) {
                    for (int kx = -half; kx <= half; kx++) {
                        int px = Math.max(0, Math.min(x + kx, width - 1));
                        int py = Math.max(0, Math.min(y + ky, height - 1));
                        int grayVal = gray.getRGB(px, py) & 0xFF;
                        maxGray = Math.max(maxGray, grayVal);
                    }
                }

                ImageUtils.setPixel(output, x, y, maxGray, maxGray, maxGray);
            }
        }
    }
}
