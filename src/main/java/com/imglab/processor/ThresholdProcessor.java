package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 阈值二值化处理器
 * 将灰度图像转换为黑白二值图像
 */
public class ThresholdProcessor extends AbstractImageProcessor {

    /** 阈值，范围0-255 */
    private final int threshold;

    /**
     * 构造方法
     * @param threshold 二值化阈值，默认128
     */
    public ThresholdProcessor(int threshold) {
        super("阈值二值化", "灰度阈值二值化 (阈值: " + threshold + ")");
        this.threshold = Math.max(0, Math.min(255, threshold));
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixel = ImageUtils.getPixel(input, x, y);
                // 计算灰度值
                int gray = (int) (0.299 * pixel[0] + 0.587 * pixel[1] + 0.114 * pixel[2]);
                // 二值化
                int value = gray > threshold ? 255 : 0;
                ImageUtils.setPixel(output, x, y, value, value, value);
            }
        }
    }
}
