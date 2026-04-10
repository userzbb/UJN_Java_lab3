package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 颜色反转处理器
 * 将每个像素的RGB值反转: new = 255 - old
 */
public class InvertProcessor extends AbstractImageProcessor {

    public InvertProcessor() {
        super("颜色反转", "反转图像颜色 (255 - 原值)");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixel = ImageUtils.getPixel(input, x, y);
                int r = 255 - pixel[0];
                int g = 255 - pixel[1];
                int b = 255 - pixel[2];
                ImageUtils.setPixel(output, x, y, r, g, b);
            }
        }
    }
}
