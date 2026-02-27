package com.hamdan.agiticket.api.response;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record ApiPaginationDto<D>(List<D> content, int page, int totalPages, boolean hasPrevious, boolean hasNext) {

    public <T> ApiPaginationDto(Page<T> page, Function<T, D> mapper) {
        this(
            page.stream().map(mapper).toList(),
            page.getNumber(),
            page.getTotalPages(),
            page.hasNext(),
            page.hasPrevious()
        );
    }

}
