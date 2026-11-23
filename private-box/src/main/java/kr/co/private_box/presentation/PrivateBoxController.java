package kr.co.private_box.presentation;

import jakarta.validation.Valid;
import kr.co.private_box.application.PrivateBoxService;
import kr.co.private_box.presentation.dto.VerifyUrlPasswordRequest;
import kr.co.private_box.presentation.dto.VerifyUrlPasswordResponse;
import kr.co.private_box.presentation.dto.createPrivateBox.CreatePrivateBoxRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;

@RestController
public class PrivateBoxController {
    private final PrivateBoxService privateBoxService;

    public PrivateBoxController(PrivateBoxService privateBoxService) {
        this.privateBoxService = privateBoxService;
    }

    @PostMapping("/private-box")
    public ResponseEntity<?> createPrivateBox(@Valid @RequestBody CreatePrivateBoxRequest createPrivateBoxRequest){
        return new ResponseEntity<>(
                privateBoxService.addPrivateBox(createPrivateBoxRequest),
                HttpStatus.CREATED);
    }

    @GetMapping("/private-box")
    public ResponseEntity<?> viewPrivateUrl(){
        return new ResponseEntity<>(
                privateBoxService.viewAllPrivateUrl(),
                HttpStatus.OK);
    }

    // 존재하고 ACTIVE 상태인 링크에 비밀번호 입력 화면을 제공한다.
    @GetMapping("/{privateUrl:[23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz]{8}}")
    public ModelAndView tryPrivateUrl(@PathVariable String privateUrl){
        privateBoxService.existsByPrivateUrl(privateUrl);
        return new ModelAndView("forward:/index.html");
    }

    @PostMapping("/private-box/{privateUrl}/verify")
    public ResponseEntity<?> verifyUrlPassword(
            @PathVariable String privateUrl,
            @RequestBody VerifyUrlPasswordRequest verifyUrlPasswordRequest
    ) throws URISyntaxException {
        VerifyUrlPasswordResponse verifyUrlPasswordResponse = privateBoxService.verifyPrivateUrlPassword(privateUrl,verifyUrlPasswordRequest);
        return new ResponseEntity<>(
                verifyUrlPasswordResponse,
                HttpStatus.OK);
    }

    @PatchMapping("/private-box/{privateUrl}/expire")
    public ResponseEntity<?> expirePrivateUrl(
            @PathVariable String privateUrl,
            @RequestBody VerifyUrlPasswordRequest verifyUrlPasswordRequest
    ){
        privateBoxService.expirePrivateUrl(privateUrl,verifyUrlPasswordRequest);
        return new ResponseEntity<>(
                HttpStatus.OK);
    }
}
