package kr.co.private_box.infrastructure.jpa;

import kr.co.private_box.domain.PrivateBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivateBoxJpaRepository extends JpaRepository<PrivateBox,Long> {
    Optional<PrivateBox> findByPrivateUrl(String privateUrl);
}
