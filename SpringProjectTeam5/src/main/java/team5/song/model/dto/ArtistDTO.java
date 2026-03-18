package team5.song.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private Integer artistID;
    private String artistName;
    private String countryName;
    private Date birthDate;
    private String artistPhoto;
    private Date createdAt;
}
