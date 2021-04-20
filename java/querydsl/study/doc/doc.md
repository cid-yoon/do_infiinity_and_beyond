# QueryDSL

### 기본 QType

Instance를 사용하는 법 2가지

```java
// 직접 만들어서 쓰기

QMember member=new QMeneber("member");

// 미리 만들어진 객체 사용
        QMember.member member=Qmember.member;

// static Import 하면
import xxxxx.QMember;

// 이대로 사용
member;
```

QueryDsl은 결국 JPQL을 만들어주는 빌더, 어떻게 만들어지는지 보고 싶다면 use_comment

```yaml
jpa:
  hibernate:
    ddl-auto: create
  properties:
    hibernate:
      format_sql: true
      # show_sql: true
      use_sql_comments: true  //주석으로 표시
```

### JPQL vs QueryDsl

```java
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

```

### 장점

`prepare statement의 parameter binding` 방식을 사용 문자가 아닌 type으로 작성

`정적 타입처럼 사용` 할 수 있기에 실수를 방지

JPAQueryFactory는 필드 레벨로 가져 가서 사용 가능

* EM 자체가 MultiThread에 영향 받지 않게 되어 있음

## 복수 개 가져오기

```java

public class TEST {
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

```

> fetchResult 같은 경우에 페이징 쿼리가 복잡해지면 데이터(컨텐츠)를 가져오는 쿼리랑 totalCount를 가져오는 쿼리가 다를때가 있음(성능 최적화를 위해)
> 이렇게 성능이 중요하고 복잡해 지는 경우에는 fetchResult 대신 컨텐츠, 카운트 호출을 두번 하는게 더 좋음

### 페이징

```java

// offset 1, limit 2
// member 1 ~4 중에 3,2가 뽑힘
List<Member> result=factory
        .selectFrom(member)
        .orderBy(member.userName.desc())
        .offset(1)
        .limit(2)
        .fetch();

```

### 집합

```java
        /**
 * 튜플로 반환, 여러개의 타입이 있을때 가져옴
 * select {alias 집계함수}
 */
        List<Tuple> results=factory
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
```

### 그룹

```java

@Test
@DisplayName("그룹")
public class TEST {
    public void group() throws Exception {

        /**
         * 팀의 이름과 각 팀의 평균 연령을 구해라
         * join 후 grouping
         * member.team, team == member.team.id == team.id
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
```