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

    // prepare statement의 parameter binding 방식을 사용
    // 문자가 아닌 type으로 작성
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