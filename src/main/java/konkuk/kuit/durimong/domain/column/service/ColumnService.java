package konkuk.kuit.durimong.domain.column.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.column.dto.response.CategoryDetailRes;
import konkuk.kuit.durimong.domain.column.dto.response.CategoryRes;
import konkuk.kuit.durimong.domain.column.dto.response.ColumnRes;
import konkuk.kuit.durimong.domain.column.entity.Column;
import konkuk.kuit.durimong.domain.column.entity.ColumnCategory;
import konkuk.kuit.durimong.domain.column.repository.CategoryRepository;
import konkuk.kuit.durimong.domain.column.repository.ColumnRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final CategoryRepository categoryRepository;

    public CategoryRes getAllCategories() {
        List<CategoryRes.CategoryDTO> categoryList = categoryRepository.findAll().stream()
                .map(category -> new CategoryRes.CategoryDTO(
                        category.getName(),
                        category.getImage()
                ))
                .collect(Collectors.toList());

        if (categoryList.isEmpty()) {
            throw new CustomException(ErrorCode.COLUMN_CATEGORY_NOT_FOUND);
        }

        return new CategoryRes(categoryList);
    }

    public CategoryDetailRes getCategoryDetail(Long id) {
        ColumnCategory category = categoryRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.COLUMN_CATEGORY_NOT_FOUND));

        return new CategoryDetailRes(category.getDetail());
    }

    public ColumnRes viewColumn(Long categoryId) {
        Column column = columnRepository.findByCategory_CategoryId(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        return new ColumnRes(
                column.getCategory().getName(),
                column.getTitle(),
                column.getSubtitle(),
                column.getContent(),
                column.getImage()
        );
    }
}
