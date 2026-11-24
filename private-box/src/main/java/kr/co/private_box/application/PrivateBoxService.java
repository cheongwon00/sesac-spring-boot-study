package kr.co.private_box.application;

import kr.co.private_box.domain.PrivateBox;
import kr.co.private_box.domain.PrivateBoxRepository;
import kr.co.private_box.domain.PrivateBoxStatus;
import kr.co.private_box.domain.PrivateUrlFactory;
import kr.co.private_box.domain.exception.GoneException;
import kr.co.private_box.domain.exception.LackOfPrivateUrlResource;
import kr.co.private_box.domain.exception.NotFoundException;
import kr.co.private_box.domain.exception.UnAuthorizedException;
import kr.co.private_box.presentation.dto.PrivateUrlInformResponse;
import kr.co.private_box.presentation.dto.VerifyUrlPasswordRequest;
import kr.co.private_box.presentation.dto.VerifyUrlPasswordResponse;
import kr.co.private_box.presentation.dto.createPrivateBox.CreatePrivateBoxRequest;
import kr.co.private_box.presentation.dto.createPrivateBox.CreatePrivateBoxResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrivateBoxService {
    private final PrivateBoxRepository privateBoxRepository;
//    private final ValidationService validationService;

    public PrivateBoxService(PrivateBoxRepository privateBoxRepository
//            , ValidationService validationService
    ) {
        this.privateBoxRepository = privateBoxRepository;
//        this.validationService = validationService;
    }

    @Transactional
    public CreatePrivateBoxResponse addPrivateBox(CreatePrivateBoxRequest createPrivateBoxRequest){
        //1. privateUrl 생성 (중복 체크 및 최대 5번)
        String privateUrl = generateUniquePrivateUrl();
        //2. PrivateBox 만들기
        PrivateBox privateBox = new PrivateBox(
                createPrivateBoxRequest.originalUrl(),
                privateUrl,
                createPrivateBoxRequest.urlPassword());
        //3. 검증
//        validationService.checkValid(privateBox);
        //4. 레포지토리에 저장
        return CreatePrivateBoxResponse.from(privateBoxRepository.add(privateBox));
    }

    @Transactional(readOnly = true)
    public List<PrivateUrlInformResponse> viewAllPrivateUrl(){
        return privateBoxRepository.findAll().stream()
                .map(PrivateUrlInformResponse::from)
                .toList();
    }


    @Transactional(readOnly = true)
    public void existsByPrivateUrl(String privateUrl){
        PrivateBox privateBox = privateBoxRepository.findByPrivateUrl(privateUrl);
        confirmValidPrivateUrl(privateBox);
    }

    @Transactional(readOnly = true)
    public VerifyUrlPasswordResponse verifyPrivateUrlPassword(String privateUrl, VerifyUrlPasswordRequest verifyUrlPasswordRequest){
        PrivateBox privateBox = privateBoxRepository.findByPrivateUrl(privateUrl);
        confirmValidPrivateUrl(privateBox);
        if (!privateBox.isEqualsPassword(verifyUrlPasswordRequest.urlPassword()))
            throw new UnAuthorizedException("privateUrl의 비밀번호가 잘못됐습니다.");
        return VerifyUrlPasswordResponse.from(privateBox);
    }

    @Transactional
    public void expirePrivateUrl(String privateUrl, VerifyUrlPasswordRequest verifyUrlPasswordRequest){
        PrivateBox privateBox = privateBoxRepository.findByPrivateUrl(privateUrl);
        confirmValidPrivateUrl(privateBox);
        if (!privateBox.isEqualsPassword(verifyUrlPasswordRequest.urlPassword()))
            throw new UnAuthorizedException("privateUrl의 비밀번호가 잘못됐습니다.");
        privateBox.expirePrivateUrl();
        privateBoxRepository.update(privateBox);
    }

    private void confirmValidPrivateUrl(PrivateBox privateBox) {
        if (null == privateBox)
            throw new NotFoundException("해당 privateUrl이 존재하지 않습니다.");
        if (privateBox.isExpired())
            throw new GoneException("해당 privateUrl은 만료되었습니다.");
    }


    private String generateUniquePrivateUrl(){
        final int MAX_TRY_COUNT = 5;
        int count = 0;
        String privateUrl = "";
        while(count++ < MAX_TRY_COUNT){
            privateUrl = PrivateUrlFactory.generatePrivateUrl();
            PrivateBox find = privateBoxRepository.findByPrivateUrl(privateUrl);
            if(null == find) break;//사용중이지 않은 privateUrl을 찾음
        }
        if (count>MAX_TRY_COUNT)
            throw new LackOfPrivateUrlResource("privateUrl 자원이 부족합니다.");

        return privateUrl;
    }
}
