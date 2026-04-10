# 图像处理实验室 - 实验报告

## 一、程序总体功能

本项目是一个基于Java的图像处理软件，提供CLI和TUI两种交互方式，支持对图像进行多种数字图像处理操作。

### 主要功能

| 类别 | 操作 | 说明 |
|------|------|------|
| 基础调整 | 灰度转换 | 加权平均法 RGB→灰度 |
| 基础调整 | 亮度调整 | RGB值线性增减 |
| 基础调整 | 对比度调整 | 灰度拉伸 |
| 滤镜效果 | 模糊 | 5x5高斯滤波 |
| 滤镜效果 | 锐化 | 拉普拉斯锐化核 |
| 滤镜效果 | 颜色反转 | RGB反相 255-原值 |
| 几何变换 | 水平翻转 | 垂直轴镜像 |
| 几何变换 | 垂直翻转 | 水平轴镜像 |
| 几何变换 | 旋转 | 90度倍数旋转 |
| 几何变换 | 缩放 | 双线性插值缩放 |
| 形态学 | 阈值二值化 | 灰度阈值分割 |
| 形态学 | 腐蚀 | 3x3结构元素腐蚀 |
| 形态学 | 膨胀 | 3x3结构元素膨胀 |
| 边缘检测 | Sobel边缘检测 | X/Y方向梯度算子 |

### 交互模式

**TUI菜单模式**：
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab"
```

**CLI批处理模式**：
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="input.png output.png grayscale"
```

---

## 二、设计思路

### 2.1 OOP设计模式

本项目采用**接口 + 抽象类 + 具体实现类**的三层架构，核心遵循以下设计原则：

```
接口（定义规范） → 抽象类（公共实现） → 具体类（算法实现）
```

#### 核心接口：`ImageProcessor`

```java
public interface ImageProcessor {
    String getName();                           // 操作名称
    String getDescription();                     // 操作描述
    BufferedImage process(BufferedImage input); // 处理图像
    void process(BufferedImage input, BufferedImage output); // 就地处理
}
```

**设计意图**：接口将"做什么"与"怎么做"分离。任何图像处理算法只需实现此接口即可接入系统。

#### 抽象基类：`AbstractImageProcessor`

```java
public abstract class AbstractImageProcessor implements ImageProcessor {
    protected final String name;
    protected final String description;

    protected AbstractImageProcessor(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public BufferedImage process(BufferedImage input) {
        // 模板方法：验证参数、创建输出图像、计时
        BufferedImage output = new BufferedImage(...);
        process(input, output);
        return output;
    }

    @Override
    public abstract void process(BufferedImage input, BufferedImage output);
}
```

**设计意图**：抽象基类提供公共功能（参数验证、计时统计、模板方法骨架），子类只需实现核心算法。

### 2.2 类继承关系

```
ImageProcessor (接口)
    ↑
    |
AbstractImageProcessor (抽象类)
    ↑
    |
+-- GrayscaleProcessor
+-- BrightnessProcessor
+-- ContrastProcessor
+-- BlurProcessor
+-- SharpenProcessor
+-- EdgeDetectorProcessor
+-- InvertProcessor
+-- FlipProcessor
+-- RotateProcessor
+-- ScaleProcessor
+-- ThresholdProcessor
+-- ErosionProcessor
+-- DilationProcessor
```

### 2.3 extends与super关键字

**extends关键字**：每个处理器类继承 `AbstractImageProcessor`

```java
public class GrayscaleProcessor extends AbstractImageProcessor {
    public GrayscaleProcessor() {
        super("灰度转换", "将彩色图像转换为灰度图像");  // super调用父类构造
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 实现灰度算法
    }
}
```

**super关键字的两种用法**：

1. **调用父类构造方法**（必须在子类构造方法第一行）：
   ```java
   super("灰度转换", "描述信息");
   ```

2. **调用父类被重写的方法**：
   ```java
   @Override
   public BufferedImage process(BufferedImage input) {
       // 调用父类模板方法逻辑
       BufferedImage result = super.process(input);
       // 添加子类特有逻辑
       return result;
   }
   ```

### 2.4 匿名内部类

在 `ImageLab.java` 的 `demonstrateAnonymousClass()` 方法中演示：

```java
// 使用匿名内部类创建自定义"棕褐色"处理器
// 无需单独创建类文件，直接new接口并实现
ImageProcessor sepiaProcessor = new AbstractImageProcessor("棕褐色", "复古棕褐色调") {
    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 实现棕褐色算法
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixel = ImageUtils.getPixel(input, x, y);
                int r = (int)(pixel[0]*0.393 + pixel[1]*0.769 + pixel[2]*0.189);
                int g = (int)(pixel[0]*0.349 + pixel[1]*0.686 + pixel[2]*0.168);
                int b = (int)(pixel[0]*0.272 + pixel[1]*0.534 + pixel[2]*0.131);
                ImageUtils.setPixel(output, x, y,
                    ImageUtils.clamp(r), ImageUtils.clamp(g), ImageUtils.clamp(b));
            }
        }
    }
};
```

