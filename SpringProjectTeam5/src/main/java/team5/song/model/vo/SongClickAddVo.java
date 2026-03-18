package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongClickAddVo {
    private Integer songID;
    private Integer profileID;
}
