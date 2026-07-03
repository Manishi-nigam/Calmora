package com.calmora.repository;

import com.calmora.model.SavedShort;
import com.calmora.model.ShortVideo;    
import com.calmora.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedShortRepository extends JpaRepository<SavedShort, Long> {

    List<SavedShort> findByUser(User user);

    Optional<SavedShort> findByUserAndShortVideo(User user, ShortVideo shortVideo);
}
