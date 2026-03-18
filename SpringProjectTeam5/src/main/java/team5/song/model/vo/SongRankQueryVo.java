package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查詢歌曲排行榜的參數
 * 頁碼,頁容量,類型,歌手,國家,日期(幾天內),總數量
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongRankQueryVo {
    private String genreName;
    private String artistName;
    private String countryName;
    private Integer day;
    private Integer count;
}
