package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import team5.profile.model.bean.ProfilesBean;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "playlists")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "playlistID")
@JsonIgnoreProperties({"playlistSongs"})  // 忽略 playlistSongs 屬性，避免循環引用
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlistID")
    private Integer playlistID;

    @Column(name = "playlistName", nullable = false)
    private String playlistName;

    @Column(name = "playlistDescription")
    private String playlistDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profileId")
    private ProfilesBean profile;

    @Column(name = "playlistCreatedAt", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date playlistCreatedAt = new Date();

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
    private Set<PlaylistSong> playlistSongs;
}
