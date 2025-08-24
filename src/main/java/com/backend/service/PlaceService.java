package com.backend.service;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import com.backend.domain.place.Place;
import com.backend.dto.response.PlaceResponse;
import com.backend.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<PlaceResponse> getAllPlaces() {
        List<Place> placeList = placeRepository.findAllByOrderByCategoryId();

        return placeList.stream()
                .map(PlaceResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(Long id) {
        Place place = placeRepository.findByIdWithPictures(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        return PlaceResponse.from(place);
    }
}
