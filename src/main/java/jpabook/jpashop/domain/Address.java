package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter // 값 타입은 Setter를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스로 만듦.
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // 임베디드 타입(@Embeddable)은 자바 기본 생성자를 public 또는 protected로 설정해야 한다. public 보다는 protected가 그나마 더 안전.
    protected Address(){
    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