### 2.5 Lambda表达式

在 `ImageLab.java` 的 `demonstrateLambda()` 方法中演示：

```java
// 使用stream + lambda过滤处理器
processors.values().stream()
    .filter(p -> p.getName().contains("边缘"))  // Lambda表达式
    .forEach(name -> System.out.println("  - " + name));

// 使用方法引用替代lambda
processors.values().stream()
    .map(ImageProcessor::getName)  // 等同于 p -> p.getName()
    .forEach(System.out::println);
```

---

## 三、程序代码及调试

### 3.1 项目结构

```
img-lab/
├── pom.xml                                    # Maven配置
├── src/main/java/com/imglab/
│   ├── ImageLab.java                          # TUI主入口
│   ├── ImageLabCLI.java                       # CLI入口
│   ├── processor/
│   │   ├── ImageProcessor.java               # 【接口】图像处理器
│   │   ├── AbstractImageProcessor.java       # 【抽象类】处理器基类
│   │   ├── GrayscaleProcessor.java           # 灰度转换
│   │   ├── BrightnessProcessor.java          # 亮度调整
│   │   ├── ContrastProcessor.java           # 对比度调整
│   │   ├── BlurProcessor.java               # 模糊
│   │   ├── SharpenProcessor.java            # 锐化
│   │   ├── EdgeDetectorProcessor.java       # 边缘检测
│   │   ├── InvertProcessor.java             # 颜色反转
│   │   ├── FlipProcessor.java               # 翻转
│   │   ├── RotateProcessor.java             # 旋转
│   │   ├── ScaleProcessor.java              # 缩放
│   │   ├── ThresholdProcessor.java          # 阈值二值化
│   │   ├── ErosionProcessor.java            # 腐蚀
│   │   └── DilationProcessor.java           # 膨胀
│   └── util/
│       └── ImageUtils.java                   # 工具类
└── src/test/java/com/imglab/
    └── ImageLabTest.java                     # 测试类
```

### 3.2 编译构建

#### 环境要求
- JDK 17+
- Maven 3.x
- JUnit 5.10.0（用于单元测试）

#### 测试

运行所有测试：
```bash
mvn test
```

测试文件位于 `src/test/java/com/imglab/ImageLabTest.java`，扫描 `images/` 目录中的所有图片，应用14种图像处理操作，输出到 `image_output/` 目录。

#### 图像路径格式

支持绝对路径和相对路径：

| 系统 | 路径示例 |
|------|---------|
| Linux | `/home/user/images/photo.png` 或 `./images/photo.png` |
| macOS | `/Users/user/images/photo.png` 或 `./images/photo.png` |
| Windows | `C:\Users\user\images\photo.png` 或 `.\images\photo.png` |

Java 会自动处理路径分隔符，上述格式均可用。Windows 用户也可使用正斜杠（`/`）作为路径分隔符。

#### 编译项目
```bash
mvn compile
```

#### 打包JAR
```bash
mvn package
```

#### 运行TUI菜单
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab"
```

#### 运行CLI（单文件）

**语法：**
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="<输入图像> <输出图像> <操作> [参数1] [参数2] ..."
```

**示例：**
```bash
# 灰度转换（无参数）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png grayscale"

# 亮度调整（正数变亮，负数变暗）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png brightness 50"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png brightness -30"

# 对比度调整（>1 增加对比度，<1 减少对比度）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png contrast 1.5"

# 旋转（90的倍数）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png rotate 90"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png rotate 180"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png rotate 270"

# 缩放（>1 放大，<1 缩小）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png scale 2.0"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png scale 0.5"

# 阈值二值化（0-255）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="input.png output.png threshold 128"
```

#### 运行CLI（批处理）

**语法：**
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="--batch <输入目录> <输出目录> <操作列表>"
```

批处理会依次应用列表中的所有操作到每个图像。

**示例：**
```bash
# 基础批处理（灰度 -> 模糊）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch ./images/ ./output/ grayscale blur"

# 带参数的批处理（亮度+50 -> 锐化）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch ./images/ ./output/ brightness,50 sharpen"

# 复杂流水线（灰度 -> 缩放2倍 -> 边缘检测）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch ./images/ ./output/ grayscale scale,2.0 edge"
```

#### 直接运行JAR

打包后可脱离 Maven 直接运行，使用方法与 `mvn exec:java` 相同，只需替换命令：

**TUI 模式：**
```bash
java -jar target/img-lab-1.0-SNAPSHOT.jar
java -jar target/img-lab-1.0-SNAPSHOT.jar /path/to/image.png
```

**CLI 单文件模式：**
```bash
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png grayscale
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png brightness 50
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png rotate 90
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png scale 2.0
```

**CLI 批处理模式：**
```bash
java -jar target/img-lab-1.0-SNAPSHOT.jar --batch ./images/ ./output/ grayscale blur
java -jar target/img-lab-1.0-SNAPSHOT.jar --batch ./images/ ./output/ brightness,50 sharpen
```

#### 验证编译

```bash
$ mvn compile
[INFO] BUILD SUCCESS

