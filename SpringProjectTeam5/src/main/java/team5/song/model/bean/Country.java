package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Countries")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "countryID")
@JsonIgnoreProperties({"artists", "songs"})  // 忽略 artists 和 songs 屬性，避免循環引用
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "countryID")
    private Integer countryID;

    @Column(name = "countryName", nullable = false)
    private String countryName;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<Artist> artists;

//    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
//    private Set<Song> songs;
}
