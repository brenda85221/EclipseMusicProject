package team5.song.controller;

import com.google.cloud.storage.Storage;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team5.song.dao.SongDao;
import team5.song.model.bean.*;
import team5.song.model.dto.*;
import team5.song.model.vo.*;
import team5.song.service.SongService;
import team5.song.utils.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@MultipartConfig
@Controller
//@CrossOrigin(origins = "*")
@RequestMapping("song")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private Storage storage;
    @Value("${video.storage.prefix}")
    private String videoUploadPath;
    @Autowired
    private SongDao songDao;

    /**
     * pageIndex, pageSize預設值為-1 ，可不放參數(查詢全部)
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param keyword 關鍵字
     * @return List<Genre>
     */
    @GetMapping("genre")
    @ResponseBody
    public Result getGenre(@RequestParam(required = false, defaultValue = "-1") int pageIndex,
                           @RequestParam(required = false, defaultValue = "-1") int pageSize,
                           @RequestParam(required = false, defaultValue = "") String keyword) {
        Map genre = songService.getGenre(pageIndex, pageSize, keyword);
        if (genre != null) {
            int i = (int) genre.get("totalDataNum");
            if (i == 0){
                return Result.buildResult(401, "no genre data", null);
            }
            return Result.buildResult(200, "getGenre Success", genre);
        }
        return Result.buildResult(400, "getGenre Fail", null);
    }

    /**
     * 傳入id返回對應的Genre對象
     * @param id int
     * @return Genre
     */
    @GetMapping("genre/{id}")
    @ResponseBody
    public Result getGenre(@PathVariable int id) {
        Genre genreById = songService.getGenreById(id);
        if (genreById != null) {
            return Result.buildResult(200, "getGenre Success", genreById);
        }
        return Result.buildResult(400, "getGenre Fail", null);
    }

    /**
     * @param vo
     * vo:
     *    Integer pageIndex;   // 分頁索引  預設為1
     *    Integer sizePerPage; // 每頁顯示數量  預設為10
     *    String songGenre;    // 類型  預設為空
     *    String artistName;   // 表演者名稱  預設為空
     *    String songTitle;    // 歌曲標題  預設為空
     *    String countryName;  //國家名稱  預設為空
     *    String songAlbum;    // 專輯名稱  預設為空
     *    Boolean songIsDeleted; //是否刪除  預設為否
     * @return List<Song>
     */
    @GetMapping("/song")
    @ResponseBody
    public Result getSongByPagination(SongPaginationQueryParameterVo vo) {
        System.out.println(vo.toString());
        Map songByPagination = songService.getSongByPagination(vo);
        if (songByPagination != null) {
            return Result.buildResult(200, "getSong Success", songByPagination);
        }
        return Result.buildResult(400, "getSong Fail", null);
    }

    /**
     * 透過ID查找歌曲(完整資料)
     * @param id songID
     * @return Song
     */
    @GetMapping("/song/{id}")
    @ResponseBody
    public Result getSongById(@PathVariable int id) {
        Song songById = songService.getSongById(id);
        if (songById != null) {
            return Result.buildResult(200, "getSongById Success", songById);
        }
        else{
            return Result.buildResult(400, "getSongById Fail", null);
        }
    }

    /**
     * 傳入 albumQueryVo返回albumList
     * @param albumQueryVo
     * albumQueryVo:
     *     pageIndex:頁碼
     *     pageSize:頁容量
     *     albumName:專輯名稱
     *     artistName:歌手名稱
     * @return albumList
     */
    @GetMapping("album")
    @ResponseBody
    public Result getAlbum(AlbumQueryVo albumQueryVo) {
        Map album = songService.getAlbum(albumQueryVo);
        if (album != null) {
            return Result.buildResult(200, "getAlbum Success", album);
        }
        else{
            return Result.buildResult(400, "getAlbum Fail", null);
        }
    }

    /**
     * 透過專輯ID返回專輯
     * @param id albumID
     * @return album
     */
    @GetMapping("album/{id}")
    @ResponseBody
    public Result getAlbumByID(@PathVariable int id){
        Album albumById = songService.getAlbumById(id);
        if (albumById != null) {
            return Result.buildResult(200, "getAlbumByID Success", albumById);
        }
        else{
            return Result.buildResult(400, "getAlbumByID Fail", null);
        }
    }

    @GetMapping("getAlbumSongByName")
    @ResponseBody
    public Result getAlbumSongByName(String name) {
        List albumSongByName = songService.getAlbumSongByName(name);
        if (albumSongByName != null) {
            return Result.buildResult(200,"getAlbumByName Success", albumSongByName);
        }
        else {
            return Result.buildResult(400,"getAlbumByName Fail", null);
        }
    }

    @GetMapping("albumSong/{albumID}")
    @ResponseBody
    public Result getAlbumSongByID(@PathVariable int albumID) {
        List<SongDTO> albumSongByID = songService.getAlbumSongByID(albumID);
        if (albumSongByID!= null &&!albumSongByID.isEmpty()){
            return Result.buildResult(200, "getAlbumSongByID Success", albumSongByID);
        }
        else{
            return Result.buildResult(400, "getAlbumSongByID Fail", null);
        }
    }

    /**
     * 傳入專輯ID還有songID，將song表的albumsID做修改
     * @param albumID
     * @param songIDs
     * @return
     */
    @PostMapping("albumSong/{albumID}")
    @ResponseBody
    public Result insertAlbumSong(@PathVariable int albumID, @RequestBody List<Integer> songIDs) {
        boolean b = songService.insertAlbumSong(albumID, songIDs);
        if (b){
            return Result.buildResult(200,"insert albumSong success", null);
        }else{
            return Result.buildResult(400,"insert albumSong Fail", null);
        }
    }

    /**
     * 新增專輯
     */
    @PostMapping("album")
    @ResponseBody
    public Result insertAlbum(@RequestParam String albumName,
                              @RequestParam Date albumReleaseDate,
                              @RequestParam MultipartFile albumPhoto,
                              @RequestParam int artistID
    ) {
        boolean b = songService.insertAlbum(albumName, albumReleaseDate, albumPhoto, artistID);
        if (b){
            return Result.buildResult(200,"Insert Success", albumName);
        }else{
            return Result.buildResult(400,"Insert Fail", null);
        }
    }

    /**
     *  修改專輯
     */
    @PutMapping("album")
    @ResponseBody
    public Result updateAlbum(@RequestParam Integer albumID,
                              @RequestParam(required = false, defaultValue = "") String albumName,
                              @RequestParam(required = false) Date albumReleaseDate,
                              @RequestParam(required = false) MultipartFile albumPhoto,
                              @RequestParam(required = false, defaultValue = "") Integer artistID
                              ){
        System.out.println("albumName:"+albumName);
        System.out.println("albumReleaseDate:"+albumReleaseDate);
        System.out.println("albumPhoto:"+albumPhoto);
        System.out.println("artistID:"+artistID);

        boolean b = songService.updateAlbum(albumID, albumName, albumReleaseDate, albumPhoto, artistID);
        if (b){
            return Result.buildResult(200,"update album success",null);
        }else{
            return Result.buildResult(400, "update album fail",null);
        }
    }

    /**
     * 刪除專輯
     */
    @DeleteMapping("album/{albumID}")
    @ResponseBody
    public Result deleteAlbum(@PathVariable Integer albumID){
        boolean b = songService.deleteAlbum(albumID);
        if (b){
            return Result.buildResult(200, "delete album success", null);
        }else{
            return Result.buildResult(400, "delete album fail", null);
        }
    }

    /**
     * 刪除專輯裡的歌曲
     */
    @DeleteMapping("albumSong/{albumID}")
    @ResponseBody
    public Result deleteAlbumSong(@PathVariable Integer albumID, @RequestBody List<Integer> songIDs){
        boolean b = songService.deleteAlbumSong(albumID, songIDs);
        if (b){
            return Result.buildResult(200, "delete albumSong success", null);
        }else{
            return Result.buildResult(400, "delete albumSong fail", null);
        }
    }



    /**
     * 傳入SongID List 將這些歌曲的songIsDeleted改成false
     * @param songIDs SongID List
     * @return 成功:返回200, 失敗:返回400
     */
    @DeleteMapping("publish")
    @ResponseBody
    public Result publishSong(@RequestBody List<Integer> songIDs) {
        boolean b = songService.publishSong(songIDs);
        if (b){
            return Result.buildResult(200,"Publish Success", songIDs);
        }else{
            return Result.buildResult(400,"Publish Fail", null);
        }

    }
    @DeleteMapping("unpublish")
    @ResponseBody
    public Result unpublishSong(@RequestBody List<Integer> songIDs) {
        boolean b = songService.unpublishSong(songIDs);
        if (b){
            return Result.buildResult(200,"Unpublish Success", songIDs);
        }else{
            return Result.buildResult(400,"Unpublish Fail", null);
        }
    }

    /**
     * 獲取國家
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param keyword 關鍵字
     */
    @GetMapping("country")
    @ResponseBody
    public Result getCountry(@RequestParam int pageIndex,
                                @RequestParam int pageSize,
                                @RequestParam(required = false, defaultValue = "") String keyword) {
        Map country = songService.getCountry(pageIndex, pageSize, keyword);
        if (!country.isEmpty()){
            return Result.buildResult(200,"getAllCountry Success",country);
        }else{
            return Result.buildResult(400,"getAllCountry Fail",null);
        }
    }

    /**
     * 獲取歌手列表
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param keyword 關鍵字
     */
    @GetMapping("artist")
    @ResponseBody
    public Result getArtist(@RequestParam(defaultValue = "-1", required = false) int pageIndex,
                            @RequestParam(defaultValue = "-1", required = false) int pageSize,
                            @RequestParam(required = false, defaultValue = "") String keyword,
                            @RequestParam(required = false, defaultValue = "") String countryName,
                            @RequestParam(required = false, defaultValue = "name")String sort) {
        Map artist = songService.getArtist(pageIndex, pageSize, keyword, countryName , sort);
        if (!artist.isEmpty()){
            return Result.buildResult(200,"getArtist Success",artist);
        }else{
            return Result.buildResult(400,"getArtist Fail",null);
        }
    }

    @GetMapping("artist/{artistID}")
    @ResponseBody
    public Result getArtistByID(@PathVariable Integer artistID) {
        ArtistDTO artistByID = songService.getArtistByID(artistID);
        if (artistByID!=null){
            return Result.buildResult(200,"getArtistByID Success",artistByID);
        }else{
            return Result.buildResult(400,"getArtistByID fail",null);
        }
    }

    /**
     * @param country 國家
     * @return true:成功插入  false:已有這筆資料
     */

    @PostMapping("country")
    @ResponseBody
    public Result insertCountry(@RequestBody Country country) {
        System.out.println(country);
        boolean b = songService.insertCountry(country);
        if (b){
            return Result.buildResult(200,"Insert Country Success", country);
        }
        else{
            return Result.buildResult(400,"Insert CountryFail", null);
        }
    }

    @PostMapping("artist")
    @ResponseBody
    public Result insertArtist(@RequestParam MultipartFile artistPhoto,
                               @RequestParam String artistName,
                               @RequestParam String countryName,
                               @RequestParam Date birthDate) {

        System.out.println(birthDate);
        ArtistUploadVo artistUploadVo = new ArtistUploadVo(artistName, countryName, birthDate, artistPhoto);

        boolean b = songService.insertArtist(artistUploadVo);
        if (b){
            return Result.buildResult(200,"Insert Success", null);
        }
        return Result.buildResult(400,"Insert Fail", null);
    }


    @PostMapping("insertSong")
    @ResponseBody
    public Result insertSong(@RequestBody Song song) {
        boolean b = songService.insertSong(song);
        if (b){
            return Result.buildResult(200,"Insert Song Success", song.getSongID());
        }else{
            return Result.buildResult(400,"Insert Song Fail", null);
        }
    }

    @PostMapping("uploadSong")
    @ResponseBody
    public Result uploadSong(@RequestParam String songTitle,
                             @RequestParam int artistID,
                             @RequestParam String songGenre,
                             @RequestParam String country,
                             @RequestParam String songInfo,
                             @RequestParam Date createDate,
                             @RequestParam MultipartFile songFile,
                             @RequestParam MultipartFile photoFile
                             ) {
        System.out.println("songTitle:"+songTitle);
        System.out.println("artistID:"+artistID);
        System.out.println("songGenre:"+songGenre);
        System.out.println("country:"+country);
        System.out.println("songInfo:"+songInfo);
        System.out.println("createDate:"+createDate);
        System.out.println("songFile:"+songFile.getOriginalFilename());
        System.out.println("photoFile:"+photoFile.getOriginalFilename());

        SongUploadVo songUploadVo = new SongUploadVo(songTitle, artistID, songGenre, country, songInfo,createDate, songFile, photoFile);
        try{
            boolean b = songService.uploadSong(songUploadVo);
            if (b) {
                return Result.buildResult(200, "Upload Success", null);
            }else{
                return Result.buildResult(400, "Upload Failed", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.buildResult(400, "Upload Failed", null);
        }
    }
//    @GetMapping("/stream2/**")
//    public void GCSStream(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        storage = StorageOptions.getDefaultInstance().getService();
//
//        // GCS bucket 名稱
//        String bucketName = "ispanproject";
//
//        // 獲取路徑訊息
//        String fullPathURI = request.getRequestURI();
//        fullPathURI = fullPathURI.substring(fullPathURI.indexOf("/stream2/") + "/stream2/".length());
//
//        // 解碼路徑、處理特殊字符
//        String objectName = URLDecoder.decode(fullPathURI, StandardCharsets.UTF_8); // 例如：index.m3u8 或 .ts 文件
//        System.out.println("解碼後的路徑信息：" + objectName);
//
//        // 找到對應的資料
//        Blob blob = storage.get(bucketName, objectName);
//        if (blob != null) {
//            System.out.println("have data");
//            // 根據檔案格式設置響應格式
//            String contentType = objectName.endsWith(".m3u8") ? "application/vnd.apple.mpegurl" : "video/MP2T";
//            response.setContentType(contentType);
//            response.setContentLengthLong(blob.getSize());
//
//            // 將 blob 內容寫入響應輸出流
//            try (OutputStream out = response.getOutputStream()) {
//                blob.downloadTo(out);
//            }
//        } else {
//            System.out.println("no data");
//            // 如果文件未找到，則發送 404 錯誤
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件未找到");
//        }
//    }

    @GetMapping("/stream2/**")
    public void Stream(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 獲取路徑訊息
        String fullPathURI = request.getRequestURI();
        fullPathURI = fullPathURI.substring(fullPathURI.indexOf("/stream2/") + "/stream2/".length());

        // 解碼路徑、處理特殊字符
        String objectName = URLDecoder.decode(fullPathURI, StandardCharsets.UTF_8); // 例如：index.m3u8 或 .ts 文件
        System.out.println("解碼後的路徑信息：" + objectName);

        // 真實播放路徑
        String videFilePath = videoUploadPath + objectName;
        System.out.println("播放路徑:"+videFilePath);

        // 組裝文件路徑
        File file = new File(videFilePath);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File not found");
            return;
        }

        // 設置內容類型
        String fileName = file.getName();
        if (fileName.endsWith(".m3u8")) {
            response.setContentType("application/vnd.apple.mpegurl");
        } else if (fileName.endsWith(".ts")) {
            response.setContentType("video/MP2T");
        }
        response.setContentLengthLong(file.length());

        // 返回文件內容
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    @PostMapping("songClick")
    @ResponseBody
    public Result insertSongClick(@RequestBody SongClickAddVo songClickAddVo) {
        Integer songID = songClickAddVo.getSongID();
        Integer profileID = null;
        if (songClickAddVo.getProfileID() != null){
            profileID = songClickAddVo.getProfileID();
        }else{
            profileID = 0;
        }
        System.out.println("songID:"+songID);
        System.out.println("profileID:"+ profileID);
        boolean b = songService.insertSongClick(songID, profileID);
        if (b){
            return Result.buildResult(200,"Insert Success", null);
        }
        else{
            return Result.buildResult(400,"Insert Fail", null);
        }
    }

    /**
     * 創建播放清單
     */
    @PostMapping("playlist")
    @ResponseBody
    public Result createPlaylist(@RequestParam String playlistName,
                                 @RequestParam String playlistDescription,
                                 @RequestParam int profileID){
        boolean playlist = songService.createPlaylist(playlistName, playlistDescription, profileID);
        if (playlist){
            return Result.buildResult(200, "新增播放清單成功", null);
        }else{
            return Result.buildResult(400, "Fail",null);
        }
    }

    /**
     * 傳入playlist 根據id刪除
     * @param playlistIDs playlistID集合
     * @return 成功:true, 失敗:false
     */
    @DeleteMapping("playlist")
    @ResponseBody
    public Result deletePlaylist(@RequestBody List<Integer> playlistIDs){
        boolean b = songService.deletePlaylist(playlistIDs);
        if (b){
            return Result.buildResult(200,"Delete Success", null);
        }
        else{
            return Result.buildResult(400,"Delete Fail", null);
        }
    }

    /**
     * 傳入playlistID修改播放清單
     */
    @PutMapping("playlist")
    @ResponseBody
    public Result updatePlaylist(@RequestParam Integer playlistID,
                                 @RequestParam(required = false, defaultValue = "") String playlistName,
                                 @RequestParam(required = false, defaultValue = "") String playlistDescription){
        boolean b = songService.updatePlaylist(playlistID, playlistName, playlistDescription);
        if (b){
            return Result.buildResult(200, "update playlist success", null);
        }else{
            return Result.buildResult(400, "update playlist fail", null);
        }
    }

    /**
     * 新增播放清單歌曲
     */
    @PostMapping("playlistSong")
    @ResponseBody
    public Result insertPlaylistSong(@RequestBody InsertPlaylistSongVo insertPlaylistSongVo){
        System.out.println(insertPlaylistSongVo.toString());
        boolean b = songService.insertPlaylistSong(insertPlaylistSongVo);
        if (b){
            return Result.buildResult(200, "insert song success", null);
        }else{
            return Result.buildResult(400, "insert song fail", null);
        }
    }

    /**
     * 傳入使用者ID返回他所有播放清單
     * 若pageIndex = -1, pageSize = -1 則搜尋全部
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param profileID profileID
     * @return
     */
    @GetMapping("playlist")
    @ResponseBody
    public Result getPlaylistByProfileID(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam Integer profileID){
        Map playlistByProfileID = songService.getPlaylistByProfileID(pageIndex, pageSize, profileID);
        if (playlistByProfileID != null){
            return Result.buildResult(200, "get playlist success", playlistByProfileID);
        }else{
            return Result.buildResult(400, "get playlist fail", null);
        }
    }

    /**
     * 傳入播放清單ID返回所有歌曲
     */
    @GetMapping("playlistSong/{playlistID}")
    @ResponseBody
    public Result getPlaylistSongByPlaylistID(@PathVariable Integer playlistID){
        List<PlaylistSongDTO> playlistSongByPlaylistID = songService.getPlaylistSongByPlaylistID(playlistID);
        if (playlistSongByPlaylistID != null){
            return Result.buildResult(200, "get playlistSong success", playlistSongByPlaylistID);
        }
        else{
            return Result.buildResult(400, "get playlistSong fail", null);
        }
    }

    /**
     * 傳入播放清單ID返回歌曲數量
     */
    @GetMapping("playlistSongCount/{playlistID}")
    @ResponseBody
    public Result getPlaylistSongCount(@PathVariable Integer playlistID){
        int playlistSongCount = songService.getPlaylistSongCount(playlistID);
        return Result.buildResult(200, "get playlistSong success", playlistSongCount);
    }

    /**
     * 傳入播放清單ID,歌曲ID，刪除播放清單裡的歌曲
     */
    @DeleteMapping("playlistSong/{playlistID}")
    @ResponseBody
    public Result deletePlaylistSong(@PathVariable Integer playlistID, @RequestBody List<Integer> songIDs){
        boolean b = songService.deletePlaylistSong(playlistID, songIDs);
        if (b){
            return Result.buildResult(200, "delete song success!" , null);
        }
        else{
            return Result.buildResult(400, "delete song fail!" , null);
        }
    }

    @GetMapping("songPlaylists")
    @ResponseBody
    public Result checkSongInWhichPlaylists(@RequestParam Integer songID, @RequestParam Integer profileID){
        List<Integer> integers = songService.checkSongInWhichPlaylists(profileID,songID);
        if (integers != null && !integers.isEmpty()){
            return Result.buildResult(200, "check song in which playlists success", integers);
        }
        else{
            return Result.buildResult(400, "check song in which playlists fail", null);
        }
    }


    /**
     * 傳入ArtistID的List，做刪除
     * @param artistIDs ArtistID的集合
     * @return 成功:true, 失敗:false
     */
    @DeleteMapping("artist")
    @ResponseBody
    public Result deleteArtist(@RequestBody List<Integer> artistIDs){
        boolean b = songService.deleteArtist(artistIDs);
        if (b){
            return Result.buildResult(200,"Delete Success", null);
        }
        else{
            return Result.buildResult(400,"Delete Fail", null);
        }
    }

    /**
     * 傳入要覆蓋的資料，若為空則不修改
     * @param songID songID
     * @param songTitle String
     * @param artistID int
     * @param songGenre String
     * @param country String
     * @param songInfo String
     * @param photoFile String
     * @return 成功:200, 失敗:400
     */
    @PutMapping("song")
    @ResponseBody
    public Result updateSongs(@RequestParam int songID,
                              @RequestParam(required = false, defaultValue = "")String songTitle,
                              @RequestParam(required = false, defaultValue = "0") int artistID,
                              @RequestParam(required = false, defaultValue = "") String songGenre,
                              @RequestParam(required = false, defaultValue = "") String country,
                              @RequestParam(required = false, defaultValue = "0") int albumID,
                              @RequestParam(required = false, defaultValue = "") String songInfo,
                              @RequestParam(required = false) MultipartFile photoFile){
        SongUpdateVo songUpdateVo = new SongUpdateVo();
        songUpdateVo.setSongID(songID);
        songUpdateVo.setSongTitle(songTitle);
        songUpdateVo.setArtistID(artistID);
        songUpdateVo.setSongGenreName(songGenre);
        songUpdateVo.setSongInfo(songInfo);
        songUpdateVo.setCountryName(country);
        songUpdateVo.setAlbumID(albumID);
        songUpdateVo.setPhotoFile(photoFile);

        boolean b = songService.updateSong(songUpdateVo);
        if (b){
            return Result.buildResult(200,"Update Success", null);
        }
        else{
            return Result.buildResult(400,"Update Fail", null);
        }
    }

    @PutMapping("artist")
    @ResponseBody
    public Result updateArtist(@RequestParam Integer artistID,
                               @RequestParam(required = false, defaultValue = "")String artistName,
                               @RequestParam(required = false, defaultValue = "")String countryName,
                               @RequestParam (required = false) Date birthDate,
                               @RequestParam(required = false )MultipartFile artistImage
    ){
        ArtistUpdateVo artistUpdateVo = new ArtistUpdateVo();
        artistUpdateVo.setArtistID(artistID);
        artistUpdateVo.setArtistName(artistName);
        artistUpdateVo.setCountryName(countryName);
        artistUpdateVo.setBirthDate(birthDate);
        artistUpdateVo.setArtistImage(artistImage);

        boolean b = songService.updateArtist(artistUpdateVo);
        if (b){
            return Result.buildResult(200,"Update Success", null);
        }
        else{
            return Result.buildResult(400,"Update Fail", null);
        }
    }


    @GetMapping("songRank")
    @ResponseBody
    public Result getSongRank(@RequestParam(defaultValue = "") String genreName,
                              @RequestParam(defaultValue = "") String artistName,
                              @RequestParam(defaultValue = "") String countryName,
                              @RequestParam(defaultValue = "7") Integer day,
                              @RequestParam(defaultValue = "30") Integer count){
        SongRankQueryVo songRankQueryVo = new SongRankQueryVo(genreName, artistName, countryName, day, count);
        System.out.println(songRankQueryVo);
        List<SongRankDTO> songRank = songService.getSongRank(songRankQueryVo);
        if (songRank != null){
            return Result.buildResult(200, "get songRank success", songRank);
        }
        else{
            return Result.buildResult(400, "get songRank fail", null);
        }
    }

    /**
     * 傳入profileID返回近10筆播放紀錄
     * @param profileID
     * @return
     */
    @GetMapping("history/{profileID}")
    @ResponseBody
    public Result getHistoryByProfileID(@PathVariable Integer profileID){
        List<SongPlayHistoryDTO> historyByProfileID = songService.getHistoryByProfileID(profileID);
        if (historyByProfileID != null){
            return Result.buildResult(200, "get historyByProfileID success", historyByProfileID);
        }
        else{
            return Result.buildResult(400, "get historyByProfileID fail", null);
        }

    }

    @GetMapping("randomSong/{number}")
    @ResponseBody
    public Result getRandomSong(@PathVariable Integer number){
        List<SongDTO> randomSong = songService.getRandomSong(number);
        if (randomSong != null){
            return Result.buildResult(200, "get randomSong success", randomSong);
        }
        else{
            return Result.buildResult(400, "get randomSong fail", null);
        }
    }
}
