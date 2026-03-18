package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import team5.profile.model.bean.ProfilesBean;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "songClicks")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "songClickID")
public class SongClick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "songClickID")
    private Integer songClickID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "songID", nullable = false)
    private Song song;

    @Column(name = "songClickDate", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date songClickDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId")
    private ProfilesBean profile;

}
