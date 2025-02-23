package konkuk.kuit.durimong.domain.column.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.kuit.durimong.domain.column.dto.response.CategoryRes;
import konkuk.kuit.durimong.domain.column.dto.response.ColumnRes;
import konkuk.kuit.durimong.domain.column.dto.response.KeywordSearchRes;
import konkuk.kuit.durimong.domain.column.service.ColumnService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("columns")
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "칼럼 카테고리 화면을 조회합니다.")
    @Tag(name = "Column", description = "칼럼 관련 API")
    @CustomExceptionDescription(COLUMN_CATEGORY)
    public SuccessResponse<CategoryRes> getCategoryPage() {

        return SuccessResponse.ok(columnService.getAllCategories());
    }

    @GetMapping("/search")
    @Operation(summary = "칼럼 키워드 검색 결과", description = "키워드로 칼럼을 검색합니다.")
    @Tag(name = "Column", description = "칼럼 관련 API")
    @CustomExceptionDescription(COLUMN_SEARCH)
    public SuccessResponse<KeywordSearchRes> getKeywordSearchResult(@RequestParam @Parameter(description = "검색 키워드", example = "스트레스") String keyword) {

        return SuccessResponse.ok(columnService.searchColumnsByKeyword(keyword));
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "칼럼 조회", description = "각 칼럼을 조회합니다.")
    @Tag(name = "Column", description = "칼럼 관련 API")
    @CustomExceptionDescription(COLUMN_VIEW)
    public SuccessResponse<ColumnRes> getCategory(@PathVariable Long categoryId) {

        return SuccessResponse.ok(columnService.viewColumn(categoryId));
    }

}
