package com.imglab.processor;

import java.awt.image.BufferedImage;

/**
 * 图像处理器接口
 * 定义所有图像处理操作的统一规范
 *
 * 使用接口将"做什么"与"怎么做"分离
 * 任何图像处理操作只需实现此接口即可接入系统
 */
public interface ImageProcessor {

    /**
     * 获取处理操作名称
     */
    String getName();

    /**
     * 获取处理操作描述
     */
    String getDescription();

    /**
     * 处理图像，返回处理后的新图像（原图不变）
     * @param input 输入图像
     * @return 处理后的图像
     */
    BufferedImage process(BufferedImage input);

    /**
     * 就地处理图像，将结果写入output
     * @param input 输入图像
     * @param output 输出图像（必须与输入尺寸相同）
     */
    void process(BufferedImage input, BufferedImage output);
}