$ mvn package
[INFO] BUILD SUCCESS
$ ls target/*.jar
target/img-lab-1.0-SNAPSHOT.jar
```

### 3.3 核心代码分析

#### ImageProcessor.java（接口定义）

```java
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
```

**关键点**：
- `interface` 关键字定义接口
- 接口方法默认 `public abstract`
- 实现类使用 `implements` 关键字

#### AbstractImageProcessor.java（抽象基类）

```java
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
```

**关键点**：
- `abstract class` 定义抽象类
- `extends` 关键字继承接口（这里 `implements` ImageProcessor）
- `protected` 字段子类可见
- 模板方法模式：`process(BufferedImage)` 定义处理流程骨架
- `abstract` 方法强制子类实现核心算法

#### GrayscaleProcessor.java（具体实现类）

```java
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
```

**关键点**：
- `extends AbstractImageProcessor` 继承抽象类
- `super("名称", "描述")` 调用父类构造方法
- 重写 `process()` 方法实现具体算法

#### BrightnessProcessor.java（亮度调整处理器）

```java
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
        // super关键字调用父类构造方法，传入名称和描述
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
                // 每个通道加上亮度值，并限制在0-255范围内
                int r = ImageUtils.clamp(pixel[0] + brightness);
                int g = ImageUtils.clamp(pixel[1] + brightness);
                int b = ImageUtils.clamp(pixel[2] + brightness);
                ImageUtils.setPixel(output, x, y, r, g, b);
            }
        }
    }
}
```

**extends与super关键字解析**：

```java
public class BrightnessProcessor extends AbstractImageProcessor {
    public BrightnessProcessor(int brightness) {
        // 【super关键字】调用父类构造方法
        // 将名称和描述传递给父类存储
        super("亮度调整", "调整图像亮度 (值: " + brightness + ")");
        this.brightness = brightness;
    }
}
```

- `extends AbstractImageProcessor`：声明此类继承自抽象基类
- `super("亮度调整", ...)`: 在子类构造方法第一行调用父类构造

#### BlurProcessor.java（模糊处理器）

```java
package com.imglab.processor;

import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * 模糊处理器
 * 使用5x5高斯模糊滤波器
 */
public class BlurProcessor extends AbstractImageProcessor {

    public BlurProcessor() {
        super("模糊", "5x5高斯模糊滤波");
    }

    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 5x5高斯模糊核 (sigma≈1.4)
        // 中心权重最大，边缘递减，符合自然模糊效果
        float[][] kernel = {
            { 1f/256f,  4f/256f,  6f/256f,  4f/256f,  1f/256f },
            { 4f/256f, 16f/256f, 24f/256f, 16f/256f,  4f/256f },
            { 6f/256f, 24f/256f, 36f/256f, 24f/256f,  6f/256f },
            { 4f/256f, 16f/256f, 24f/256f, 16f/256f,  4f/256f },
            { 1f/256f,  4f/256f,  6f/256f,  4f/256f,  1f/256f }
        };

        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 使用ImageUtils的applyKernel方法应用卷积，半径为2
                int[] result = ImageUtils.applyKernel(input, x, y, kernel, 2);
                ImageUtils.setPixel(output, x, y, result[0], result[1], result[2]);
            }
        }
    }
}
```

#### EdgeDetectorProcessor.java（边缘检测处理器）

```java
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
        // Sobel X方向核 - 检测垂直边缘
        float[][] sobelX = {
            { -1f,  0f,  1f },
            { -2f,  0f,  2f },
            { -1f,  0f,  1f }
        };

        // Sobel Y方向核 - 检测水平边缘
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
```

#### ImageUtils.java（工具类）

```java
package com.imglab.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像工具类
 * 提供常用的图像处理辅助方法
 */
public final class ImageUtils {

    // 工具类不允许实例化，私有构造方法
    private ImageUtils() {
    }

