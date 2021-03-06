package com.test.teamlog.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "user_follow",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"from_user_id","to_user_id"}
                )
        }
)
public class UserFollow {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

}
