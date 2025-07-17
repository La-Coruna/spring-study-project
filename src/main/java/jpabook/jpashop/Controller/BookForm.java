package jpabook.jpashop.Controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    private Long id; // 아이디가 있어야 수정이 가능.

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