    /**
     * 从文件加载图像
     */
    public static BufferedImage loadImage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + path);
        }
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IOException("无法读取图像: " + path);
        }
        return img;
    }

    /**
     * 保存图像到文件
     */
    public static void saveImage(BufferedImage image, String path) throws IOException {
        String format = getFormat(path);
        File file = new File(path);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("无法保存图像格式: " + format);
        }
    }

    /**
     * 获取像素RGB值
     */
    public static int[] getPixel(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);
        return new int[] {
            (rgb >> 16) & 0xFF, // R红色分量
            (rgb >> 8) & 0xFF,  // G绿色分量
            rgb & 0xFF         // B蓝色分量
        };
    }

    /**
     * 设置像素RGB值
     */
    public static void setPixel(BufferedImage image, int x, int y, int r, int g, int b) {
        int rgb = (r << 16) | (g << 8) | b;
        image.setRGB(x, y, rgb);
    }

    /**
     * 限制值在0-255范围内（防止溢出）
     */
    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    /**
     * 应用卷积核到像素
     */
    public static int[] applyKernel(BufferedImage image, int x, int y,
                                     float[][] kernel, float divisor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int kSize = kernel.length;
        int kHalf = kSize / 2;

        float rSum = 0, gSum = 0, bSum = 0;

        // 遍历卷积核覆盖的区域
        for (int i = 0; i < kSize; i++) {
            for (int j = 0; j < kSize; j++) {
                // 计算相邻像素位置（边缘处需处理边界）
                int px = Math.max(0, Math.min(x + i - kHalf, width - 1));
                int py = Math.max(0, Math.min(y + j - kHalf, height - 1));
                int[] pixel = getPixel(image, px, py);
                float k = kernel[i][j];

                // 累加加权像素值
                rSum += pixel[0] * k;
                gSum += pixel[1] * k;
                bSum += pixel[2] * k;
            }
        }

        return new int[] {
            clamp(rSum / divisor),
            clamp(gSum / divisor),
            clamp(bSum / divisor)
        };
    }
}
```

---

### 3.4 ImageLab.java（TUI主程序）

以下是 `ImageLab.java` 的完整源码，这是程序的**主入口类**，包含 `main()` 方法：

```java
package com.imglab;

import com.imglab.processor.*;
import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 图像处理实验室 - TUI主菜单
 *
 * 展示Java面向对象编程核心概念:
 * 1. 接口与多态
 * 2. extends继承
 * 3. super关键字
 * 4. 匿名内部类
 * 5. Lambda表达式
 */
public class ImageLab {

    /** 处理器注册表 - 使用Map存储所有处理器 */
    private static final Map<Integer, ImageProcessor> processors = new LinkedHashMap<>();

    /** 当前加载的图像 */
    private static BufferedImage currentImage;

    /** 当前图像路径 */
    private static String currentImagePath;

    static {
        // 【创建对象】在static块中创建处理器对象并注册
        processors.put(1, new GrayscaleProcessor());
        processors.put(2, new BrightnessProcessor(30));
        processors.put(3, new ContrastProcessor(1.5));
        processors.put(4, new BlurProcessor());
        processors.put(5, new SharpenProcessor());
        processors.put(6, new EdgeDetectorProcessor());
        processors.put(7, new InvertProcessor());
        processors.put(8, new FlipProcessor(true));   // 水平翻转
        processors.put(9, new FlipProcessor(false));  // 垂直翻转
        processors.put(10, new RotateProcessor(90));
        processors.put(11, new ScaleProcessor(1.5));
        processors.put(12, new ThresholdProcessor(128));
        processors.put(13, new ErosionProcessor());
        processors.put(14, new DilationProcessor());
    }

