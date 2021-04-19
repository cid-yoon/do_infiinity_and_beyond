package cid.study.querydsl.domain.example;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @BeforeEach
    void before() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member m1 = new Member("member1", 10, teamA);
        Member m2 = new Member("member2", 20, teamA);

        Member m3 = new Member("member3", 30, teamB);
        Member m4 = new Member("member4", 40, teamB);

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);
    }

    @Test
    public void startJPQL() {

        // member 1을 찾아라
        Member findMember =
                em.createQuery("select m from Member m where m.userName = :username", Member.class)
                        .setParameter("username", "member1")
                        .getSingleResult();

        Assertions.assertThat(findMember.getUserName()).isEqualTo("member1");
    }

    @Test
    public void startQueryDsl() {

        // member 1을 찾아라
        JPAQueryFactory factory = new JPAQueryFactory(em);

        QMember m = new QMember("m");
        Member findMember = factory
                .select(m)
                .from(m)
                .where(m.userName.eq("member1"))
                .fetchOne();
        assert findMember != null;
        Assertions.assertThat(findMember.getUserName()).isEqualTo("member1");

    }
}

