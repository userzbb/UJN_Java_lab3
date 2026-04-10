# Image Lab - 图像处理实验室

基于 Java 的图像处理工具，演示 Java 面向对象编程核心概念。

## 功能特性

- **14种图像处理操作**：灰度、亮度、对比度、模糊、锐化、边缘检测、颜色反转、翻转、旋转、缩放、阈值二值化、腐蚀、膨胀
- **双交互模式**：TUI 菜单驱动 / CLI 命令行批处理
- **纯 Java 实现**：无外部依赖，使用标准库 `javax.imageio`

## 环境要求

- JDK 17+
- Maven 3.x

查看 Java 版本：
```bash
java -version
javac -version
```

---

## 图像路径格式

支持绝对路径和相对路径：

| 系统 | 路径示例 |
|------|---------|
| Linux | `/home/user/images/photo.png` 或 `./images/photo.png` |
| macOS | `/Users/user/images/photo.png` 或 `./images/photo.png` |
| Windows | `C:\Users\user\images\photo.png` 或 `.\images\photo.png` |

Java 会自动处理路径分隔符，上述格式均可用。Windows 用户也可使用正斜杠（`/`）作为路径分隔符。

---

## 编译构建

### 编译项目

```bash
mvn compile
```

编译后的 class 文件位于 `target/classes/`

### 打包 JAR

```bash
mvn package
```

生成 JAR 文件：`target/img-lab-1.0-SNAPSHOT.jar`

### 清理构建

```bash
mvn clean
```

---

## 运行

### TUI 菜单模式（交互式）

```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab"
```

启动后显示文字菜单，输入数字选择操作。可多次处理同一图像。

**命令行参数**（可选）：直接指定输入图像
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab" -Dexec.args="/path/to/image.png"
```

### CLI 命令行模式（单文件）

**语法：**
```bash
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="<输入图像> <输出图像> <操作> [参数1] [参数2] ..."
```

**基础示例：**
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

### CLI 批处理模式

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

# 多种输出（对每个输入图像应用多个处理，生成多个输出）
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch ./photos/ ./grayscale/ grayscale"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" \
     -Dexec.args="--batch ./photos/ ./edges/ edge"
```

**注意：** 批处理目前对每个操作是独立处理的，即 `--batch in/ out/ op1 op2` 会先处理所有文件的 op1，再处理所有文件的 op2，而不是链式处理单个文件。

---

## CLI 支持的操作

### 操作列表与参数说明

| 操作 | CLI 命令 | 参数说明 | 示例 |
|------|----------|----------|------|
| 灰度转换 | `grayscale` / `gray` | 无 | `grayscale` |
| 亮度调整 | `brightness` | 整数（正数变亮，负数变暗） | `brightness 50` / `brightness -30` |
| 对比度调整 | `contrast` | 小数（>1 增加，<1 减少） | `contrast 1.5` / `contrast 0.8` |
| 模糊 | `blur` | 无 | `blur` |
| 锐化 | `sharpen` | 无 | `sharpen` |
| 边缘检测 | `edge` | 无（Sobel算子） | `edge` |
| 颜色反转 | `invert` | 无 | `invert` |
| 水平翻转 | `flip-h` | 无 | `flip-h` |
| 垂直翻转 | `flip-v` | 无 | `flip-v` |
| 旋转 | `rotate` | 度数（90的倍数） | `rotate 90` / `rotate 180` / `rotate 270` |
| 缩放 | `scale` | 比例（>1 放大，<1 缩小） | `scale 2.0` / `scale 0.5` |
| 阈值二值化 | `threshold` | 0-255 的整数 | `threshold 128` |
| 腐蚀 | `erode` | 无 | `erode` |
| 膨胀 | `dilate` | 无 | `dilate` |

### 参数默认值

带参数的操作如果省略参数会使用默认值：

| 操作 | 默认参数值 |
|------|-----------|
| `brightness` | 30 |
| `contrast` | 1.5 |
| `rotate` | 90 |
| `scale` | 1.5 |
| `threshold` | 128 |

---

## 项目结构

```
img-lab/
├── pom.xml                                    # Maven 配置
├── README.md                                  # 本文件
├── REPORT.md                                  # 实验报告
├── images/                                    # 输入图像目录
├── image_output/                              # 输出图像目录
└── src/
    ├── main/java/com/imglab/
    │   ├── ImageLab.java                     # TUI 主入口（菜单选项 1-14）
    │   ├── ImageLabCLI.java                   # CLI 入口（支持单文件和批处理）
    │   ├── processor/
    │   │   ├── ImageProcessor.java            # 接口：图像处理器规范
    │   │   ├── AbstractImageProcessor.java    # 抽象基类：模板方法模式
    │   │   ├── GrayscaleProcessor.java        # 灰度转换
    │   │   ├── BrightnessProcessor.java       # 亮度调整
    │   │   ├── ContrastProcessor.java         # 对比度调整
    │   │   ├── BlurProcessor.java             # 模糊
    │   │   ├── SharpenProcessor.java           # 锐化
    │   │   ├── EdgeDetectorProcessor.java     # 边缘检测
    │   │   ├── InvertProcessor.java           # 颜色反转
    │   │   ├── FlipProcessor.java             # 翻转（水平/垂直）
    │   │   ├── RotateProcessor.java           # 旋转
    │   │   ├── ScaleProcessor.java            # 缩放
    │   │   ├── ThresholdProcessor.java        # 阈值二值化
    │   │   ├── ErosionProcessor.java          # 腐蚀
    │   │   └── DilationProcessor.java         # 膨胀
    │   └── util/
    │       └── ImageUtils.java                # 工具类：图像加载/保存/像素操作/卷积
    └── test/java/com/imglab/
        └── ImageLabTest.java                  # 集成测试：验证所有14种处理器
```

---

## 直接运行 JAR

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

---

## TUI 菜单说明

运行 TUI 模式后：

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
```

输入数字选择操作。选择后会提示输入图像路径（如果未在命令行指定）。

处理完成后默认保存到 `image_output/` 目录（文件名格式：`原名_操作名.扩展名`），也可输入自定义路径。

---

## 演示功能

程序内置两个演示功能（菜单选项 98、99）：

- **选项 98 - Lambda 表达式**：演示 stream API + Lambda 过滤处理器列表
- **选项 99 - 匿名内部类**：演示用匿名内部类创建自定义"棕褐色"处理器

---

## 常见问题

**Q: 提示 "文件不存在"**
A: 检查图像路径是否正确，确保文件存在且格式受支持（PNG、JPG、BMP）

**Q: 编译报错 "release 版本不匹配"**
A: 确保 JDK 版本 >= 17，查看 `pom.xml` 中的 `<release>17</release>`

**Q: 批处理模式找不到文件**
A: 确保输入目录存在且包含 .png/.jpg/.jpeg/.bmp 文件

---

## 测试

运行单元测试验证所有处理器功能：

```bash
mvn test
```

测试会扫描 `images/` 目录中的所有图片，应用全部 14 种图像处理操作，输出到 `image_output/` 目录。

**输出文件命名格式：** `原文件名_处理器名称.扩展名`

例如：`wallhaven-y8wpxl_灰度转换.png`

**测试结构：**
```
src/test/java/com/imglab/
└── ImageLabTest.java    # 集成测试：验证所有14种处理器
```
