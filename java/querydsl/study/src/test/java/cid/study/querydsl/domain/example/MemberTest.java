package cid.study.querydsl.domain.example;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static cid.study.querydsl.domain.example.QMember.member;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory factory;

    @BeforeEach
    void before() {

        factory = new JPAQueryFactory(em);

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
        QMember m = new QMember("m");
        Member findMember = factory
                .select(m)
                .from(m)
                .where(m.userName.eq("member1"))
                .fetchOne();
        assert findMember != null;
        Assertions.assertThat(findMember.getUserName()).isEqualTo("member1");

    }

    @Test
    @DisplayName("검색, and 연산 일반")
    public void selectQueryDsl() {

        // member 1을 찾아라
        Member findMember = factory
                .selectFrom(member)
                .where(member.userName.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assert findMember != null;
        Assertions.assertThat(findMember.getUserName()).isEqualTo("member1");


    }

    @Test
    @DisplayName("검색, and 연산 일반 syntaxSugar")
    public void selectQueryDslWithSyntaxSugar() {

        // member 1을 찾아라
        Member findMember = factory
                .selectFrom(member)

                /*
                    public Q where(Predicate... o) {
                    return queryMixin.where(o); }
                 */
                // and를 일급 객체로 사용, boolean expression
                .where(
                        member.userName.eq("member1"),
                        member.age.eq(10))
                .fetchOne();

        assert findMember != null;
        Assertions.assertThat(findMember.getUserName()).isEqualTo("member1");
    }

    @Test
    @DisplayName("결과 조회 - 페이징, limit")
    public void resultFetchResult() {

        // member 1을 찾아라
        QueryResults<Member> findMember = factory
                .selectFrom(member)
                .fetchResults();

        // 호출을 2번 수행, 페이징
        List<Member> members = findMember.getResults();
        long count = findMember.getTotal();


        // 한번만
        Member first = factory.selectFrom(member)
                .fetchFirst();

        // 제한 된 개수
        QueryResults<Member> limits = factory.selectFrom(member)
                .limit(1)
                .fetchResults();


        // count만
        long total = factory.selectFrom(member).fetchCount();
        members = factory.select(member).fetch();

    }


}

