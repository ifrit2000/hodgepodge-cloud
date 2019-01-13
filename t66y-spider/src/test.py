import tesserocr

from PIL import Image


for threshold in range(256):
    # image = Image.open("codeimg.php")
    image = Image.open("12")
    image = image.convert("L")
    table = []
    for i in range(0, 256):
        if i < threshold:
            table.append(0)
        else:
            table.append(1)
    image = image.point(table, '1')
    result = tesserocr.image_to_text(image)
    print(result, threshold)
