package com.sherpa.carrier_sherpa.domain.service;

import com.sherpa.carrier_sherpa.domain.entity.Luggage;
import com.sherpa.carrier_sherpa.domain.entity.Member;
import com.sherpa.carrier_sherpa.domain.enums.LuggageType;
import com.sherpa.carrier_sherpa.domain.enums.MemberRole;
import com.sherpa.carrier_sherpa.domain.repository.LuggageRepository;
import com.sherpa.carrier_sherpa.domain.repository.MemberRepository;
import com.sherpa.carrier_sherpa.dto.LuggageReqDto;
import com.sherpa.carrier_sherpa.dto.LuggageResDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LuggageService {

    private final LuggageRepository luggageRepository;
    private final MemberRepository memberRepository;


    public LuggageService(LuggageRepository luggageRepository, MemberRepository memberRepository) {
        this.luggageRepository = luggageRepository;
        this.memberRepository = memberRepository;
    }

    public List<LuggageResDto> findByMemberId(String id){
        System.out.println("id = " + id);
        List<Luggage> luggages =luggageRepository.findByMember_Id(id);
        // NPE 발생 가능 , 오류 처리 필요할지도?
        // NPE 발생 X -> 아무것도 없는 애들도 그냥 빈 리스트로 출력

        return luggages.stream()
                .map(luggage -> new LuggageResDto(luggage.getId(),luggage.getMember().getId()))
                .collect(Collectors.toList());
    }

    public Luggage create(String email,LuggageReqDto luggageReqDto) {
        // 일단 유저 빼고 Luggage 등록하는 법부터 익히자
        // Member 확인을 위해 MmeberREpository를 쓴다? 불필요한데... 일단 하자!

        Optional<Member> testMember = memberRepository.findByEmail(email);
        if (testMember==null) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        Luggage luggage = new Luggage(
                testMember.get(),
                "seoul",
                "kangwon",
                "2022.11.01",
                "2022.11.10",
                null,
                LuggageType.MEDIUM

        );
        return this.luggageRepository.save(luggage);
    }

    public Luggage update(String luggageID, LuggageReqDto luggageReqDto){

        Optional<Member> testMember = memberRepository.findByEmail("email");
        Luggage updateluggage = new Luggage(
                testMember.get(),
                "busan",
                "kangwon",
                "2022.11.01",
                "2022.11.10",
                null,
                LuggageType.SMALL
        );

        Optional<Luggage> luggage = luggageRepository.findById(luggageID);
        if (luggage==null){
            throw new IllegalStateException("존재하지 않는 캐리어입니다.");
        }
        luggage.get().update(
                testMember.get(),
                "busan",
                "kangwon",
                "2022.11.01",
                "2022.11.10",
                null,
                LuggageType.SMALL
        );
        luggageRepository.save(luggage.get());
        return null;
    }

    public Luggage delete(String luggageId){
        luggageRepository.deleteById(luggageId);
        return null;
    }

}