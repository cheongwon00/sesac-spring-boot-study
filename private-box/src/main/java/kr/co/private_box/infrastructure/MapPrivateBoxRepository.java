package kr.co.private_box.infrastructure;

import kr.co.private_box.domain.PrivateBox;
import kr.co.private_box.domain.PrivateBoxRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MapPrivateBoxRepository implements PrivateBoxRepository {
    private Map<String, PrivateBox> privateBoxes = new ConcurrentHashMap<>();
    AtomicLong atomicLong = new AtomicLong(1L);

    public PrivateBox add(PrivateBox privateBox){
        privateBox.setId(atomicLong.getAndAdd(1L));
        privateBoxes.put(privateBox.getPrivateUrl(),privateBox);
        return privateBox;
    }

    public List<PrivateBox> findAll(){
        return new ArrayList<>(privateBoxes.values());
    }

    public PrivateBox findByPrivateUrl(String privateUrl){
        return privateBoxes.get(privateUrl);//없으면 null반환
    }

    public PrivateBox update(PrivateBox privateBox){
        privateBoxes.put(privateBox.getPrivateUrl(),privateBox);
        return privateBox;
    }
}
