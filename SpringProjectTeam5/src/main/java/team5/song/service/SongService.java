package team5.song.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import team5.song.dao.SongDao;
import team5.song.model.bean.*;
import team5.song.model.dto.*;
import team5.song.model.vo.*;
import team5.song.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Transactional
public class SongService {
    @Autowired
    private SongDao songDao;

    @Value("${photo.storage.prefix}")
    private String imgPath;
    @Value("${video.storage.prefix}")
    private String videoUploadPath;

    public Map getGenre(int pageIndex, int pageSize, String keyword){
        int totalDataNum = songDao.getGenreCount(keyword);
        List<Genre> pageData = songDao.getGenre(pageIndex, pageSize, keyword);
        int totalPageNum = totalDataNum % pageSize == 0 ? totalDataNum / pageSize : totalDataNum / pageSize + 1;
        Map map = new HashMap();

        map.put("totalDataNum", totalDataNum);
        map.put("totalPageNum", totalPageNum);
        map.put("pageData", pageData);
        map.put("pageIndex",pageIndex);
        map.put("pageSize",pageSize);
        return map;
    }

    public Genre getGenreById(int id){
        return songDao.getGenreByID(id);
    }

    public Song getSongById(int id){
        return songDao.getSongById(id);
    }

    public Map<String, Object> getAlbum(AlbumQueryVo albumQueryVo) {
        int pageIndex = albumQueryVo.getPageIndex();
        int pageSize = albumQueryVo.getPageSize();
        int totalDataNum = songDao.getAlbumCount(albumQueryVo);
        int totalPageNum = (totalDataNum + pageSize - 1) / pageSize;
        List<AlbumDTO> album = songDao.getAlbum(albumQueryVo);

        Map<String, Object> map = new HashMap<>();
        map.put("totalDataNum", totalDataNum);
        map.put("totalPageNum", totalPageNum);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        map.put("pageData", album);

        return map;
    }


    public Album getAlbumById(int id){
        return songDao.getAlbumByID(id);
    }

    public List<SongDTO> getAlbumSongByID(int id){
        return songDao.getAlbumSongByID(id);
    }
    public boolean insertAlbumSong(int albumID,List<Integer> songIDs){
        return songDao.insertAlbumSong(albumID,songIDs);
    }

    public List getAlbumSongByName(String name){
        return songDao.getAlbumSongByName(name);
    }

