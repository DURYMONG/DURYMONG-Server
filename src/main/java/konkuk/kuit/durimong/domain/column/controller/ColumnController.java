package konkuk.kuit.durimong.domain.column.controller;

import io.swagger.v3.oas.annotations.Operation;
import konkuk.kuit.durimong.domain.column.dto.response.CategoryRes;
import konkuk.kuit.durimong.domain.column.service.ColumnService;
import konkuk.kuit.durimong.global.annotation.CustomExceptionDescription;
import konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription;
import konkuk.kuit.durimong.global.exception.ErrorCode;
import konkuk.kuit.durimong.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static konkuk.kuit.durimong.global.config.swagger.SwaggerResponseDescription.COLUMN_CATEGORY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("columns")
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "칼럼 카테고리 화면을 조회합니다.")
    @CustomExceptionDescription(COLUMN_CATEGORY)
    public SuccessResponse<CategoryRes> getCategoryPage() {

        return SuccessResponse.ok(columnService.getAllCategories());
    }

}
