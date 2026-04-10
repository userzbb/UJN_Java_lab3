package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 翻转处理器
 * 支持水平和垂直翻转
 */
public class FlipProcessor extends AbstractImageProcessor {

    /** true为水平翻转，false为垂直翻转 */
    private final boolean horizontal;

    /**
     * 构造方法
     * @param horizontal true水平翻转，false垂直翻转
     */
    public FlipProcessor(boolean horizontal) {
        super(horizontal ? "水平翻转" : "垂直翻转",
              horizontal ? "沿垂直轴镜像翻转" : "沿水平轴镜像翻转");
        this.horizontal = horizontal;
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        if (horizontal) {
            // 水平翻转
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int[] pixel = ImageUtils.getPixel(input, x, y);
                    ImageUtils.setPixel(output, width - 1 - x, y, pixel[0], pixel[1], pixel[2]);
                }
            }
        } else {
            // 垂直翻转
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int[] pixel = ImageUtils.getPixel(input, x, y);
                    ImageUtils.setPixel(output, x, height - 1 - y, pixel[0], pixel[1], pixel[2]);
                }
            }
        }
    }
}
