package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 旋转处理器
 * 按指定角度旋转图像（只支持90度倍数）
 */
public class RotateProcessor extends AbstractImageProcessor {

    /** 旋转角度（度） */
    private final int degrees;

    /**
     * 构造方法
     * @param degrees 旋转角度（90的倍数）
     */
    public RotateProcessor(int degrees) {
        super("旋转" + degrees + "°", "图像旋转" + degrees + "度");
        this.degrees = degrees % 360;
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int width = input.getWidth();
        int height = input.getHeight();

        // 根据旋转角度确定源像素映射
        int times = ((degrees % 360) + 360) % 360 / 90;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int srcX = x;
                int srcY = y;

                // 根据旋转次数应用变换
                switch (times) {
                    case 1: // 90度
                        srcX = y;
                        srcY = width - 1 - x;
                        break;
                    case 2: // 180度
                        srcX = width - 1 - x;
                        srcY = height - 1 - y;
                        break;
                    case 3: // 270度
                        srcX = height - 1 - y;
                        srcY = x;
                        break;
                }

                // 边界检查
                srcX = Math.max(0, Math.min(srcX, width - 1));
                srcY = Math.max(0, Math.min(srcY, height - 1));

                int[] pixel = ImageUtils.getPixel(input, srcX, srcY);
                ImageUtils.setPixel(output, x, y, pixel[0], pixel[1], pixel[2]);
            }
        }
    }

    @Override
    public BufferedImage process(BufferedImage input) {
        // 旋转90度需要交换宽高
        if (degrees % 180 == 90) {
            BufferedImage rotated = new BufferedImage(
                input.getHeight(),
                input.getWidth(),
                input.getType()
            );
            process(input, rotated);
            return rotated;
        }
        return super.process(input);
    }
}
