package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 缩放处理器
 * 按指定比例缩放图像
 */
public class ScaleProcessor extends AbstractImageProcessor {

    /** 缩放比例 */
    private final double scale;

    /**
     * 构造方法
     * @param scale 缩放比例，0.5表示缩小一半，2.0表示放大两倍
     */
    public ScaleProcessor(double scale) {
        super("缩放" + (scale * 100) + "%", "图像缩放 (比例: " + scale + ")");
        this.scale = scale;
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        int srcWidth = input.getWidth();
        int srcHeight = input.getHeight();
        int dstWidth = output.getWidth();
        int dstHeight = output.getHeight();

        for (int y = 0; y < dstHeight; y++) {
            for (int x = 0; x < dstWidth; x++) {
                // 双线性插值计算源像素位置
                double srcX = x / scale;
                double srcY = y / scale;

                int x1 = (int) srcX;
                int y1 = (int) srcY;
                int x2 = Math.min(x1 + 1, srcWidth - 1);
                int y2 = Math.min(y1 + 1, srcHeight - 1);

                // 边界检查
                x1 = Math.max(0, Math.min(x1, srcWidth - 1));
                y1 = Math.max(0, Math.min(y1, srcHeight - 1));
                x2 = Math.max(0, Math.min(x2, srcWidth - 1));
                y2 = Math.max(0, Math.min(y2, srcHeight - 1));

                // 获取四个邻近像素
                int[] p11 = ImageUtils.getPixel(input, x1, y1);
                int[] p12 = ImageUtils.getPixel(input, x1, y2);
                int[] p21 = ImageUtils.getPixel(input, x2, y1);
                int[] p22 = ImageUtils.getPixel(input, x2, y2);

                // 双线性插值
                double fx = srcX - x1;
                double fy = srcY - y1;

                int r = (int) (p11[0] * (1-fx) * (1-fy) + p21[0] * fx * (1-fy) +
                              p12[0] * (1-fx) * fy + p22[0] * fx * fy);
                int g = (int) (p11[1] * (1-fx) * (1-fy) + p21[1] * fx * (1-fy) +
                              p12[1] * (1-fx) * fy + p22[1] * fx * fy);
                int b = (int) (p11[2] * (1-fx) * (1-fy) + p21[2] * fx * (1-fy) +
                              p12[2] * (1-fx) * fy + p22[2] * fx * fy);

                ImageUtils.setPixel(output, x, y, r, g, b);
            }
        }
    }

    @Override
    public BufferedImage process(BufferedImage input) {
        int newWidth = (int) (input.getWidth() * scale);
        int newHeight = (int) (input.getHeight() * scale);
        newWidth = Math.max(1, newWidth);
        newHeight = Math.max(1, newHeight);

        BufferedImage scaled = new BufferedImage(newWidth, newHeight, input.getType());
        process(input, scaled);
        return scaled;
    }
}