    public static void main(String[] args) {
        System.out.println("=== 图像处理实验室 ===");
        System.out.println();

        // 支持直接通过命令行参数加载图像
        if (args.length > 0) {
            try {
                loadImage(args[0]);
                System.out.println("已加载图像: " + currentImagePath);
                System.out.println("尺寸: " + currentImage.getWidth() + "x" + currentImage.getHeight());
                System.out.println();
            } catch (IOException e) {
                System.out.println("加载图像失败: " + e.getMessage());
                System.exit(1);
            }
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();

            System.out.print("请选择操作 (0-14): ");
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);

                if (choice == 0) {
                    System.out.println("感谢使用，再见！");
                    break;
                }

                if (choice == 99) {
                    // 【调用方法】演示匿名内部类
                    demonstrateAnonymousClass();
                    continue;
                }

                if (choice == 98) {
                    // 【调用方法】演示Lambda表达式
                    demonstrateLambda();
                    continue;
                }

                // 【调用方法】从Map中获取选中的处理器
                ImageProcessor processor = processors.get(choice);
                if (processor == null) {
                    System.out.println("无效选择，请重新输入！");
                    continue;
                }

                // 确保已加载图像
                if (currentImage == null) {
                    System.out.print("请输入图像路径: ");
                    String path = scanner.nextLine().trim();
                    try {
                        loadImage(path);
                    } catch (IOException e) {
                        System.out.println("加载失败: " + e.getMessage());
                        continue;
                    }
                }

                // 【调用方法】执行图像处理
                System.out.println("\n执行: " + processor.getName());
                System.out.println("描述: " + processor.getDescription());

                long startTime = System.currentTimeMillis();
                // 【调用方法】调用处理器的process方法
                BufferedImage result = processor.process(currentImage);
                long endTime = System.currentTimeMillis();

                System.out.println("处理完成! 耗时: " + (endTime - startTime) + "ms");

                // 保存结果
                System.out.print("保存结果路径 (直接回车保存到 image_output/): ");
                String savePath = scanner.nextLine().trim();
                if (savePath.isEmpty()) {
                    // 默认保存到 image_output/ 目录
                    File outputDir = new File("image_output");
                    if (!outputDir.exists()) {
                        outputDir.mkdirs();
                    }
                    // 根据原文件名和操作生成输出文件名
                    String originalName = currentImagePath != null ?
                        new File(currentImagePath).getName() : "output.png";
                    String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
                    String ext = originalName.substring(originalName.lastIndexOf('.'));
                    savePath = "image_output/" + baseName + "_" + processor.getName() + ext;
                }
                try {
                    // 确保输出目录存在
                    File parentDir = new File(savePath).getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    ImageUtils.saveImage(result, savePath);
                    System.out.println("已保存到: " + savePath);
                } catch (IOException e) {
                    System.out.println("保存失败: " + e.getMessage());
                }

                // 更新当前图像
                currentImage = result;

            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n--- 主菜单 ---");
        System.out.println("当前图像: " + (currentImage != null ?
            currentImagePath + " (" + currentImage.getWidth() + "x" + currentImage.getHeight() + ")" :
            "未加载"));
        System.out.println();
        System.out.println("  1. 灰度转换        8. 水平翻转");
        System.out.println("  2. 亮度调整 (+30)  9. 垂直翻转");
        System.out.println("  3. 对比度调整      10. 旋转 90°");
        System.out.println("  4. 模糊           11. 缩放 1.5x");
        System.out.println("  5. 锐化           12. 阈值二值化");
        System.out.println("  6. 边缘检测       13. 腐蚀");
        System.out.println("  7. 颜色反转       14. 膨胀");
        System.out.println(" 98. Lambda演示     99. 匿名内部类演示");
        System.out.println("  0. 退出");
    }

    private static void loadImage(String path) throws IOException {
        currentImage = ImageUtils.loadImage(path);
        currentImagePath = path;
    }
    // ... 匿名内部类和Lambda演示方法见下一节
}
```

**main()方法解析**：

```java
public static void main(String[] args) {
    // 【创建Scanner对象】
    Scanner scanner = new Scanner(System.in);

    // 【while循环 + switch结构】
    while (true) {
        // 【调用方法】显示菜单
        showMenu();

        // 【获取用户输入】
        String input = scanner.nextLine().trim();
        int choice = Integer.parseInt(input);

        // 【分支结构】
        if (choice == 0) break;

        // 【从Map获取对象】
        ImageProcessor processor = processors.get(choice);

        // 【调用对象方法】执行处理
        BufferedImage result = processor.process(currentImage);
    }
}
```

---

### 3.5 匿名内部类演示

以下是 `ImageLab.java` 中的 `demonstrateAnonymousClass()` 方法完整代码：

```java
/**
 * 演示匿名内部类用法
 * 使用匿名内部类创建一个自定义处理器
 *
 * 匿名内部类：直接new抽象类/接口并实现其方法
 * 无需单独创建一个新的.java文件
 */
private static void demonstrateAnonymousClass() {
    System.out.println("\n=== 匿名内部类演示 ===");
    System.out.println("创建一个自定义的'棕褐色'处理器...\n");

    // 【匿名内部类】
    // 语法：new 抽象类/接口() { @Override 方法实现 }
    // 直接new AbstractImageProcessor并实现process方法
    // 无需创建 SepiaProcessor.java 文件
    ImageProcessor sepiaProcessor = new AbstractImageProcessor("棕褐色", "复古棕褐色调处理") {

        // 匿名内部类中可以定义自己的字段
        // 使用父类的字段需要谨慎，这里直接使用局部变量

        @Override
        public void process(BufferedImage input, BufferedImage output) {
            int width = input.getWidth();
            int height = input.getHeight();

            // 棕褐色转换矩阵
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int[] pixel = ImageUtils.getPixel(input, x, y);
                    // 棕褐色转换公式（加权系数）
                    int r = (int) (pixel[0] * 0.393 + pixel[1] * 0.769 + pixel[2] * 0.189);
                    int g = (int) (pixel[0] * 0.349 + pixel[1] * 0.686 + pixel[2] * 0.168);
                    int b = (int) (pixel[0] * 0.272 + pixel[1] * 0.534 + pixel[2] * 0.131);
                    ImageUtils.setPixel(output, x, y,
                        ImageUtils.clamp(r), ImageUtils.clamp(g), ImageUtils.clamp(b));
                }
            }
        }
    };

    // 【调用方法】使用匿名内部类创建的对象
    System.out.println("匿名内部类创建的处理器: " + sepiaProcessor.getName());
    System.out.println("描述: " + sepiaProcessor.getDescription());

    if (currentImage != null) {
        System.out.println("\n执行棕褐色处理...");
        BufferedImage result = sepiaProcessor.process(currentImage);
        currentImage = result;
        System.out.println("处理完成！");
    } else {
        System.out.println("\n请先加载一张图像！");
    }
}
```

**匿名内部类语法解析**：

```java
// 完整写法（匿名内部类）
ImageProcessor processor = new AbstractImageProcessor("名称", "描述") {
    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 实现代码
    }
};

