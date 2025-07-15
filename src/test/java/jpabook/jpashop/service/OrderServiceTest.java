package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private EntityManager em;
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();
        int initialStockQuantity = 10;
        Book book = createBook("test-book", 10000, initialStockQuantity);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertThat(order.getStatus()).withFailMessage("상품 주문시 주문상태는 ORDER").isEqualTo(OrderStatus.ORDER);
        assertThat(order.getOrderItems().size()).withFailMessage("주문한 상품 종류 수가 정확").isEqualTo(1);
        assertThat(book.getStockQuantity()).withFailMessage("주문한 수만큼 상품 재고 차감").isEqualTo(initialStockQuantity - orderCount);
        assertThat(order.getTotalPrice()).withFailMessage("총 가격 = 책 가격 * 주문 개수").isEqualTo(book.getPrice() * orderCount);
        assertThat(order.getDelivery().getAddress()).withFailMessage("배송지 정보 잘 기입됐는지 확인").isEqualTo(member.getAddress());
    }
    
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        int initialStockQuantity = 1;
        Book book = createBook("test-book", 10000, initialStockQuantity);
        int orderCount = 2;

        //when
        NotEnoughStockException e = assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });

        //then
        assertThat(e.getMessage()).isEqualTo("need more stock");
    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        int initialStockQuantity = 10;
        Book book = createBook("test-book", 10000, initialStockQuantity);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(initialStockQuantity);
    }
    
    @Test
    public void 주문검색() throws Exception{
        //given
        Member member = createMember();
        int initialStockQuantity = 10;
        Book book = createBook("test-book", 10000, initialStockQuantity);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName(member.getName());
        Order order = orderService.findOrders(orderSearch).getFirst();

        //then
        assertThat(order.getStatus()).withFailMessage("상품 주문시 주문상태는 ORDER").isEqualTo(OrderStatus.ORDER);
        assertThat(order.getOrderItems().size()).withFailMessage("주문한 상품 종류 수가 정확").isEqualTo(1);
        assertThat(book.getStockQuantity()).withFailMessage("주문한 수만큼 상품 재고 차감").isEqualTo(initialStockQuantity - orderCount);
        assertThat(order.getTotalPrice()).withFailMessage("총 가격 = 책 가격 * 주문 개수").isEqualTo(book.getPrice() * orderCount);
        assertThat(order.getDelivery().getAddress()).withFailMessage("배송지 정보 잘 기입됐는지 확인").isEqualTo(member.getAddress());
    }
    
    private Member createMember(){
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강남","12345"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int quantity){
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

}