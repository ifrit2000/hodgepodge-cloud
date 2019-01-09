package io.github.cd871127.hodgepodge.cloud.lib.util;

import lombok.Data;

import java.util.List;

@Data
public class PageList<T> {
    List<T> data;
    Integer pageNum;
    Long totalCount;
}