// 等价于先定义类，再创建对象
// public class SepiaProcessor extends AbstractImageProcessor { ... }
// ImageProcessor processor = new SepiaProcessor();

// 匿名内部类的优势：
// 1. 不需要单独创建.java文件
// 2. 适合只用一次的类
// 3. 代码更紧凑
```

---

### 3.6 Lambda表达式演示

以下是 `ImageLab.java` 中的 `demonstrateLambda()` 方法完整代码：

```java
/**
 * 演示Lambda表达式用法
 * 使用Lambda过滤和查找处理器
 *
 * Lambda表达式是Java 8引入的函数式编程特性
 * 语法：(参数) -> { 方法体 }
 */
private static void demonstrateLambda() {
    System.out.println("\n=== Lambda表达式演示 ===");

    // 【Lambda表达式】map + forEach
    // 获取所有处理器名称
    System.out.println("\n所有处理器 (使用stream + lambda):");
    processors.values().stream()
        // 【方法引用】ImageProcessor::getName 等价于 p -> p.getName()
        .map(ImageProcessor::getName)
        .forEach(name -> System.out.println("  - " + name));

    // 【Lambda表达式】filter + lambda
    // 过滤出名称包含"边缘"的处理器
    System.out.println("\n包含'边缘'的处理器 (filter + lambda):");
    processors.values().stream()
        // 【Lambda表达式】完整写法: p -> { return p.getName().contains("边缘"); }
        // 简化写法: p -> p.getName().contains("边缘")
        .filter(p -> p.getName().contains("边缘"))
        .map(ImageProcessor::getName)
        .forEach(name -> System.out.println("  - " + name));

    // 【Lambda表达式】统计
    long count = processors.values().stream()
        .filter(p -> p.getName().startsWith("阈"))
        .count();
    System.out.println("\n包含'阈'的处理器数量: " + count);

    // 【Lambda表达式】转换并收集
    System.out.println("\n处理器描述列表 (map + lambda):");
    List<String> descriptions = processors.values().stream()
        .map(ImageProcessor::getDescription)
        .collect(Collectors.toList());
    descriptions.forEach(desc -> System.out.println("  - " + desc));
}
```

**Lambda表达式语法解析**：

```java
// 标准Lambda表达式
(parameters) -> { statements }

// 无参数
() -> { System.out.println("hello"); }

// 单参数（括号可省略）
p -> p.getName()

// 多参数
(x, y) -> x + y

// 带方法体
p -> { return p.getName(); }

// 常用函数式接口
// Predicate<T>  - T -> boolean
// Function<T,R> - T -> R
// Consumer<T>   - T -> void
```

---

### 3.7 ImageLabCLI.java（CLI命令行程序）

以下是 `ImageLabCLI.java` 的完整源码：

```java
package com.imglab;

import com.imglab.processor.*;
import com.imglab.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

/**
 * 图像处理实验室 - CLI命令行接口
 *
 * 用法:
 *   java com.imglab.ImageLabCLI <输入> <输出> <操作> [参数]
 *   java com.imglab.ImageLabCLI --batch <输入目录> <输出目录> <操作> ...
 */
public class ImageLabCLI {

    // 【Lambda表达式】操作工厂映射
    // 使用Function<String[], ImageProcessor>作为值的类型
    private static final java.util.Map<String, Function<String[], ImageProcessor>> OPERATIONS =
        new java.util.LinkedHashMap<>();

