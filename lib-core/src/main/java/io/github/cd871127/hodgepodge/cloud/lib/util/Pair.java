package io.github.cd871127.hodgepodge.cloud.lib.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class Pair<K, V> {
    private K left;
    private V right;

    public Pair() {
    }

    public Pair(K left, V right) {
        this();
        setLeft(left);
        setRight(right);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
