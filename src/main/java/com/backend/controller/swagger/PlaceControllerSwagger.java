package com.backend.controller.swagger;

import com.backend.common.config.SwaggerError404;
import com.backend.common.config.SwaggerError500;
import com.backend.dto.BaseResponse;
import com.backend.dto.response.PlaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "관광지 API")
public interface PlaceControllerSwagger {

    @Operation(
            summary = "관광지 목록 조회",
            description = "관광지 목록을 조회한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공 시 관광지 목록을 카테고리 순으로 조회한다.",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            }
    )
    @SwaggerError500
    ResponseEntity<BaseResponse<List<PlaceResponse>>> getAllPlaces();

    @Operation(
            summary = "관광지 목록 조회",
            description = "관광지 정보를 조회한다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공 시 관광지 정보를 조회한다.",
                            content = @Content(schema = @Schema(implementation = PlaceResponse.class))
                    )
            }
    )
    @SwaggerError404(description = "존재하지 않는 관광지")
    @SwaggerError500
    ResponseEntity<BaseResponse<PlaceResponse>> getPlaceById(@PathVariable Long id);
}
