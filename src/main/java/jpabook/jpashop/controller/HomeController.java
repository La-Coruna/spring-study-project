package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model){
        if(memberId == null){
            log.info("아이디 쿠키가 없음");
            return "home";
        }

        Member loginMember = memberService.findOne(memberId);
        if(loginMember == null){
            log.info("쿠키에 저장된 멤버 아이디에 해당하는 멤버가 없음");
            return "home";
        }

        log.info("로그인되어 있음.");
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
