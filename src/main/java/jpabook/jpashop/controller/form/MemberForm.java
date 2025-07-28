package jpabook.jpashop.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    @NotEmpty
    private String name;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

    private String city;
    private String street;
    private String zipcode;

    public Member toEntity() {
        Member member = new Member();
        member.setName(name);
        member.setLoginId(loginId);
        member.setPassword(password);
        member.setAddress(new Address(city, street, zipcode));
        return member;
    }
}
