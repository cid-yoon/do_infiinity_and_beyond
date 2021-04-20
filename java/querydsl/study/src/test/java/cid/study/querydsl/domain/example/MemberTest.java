package cid.study.querydsl.domain.example;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static cid.study.querydsl.domain.example.QMember.member;
import static cid.study.querydsl.domain.example.QTeam.team;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(findMember.getUserName()).isEqualTo("member1");
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
        assertThat(findMember.getUserName()).isEqualTo("member1");

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
        assertThat(findMember.getUserName()).isEqualTo("member1");


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
        assertThat(findMember.getUserName()).isEqualTo("member1");
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

    @Test
    @DisplayName("정렬")
    public void sortQueryDsl() {

        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));
        /**
         * 회원 정렬 순서
         * 1. 회원 나이 내림 차순(desc)
         * 2. 회원 이름 올림차순(asc)
         * 단 2에서 회원 이름이 없으면 마지막에 출력(null last)
         */
        List<Member> findMembers = factory
                .selectFrom(member)

                /*
                    public Q where(Predicate... o) {
                    return queryMixin.where(o); }
                 */
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.userName.asc().nullsLast())
                .fetch();

        Member member5 = findMembers.get(0);
        Member member6 = findMembers.get(1);
        Member memberNull = findMembers.get(2);

        assertThat(member5.getUserName()).isEqualTo("member5");
        assertThat(member6.getUserName()).isEqualTo("member6");
        assertThat(memberNull.getUserName()).isNull();
    }

    @Test
    @DisplayName("결과 조회 - 페이징, limit")
    public void resultFetchResultPageAndLimit() {

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

    @Test
    @DisplayName("페이징")
    public void paging1() {

        List<Member> result = factory
                .selectFrom(member)
                .orderBy(member.userName.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("페이징2 - fetchResult")
    public void pagingFetchResult() {

        /**
         * 카운트 쿼리까지 같이 나감
         */
        QueryResults<Member> result = factory
                .selectFrom(member)
                .orderBy(member.userName.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(result.getTotal()).isEqualTo(4);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("집합")
    public void aggregation() {

        /**
         * 튜플로 반환, 여러개의 타입이 있을때 가져옴
         */
        List<Tuple> results = factory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        /**
         * 데이터 타입이 여러개가 들어오기에 Tuple을 사용
         * 실무에서는 DTO로 뽑아오는 걸 많이 사용
         */
        Tuple tuple = results.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    @Test
    @DisplayName("그룹")
    public void group() throws Exception {

        /**
         * 팀의 이름과 각 팀의 평균 연령을 구해라
         */
        // Given
        List<Tuple> result = factory.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15); // (10 + 20) / 2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35); // (30 + 40) / 2

        // When

        // Then


    }


}

