package jpabook.jpashop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jpabook.jpashop.controller.form.LoginForm;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.LoginService;
import jpabook.jpashop.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String LoginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginV2(@ModelAttribute @Valid LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        log.info("login? {}", loginMember);

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // sessionManager에서 세션을 생성히고, response에 쿠키로 세션ID를 설정해줌.
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    /**
     * 로그아웃 V2
     * logoutV2는 쿠키 삭제를 직접 하지 않으므로, JSESSIONID 같은 쿠키는 여전히 존재.
     * 하지만 서버 쪽에서 그 세션을 무효화했기 때문에 다시 로그인해야 하는 상태.
     */
    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