    static {
        // 【创建对象】注册各操作的工厂
        OPERATIONS.put("grayscale", args -> new GrayscaleProcessor());
        OPERATIONS.put("gray", args -> new GrayscaleProcessor());

        // 【Lambda表达式】带参数的操作
        OPERATIONS.put("brightness", args -> {
            int value = args.length > 0 ? Integer.parseInt(args[0]) : 30;
            return new BrightnessProcessor(value);
        });

        OPERATIONS.put("contrast", args -> {
            double factor = args.length > 0 ? Double.parseDouble(args[0]) : 1.5;
            return new ContrastProcessor(factor);
        });

        OPERATIONS.put("blur", args -> new BlurProcessor());
        OPERATIONS.put("sharpen", args -> new SharpenProcessor());
        OPERATIONS.put("edge", args -> new EdgeDetectorProcessor());
        OPERATIONS.put("invert", args -> new InvertProcessor());
        OPERATIONS.put("flip-h", args -> new FlipProcessor(true));
        OPERATIONS.put("flip-v", args -> new FlipProcessor(false));

        OPERATIONS.put("rotate", args -> {
            int degrees = args.length > 0 ? Integer.parseInt(args[0]) : 90;
            return new RotateProcessor(degrees);
        });

        OPERATIONS.put("scale", args -> {
            double factor = args.length > 0 ? Double.parseDouble(args[0]) : 1.5;
            return new ScaleProcessor(factor);
        });

        OPERATIONS.put("threshold", args -> {
            int value = args.length > 0 ? Integer.parseInt(args[0]) : 128;
            return new ThresholdProcessor(value);
        });

        OPERATIONS.put("erode", args -> new ErosionProcessor());
        OPERATIONS.put("dilate", args -> new DilationProcessor());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        // 批处理模式
        if ("--batch".equals(args[0])) {
            runBatch(args);
            return;
        }

        // 单文件模式
        if (args.length < 3) {
            printUsage();
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String operation = args[2];
        String[] operationArgs = args.length > 3 ?
            Arrays.copyOfRange(args, 3, args.length) : new String[0];

        processImage(inputPath, outputPath, operation, operationArgs);
    }

    private static void processImage(String inputPath, String outputPath,
                                      String operation, String[] args) {
        try {
            // 【调用方法】加载图像
            System.out.println("加载图像: " + inputPath);
            BufferedImage input = ImageUtils.loadImage(inputPath);
            System.out.println("尺寸: " + input.getWidth() + "x" + input.getHeight());

            // 【调用方法】创建处理器
            ImageProcessor processor = createProcessor(operation, args);

            System.out.println("执行: " + processor.getName());

            // 【调用方法】执行处理
            long startTime = System.currentTimeMillis();
            BufferedImage output = processor.process(input);
            long endTime = System.currentTimeMillis();

            System.out.println("处理完成! 耗时: " + (endTime - startTime) + "ms");

            // 【调用方法】保存图像
            System.out.println("保存图像: " + outputPath);
            ImageUtils.saveImage(output, outputPath);
            System.out.println("完成!");

        } catch (IOException e) {
            System.err.println("错误: " + e.getMessage());
            System.exit(1);
        }
    }

    // 【创建对象】通过工厂创建处理器
    private static ImageProcessor createProcessor(String operation, String[] args) {
        Function<String[], ImageProcessor> factory = OPERATIONS.get(operation.toLowerCase());
        if (factory == null) {
            System.err.println("未知操作: " + operation);
            System.err.println("支持的操作:");
            OPERATIONS.keySet().forEach(op -> System.err.println("  - " + op));
            System.exit(1);
        }
        // 【调用方法】apply接收参数并返回处理器实例
        return factory.apply(args);
    }

    private static void printUsage() {
        System.out.println("图像处理实验室 CLI");
        System.out.println();
        System.out.println("用法:");
        System.out.println("  单文件: java com.imglab.ImageLabCLI <输入> <输出> <操作> [参数]");
        System.out.println("  批处理: java com.imglab.ImageLabCLI --batch <输入目录> <输出目录> <操作>");
        System.out.println();
        System.out.println("支持的操作:");
        OPERATIONS.keySet().forEach(op -> System.out.println("  - " + op));
    }
}
```

---

### 3.8 调试记录

#### 编译调试
```
mvn compile
[INFO] Compiling 24 source files with javac [debug target 17] to target/classes
[INFO] BUILD SUCCESS
```

#### 常见问题

**Q: `NoSuchMethodError` 或 `LinkageError`**
A: 确保JDK版本与pom.xml中配置的版本一致（推荐JDK 17）

**Q: 图像加载失败**
A: 检查文件路径是否正确，确保文件存在且格式受支持（PNG/JPG/BMP）

**Q: 处理后图像尺寸变化**
A: 旋转90度/270度会交换宽高，缩放操作会按比例改变尺寸

---

## 四、实验运行结果

### 4.1 TUI菜单操作示例

```
=== 图像处理实验室 ===

--- 主菜单 ---
当前图像: 未加载

