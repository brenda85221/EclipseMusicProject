package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "songs")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "songID")
//@JsonIgnoreProperties({"artists","album"})  // 忽略所有關聯屬性，避免循環引用
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "songID")
    private Integer songID;

    @Column(name = "songTitle", nullable = false)
    private String songTitle;

    @Column(name = "songDuration")
    private Integer songDuration;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "genreID")
    private Genre genre;

    @ManyToOne(fetch = FetchType.EAGER,  cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "CountryID")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AlbumID")
    private Album album;

    @ManyToOne(fetch = FetchType.EAGER,  cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "artistID")
    private Artist artist;

    @Column(name = "songPath", nullable = false)
    private String songPath;

    @Column(name = "coverImagePath", nullable = false)
    private String coverImagePath;

    @Column(name = "songInfo")
    private String songInfo;

    @Column(name = "songCreateAt")
    private Date songCreateAt = new Date();

    @Column(name = "songLastUpdate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date songLastUpdate = new Date();

    @Column(name = "songIsDeleted", nullable = false, columnDefinition = "BIT DEFAULT false")
    private Boolean songIsDeleted;
}
