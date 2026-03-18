package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistCreateVo {
    private String playlistName;
    private String playlistDescription;
    private Integer profiledId;
}
