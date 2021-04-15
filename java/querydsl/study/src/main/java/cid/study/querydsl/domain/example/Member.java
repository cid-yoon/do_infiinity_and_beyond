package cid.study.querydsl.domain.example;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "userName", "age"})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String userName;
    private Integer age;

    // 내가 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String userName) {
        this(userName, 0);
    }

    public Member(String userName, Integer age, Team team) {
        this.userName = userName;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }

    }

    public Member(String userName, Integer age) {
        this(userName, age, null);
    }

    /**
     * 팀에 연관된 나도 변경 해 줘야 하니
     *
     * @param team 변경 될 팀 정보
     */
    private void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
