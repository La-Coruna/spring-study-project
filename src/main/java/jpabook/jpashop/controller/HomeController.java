package jpabook.jpashop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.session.SessionConst;
import jpabook.jpashop.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final SessionManager sessionManager;
    /*
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
    */

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        if(loginMember == null){
            log.info("로그인 X");
            return "home";
        }

        log.info("로그인 O");
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
