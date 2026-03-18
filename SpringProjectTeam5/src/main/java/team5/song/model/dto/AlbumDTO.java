package team5.song.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private Integer albumID;
    private String albumName;
    private Date albumReleaseDate;
    private String albumCoverPath;
    private String artistName;
    private String artistPhoto;
}
