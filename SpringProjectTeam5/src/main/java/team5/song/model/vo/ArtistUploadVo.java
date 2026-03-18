package team5.song.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistUploadVo {
    private String artistName;
    private String countryName;
    private Date birthDate;
    private MultipartFile artistPhoto;
}
