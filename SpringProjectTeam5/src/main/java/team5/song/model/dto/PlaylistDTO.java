package team5.song.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    private Integer playlistId;
    private String playlistName;
    private String playlistDescription;
}
