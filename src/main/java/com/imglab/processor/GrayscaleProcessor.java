package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 灰度转换处理器
 * 使用加权平均法将彩色图像转换为灰度图
 * 公式: Gray = 0.299*R + 0.587*G + 0.114*B
 */
public class GrayscaleProcessor extends AbstractImageProcessor {

    public GrayscaleProcessor() {
        // super关键字调用父类构造方法
        // 传入操作名称和描述
        super("灰度转换", "将彩色图像转换为灰度图像");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixel = ImageUtils.getPixel(input, x, y);
                // 加权平均法计算灰度
                int gray = (int) (0.299 * pixel[0] + 0.587 * pixel[1] + 0.114 * pixel[2]);
                ImageUtils.setPixel(output, x, y, gray, gray, gray);
            }
        }
    }
}
