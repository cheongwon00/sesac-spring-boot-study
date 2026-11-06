package com.example.sesac_spring_boot_study.controller;

import com.example.sesac_spring_boot_study.domain.StudyMember;
import com.example.sesac_spring_boot_study.service.StudyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class StudyController {

    private final StudyService service;

    public StudyController(StudyService service) {
        this.service = service;
    }

    @PostMapping("/study/participants")
    public String signIn(@RequestParam(name = "name") String name){
        return service.signMember(name).map(StudyMember::toString).orElse("공백 또는 중복 이름입니다");
    }

    @GetMapping("/study/participants")
    public String getParticipants(){
        return service.findAll();
    }

    @PostMapping("/study/cancel")
    public String cancel(@RequestParam(name = "memberId") String memberId){
        return service.cancel(memberId);
    }

    @PostMapping("/study/capacity")
    public String changeCapacity(@RequestParam(name = "capacity") String capacity){
        return service.changeCapacity(capacity);
    }
}