    public boolean insertAlbum(String albumName, Date albumReleaseDate,MultipartFile albumPhoto,int artistID){
        try{
            String filename = UUID.randomUUID() + ".jpg";
            // 設定albums的封面路徑
            Path albumCoverPath = Paths.get(imgPath + "song/albumImg/"+filename);
            // 如果資料夾不存在則建立
            if (!Files.exists(albumCoverPath.getParent())){
                Files.createDirectories(albumCoverPath);
            }
            // 將檔案放到指定位置
            albumPhoto.transferTo(albumCoverPath.toFile());
            // 建立要儲存到資料庫的路徑
            String databasePath = "img/"+albumCoverPath.toString().replace("\\","/").replace(imgPath,"");

            return songDao.insertAlbum(albumName, albumReleaseDate, databasePath, artistID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAlbum(Integer albumID, String albumName,Date albumReleaseDate,MultipartFile albumPhoto,Integer artistID){
        try{
            String databasePath = "";
            if (albumPhoto != null && !albumPhoto.isEmpty()){
                Path photoPath = Paths.get(imgPath + "song/albumImg/" + UUID.randomUUID() + ".jpg");
                if (!Files.exists(photoPath.getParent())){
                    Files.createDirectories(photoPath.getParent());
                }
                // 將圖片保存到指定路徑
                albumPhoto.transferTo(photoPath);
                databasePath = photoPath.toString().replace("\\", "/").replace(imgPath, "img/");
            }
            return songDao.updateAlbum(albumID, albumName, albumReleaseDate, databasePath, artistID);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean deleteAlbum(Integer albumID){
        return songDao.deleteAlbum(albumID);
    }

    public boolean deleteAlbumSong(Integer albumID, List<Integer> songIDs){
        return songDao.deleteAlbumSong(albumID, songIDs);
    }

    public boolean publishSong(List<Integer> songIDs){
        return songDao.publishSong(songIDs);
    }

    public boolean unpublishSong(List<Integer> songIDs){
        return songDao.unpublishSong(songIDs);
    }

    public boolean insertArtist(ArtistUploadVo artistUploadVo){
        try{
            Artist artist = new Artist();
            artist.setArtistName(artistUploadVo.getArtistName());

            Country country = new Country();;
            country.setCountryName(artistUploadVo.getCountryName());
            artist.setCountry(country);

            System.out.println("birthDate:"+artistUploadVo.getBirthDate());

            artist.setBirthDate(artistUploadVo.getBirthDate());

            // 檔案名稱
            String originalFilename = artistUploadVo.getArtistPhoto().getOriginalFilename();
            // 檔案真實路徑
            String photoPath = imgPath + "song/artistImg/" + UUID.randomUUID() + originalFilename;
            Path photoFilePath = Paths.get(photoPath);
            if (!Files.exists(photoFilePath.getParent())){
                Files.createDirectories(photoFilePath.getParent());
            }
            // 將圖片保存到指定路徑
            Files.copy(artistUploadVo.getArtistPhoto().getInputStream(), photoFilePath);
            String databasePath = photoFilePath.toString().replace("\\", "/").replace(imgPath, "img/");
            artist.setArtistPhoto(databasePath);

            songDao.insertArtist(artist);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Map<String, Object> getCountry(int pageIndex, int pageSize, String keyword) {
        List<Country> country = songDao.getCountry(pageIndex, pageSize, keyword);
        int countryCount = songDao.getCountryCount(keyword);
        int totalPageNum = (countryCount + pageSize - 1) / pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("totalDataNum", countryCount);
        map.put("totalPageNum", totalPageNum);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        map.put("pageData", country);

        return map;
    }


    public Map getArtist(int pageIndex, int pageSize, String keyword, String countryName, String sort){
        List<ArtistDTO> artist = songDao.getArtist(pageIndex, pageSize, keyword, countryName,  sort);
        int artistCount = songDao.getArtistCount(keyword, countryName);
        int totalPageNum = (artistCount + pageSize - 1) / pageSize;

        //將資料放入map
        Map<String, Object> map = new HashMap<>();
        map.put("totalDataNum", artistCount);
        map.put("totalPageNum", totalPageNum);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        map.put("pageData", artist);

        return map;
    }

    public ArtistDTO getArtistByID(Integer artistID){
        return songDao.getArtistByID(artistID);
    }

    public boolean insertCountry(Country country){
        return songDao.insertCountry(country);
    }



    public boolean insertSong(Song song){
        return songDao.insertSong(song) > 0;
    }

    public Map getSongByPagination(SongPaginationQueryParameterVo vo){
        long totalDataNum = songDao.getSongCountByConditions(vo);  //總數
        List<SongDTO> songByPagination = songDao.getSongByPagination(vo); //資料
        int sizePerPage = vo.getSizePerPage();
        int pageIndex = vo.getPageIndex();
        long totalPageNum = totalDataNum % sizePerPage == 0 ? totalDataNum / sizePerPage : totalDataNum / sizePerPage + 1;
        Map map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", sizePerPage);
        map.put("totalDataNum", totalDataNum);
        map.put("pageData", songByPagination);
        map.put("totalPageNum", totalPageNum);
        return map;
    }

    public boolean uploadSong(SongUploadVo songUploadVo) throws Exception {
        MultipartFile songFile = songUploadVo.getSongFile();
        if (songUploadVo.getSongTitle() == null || songUploadVo.getSongTitle().isEmpty()){
            songUploadVo.setSongTitle(songFile.getName());
        }
        if (songUploadVo.getSongInfo() == null || songUploadVo.getSongInfo().isEmpty()){
            songUploadVo.setSongInfo("");
        }
//        設置其他參數
        Country country = new Country();
        country.setCountryName(songUploadVo.getCountryName());
        Artist artist = new Artist();
        artist.setArtistID(songUploadVo.getArtistID());
        Genre genre = new Genre();
        genre.setGenreName(songUploadVo.getSongGenreName());
//        設置song參數
        Song song = new Song();
        song.setSongTitle(songUploadVo.getSongTitle());
        song.setSongInfo(songUploadVo.getSongInfo());
        song.setCountry(country);
        song.setArtist(artist);
        song.setGenre(genre);
        song.setSongCreateAt(songUploadVo.getCreateDate());
        song.setCoverImagePath("");
        song.setSongPath("");
        song.setSongIsDeleted(false);

        int songID = songDao.insertSong(song);
        System.out.println("songID:"+songID);
        if (songID == -1){
            throw new Exception("insert song error");
        }

//      影片、圖片轉換
        String videoInputPath = videoUploadPath+"song/mp4/"+songID;
        String videoOutputPath = videoUploadPath+"song/hls/"+songID;
        String photoPath = imgPath+"song/songImg/";
        String photoOriginalFilename = songUploadVo.getPhotoFile().getOriginalFilename();

        System.out.println("videoInputPath:"+videoInputPath);
        System.out.println("videoOutputPath:"+videoOutputPath);
        System.out.println("photoPath:"+photoPath);

//        創建路徑
        File videoInputfile = new File(videoInputPath+"/"+songFile.getOriginalFilename());
        File videoOutputfile = new File(videoOutputPath);
        File photofile = new File(photoPath+"/" + UUID.randomUUID() + photoOriginalFilename);

        if (!videoInputfile.getParentFile().exists()){
            videoInputfile.mkdirs();
        }
        if (!videoOutputfile.getAbsoluteFile().exists()){
            videoOutputfile.mkdirs();
        }
        if (!photofile.getParentFile().exists()){
            photofile.mkdirs();
        }

       try{
           songUploadVo.getSongFile().transferTo(videoInputfile);
           songUploadVo.getPhotoFile().transferTo(photofile);
           boolean ffmpeg = FileUtil.toFfmpeg(videoInputfile.toString(), videoOutputPath);
           Long songDuration = FileUtil.getVideoDuration(videoInputfile.toString());
           //上傳到google cloud storage
//           String songPath = GCSUploader.uploadFileToGCS("ispanproject", videoOutputPath, songID + "");
           // 影片的路徑(上傳給資料庫儲存的路徑)
           String songDatabasePath = videoOutputPath.replaceFirst(videoUploadPath, "");
           song.setSongPath(songDatabasePath+'/');
           System.out.println("photofile:"+photofile);
           String databasePath = photofile.toString().replace("\\", "/").replace(imgPath, "img/");
           System.out.println("databasePath:"+databasePath);
           song.setCoverImagePath(databasePath);
           song.setSongID(songID);
           song.setSongDuration(songDuration.intValue());
           return songDao.updateSong(song);

       }catch (Exception e){
           e.printStackTrace();
       }
//        System.out.println("videoPath:"+videoPath.getAbsolutePath());
//        songUploadVo.getSongFile().transferTo(new File(videoUploadPath));

        return true;
    }
    public boolean insertSongClick(int songID, int profileID){
        return songDao.insertSongClick(songID,profileID);
    }

    public boolean createPlaylist(String playlistName,String playlistDescription,int profileID){
        return songDao.createPlaylist(playlistName, playlistDescription, profileID);
    }

    public boolean deletePlaylist(List<Integer> playlistIDs){
        return songDao.deletePlaylist(playlistIDs);
    }
    public boolean updatePlaylist(Integer playlistID, String playlistName, String playlistDescription){
        return songDao.updatePlaylist(playlistID, playlistName, playlistDescription);
    }

    public boolean deleteArtist(List<Integer> artistIDs){
        return songDao.deleteArtist(artistIDs);
    }
    public boolean insertPlaylistSong(InsertPlaylistSongVo insertPlaylistSongVo){
        int playlistId = insertPlaylistSongVo.getPlaylistId();
        int songId = insertPlaylistSongVo.getSongId();
        return songDao.insertPlaylistSong(playlistId, songId);
    }
    public Map getPlaylistByProfileID(int pageIndex, int pageSize, int profileID){
        System.out.println("pageIndex"+pageIndex);
        System.out.println("pageSize"+pageSize);
        System.out.println("profileID"+profileID);
        // pageIndex
        // pageSize
        // pageData
        List<PlaylistDTO> pageData = songDao.getPlaylistByProfileID(pageIndex, pageSize, profileID);
        // totalDataNum
        int totalDataNum = songDao.getPlaylistCountByProfileID(profileID).intValue();
        // totalPageNum
        int totalPageNum = totalDataNum%pageSize ==0? totalDataNum/pageSize:totalDataNum/pageSize+1;
        Map map = new HashMap();
        map.put("totalDataNum",totalDataNum);
        map.put("totalPageNum",totalPageNum);
        map.put("pageIndex",pageIndex);
        map.put("pageSize",pageSize);
        map.put("pageData",pageData);
        return map;
    }
    public List<PlaylistSongDTO> getPlaylistSongByPlaylistID(int playlistID){
        return songDao.getPlaylistSongByPlaylistID(playlistID);
    }

    public int getPlaylistSongCount(int playlistID){
        return songDao.getPlaylistSongCountByPlaylistID(playlistID);
    }
    public boolean deletePlaylistSong(Integer playlistID, List<Integer>songID){
        return songDao.deletePlaylistSong(playlistID, songID);
    }

    public List<Integer> checkSongInWhichPlaylists(int profileID, int songID){
        return songDao.checkSongInWhichPlaylists(profileID, songID);
    }


    public boolean updateSong(SongUpdateVo songUpdateVo){
        try{
            if (songUpdateVo.getPhotoFile()!=null && songUpdateVo.getPhotoFile().getOriginalFilename()!=null && !songUpdateVo.getPhotoFile().getOriginalFilename().trim().isEmpty()){
                // 檔案名稱
                String originalFilename = songUpdateVo.getPhotoFile().getOriginalFilename();
                // 檔案真實路徑
                String photoPath = imgPath + "song/songImg/" + songUpdateVo.getSongID() + "-" + UUID.randomUUID() + originalFilename;
                Path photoFilePath = Paths.get(photoPath);
                if (!Files.exists(photoFilePath.getParent())){
                    Files.createDirectories(photoFilePath.getParent());
                }
                // 將圖片保存到指定路徑
                Files.copy(songUpdateVo.getPhotoFile().getInputStream(), photoFilePath);
                String databasePath = photoFilePath.toString().replace("\\", "/").replace(imgPath, "img/");
                songUpdateVo.setPhotoPath(databasePath);
            }else{
                songUpdateVo.setPhotoPath("");
            }
            return songDao.updateSong(songUpdateVo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateArtist(ArtistUpdateVo artistUpdateVo){
        System.out.println(artistUpdateVo.toString());
        try{
            if (artistUpdateVo.getArtistImage().getOriginalFilename()!=null && !artistUpdateVo.getArtistImage().getOriginalFilename().trim().isEmpty()){
                String originalFilename = artistUpdateVo.getArtistImage().getOriginalFilename();
                String photoPath = imgPath + "song/artistImg/" + artistUpdateVo.getArtistID() +"-"+ UUID.randomUUID() + originalFilename;
                Path photoFilePath = Paths.get(photoPath);
                if (!Files.exists(photoFilePath.getParent())){
                    Files.createDirectories(photoFilePath.getParent());
                }
                artistUpdateVo.getArtistImage().transferTo(photoFilePath);
                String databasePath = photoFilePath.toString().replace("\\", "/").replace(imgPath, "img/");
                artistUpdateVo.setImagePath(databasePath);
            }else{
                artistUpdateVo.setImagePath("");
            }
            return songDao.updateArtist(artistUpdateVo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return songDao.updateArtist(artistUpdateVo);
    }

    public List<SongRankDTO> getSongRank(SongRankQueryVo songRankQueryVo){
        return songDao.getSongRank(songRankQueryVo);
    }

    public List<SongPlayHistoryDTO> getHistoryByProfileID(int profileID){
        return songDao.getHistoryByProfileID(profileID);
    }

    public List<SongDTO> getRandomSong(int number){
        return songDao.getRandomSong(number);
    }


}
