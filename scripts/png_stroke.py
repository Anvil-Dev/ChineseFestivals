from PIL import Image


def generate_outlined_sprite(input_path, output_path, scale=4, margin=9):
    """
    生成描边图片，并增加画布留白。
    :param margin: 四周留白的像素数 (默认9，256+9+9=274)
    """
    # 1. 加载图像
    try:
        img = Image.open(input_path).convert("RGBA")
    except FileNotFoundError:
        print(f"找不到文件: {input_path}")
        return

    width, height = img.size

    # 2. 计算新画布尺寸
    # 原始内容尺寸
    content_w = width * scale
    content_h = height * scale
    # 加上留白后的总尺寸 (256 + 18 = 274)
    final_w = content_w + (margin * 2)
    final_h = content_h + (margin * 2)

    # 创建全透明背景
    new_img = Image.new("RGBA", (final_w, final_h), (0, 0, 0, 0))

    pixels = img.load()
    new_pixels = new_img.load()

    # 描边颜色 (黑色, 不透明)
    edge_color = (0, 0, 0, 255)

    # 辅助函数：判断某点是否为实体
    def is_solid(x, y):
        if x < 0 or x >= width or y < 0 or y >= height:
            return False
        return pixels[x, y][3] > 0  # Alpha > 0 视为实体

    # 3. 遍历原图像素
    for x in range(width):
        for y in range(height):
            if not is_solid(x, y):
                continue

            # --- 关键修改：计算在新画布上的起始位置 ---
            # 原有的 x*scale 加上 margin 偏移量
            sx = (x * scale) + margin
            sy = (y * scale) + margin

            # 获取四周邻居状态
            top_solid = is_solid(x, y - 1)
            bottom_solid = is_solid(x, y + 1)
            left_solid = is_solid(x - 1, y)
            right_solid = is_solid(x + 1, y)

            # ==========================
            # 1. 绘制直线边缘
            # ==========================

            # 上边缘 (如果上方是空的，就在当前块的顶部画线)
            if not top_solid:
                for i in range(scale):
                    new_pixels[sx + i, sy] = edge_color

            # 下边缘
            if not bottom_solid:
                for i in range(scale):
                    new_pixels[sx + i, sy + scale - 1] = edge_color

            # 左边缘
            if not left_solid:
                for i in range(scale):
                    new_pixels[sx, sy + i] = edge_color

            # 右边缘
            if not right_solid:
                for i in range(scale):
                    new_pixels[sx + scale - 1, sy + i] = edge_color

            # ==========================
            # 2. 绘制内转角 (修复断裂)
            # ==========================

            # 右下内转角补点
            if right_solid and bottom_solid and not is_solid(x + 1, y + 1):
                new_pixels[sx + scale - 1, sy + scale - 1] = edge_color

            # 左下内转角补点
            if left_solid and bottom_solid and not is_solid(x - 1, y + 1):
                new_pixels[sx, sy + scale - 1] = edge_color

            # 右上内转角补点
            if right_solid and top_solid and not is_solid(x + 1, y - 1):
                new_pixels[sx + scale - 1, sy] = edge_color

            # 左上内转角补点
            if left_solid and top_solid and not is_solid(x - 1, y - 1):
                new_pixels[sx, sy] = edge_color

    # 4. 保存
    new_img.save(output_path)
    print(f"处理完成！")
    print(f"原图尺寸: {width}x{height}")
    print(f"输出尺寸: {final_w}x{final_h} (包含四周各 {margin} 像素留白)")
    print(f"保存至: {output_path}")


if __name__ == "__main__":
    # margin=9, scale=4 -> 64*4 + 18 = 274
    generate_outlined_sprite("horse.png", "horse_outlined.png", scale=4, margin=9)