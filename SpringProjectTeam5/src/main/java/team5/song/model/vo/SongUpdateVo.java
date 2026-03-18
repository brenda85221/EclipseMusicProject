package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongUpdateVo {
    private Integer songID;
    private String songTitle;
    private Integer artistID;
    private String songGenreName;
    private String countryName;
    private Integer albumID;
    private String songInfo;
    private MultipartFile photoFile;
    private String photoPath;
}