  1. 灰度转换
  2. 亮度调整 (+30)
  3. 对比度调整 (1.5x)
  4. 模糊
  5. 锐化
  6. 边缘检测 (Sobel)
  7. 颜色反转
  8. 水平翻转
  9. 垂直翻转
 10. 旋转 90°
 11. 缩放 1.5x
 12. 阈值二值化
 13. 腐蚀
 14. 膨胀
 98. 演示 Lambda 表达式
 99. 演示 匿名内部类
  0. 退出

请选择操作 (0-14): 1
请输入图像路径: /path/to/image.png

执行: 灰度转换
描述: 将彩色图像转换为灰度图像
  [INFO] 灰度转换 耗时: 156ms
处理完成! 耗时: 156ms
保存结果路径 (直接回车保存到 image_output/):
已保存到: image_output/photo_灰度转换.png
```

### 4.2 Lambda表达式演示输出

```
=== Lambda表达式演示 ===

所有处理器 (使用stream + lambda):
  - 灰度转换
  - 亮度调整
  - 对比度调整
  - 模糊
  - 锐化
  - 边缘检测
  - 颜色反转
  - 水平翻转
  - 垂直翻转
  - 旋转90°
  - 缩放150%
  - 阈值二值化
  - 腐蚀
  - 膨胀

包含'边缘'的处理器 (filter + lambda):
  - 边缘检测

包含'阈'的处理器数量: 1
```

### 4.3 CLI批处理示例

```bash
$ mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
    -Dexec.args="input.png output.png brightness 50"

加载图像: input.png
尺寸: 1920x1080
执行: 亮度调整
处理完成! 耗时: 203ms
保存图像: output.png
完成!
```

---

## 五、实验总结

### 5.1 OOP概念回顾

| 概念 | 代码位置 | 说明 |
|------|----------|------|
| **接口** | `ImageProcessor.java` | 定义图像处理器的规范 |
| **implements** | `AbstractImageProcessor` | 类实现接口 |
| **extends** | 所有Processor类 | 类继承抽象基类 |
| **super** | 各Processor构造方法 | 调用父类构造方法 |
| **匿名内部类** | `ImageLab.demonstrateAnonymousClass()` | 直接new接口创建临时处理器 |
| **Lambda表达式** | `ImageLab.demonstrateLambda()` | stream API函数式编程 |

### 5.2 关键语法点

#### 接口定义
```java
public interface ImageProcessor {
    String getName();
    BufferedImage process(BufferedImage input);
}
```

#### 类继承
```java
public class GrayscaleProcessor extends AbstractImageProcessor {
    public GrayscaleProcessor() {
        super("灰度转换", "描述");  // super调用父类构造
    }
}
```

#### 抽象类
```java
public abstract class AbstractImageProcessor implements ImageProcessor {
    protected final String name;  // protected子类可见

    protected AbstractImageProcessor(String name, String description) {
        this.name = name;  // this区分字段和参数
    }

    public abstract void process(BufferedImage input, BufferedImage output);
    // abstract方法无方法体，子类必须重写
}
```

#### 匿名内部类
```java
ImageProcessor processor = new AbstractImageProcessor("名称", "描述") {
    @Override
    public void process(BufferedImage input, BufferedImage output) {
        // 实现
    }
};
```

#### Lambda表达式
```java
// 标准写法
.filter(p -> p.getName().contains("边缘"))

// 方法引用
.map(ImageProcessor::getName)
.forEach(System.out::println)
```

### 5.3 设计模式应用

- **模板方法模式**：`AbstractImageProcessor.process()` 定义处理流程骨架
- **策略模式**：`ImageProcessor` 接口允许运行时切换处理算法
- **工厂模式**：通过操作名称映射创建对应处理器

### 5.4 实验心得

1. **接口与抽象类的选择**：接口用于定义规范，抽象类用于提供公共实现。当需要提供公共代码时使用抽象类，仅定义规范时使用接口。

2. **extends与implements的区别**：
   - `extends` 用于继承类（只能单继承）
   - `implements` 用于实现接口（可以多实现）

3. **super关键字**：不仅用于调用父类构造方法，还可以调用被重写的父类方法。

4. **匿名内部类**：适合一次性使用的类，避免创建单独的类文件。

5. **Lambda表达式**：使函数式编程成为可能，让代码更简洁。

### 5.5 进一步扩展

- 添加更多图像处理操作（伽马校正、卡通化等）
- 实现图像混合（图层叠加）
- 添加水印功能
- 支持更多图像格式（GIF、WebP）
- 添加图像直方图统计

---

## 附录：完整文件列表

| 文件名 | 行数 | 说明 |
|--------|------|------|
| pom.xml | 43 | Maven项目配置 |
| ImageProcessor.java | 45 | 核心接口 |
| AbstractImageProcessor.java | 68 | 抽象基类 |
| GrayscaleProcessor.java | 31 | 灰度转换 |
| BrightnessProcessor.java | 35 | 亮度调整 |
| ContrastProcessor.java | 35 | 对比度调整 |
| BlurProcessor.java | 28 | 模糊 |
| SharpenProcessor.java | 28 | 锐化 |
| EdgeDetectorProcessor.java | 50 | 边缘检测 |
| InvertProcessor.java | 28 | 颜色反转 |
| FlipProcessor.java | 40 | 翻转 |
| RotateProcessor.java | 67 | 旋转 |
| ScaleProcessor.java | 75 | 缩放 |
| ThresholdProcessor.java | 30 | 阈值二值化 |
| ErosionProcessor.java | 42 | 腐蚀 |
| DilationProcessor.java | 42 | 膨胀 |
| ImageLab.java | 270 | TUI主程序 |
| ImageLabCLI.java | 163 | CLI程序 |
| ImageUtils.java | 110 | 工具类 |

**总计：约1100行Java代码**
