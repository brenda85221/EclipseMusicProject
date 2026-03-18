package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumQueryVo {
    private int pageIndex;
    private int pageSize;
    private String albumName;
    private String artistName;
}
