# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
mvn compile          # Compile the project
mvn package          # Build JAR to target/img-lab-1.0-SNAPSHOT.jar
mvn clean            # Clean build artifacts
```

## Run Commands

```bash
# TUI menu mode
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLab" -Dexec.args="/path/to/image.png"  # with image arg

# CLI mode
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="input.png output.png grayscale"
mvn exec:java -Dexec.mainClass="com.imglab.ImageLabCLI" -Dexec.args="--batch input/ output/ brightness,30 blur"

# Run JAR directly
java -jar target/img-lab-1.0-SNAPSHOT.jar
java -jar target/img-lab-1.0-SNAPSHOT.jar input.png output.png grayscale
```

## Project Architecture

This is a Java OOP demonstration project with two separate labs:

### `com.imglab` - Image Processing Lab
Uses **interface + abstract class + concrete class**三层架构:
```
ImageProcessor (interface) → AbstractImageProcessor (abstract) → *Processor (13 implementations)
```

**Core components:**
- `ImageProcessor` interface - defines `getName()`, `getDescription()`, `process(BufferedImage)`, `process(BufferedImage, BufferedImage)`
- `AbstractImageProcessor` - template method pattern; `process(input)` validates, creates output buffer, calls abstract `process(input, output)`, logs timing
- 13 processors: GrayscaleProcessor, BrightnessProcessor, ContrastProcessor, BlurProcessor, SharpenProcessor, EdgeDetectorProcessor, InvertProcessor, FlipProcessor, RotateProcessor, ScaleProcessor, ThresholdProcessor, ErosionProcessor, DilationProcessor
- `ImageUtils` - utility class (private constructor) with `loadImage()`, `saveImage()`, `getPixel()`, `setPixel()`, `clamp()`, `applyKernel()`

**Entry points:**
- `ImageLab` - TUI menu (main class configured in pom.xml)
- `ImageLabCLI` - command-line interface with operation factory pattern using `Function<String[], ImageProcessor>`

### `com.commlab3` - Signal Processing Lab
Demonstrates class inheritance hierarchy:
```
SignalProcessor (interface)
BaseSignal (abstract base)
  ├── AnalogSignal
  ├── DigitalSignal
  └── FiberOpticSignal
SignalProcessorTest
```

## CLI Operations

| Operation | Command | Parameters |
|-----------|---------|------------|
| Grayscale | `grayscale` / `gray` | none |
| Brightness | `brightness` | integer (e.g., 30, -50) |
| Contrast | `contrast` | double (e.g., 1.5) |
| Blur | `blur` | none |
| Sharpen | `sharpen` | none |
| Edge detection | `edge` | none (Sobel) |
| Invert | `invert` | none |
| Flip horizontal | `flip-h` | none |
| Flip vertical | `flip-v` | none |
| Rotate | `rotate` | degrees (90 multiples) |
| Scale | `scale` | factor (e.g., 0.5, 2.0) |
| Threshold | `threshold` | 0-255 |
| Erode | `erode` | none |
| Dilate | `dilate` | none |
