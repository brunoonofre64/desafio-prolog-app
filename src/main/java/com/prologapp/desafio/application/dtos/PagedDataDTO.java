package com.prologapp.desafio.application.dtos;

import lombok.*;

import java.util.List;

/**
 * Classe genérica para encapsular dados paginados.
 * @param <T> O tipo de dados contidos na página.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedDataDTO<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * Método de fábrica para criar uma instância validada de PagedDataDTO.
     */
    public static <T> PagedDataDTO<T> of(
            List<T> content,
            long totalElements,
            int currentPage,
            int pageSize
    ) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean hasNext = currentPage < totalPages - 1;
        boolean hasPrevious = currentPage > 0;

        return PagedDataDTO.<T>builder()
                .content(content)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }
}
