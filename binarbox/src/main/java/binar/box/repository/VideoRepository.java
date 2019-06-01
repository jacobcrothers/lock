package binar.box.repository;

import binar.box.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends BaseJpaRepository<Video, Long> {
    Video findByIdAndUploadedSourceFileTrue(Long videoId);

    List<Video> findByUploadExpirationDateLessThanAndUploadedSourceFileFalse(Date from);

    Optional<Video> findByLockId(Long lockId);
}


