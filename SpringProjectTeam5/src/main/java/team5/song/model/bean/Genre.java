package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "genre_song")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "genreID")
@JsonIgnoreProperties({"songs"})  // 忽略 songs 屬性，避免循環引用
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genreID")
    private Integer genreID;

    @Column(name = "genreName", nullable = false)
    private String genreName;

//    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
//    private Set<Song> songs;
}
