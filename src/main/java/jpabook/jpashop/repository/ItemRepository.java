package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long save(Item item){
        // 엔티티 생성과 수정을 한번에 해서 간단하지만, 실무에서는 잘 사용하지 않는 형태.
        // 실무에서는 변경되는 필드만 뷰에 노출시키기 때문에, 모든 필드가 교체되는 병합 방식은 잘 사용 안함.
        if(item.getId() == null){
            em.persist(item);
        } else {
            em.merge(item);
        }
        return item.getId();
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
