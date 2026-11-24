package kr.co.private_box.infrastructure.jpa;

import kr.co.private_box.domain.PrivateBox;
import kr.co.private_box.domain.PrivateBoxRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaPrivateBoxRepository implements PrivateBoxRepository {
    private final PrivateBoxJpaRepository jpaRepository;

    public JpaPrivateBoxRepository(PrivateBoxJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PrivateBox add(PrivateBox privateBox) {
        return jpaRepository.save(privateBox);
    }

    @Override
    public PrivateBox findByPrivateUrl(String privateUrl) {
        return jpaRepository.findByPrivateUrl(privateUrl).orElse(null);
    }

    @Override
    public PrivateBox update(PrivateBox privateBox) {
        return jpaRepository.save(privateBox);
    }

    @Override
    public List<PrivateBox> findAll() {
        return jpaRepository.findAll();
    }
}
