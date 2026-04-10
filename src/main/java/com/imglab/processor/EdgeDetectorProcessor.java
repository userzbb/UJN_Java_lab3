package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 边缘检测处理器
 * 使用Sobel算子检测图像边缘
 */
public class EdgeDetectorProcessor extends AbstractImageProcessor {

    public EdgeDetectorProcessor() {
        super("边缘检测", "Sobel边缘检测算子");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // Sobel X方向核
        float[][] sobelX = {
            { -1f,  0f,  1f },
            { -2f,  0f,  2f },
            { -1f,  0f,  1f }
        };

        // Sobel Y方向核
        float[][] sobelY = {
            { -1f, -2f, -1f },
            {  0f,  0f,  0f },
            {  1f,  2f,  1f }
        };

        int width = input.getWidth();
        int height = input.getHeight();

        // 先转换为灰度图以便处理
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        GrayscaleProcessor grayscale = new GrayscaleProcessor();
        grayscale.process(input, gray);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 分别计算X和Y方向的梯度
                int[] gx = ImageUtils.applyKernel(gray, x, y, sobelX, 1f);
                int[] gy = ImageUtils.applyKernel(gray, x, y, sobelY, 1f);

                // 计算梯度幅度
                int magnitude = ImageUtils.clamp(
                    (int) Math.sqrt(gx[0]*gx[0] + gy[0]*gy[0])
                );

                // 边缘为白色，背景为黑色
                ImageUtils.setPixel(output, x, y, magnitude, magnitude, magnitude);
            }
        }
    }
}
