package com.sherpa.carrier_sherpa.domain.service;

import com.sherpa.carrier_sherpa.domain.entity.Luggage;
import com.sherpa.carrier_sherpa.domain.entity.Member;
import com.sherpa.carrier_sherpa.domain.enums.LuggageType;
import com.sherpa.carrier_sherpa.domain.repository.LuggageRepository;
import com.sherpa.carrier_sherpa.domain.repository.MemberRepository;
import com.sherpa.carrier_sherpa.dto.LuggageReqDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LuggageService {

    private final LuggageRepository luggageRepository;
    private final MemberRepository memberRepository;

    // 운송자의 화면에 나올 짐들의 최대 거리
    public static final int MAX_DISTANT = 1000;


    public LuggageService(LuggageRepository luggageRepository, MemberRepository memberRepository) {
        this.luggageRepository = luggageRepository;
        this.memberRepository = memberRepository;
    }

    public Luggage findByMemberId(Long memberId){
        Luggage luggage = new Luggage();
        return luggageRepository.findByMember_Id(memberId).get();
    }

    public Luggage create(String email,LuggageReqDto luggageReqDto) {
        // 일단 유저 빼고 Luggage 등록하는 법부터 익히자
        // Member 확인을 위해 MmeberREpository를 쓴다? 불필요한데... 일단 하자!

        Member testMember = memberRepository.findByEmail(email);
        if (testMember==null) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        Luggage luggage = new Luggage(
                123L,
                testMember,
                "seoul",
                "kangwon",
                "2022.11.01",
                "2022.11.10",
                null,
                LuggageType.MEDIUM,
                3.1234,
                2.1234
        );
        return this.luggageRepository.save(luggage);
    }

    public Luggage update(Long luggageID, LuggageReqDto luggageReqDto){

        Member testMember = memberRepository.findByEmail("email");
        Luggage updateluggage = new Luggage(
                100L,
                testMember,
                "busan",
                "kangwon",
                "2022.11.01",
                "2022.11.10",
                null,
                LuggageType.SMALL,
                2.1234,
                3.1234
        );

        Optional<Luggage> luggage = luggageRepository.findById(luggageID);
        if (luggage==null){
            throw new IllegalStateException("존재하지 않는 캐리어입니다.");
        }
        luggage.get().update(
                testMember,
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

    public Luggage delete(Long luggageId){
        luggageRepository.deleteById(luggageId);
        return null;
    }

    public List<Luggage> getLuggageListInMaxDistance(double userLat, double userLon) {
        List<Luggage> allLuggage = luggageRepository.findAll();
        List<Luggage> nearLuggageList = new ArrayList<>();

        for (Luggage luggage : allLuggage) {
            double distance = getDistance(userLat, luggage.getLat(), userLon, luggage.getLon());

            if (distance < MAX_DISTANT) {
                nearLuggageList.add(luggage);
            }
        }

        return nearLuggageList;
    }


public double getDistance(double lat1, double lat2, double lon1,
                                 double lon2) {

    final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    distance = Math.pow(distance, 2);

    return Math.sqrt(distance);
}

}