package com.imglab.processor;

import java.awt.image.BufferedImage;

/**
 * 图像处理器抽象基类
 *
 * 使用extends关键字继承此抽象类可以简化处理器实现
 * 抽象基类提供公共功能：参数验证、计时统计等
 *
 * 设计模式：模板方法模式 - process()定义处理流程骨架
 */
public abstract class AbstractImageProcessor implements ImageProcessor {

    /** 处理操作名称 */
    protected final String name;

    /** 处理操作描述 */
    protected final String description;

    /**
     * 构造方法 - 子类通过super调用
     * @param name 处理操作名称
     * @param description 处理操作描述
     */
    protected AbstractImageProcessor(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * 模板方法：处理图像并统计时间
     * @param input 输入图像
     * @return 处理后的图像
     */
    @Override
    public BufferedImage process(BufferedImage input) {
        if (input == null) {
            throw new IllegalArgumentException("输入图像不能为null");
        }

        long startTime = System.currentTimeMillis();

        // 创建输出图像（与输入尺寸相同）
        BufferedImage output = new BufferedImage(
            input.getWidth(),
            input.getHeight(),
            input.getType()
        );

        // 调用子类实现的处理逻辑
        process(input, output);

        long endTime = System.currentTimeMillis();
        System.out.println("  [INFO] " + name + " 耗时: " + (endTime - startTime) + "ms");

        return output;
    }

    /**
     * 就地处理 - 子类必须实现具体算法
     * @param input 输入图像
     * @param output 输出图像
     */
    @Override
    public abstract void process(BufferedImage input, BufferedImage output);

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
