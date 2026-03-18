package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongPaginationQueryParameterVo {
    private Integer pageIndex;       // 分頁索引
    private Integer sizePerPage;     // 每頁顯示數量
    private String songGenre;    // 類型
    private String artistName;   // 表演者名稱
    private String songTitle;    // 歌曲標題
    private String countryName;  //國家名稱
    private String songAlbum;    // 專輯名稱
    private Boolean songIsDeleted; //是否刪除
}
