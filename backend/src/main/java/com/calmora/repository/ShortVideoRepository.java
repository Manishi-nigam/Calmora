package com.calmora.repository;

import com.calmora.model.ShortVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortVideoRepository extends JpaRepository<ShortVideo,Long> {
}
