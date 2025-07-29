package jpabook.jpashop.session;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();
    
    @Test
    @DisplayName("세션 테스트")
    public void sessionTest() throws Exception{
        //given
        // 세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        // 요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //when 1
        // 세션 조회
        Object result = sessionManager.getSession(request);

        //then 1
        assertThat(result).isEqualTo(member);

        //when 2
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
    
}