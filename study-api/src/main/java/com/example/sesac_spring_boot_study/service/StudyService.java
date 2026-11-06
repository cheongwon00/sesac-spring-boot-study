package com.example.sesac_spring_boot_study.service;

import com.example.sesac_spring_boot_study.domain.State;
import com.example.sesac_spring_boot_study.domain.StudyMember;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StudyService {
    private Map<Long,StudyMember> members = new LinkedHashMap<>();
    private int capacity = 3;
    private int capacityCnt = 0;
    private Long id = 0l;

    //신청
    public Optional<StudyMember> signMember(String name){
        name = name.trim();
        if(name.equals("")) return Optional.empty();
        if(isContains(name)) return Optional.empty();
        State state = State.HOLD;
        if(capacityCnt != capacity){
            state = State.COMPLETE;
            capacityCnt++;
        }

        StudyMember member = new StudyMember(id,name, state);
        members.put(id,member);
        id++;
        return Optional.of(member);
    }

    //조회
    public String findAll(){
        if(members.isEmpty()) return "참가자가 없습니다";
        StringBuilder sb = new StringBuilder();
        for(Long id :members.keySet()) {
            StudyMember member = members.get(id);
            sb.append(member.toString())
                    .append("\n");
        }
        return sb.toString();
    }


    //취소
    public String cancel(String memberId){
        if(!memberId.matches("\\d+")) return "잘못된 형식입니다";
        Long mId = Long.parseLong(memberId);
        if(!members.containsKey(mId)) return "없는 id입니다";

        StudyMember member = members.get(mId);
        if(member.getState() == State.HOLD){
            members.remove(mId);
            return "취소 성공";
        }
        //확정 참가자 취소
        members.remove(mId);
        if(!holdToCompleted()){
            capacityCnt--;
        }
        return "취소 성공";
    }

    //정원 변경
    public String changeCapacity(String stringCapacity){
        if(stringCapacity.trim().equals("")) return "잘못된 형식입니다";
        if(!stringCapacity.matches("\\d+")) return "잘못된 형식입니다";
        int inputCapacity = Integer.parseInt(stringCapacity);

        if((inputCapacity < 1) || (inputCapacity>10)) return "1~10만 가능합니다";
        if(inputCapacity<capacityCnt){
            StringBuilder sb = new StringBuilder();
            sb
                    .append("실패\n")
                    .append("현재 정원: ")
                    .append(capacity)
                    .append("\n")
                    .append("현재 확정 인원: ")
                    .append(capacityCnt)
                    .append("\n");
            return sb.toString();
        }
        capacity = inputCapacity;
        while(true){
            if(capacityCnt == capacity) break;
            if(holdToCompleted()){
                capacityCnt++;
            }else break;
        }
        return "정원 변경 성공";
    }


    private boolean isContains(String name){
        for(Long id :members.keySet()){
            StudyMember member = members.get(id);
            if(member.getName().equals(name)) return true;
        }
        return false;
    }
    private boolean holdToCompleted(){
        Long findId = -1l;
        for(Long id : members.keySet()){
            StudyMember member = members.get(id);
            if(member.getState() == State.HOLD){
                findId =  member.getId();
                break;
            }
        }
        if(findId != -1){
            StudyMember findMember = members.get(findId);
            findMember.setState(State.COMPLETE);
            return true;
        }
        return false;
    }
}
