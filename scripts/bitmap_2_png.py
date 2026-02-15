import json
from PIL import Image


def json_to_bitmap(json_source, output_filename="output.png", margin=10):
    """
    将包含 [x, y] 坐标的 JSON 数据转换为 PNG 图片。

    :param json_source: JSON 字符串 或 文件路径
    :param output_filename: 输出图片的文件名
    :param margin: 图片边缘的留白（像素）
    """

    # 1. 加载数据
    try:
        # 尝试判断输入是文件路径还是JSON字符串
        if json_source.endswith('.json'):
            with open(json_source, 'r', encoding='utf-8') as f:
                points = json.load(f)
        else:
            points = json.loads(json_source)
    except Exception as e:
        print(f"数据加载失败: {e}")
        return

    if not points:
        print("数据为空，无法生成图片。")
        return

    # 2. 计算图片尺寸
    # 获取最大的 X 和 Y 值来确定画布大小
    # 假设坐标都是正整数。如果有负数，需要做额外的平移处理。
    max_x = max(p[0] for p in points)
    max_y = max(p[1] for p in points)

    # 宽度和高度 = 最大坐标 + 1 (因为坐标从0开始) + 留白
    width = max_x + 1 + (margin * 2)
    height = max_y + 1 + (margin * 2)

    print(f"画布尺寸: {width}x{height}, 点的数量: {len(points)}")

    # 3. 创建画布
    # mode='1' 表示 1位像素 (黑白), color=1 表示默认背景为白色
    # 如果你想要RGB模式，可以使用 mode='RGB', color='white'
    img = Image.new(mode='1', size=(width, height), color=1)

    # 4. 绘制像素
    # 获取像素操作对象
    pixels = img.load()

    for x, y in points:
        # 加上 margin 偏移量，让图像居中
        draw_x = x + margin
        draw_y = y + margin

        # 确保坐标在画布范围内
        if 0 <= draw_x < width and 0 <= draw_y < height:
            pixels[draw_x, draw_y] = 0  # 0 表示黑色
        else:
            print(f"警告: 点 [{x}, {y}] 超出画布范围，已跳过。")

    # 5. 保存图片
    img.save(output_filename)
    print(f"成功保存图片至: {output_filename}")


# ==========================================
# 使用示例
# ==========================================

if __name__ == "__main__":
    json_to_bitmap("horse.json", "horse.png")
