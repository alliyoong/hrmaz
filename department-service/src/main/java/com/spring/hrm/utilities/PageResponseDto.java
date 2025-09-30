package com.spring.hrm.utilities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class PageResponseDto<T> {
    private List<T> content;
    private int number;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PageResponseDto() {
    }

    public PageResponseDto(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages,
            boolean last) {
        this.content = content;
        this.number = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}
