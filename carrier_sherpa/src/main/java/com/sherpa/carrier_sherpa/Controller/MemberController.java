package com.sherpa.carrier_sherpa.Controller;

import com.sherpa.carrier_sherpa.domain.entity.Member;
import com.sherpa.carrier_sherpa.domain.service.MemberService;
import com.sherpa.carrier_sherpa.dto.MemberCreateReqDto;
import com.sherpa.carrier_sherpa.dto.MemberFormDto;
import com.sherpa.carrier_sherpa.dto.MemberResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @ResponseBody
    @PostMapping("/signup")
    public MemberResDto signUp(
            HttpServletRequest httpServletRequest,
            @RequestBody MemberCreateReqDto memberCreateReqDto
    ){
        return memberService.signUp(memberCreateReqDto);
    }

    @ResponseBody
    @PostMapping("/signin")
    public MemberResDto signIn(
            HttpServletRequest httpServletRequest,
            @RequestBody MemberFormDto memberFormDto
    ){
        HttpSession session = httpServletRequest.getSession();
        MemberResDto loginMember = memberService.signIn(memberFormDto);
        session.setAttribute("loginMember",memberService.signIn(memberFormDto));
        return loginMember;
    }

    @ResponseBody
    @GetMapping("/test")
    public MemberResDto test(
            HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        MemberFormDto memberFormDto = (MemberFormDto) session.getAttribute("loginMember");
        return memberService.findByEmail(memberFormDto.getEmail());
    }
}
