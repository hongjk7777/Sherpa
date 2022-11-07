package com.sherpa.carrier_sherpa.domain.service;

import com.sherpa.carrier_sherpa.domain.entity.Luggage;
import com.sherpa.carrier_sherpa.domain.entity.Member;
import com.sherpa.carrier_sherpa.domain.enums.LuggageStatus;
import com.sherpa.carrier_sherpa.domain.enums.LuggageType;
import com.sherpa.carrier_sherpa.domain.enums.MemberRole;
import com.sherpa.carrier_sherpa.domain.exception.BaseException;
import com.sherpa.carrier_sherpa.domain.exception.ErrorCode;
import com.sherpa.carrier_sherpa.domain.repository.LuggageRepository;
import com.sherpa.carrier_sherpa.domain.repository.MemberRepository;
import com.sherpa.carrier_sherpa.dto.LuggageReqDto;
import com.sherpa.carrier_sherpa.dto.LuggageResDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        List<Luggage> luggages =luggageRepository.findByMember_Id(id);
        // NPE 발생 가능 , 오류 처리 필요할지도?
        // NPE 발생 X -> 아무것도 없는 애들도 그냥 빈 리스트로 출력

        return luggages.stream()
                .map(luggage -> new LuggageResDto(luggage.getId(),luggage.getMember().getId()))
                .collect(Collectors.toList());
    }

    public Luggage create(String memberId,LuggageReqDto luggageReqDto) {
        // Member 확인을 위해 MmeberREpository를 쓴다? 불필요해보이지만...
        Member loginMember = memberRepository.findById(memberId).orElseThrow(
                ()-> new BaseException(
                        ErrorCode.NOT_USER,
                        "해당하는 유저가 존재하지 않습니다."
                )
        );
        Luggage luggage = new Luggage(
                loginMember,
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

        Member loginMember = memberRepository.findById(memberId).orElseThrow(
                ()-> new BaseException(
                        ErrorCode.NOT_USER,
                        "해당하는 유저가 존재하지 않습니다."
                )
        );
        Luggage luggage = luggageRepository.findById(luggageId).orElseThrow(
                ()-> new BaseException(
                        ErrorCode.NOT_LUGGAGE,
                        "해당하는 짐이 존재하지 않습니다."
                )
        );
        // 잘못된 캐리어 id에 대해 여기서 미리 오류가 발생해버려서 출력을 못함. 하고 싶은데

//        validation
//                1. user 존재 여부
//                2. luggage 존재 여부
//                3. user == luggage
        if (!luggage.getMember().getId().equals(memberId)) {
            throw new BaseException(
                    ErrorCode.NOT_LUGGAGE,
                    "캐리어 변경 권한이 존재하지 않습니다."
            );
        }
        luggage.update(
                loginMember,
                luggageReqDto.getStart(),
                luggageReqDto.getDestination(),
                luggageReqDto.getStart_time(),
                luggageReqDto.getEnd_time(),
                luggageReqDto.getLuggage_image_url(),
                luggageReqDto.getLuggageType(),
                luggage.getStatus()
        );
        return luggageRepository.save(luggage);
    }

    @Transactional
    public Luggage delete(String luggageId,String memberId){
        // causw에는 왜 throw하는 문법이 보이질 않지, @ExceptionHandler
        Member loginMember = memberRepository.findById(memberId).orElseThrow(
                ()-> new BaseException(
                        ErrorCode.NOT_USER,
                        "해당하는 유저가 존재하지 않습니다."
                )
        );
        Luggage luggage = luggageRepository.findById(luggageId).orElseThrow(
                ()-> new BaseException(
                ErrorCode.NOT_LUGGAGE,
                "해당하는 짐이 존재하지 않습니다."
        )
        );
        // 이미 삭제되 경우 validation 추가 -> 존재하지 않는 캐리어와 이미 삭제한 캐리어 라는 중복되는 validation
        if (!luggage.getMember().getId().equals(memberId)) {
            throw new BaseException(
                    ErrorCode.NOT_LUGGAGE,
                    "캐리어 변경 권한이 존재하지 않습니다."
            );
        }
        // 완전 삭제는 아니고 일단 구현. 추후 refactoring
        if (luggage.getStatus().equals(LuggageStatus.INACTIVE)) {
            throw new BaseException(
                    ErrorCode.NOT_LUGGAGE,
                    "이미 삭제된 캐리어입니다."
            );
        }
        luggage.setStatus(LuggageStatus.INACTIVE);
        return luggageRepository.save(luggage);
    }

}