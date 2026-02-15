import json
from PIL import Image


def image_to_json_bitmap(input_image_path, output_json_path):
    """
    将图片逆时针旋转90度后，提取黑色像素坐标并保存为JSON。
    """
    try:
        # 1. 打开图片并转换为 RGBA 模式
        img = Image.open(input_image_path).convert("RGBA")
    except Exception as e:
        print(f"无法打开图片: {e}")
        return

    print(f"原始尺寸: {img.size}")

    # 2. 逆时针旋转 90 度 (Counter-Clockwise)
    # expand=True 确保旋转后图像尺寸自动调整，不会被裁剪
    # 使用 transpose(Image.ROTATE_90) 也是标准做法，且处理正方形或矩形都很安全
    rotated_img = img.transpose(Image.Transpose.ROTATE_90)

    width, height = rotated_img.size
    print(f"旋转后尺寸 (逆时针90°): {width}x{height}")

    pixels = rotated_img.load()
    black_points = []

    # 3. 遍历旋转后的图片像素
    for y in range(height):
        for x in range(width):
            r, g, b, a = pixels[x, y]

            # 判定逻辑：
            # 1. 透明度 a 必须大于 0 (或设置一个阈值比如 128)
            # 2. 颜色必须是黑色 (R=0, G=0, B=0)
            # 注意：如果你的图片是“非白即黑”，可以放宽条件，比如 r < 128

            # 这里使用严格的黑色且不透明判定
            if a > 128 and r == 0 and g == 0 and b == 0:
                black_points.append([x, y])

    # 4. 保存为 JSON
    with open(output_json_path, 'w', encoding='utf-8') as f:
        # separators=(',', ':') 可以去除空格，让文件更小
        json.dump(black_points, f, separators=(',', ':'))

    print(f"成功生成 JSON 文件: {output_json_path}")
    print(f"共提取了 {len(black_points)} 个点。")


# =================使用示例=================
if __name__ == "__main__":
    # 请确保 input.png 存在，或者修改为你的图片路径
    # 生成的 output.json 即为旋转后的位图坐标列表
    image_to_json_bitmap("horse_outlined.png", "horse.json")
