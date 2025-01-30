package konkuk.kuit.durimong.domain.column.service;

import jakarta.transaction.Transactional;
import konkuk.kuit.durimong.domain.column.dto.response.CategoryDetailRes;
import konkuk.kuit.durimong.domain.column.dto.response.CategoryRes;
import konkuk.kuit.durimong.domain.column.dto.response.ColumnRes;
import konkuk.kuit.durimong.domain.column.dto.response.KeywordSearchRes;
import konkuk.kuit.durimong.domain.column.entity.Column;
import konkuk.kuit.durimong.domain.column.entity.ColumnCategory;
import konkuk.kuit.durimong.domain.column.repository.CategoryRepository;
import konkuk.kuit.durimong.domain.column.repository.ColumnRepository;
import konkuk.kuit.durimong.global.exception.CustomException;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
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
                        category.getCategoryId(),
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

    public KeywordSearchRes searchColumnsByKeyword(String keyword) {
        // Column엔터티에서는 제목, 소제목, 내용 중 키워드 포함 찾기
        // 결과가 있으면 포함하는 단어 혹은 문장 최대 25자 이내로 반환 + 그 칼럼의 카테고리 Id 참조하여 카테고리 이름
        // 결과가 없으면... {keyword}에 대한 검색결과가 없다고 반환.

        if(keyword == null || keyword.isEmpty()) {
            throw new CustomException(ErrorCode.KEYWORD_NOT_EXISTS);
        }

        if(keyword.length()>10) {
            throw new CustomException(ErrorCode.KEYWORD_LENGTH_OVER);
        }

        List<Column> resultColumns = columnRepository.searchByKeyword(keyword);

        if(resultColumns.isEmpty()) {
            throw new CustomException(ErrorCode.KEYWORD_RESULT_NOT_EXISTS);
        }

        List<KeywordSearchRes.ColumnDTO> columnList = resultColumns.stream()
                .map(column -> new KeywordSearchRes.ColumnDTO(
                        column.getCategory().getCategoryId(),
                        column.getCategory().getName(),
                        extractPreview(column,keyword)
                ))
                .collect(Collectors.toList());

        return new KeywordSearchRes(columnList);
    }

    public String extractPreview(Column column, String keyword) {
        // 제목 -> 소제목 -> 내용 우선순위
        String[] keywords = {column.getTitle(),column.getSubtitle(), column.getContent()};

        for (String preview: keywords) {
            if (preview != null && preview.contains(keyword)) {
                // 키워드 포함 25자 이내로 자르기
                // 25 - 키워드 길이 -> 키워드가 중간에 있도록
                // 10자 - 키워드 15자 - 키워드 + 키워드 => 25자 - 키워드

                int keywordIndex = preview.indexOf(keyword); // 키워드 발견위치
                // 키워드가 양끝에 있을 가능성 고려하여 Math 함수 사용 (키워드 이전 10글자 ~ 키워드 이후 15글자)
                int start = Math.max(0, keywordIndex - 10);
                int end = Math.min(preview.length(), keywordIndex + keyword.length() + 15);
                return preview.substring(start, end);
            }
        }
        return null;
    }

    public ColumnRes viewColumn(Long categoryId) {
        Column column = columnRepository.findByCategory_CategoryId(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_EXISTS));

        return new ColumnRes(
                column.getCategory().getName(),
                column.getTitle(),
                column.getSubtitle(),
                column.getContent(),
                column.getImage()
        );
    }
}
