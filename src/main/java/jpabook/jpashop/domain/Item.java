package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<Category>();

    //==비지니스 로직==//
    public void addStock(int quantity){
        stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restQuantity = stockQuantity - quantity;
        if(restQuantity < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        stockQuantity = restQuantity;
    }

}
