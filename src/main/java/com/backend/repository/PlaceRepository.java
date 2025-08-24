package com.backend.repository;

import com.backend.domain.place.Place;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.pictureUrls ORDER BY p.categoryId")
    List<Place> findAllByOrderByCategoryId();

    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.pictureUrls WHERE p.id = :id")
    Optional<Place> findByIdWithPictures(@Param("id") Long id);
}
