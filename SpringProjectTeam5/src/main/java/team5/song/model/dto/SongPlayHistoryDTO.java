package team5.song.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongPlayHistoryDTO {
    private Integer songID;
    private String songTitle;
    private Integer songDuration;
    private String genreName;       // 用 String 或簡單的 GenreDTO 代替
    private String countryName;     // 用 String 或簡單的 CountryDTO 代替
//    private String albumName;       // 用 String 或簡單的 AlbumDTO 代替
    private String artistName;      // 用簡單的 Artist 名字列表代替 Set<Artist>
    private String songPath;
    private String coverImagePath;
    private String songInfo;
    private Date songCreateAt;
    private Date songClickDate;
}
