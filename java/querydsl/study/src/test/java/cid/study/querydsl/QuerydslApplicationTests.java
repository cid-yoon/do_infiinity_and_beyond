package cid.study.querydsl;

import cid.study.querydsl.domain.Hello;
import cid.study.querydsl.domain.QHello;
import cid.study.querydsl.domain.World;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Commit
class QuerydslApplicationTests {

    //@PersistenceContext
    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QHello qHello = QHello.hello;

        Hello result = queryFactory.selectFrom(qHello)
                .fetchOne();

        assertThat(result).isEqualTo(hello);
        assertThat(result.getId()).isEqualTo(hello.getId());


        World world = new World();
        em.persist(world);

    }

}
