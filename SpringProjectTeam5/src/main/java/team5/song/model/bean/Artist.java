package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Artists")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "artistID")
@JsonIgnoreProperties({"songs", "albums"})  // 忽略 songs 和 albums 屬性，避免循環引用
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artistID")
    private Integer artistID;

    @Column(name = "artistName", nullable = false)
    private String artistName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "CountryID")
    private Country country;

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "artistPhoto")
    private String artistPhoto;

    @Column(name = "createdAt", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt = new Date();

//    @ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY)
//    private Set<Song> songs;
//
//    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
//    private Set<Album> albums;
}
