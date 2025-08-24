package com.backend.dto.response;

import com.backend.domain.place.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "관광지 정보 응답 DTO")
public record PlaceResponse(
        @Schema(description = "내부 ID") Long id,
        @Schema(description = "관광지 명") String name,
        @Schema(description = "설명") String description,
        @Schema(description = "관광지 사진 URL") List<String> pictureUrls,
        @Schema(description = "위도") Double latitude,
        @Schema(description = "경도") Double longitude,
        @Schema(description = "주소") String address,
        @Schema(description = "유아 용품 대여 가능 여부") Boolean canBabyEquipmentRental,
        @Schema(description = "카테고리 ID") String categoryId,
        @Schema(description = "휴무 요일") String closeForTheDay,
        @Schema(description = "신용카드 사용 가능 여부") Boolean canCreditCard,
        @Schema(description = "할인 정보") String discount,
        @Schema(description = "요금") String fee,
        @Schema(description = "수용 가능 인원") String occupancy,
        @Schema(description = "우편번호") Integer postCode,
        @Schema(description = "전화번호") String telNumber,
        @Schema(description = "이용 가능 시간") String timeAvailable,
        @Schema(description = "관광 소요 시간") String travelTime
        ) {
    public static PlaceResponse from(Place place) {
        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getDescription(),
                place.getPictureUrls(),
                place.getLatitude(),
                place.getLongitude(),
                place.getAddress(),
                place.getCanBabyEquipmentRental(),
                place.getCategoryId(),
                place.getCloseForTheDay(),
                place.getCanCreditCard(),
                place.getDiscount(),
                place.getFee(),
                place.getOccupancy(),
                place.getPostCode(),
                place.getTelNumber(),
                place.getTimeAvailable(),
                place.getTravelTime()
        );
    }
}
