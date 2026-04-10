package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 亮度调整处理器
 * 通过增加/减少RGB值来调整图像亮度
 */
public class BrightnessProcessor extends AbstractImageProcessor {

    /** 亮度调整值，范围-255到255 */
    private final int brightness;

    /**
     * 构造方法
     * @param brightness 亮度调整值，正数变亮，负数变暗
     */
    public BrightnessProcessor(int brightness) {
        super("亮度调整", "调整图像亮度 (值: " + brightness + ")");
        this.brightness = brightness;
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixel = ImageUtils.getPixel(input, x, y);
                int r = ImageUtils.clamp(pixel[0] + brightness);
                int g = ImageUtils.clamp(pixel[1] + brightness);
                int b = ImageUtils.clamp(pixel[2] + brightness);
                ImageUtils.setPixel(output, x, y, r, g, b);
            }
        }
    }
}
