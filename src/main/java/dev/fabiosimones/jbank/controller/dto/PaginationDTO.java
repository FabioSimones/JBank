package dev.fabiosimones.jbank.controller.dto;

public record PaginationDTO(Integer page,
                            Integer pageSize,
                            Long totalElements,
                            Integer totalPages) {
}
