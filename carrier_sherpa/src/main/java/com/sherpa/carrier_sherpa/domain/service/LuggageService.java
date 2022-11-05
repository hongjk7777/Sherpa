package com.sherpa.carrier_sherpa.domain.service;

import com.sherpa.carrier_sherpa.domain.entity.Luggage;
import com.sherpa.carrier_sherpa.domain.entity.Member;
import com.sherpa.carrier_sherpa.domain.enums.LuggageStatus;
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

    public Luggage create(String id,LuggageReqDto luggageReqDto) {
        // Member 확인을 위해 MmeberREpository를 쓴다? 불필요해보이지만...
        Optional<Member> loginMember = memberRepository.findById(id);
        if (loginMember==null) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        Luggage luggage = new Luggage(
                loginMember.get(),
                luggageReqDto.getStart(),
                luggageReqDto.getDestination(),
                luggageReqDto.getStart_time(),
                luggageReqDto.getEnd_time(),
                luggageReqDto.getLuggage_image_url(),
                luggageReqDto.getLuggageType(),
                LuggageStatus.REGISTER
        );
        return this.luggageRepository.save(luggage);
    }

    public Luggage update(String luggageId, String memberId, LuggageReqDto luggageReqDto){

        Optional<Member> loginMember = memberRepository.findById(memberId);
        Optional<Luggage> luggage = luggageRepository.findById(luggageId);
        // 잘못된 캐리어 id에 대해 여기서 미리 오류가 발생해버려서 출력을 못함. 하고 싶은데

//        validation
//                1. user 존재 여부
//                2. luggage 존재 여부
//                3. user == luggage

        if (loginMember == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        if (luggage==null){
            throw new IllegalStateException("존재하지 않는 캐리어입니다.");
        }
        if (!luggage.get().getMember().getId().equals(memberId)) {
            System.out.println("luggage.get().getMember().getId() : "+luggage.get().getMember().getId());
            System.out.println("memberId = " + memberId);
            throw new IllegalStateException("캐리어 변경 권한이 없습니다.");
        }
        luggage.get().update(
                loginMember.get(),
                luggageReqDto.getStart(),
                luggageReqDto.getDestination(),
                luggageReqDto.getStart_time(),
                luggageReqDto.getEnd_time(),
                luggageReqDto.getLuggage_image_url(),
                luggageReqDto.getLuggageType(),
                luggage.get().getStatus()
        );
        return luggageRepository.save(luggage.get());
    }

    public Luggage delete(String luggageId,String memberId){
        Optional<Member> loginMember = memberRepository.findById(memberId);
        Optional<Luggage> luggage = luggageRepository.findById(luggageId);
        // 이미 삭제되 경우 validation 추가 -> 존재하지 않는 캐리어와 이미 삭제한 캐리어 라는 중복되는 validation
        if (loginMember == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        if (luggage==null){
            throw new IllegalStateException("존재하지 않는 캐리어입니다.");
        }
        if (!luggage.get().getMember().getId().equals(memberId)) {
            throw new IllegalStateException("캐리어 변경 권한이 없습니다.");
        }
        if (luggage.get().getStatus().equals(LuggageStatus.INACTIVE)) {
            throw new IllegalStateException("이미 삭제한 캐리어입니다.");
        }
        luggage.get().setStatus(LuggageStatus.INACTIVE);
        return luggageRepository.save(luggage.get());
    }

}