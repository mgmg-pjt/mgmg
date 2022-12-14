package com.b306.mgmgproject.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "interest_music")
public class InterestMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_music_no")
    private Long interestMusicNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_no")
    private Music music;

    @Column(name = "regist_date")
    @Temporal(TemporalType.DATE)
    private Date registDate;

}
