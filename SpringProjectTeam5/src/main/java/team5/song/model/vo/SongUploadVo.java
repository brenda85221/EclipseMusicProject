package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SongUploadVo {
    private String songTitle;
    private Integer artistID;
    private String songGenreName;
    private String countryName;
    private String songInfo;
    private Date createDate;
    private MultipartFile songFile;
    private MultipartFile photoFile;

}
