package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Albums")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "albumID")
//@JsonIgnoreProperties({"songs"})  // 忽略 songs 屬性，避免循環引用
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumID")
    private Integer albumID;

    @Column(name = "albumName", nullable = false)
    private String albumName;

    @Column(name = "albumReleaseDate")
    private Date albumReleaseDate;

    @Column(name = "albumCoverPath")
    private String albumCoverPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artistID")
    private Artist artist;

    // songs 可以忽略不序列化
//    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY)
//    private Set<Song> songs;
}