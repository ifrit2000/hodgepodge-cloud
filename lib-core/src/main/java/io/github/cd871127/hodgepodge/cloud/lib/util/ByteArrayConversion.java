package io.github.cd871127.hodgepodge.cloud.lib.util;

public class ByteArrayConversion {
    private ByteArrayConversion() {
    }

    public static String byteArray2HexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b & 0xFF));
        }
        return stringBuilder.toString();
    }

    public static byte[] hexString2ByteArray(String hexString) {
        if (hexString == null || hexString.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            String subStr = hexString.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
}
