package kr.co.private_box.domain;

import java.util.List;

public interface PrivateBoxRepository {
    PrivateBox add(PrivateBox privateBox);
    PrivateBox findByPrivateUrl(String privateUrl);
    PrivateBox update(PrivateBox privateBox);
    List<PrivateBox> findAll();
}
