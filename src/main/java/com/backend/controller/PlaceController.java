package com.backend.controller;

import com.backend.controller.swagger.PlaceControllerSwagger;
import com.backend.dto.BaseResponse;
import com.backend.dto.response.PlaceResponse;
import com.backend.service.PlaceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PlaceController implements PlaceControllerSwagger {

    private final PlaceService placeService;

    @Override
    @GetMapping("/places")
    public ResponseEntity<BaseResponse<List<PlaceResponse>>> getAllPlaces() {
        List<PlaceResponse> response = placeService.getAllPlaces();

        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Override
    @GetMapping("/places/{id}")
    public ResponseEntity<BaseResponse<PlaceResponse>> getPlaceById(@PathVariable Long id) {
        PlaceResponse response = placeService.getPlaceById(id);

        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}
