package com.github.cd871127.hodgepodge.cloud.lib.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this();
        setKey(key);
        setValue(value);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        return new EqualsBuilder()
                .append(value, pair.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .toHashCode();
    }
}
