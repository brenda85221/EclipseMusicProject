package team5.song.model.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "playlistSongs")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "playlistSongID")
public class PlaylistSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlistSongID")
    private Integer playlistSongID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlistID")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "songID")
    private Song song;

    @Column(name = "songPosition")
    private Integer songPosition;
}
