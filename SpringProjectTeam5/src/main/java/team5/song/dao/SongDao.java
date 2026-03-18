package team5.song.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import team5.profile.model.bean.ProfilesBean;
import team5.song.model.bean.*;
import team5.song.model.dto.*;
import team5.song.model.vo.*;

import java.util.*;

@Transactional
@Repository
public class SongDao {
    @Autowired
    private EntityManager entityManager;

    public List<SongDTO> getSongByPagination(SongPaginationQueryParameterVo queryParameter) {
        System.out.println("queryParameter.toString()"+queryParameter.toString());
        // 動態生成 HQL 查詢語句
        String hql = buildHqlQuery(queryParameter);

        TypedQuery<SongDTO> query = entityManager.createQuery(hql, SongDTO.class);

        // 設置參數
        Map<String, Object> params = getQueryParams(queryParameter);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // 設置分頁
        int pageIndex = queryParameter.getPageIndex();
        int sizePerPage = queryParameter.getSizePerPage();
        query.setFirstResult((pageIndex - 1) * sizePerPage);
        query.setMaxResults(sizePerPage);

        List<SongDTO> resultList = query.getResultList();
        System.out.println("songData:");
        System.out.println(resultList);
        return resultList;
    }

    private String buildHqlQuery(SongPaginationQueryParameterVo queryParameter) {
        StringBuilder hql = new StringBuilder("SELECT new team5.song.model.dto.SongDTO(" +
                "s.songID, s.songTitle, s.songDuration, g.genreName, c.countryName, a.albumName, " +
                "art.artistName, s.songPath, s.coverImagePath, s.songInfo, s.songCreateAt, s.songLastUpdate, s.songIsDeleted) " +
                "FROM Song s LEFT JOIN s.genre g LEFT JOIN s.country c LEFT JOIN s.album a LEFT JOIN s.artist art WHERE 1=1");

        if (queryParameter.getSongGenre() != null && !queryParameter.getSongGenre().isEmpty()) {
            hql.append(" AND g.genreName LIKE :songGenre");
        }
        if (queryParameter.getArtistName() != null && !queryParameter.getArtistName().isEmpty()) {
            hql.append(" AND art.artistName LIKE :artistName");
        }
        if (queryParameter.getSongTitle() != null && !queryParameter.getSongTitle().isEmpty()) {
            hql.append(" AND s.songTitle LIKE :songTitle");
        }
        if (queryParameter.getSongAlbum() != null && !queryParameter.getSongAlbum().isEmpty()) {
            hql.append(" AND a.albumName LIKE :songAlbum");
        }
        if (queryParameter.getCountryName() != null && !queryParameter.getCountryName().isEmpty()) {
            hql.append(" AND c.countryName LIKE :countryName");
        }
        if (queryParameter.getSongIsDeleted() != null) {
            hql.append(" AND s.songIsDeleted = :songIsDeleted");
        }

        hql.append(" ORDER BY s.songID");

        return hql.toString();
    }


    private Map<String, Object> getQueryParams(SongPaginationQueryParameterVo queryParameter) {
        Map<String, Object> params = new HashMap<>();
        if (queryParameter.getSongGenre() != null && !queryParameter.getSongGenre().isEmpty()) {
            params.put("songGenre", "%" + queryParameter.getSongGenre() + "%");
        }
        if (queryParameter.getArtistName() != null && !queryParameter.getArtistName().isEmpty()) {
            params.put("artistName", "%" + queryParameter.getArtistName() + "%");
        }
        if (queryParameter.getSongTitle() != null && !queryParameter.getSongTitle().isEmpty()) {
            params.put("songTitle", "%" + queryParameter.getSongTitle() + "%");
        }
        if (queryParameter.getSongAlbum() != null && !queryParameter.getSongAlbum().isEmpty()) {
            params.put("songAlbum", "%" + queryParameter.getSongAlbum() + "%");
        }
        if (queryParameter.getCountryName() != null && !queryParameter.getCountryName().isEmpty()) {
            params.put("countryName", "%" + queryParameter.getCountryName() + "%");
        }
        if (queryParameter.getSongIsDeleted() != null) {
            params.put("songIsDeleted", queryParameter.getSongIsDeleted());
        }
        return params;
    }

    public long getSongCountByConditions(SongPaginationQueryParameterVo queryParameter) {
        // 動態生成 HQL 查詢語句
        StringBuilder hql = new StringBuilder("SELECT count(s.songID) FROM Song s " +
                "LEFT JOIN s.genre g " +
                "LEFT JOIN s.country c " +
                "LEFT JOIN s.album a " +
                "LEFT JOIN s.artist art WHERE 1=1");

        // 添加條件
        Map<String, Object> params = new HashMap<>();
        if (queryParameter.getSongGenre() != null && !queryParameter.getSongGenre().isEmpty()) {
            hql.append(" AND g.genreName LIKE :songGenre");
            params.put("songGenre", "%" + queryParameter.getSongGenre() + "%");
        }
        if (queryParameter.getArtistName() != null && !queryParameter.getArtistName().isEmpty()) {
            hql.append(" AND art.artistName LIKE :artistName");
            params.put("artistName", "%" + queryParameter.getArtistName() + "%");
        }
        if (queryParameter.getSongTitle() != null && !queryParameter.getSongTitle().isEmpty()) {
            hql.append(" AND s.songTitle LIKE :songTitle");
            params.put("songTitle", "%" + queryParameter.getSongTitle() + "%");
        }
        if (queryParameter.getSongAlbum() != null && !queryParameter.getSongAlbum().isEmpty()) {
            hql.append(" AND a.albumName LIKE :songAlbum");
            params.put("songAlbum", "%" + queryParameter.getSongAlbum() + "%");
        }
        if (queryParameter.getCountryName() != null && !queryParameter.getCountryName().isEmpty()) {
            hql.append(" AND c.countryName LIKE :countryName");
            params.put("countryName", "%" + queryParameter.getCountryName() + "%");
        }
        // 過濾刪除狀態
        if (queryParameter.getSongIsDeleted() != null) {
            hql.append(" AND s.songIsDeleted = :songIsDeleted");
            params.put("songIsDeleted", queryParameter.getSongIsDeleted());

        }

        // 創建查詢
        TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
        // 設置參數
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // 返回計數結果
        return query.getSingleResult();
    }

    /**
     * 隨機獲取N首歌曲
     */
    public List<SongDTO> getRandomSong(int number){
        StringBuilder hql = new StringBuilder("SELECT new team5.song.model.dto.SongDTO(" +
                "s.songID, " +
                "s.songTitle, " +
                "s.songDuration, " +
                "g.genreName, " +
                "c.countryName, " +
                "a.albumName, " +
                "art.artistName, " +
                "s.songPath, " +
                "s.coverImagePath, " +
                "s.songInfo, " +
                "s.songCreateAt, " +
                "s.songLastUpdate, " +
                "s.songIsDeleted" +
                ") FROM Song s " +
                "LEFT JOIN s.genre g " +
                "LEFT JOIN s.country c " +
                "LEFT JOIN s.album a " +
                "LEFT JOIN s.artist art WHERE 1=1 " +
                "AND s.songIsDeleted = false order by NEWID()");
        TypedQuery<SongDTO> query = entityManager.createQuery(hql.toString(), SongDTO.class);
        query.setFirstResult(0);
        query.setMaxResults(number);
        return query.getResultList();
    }

    /**
     * 若pageIndex,pageSize  =-1,則查詢全部
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @return genreList
     */
    public List<Genre> getGenre(int pageIndex, int pageSize, String keyword) {
        String hql = "from Genre where genreName like :genreName";
        TypedQuery<Genre> query = entityManager.createQuery(hql.toString(), Genre.class);
        query.setParameter("genreName", "%" + keyword + "%");
        // 檢查是否需要分頁
        if (pageIndex > 0 && pageSize > 0) {
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    public int getGenreCount(String keyword) {
        String hql = "select count(genreID) from Genre where genreName like :genreName";
        TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
        query.setParameter("genreName", "%" + keyword + "%");
        return query.getSingleResult().intValue();
    }


    public Genre getGenreByID(int id){
        return entityManager.find(Genre.class, id);
    }

    /**
     * 根據songID查詢song
     * @param id songID
     */
    public Song getSongById(int id) {
        String hql = "from Song where id=:id";
        TypedQuery<Song> query = entityManager.createQuery(hql, Song.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    /**
     * 撈出專輯資料
     * @param name 專輯名稱
     * @return SongList
     */
    public List<Song> getAlbumSongByName(String name) {
        String hql = "from Song s where s.album.albumName=:name";
        TypedQuery<Song> query = entityManager.createQuery(hql, Song.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * 返回專輯
     * @return List<Album>
     */
    public List<AlbumDTO> getAlbum(AlbumQueryVo albumQueryVo) {
        StringBuilder hql = new StringBuilder(
                "select new team5.song.model.dto.AlbumDTO(a.albumID, a.albumName, a.albumReleaseDate, " +
                        "a.albumCoverPath, a.artist.artistName, a.artist.artistPhoto) " +
                        "from Album a where 1=1 "
        );
        Map<String, Object> params = new HashMap<>();

        if (albumQueryVo.getAlbumName() != null && !albumQueryVo.getAlbumName().isEmpty()) {
            hql.append(" and a.albumName LIKE :albumName");
            params.put("albumName", "%" + albumQueryVo.getAlbumName() + "%");
        }
        if (albumQueryVo.getArtistName() != null && !albumQueryVo.getArtistName().isEmpty()) {
            hql.append(" and a.artist.artistName LIKE :artistName");
            params.put("artistName", "%" + albumQueryVo.getArtistName() + "%");
        }

        TypedQuery<AlbumDTO> query = entityManager.createQuery(hql.toString(), AlbumDTO.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        if (!(albumQueryVo.getPageIndex() == -1 && albumQueryVo.getPageSize() == -1)) {
            query.setFirstResult((albumQueryVo.getPageIndex() - 1) * albumQueryVo.getPageSize());
            query.setMaxResults(albumQueryVo.getPageSize());
        }

        return query.getResultList();
    }

    /**
     * 返回專輯
     * @return List<Album>
     */
    public int getAlbumCount(AlbumQueryVo albumQueryVo) {
        // 使用 StringBuilder 构建 HQL 查询
        StringBuilder hql = new StringBuilder("select COUNT(a.albumID) from Album a where 1=1");
        Map<String, Object> params = new HashMap<>();

        // 添加專輯名稱條件
        if (albumQueryVo.getAlbumName() != null && !albumQueryVo.getAlbumName().trim().isEmpty()) {
            hql.append(" and a.albumName LIKE :albumName");
            params.put("albumName", "%" + albumQueryVo.getAlbumName().trim() + "%");
        }

        // 添加藝術家名稱條件
        if (albumQueryVo.getArtistName() != null && !albumQueryVo.getArtistName().trim().isEmpty()) {
            hql.append(" and a.artist.artistName LIKE :artistName");
            params.put("artistName", "%" + albumQueryVo.getArtistName().trim() + "%");
        }

        // 構建查詢
        TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // 返回結果
        Long result = query.getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    /**
     * 傳入ID返回album
     * @param id albumID
     * @return ALBUM
     */
    public Album getAlbumByID(int id){
        return entityManager.find(Album.class, id);
    }

    /**
     * 返回對應的albumID所有歌曲
     * @param id
     * @return
     */
    public List<SongDTO> getAlbumSongByID(int id){
        try {
            Album album = entityManager.find(Album.class, id);
            if (album != null) {
                String jpql = "select new team5.song.model.dto.SongDTO(" +
                        "s.songID," +
                        "s.songTitle," +
                        "s.songDuration," +
                        "s.genre.genreName," +
                        "s.country.countryName," +
                        "s.album.albumName," +
                        "s.artist.artistName," +
                        "s.songPath," +
                        "s.coverImagePath," +
                        "s.songInfo," +
                        "s.songCreateAt," +
                        "s.songLastUpdate," +
                        "s.songIsDeleted) from Song s where s.songIsDeleted = false and s.album.albumID = :albumID";
                return entityManager.createQuery(jpql, SongDTO.class).setParameter("albumID", id).getResultList();
            }
            else{
                throw new RuntimeException("沒有找到Album");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertAlbumSong(int albumID,List<Integer> songIDs){
        try{
            Album album = entityManager.find(Album.class, albumID);
            if (album != null){
                String sql = "update Song set album.albumID = :albumID where songID in (:songIDs)";
                int i = entityManager.createQuery(sql).setParameter("albumID", albumID).setParameter("songIDs", songIDs).executeUpdate();
                return i > 0;
            }else{
                throw new RuntimeException("沒有找到Album");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean insertAlbum(String albumName, Date albumReleaseDate, String imgDatabasePath, int artistID) {
        try {
            Artist artist = entityManager.find(Artist.class, artistID);
            if (artist != null) {
                Album album = new Album();
                album.setAlbumName(albumName);
                album.setAlbumReleaseDate(albumReleaseDate);
                album.setAlbumCoverPath(imgDatabasePath);
                album.setArtist(artist);
                entityManager.persist(album);
                return true;
            }else{
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAlbum(Integer albumID, String albumName, Date albumReleaseDate, String albumPhotoPath, Integer artistID){
        try{
            Album album = entityManager.find(Album.class, albumID);
            if (album != null){
                if (albumName != null && !albumName.isEmpty()){
                    album.setAlbumName(albumName);
                }
                if (albumReleaseDate != null){
                    album.setAlbumReleaseDate(albumReleaseDate);
                }
                if (albumPhotoPath != null && !albumPhotoPath.isEmpty()){
                    album.setAlbumCoverPath(albumPhotoPath);
                }
                if (artistID != null){
                    Artist artist = entityManager.find(Artist.class, artistID);
                    if (artist != null){
                        album.setArtist(artist);
                    }else{
                        throw new RuntimeException("找不到Artist");
                    }
                }
                return true;
            }else{
                throw new RuntimeException("找不到Album");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 刪除專輯
     */
    public boolean deleteAlbum(Integer albumID){
        try{
            Album album = entityManager.find(Album.class, albumID);
            if (album != null){
                String hql = "update Song s set s.album.albumID = null where s.album.albumID = :albumID";
                entityManager.createQuery(hql).setParameter("albumID", albumID).executeUpdate();
                entityManager.remove(album);
                return true;
            }else{
                throw new RuntimeException("找不到Album");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean deleteAlbumSong(Integer albumID, List<Integer> songIDs){
        try{
            Album album = entityManager.find(Album.class, albumID);
            if (album != null){
                String hql = "update Song s set s.album.albumID = null where s.songID in (:songIDs) and s.album.albumID = album.albumID";
                int i = entityManager.createQuery(hql).setParameter("songIDs", songIDs).executeUpdate();
                return i>0;
            }else{
                throw new RuntimeException("找不到Album");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 傳入songID的List
     * @param songIDs songID的list
     * @return 成功就返回true (改變的資料數>0 = true)
     */
    public boolean publishSong(List<Integer> songIDs) {
        try {
            String hql = "update Song set songIsDeleted = false where songID in (:ids)";
            entityManager.createQuery(hql).setParameter("ids", songIDs).executeUpdate();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 傳入songID的List
     * @param songIDs songID的list
     * @return 成功就返回true (改變的資料數>0 = true)
     */
    public boolean unpublishSong(List<Integer> songIDs) {
        try {
            String hql = "update Song set songIsDeleted = true where songID in (:ids)";
            entityManager.createQuery(hql).setParameter("ids", songIDs).executeUpdate();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取得countryID
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param keyword 關鍵字
     * @return countryList<Country>
     */
    public List<Country> getCountry(int pageIndex, int pageSize, String keyword) {
        String hql = "from Country where countryName like :keyword order by countryName";
        TypedQuery<Country> query = entityManager.createQuery(hql, Country.class);
        query.setParameter("keyword", "%" + keyword + "%");
        if (pageIndex > 0 && pageSize > 0) {
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    /**
     * 取得country數量
     * @param keyword 關鍵字
     * @return country數量
     */
    public int getCountryCount(String keyword) {
        String hql = "select count (countryID) from Country where countryName like :keyword";
        TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getSingleResult().intValue();
    }

    /**
     * 獲取artists
     * @param pageIndex 頁碼
     * @param pageSize 頁容量
     * @param keyword 關鍵字
     * @return artist列表
     */
    public List<ArtistDTO> getArtist(int pageIndex, int pageSize, String keyword, String countryName, String sort) {
        // 動態設定排序條件
        String orderByClause;
        if ("name".equals(sort)) {
            orderByClause = "a.artistName";
        } else if ("id".equals(sort)) {
            orderByClause = "a.artistID";
        } else {
            throw new IllegalArgumentException("Invalid sort parameter: " + sort);
        }

        // 動態生成 HQL 語句
        StringBuilder hql = new StringBuilder(
                "SELECT new team5.song.model.dto.ArtistDTO(" +
                        "   a.artistID, " +
                        "   a.artistName, " +
                        "   c.countryName, " +
                        "   a.birthDate, " +
                        "   a.artistPhoto, " +
                        "   a.createdAt" +
                        ") " +
                        "FROM Artist a " +
                        "LEFT JOIN a.country c " +
                        "WHERE a.artistName LIKE :keyword "
        );

        // 如果 countryName 不為空，添加條件
        if (countryName != null && !countryName.isEmpty()) {
            hql.append("AND c.countryName = :countryName ");
        }

        hql.append("ORDER BY ").append(orderByClause);

        // 創建查詢
        TypedQuery<ArtistDTO> query = entityManager.createQuery(hql.toString(), ArtistDTO.class);
        query.setParameter("keyword", "%" + keyword + "%");

        if (countryName != null && !countryName.isEmpty()) {
            query.setParameter("countryName", countryName);
        }

        // 設置分頁
        if (pageIndex > 0 && pageSize > 0) {
            query.setFirstResult((pageIndex - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    public ArtistDTO getArtistByID(Integer artistID){
        return entityManager.createQuery(
                "SELECT new team5.song.model.dto.ArtistDTO(a.artistID," +
                        " a.artistName," +
                        "a.country.countryName," +
                        "a.birthDate," +
                        "a.artistPhoto," +
                        "a.createdAt) FROM Artist a WHERE a.id = :id",
                ArtistDTO.class
        ).setParameter("id", artistID).getSingleResult();

    }

    /**
     * @param keyword 關鍵字
     * @return 符合條件的資料數(INT)
     */
    public int getArtistCount(String keyword, String countryName) {
        // 動態生成 HQL 語句
        StringBuilder hql = new StringBuilder("SELECT COUNT(a.artistID) FROM Artist a WHERE a.artistName LIKE :keyword");

        // 如果 countryName 不為空，添加條件
        if (countryName != null && !countryName.isEmpty()) {
            hql.append(" AND a.country.countryName = :countryName");
        }

        // 創建查詢
        TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
        query.setParameter("keyword", "%" + keyword + "%");

        // 如果 countryName 有值，設置參數
        if (countryName != null && !countryName.isEmpty()) {
            query.setParameter("countryName", countryName);
        }

        // 獲取結果並轉為整數
        int totalCount = query.getSingleResult().intValue();
        System.out.println("總資料數: " + totalCount);

        return totalCount;
    }


    /**
     * 新增一個artist，如果country不存在，也順便新增
     * @param artist
     */
    public void insertArtist(Artist artist) throws Exception {
        try {
            System.out.println("1");
            Country country = artist.getCountry();
            // 使用 getResultList() 來避免 NoResultException
            TypedQuery<Country> query = entityManager.createQuery("FROM Country WHERE countryName = :countryName", Country.class);
            System.out.println("2");
            query.setParameter("countryName", country.getCountryName());

            List<Country> resultList = query.getResultList();  // 返回結果列表
            System.out.println("3");
            if (!resultList.isEmpty()) {  // 如果列表非空，表示國家存在
                System.out.println("國家存在");
                artist.setCountry(resultList.get(0));  // 取出第一筆結果
            } else {  // 國家不存在，執行插入
                System.out.println("插入國家");
                entityManager.persist(country);
            }

            entityManager.persist(artist);
        } catch (Exception e) {
            throw new Exception("Insert Error", e);  // 加上原始錯誤訊息
        }
    }


    /**
     * 傳入countryName，找有無這筆資料，若沒有就新增
     * @param country 國家
     * @return true:成功插入 false:插入失敗
     */
    public boolean insertCountry(Country country) {
        String findCountry = "from Country where countryName = :countryName";
        List resultList = entityManager.createQuery(findCountry).setParameter("countryName", country.getCountryName()).getResultList();
        if (!resultList.isEmpty()) {
            return false;
        }else{
            entityManager.persist(country);
            return true;
        }
    }


    /**
     * 上傳音樂
     * @param song
     * @return
     */
    @Transactional
    public int insertSong(Song song) {
        try {
            // 確認 Genre 是否已存在，否則使用傳入的
            String findGenre = "from Genre where genreName=:genreName";
            TypedQuery<Genre> query = entityManager.createQuery(findGenre, Genre.class);
            query.setParameter("genreName", song.getGenre().getGenreName());
            List<Genre> genreList = query.getResultList();  // 使用 getResultList() 來避免異常
            if (!genreList.isEmpty()) {
                song.setGenre(genreList.get(0));  // 如果查到結果，使用第一個結果
            } else {
                entityManager.persist(song.getGenre());
            }

            // 確認 Country 是否已存在
            String findCountry = "from Country where countryName=:countryName";
            TypedQuery<Country> countryQuery = entityManager.createQuery(findCountry, Country.class);
            countryQuery.setParameter("countryName", song.getCountry().getCountryName());
            List<Country> countryList = countryQuery.getResultList();  // 使用 getResultList() 來避免異常
            if (!countryList.isEmpty()) {
                song.setCountry(entityManager.merge(countryList.get(0)));  // 合併存在的 Country
            } else {
                entityManager.persist(song.getCountry());
            }

            // 確認 Artist 是否已存在，使用傳入的 artistID 查詢 Artist
            if (song.getArtist() != null && song.getArtist().getArtistID() != null) {
                Artist artist = entityManager.find(Artist.class, song.getArtist().getArtistID());
                if (artist != null) {
                    song.setArtist(artist);
                } else {
                    // 處理未找到 artist 的情況
                    System.out.println("Artist not found: " + song.getArtist().getArtistID());
                    return -1; // 如果需要，這裡可以返回 -1 或拋出異常
                }
            }

            // 儲存 Song，並返回生成的 ID
            entityManager.persist(song);
            return song.getSongID();  // 假設 `getSongID()` 是返回保存後的 ID

        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // 儲存失敗，返回 -1
        }
    }


    @Transactional
    public boolean updateSong(Song song) {
        Song oldSong = entityManager.find(Song.class, song.getSongID());
        if (oldSong != null) {
            oldSong.setSongID(song.getSongID());
            if (!song.getSongTitle().isEmpty()) {
                oldSong.setSongTitle(song.getSongTitle());
            }
            if (song.getSongDuration() != null) {
                oldSong.setSongDuration(song.getSongDuration());
            }
            if (song.getGenre() != null) {
                oldSong.setGenre(song.getGenre());
            }
            if (song.getCountry() != null) {
                oldSong.setCountry(song.getCountry());
            }
            if (song.getAlbum() != null) {
                oldSong.setAlbum(song.getAlbum());
            }
            if (song.getArtist() != null) {
                oldSong.setArtist(song.getArtist());
            }
            if (song.getSongPath() != null) {
                oldSong.setSongPath(song.getSongPath());
            }
            if (song.getCoverImagePath() != null) {
                oldSong.setCoverImagePath(song.getCoverImagePath());
            }
            if (song.getSongInfo() != null) {
                oldSong.setSongInfo(song.getSongInfo());
            }
            if (song.getSongLastUpdate() != null) {
                oldSong.setSongLastUpdate(song.getSongLastUpdate());
            }

            // 使用 JPA 的 merge 來處理更新
            entityManager.merge(oldSong);
            return true;
        } else {
            return false; // 找不到舊的 song，返回 false
        }
    }

    /**
     * 傳入songID，添加一筆點擊紀錄
     * @param songID songID
     * @return true:增加成功, false:增加失敗
     */
    public boolean insertSongClick(int songID, int profileID){
        Song song = entityManager.find(Song.class, songID);
        ProfilesBean profile = entityManager.find(ProfilesBean.class, profileID);
        if (song != null) {
            SongClick songClick = new SongClick();
            songClick.setSong(song);
            songClick.setProfile(profile);
            entityManager.persist(songClick);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean createPlaylist(String playlistName,String playlistDescription,int profileID){
        try{
            ProfilesBean profile = entityManager.find(ProfilesBean.class, profileID);
            if (profile != null) {
                Playlist playlist = new Playlist();
                playlist.setPlaylistName(playlistName);
                playlist.setPlaylistDescription(playlistDescription);
                playlist.setProfile(profile);
                entityManager.persist(playlist);
                return true;
            }
            else{
                throw new RuntimeException("新增播放列表失敗:profileID不存在");
            }
        }catch (Exception e){
            throw new RuntimeException("新增播放列表失敗");
        }
    }

    /**
     * 傳入playlist 根據id刪除
     * @param playlistIDs playlistID集合
     * @return 成功:true, 失敗:false
     */
    public boolean deletePlaylist(List<Integer> playlistIDs){
        try {
            for (Integer playlistID : playlistIDs){
                String hql = "delete from PlaylistSong p where p.playlist.playlistID = :playlistID";
                entityManager.createQuery(hql).setParameter("playlistID", playlistID).executeUpdate();
            }
            String hql = "delete from Playlist where playlistID in (:ids)";
            entityManager.createQuery(hql).setParameter("ids", playlistIDs).executeUpdate();
            return true;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePlaylist(Integer playlistID, String playlistName, String playlistDescription){
        try{
            Playlist playlist = entityManager.find(Playlist.class, playlistID);
            if (playlist != null){
                if (playlistName != null && !playlistName.isEmpty()){
                    playlist.setPlaylistName(playlistName);
                }
                if (playlistDescription != null && !playlistDescription.isEmpty()){
                    playlist.setPlaylistDescription(playlistDescription);
                }
                return true;
            }else{
                throw new RuntimeException("找不到playlist");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 插入播放清單歌曲
     */
    public boolean insertPlaylistSong(int playlistID, int songID) {
        try {
            // 確認播放清單是否存在
            Playlist playlist = entityManager.find(Playlist.class, playlistID);
            if (playlist == null) {
                throw new RuntimeException("找不到播放清單");
            }

            // 確認歌曲是否存在
            Song song = entityManager.find(Song.class, songID);
            if (song == null) {
                throw new RuntimeException("找不到歌曲");
            }

            // 檢查歌曲是否已經在播放清單中
            Long count = entityManager.createQuery(
                            "SELECT COUNT(ps) FROM PlaylistSong ps WHERE ps.playlist.id = :playlistID AND ps.song.id = :songID",
                            Long.class)
                    .setParameter("playlistID", playlistID)
                    .setParameter("songID", songID)
                    .getSingleResult();

            if (count > 0) {
                System.out.println("歌曲已存在於播放清單中，無需插入");
                return false; // 不插入
            }

            // 獲取最後一首歌的 position，若無資料則從 0 開始
            int maxPosition = getMaxPositionByPlaylistID(playlistID);

            // 插入新歌曲到播放清單
            PlaylistSong playlistSong = new PlaylistSong();
            playlistSong.setSong(song);
            playlistSong.setSongPosition(maxPosition + 1); // 設定下一個位置
            playlistSong.setPlaylist(playlist);
            entityManager.persist(playlistSong);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 獲取播放清單中最後一筆歌曲的 position
     */
    public int getMaxPositionByPlaylistID(int playlistID) {
        try {
            String hql = "select coalesce(max(ps.songPosition), 0) from PlaylistSong ps where ps.playlist.playlistID = :playlistID";
            return entityManager.createQuery(hql, Integer.class)
                    .setParameter("playlistID", playlistID)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // 若查詢失敗，預設為 0
    }


    /**
     * 獲取playlist總數BYID
     */
    public int getPlaylistSongCountByPlaylistID(int playlistID){
        try {
            Playlist playlist = entityManager.find(Playlist.class, playlistID);
            if (playlist != null) {
                String hql = "select count(*) from PlaylistSong where playlist.playlistID=:playlistID";
                return entityManager.createQuery(hql, long.class).setParameter("playlistID", playlistID).getSingleResult().intValue();
            }else{
                throw new RuntimeException("找不到播放清單");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 傳入profiledID, songID
     * 找出此歌曲存在此用戶的哪幾個播放清單中
     * @param profileID
     * @param songID
     * @return
     */
    public List<Integer> checkSongInWhichPlaylists(int profileID, int songID){
        try {
            String hql = "select p.playlistID from Playlist p " +
                    "left join PlaylistSong ps on p.playlistID = ps.playlist.playlistID " +
                    "where p.profile.profileId = :profileID and ps.song.songID = :songID";
            TypedQuery<Integer> query = entityManager.createQuery(hql, Integer.class)
                    .setParameter("profileID", profileID)
                    .setParameter("songID", songID);

            return query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 傳入ArtistID的List，做刪除
     * @param artistIDs ArtistID的集合
     * @return 成功:true, 失敗:false
     */
    public boolean deleteArtist(List<Integer> artistIDs){
        try {
            String hql = "delete from Artist where artistID in (:ids)";
            entityManager.createQuery(hql).setParameter("ids", artistIDs).executeUpdate();
            return true;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 傳入要覆蓋的資料，若為空則不修改
     * @param songUpdateVo 要修改的資料
     * @return 成功:true, 失敗:false
     */
    public boolean updateSong(SongUpdateVo songUpdateVo) {
        try {
            Integer songID = songUpdateVo.getSongID();
            String songTitle = songUpdateVo.getSongTitle();
            Integer artistID = songUpdateVo.getArtistID();
            Integer albumID = songUpdateVo.getAlbumID();
            String songGenreName = songUpdateVo.getSongGenreName();
            String countryName = songUpdateVo.getCountryName();
            String songInfo = songUpdateVo.getSongInfo();
            String photoPath = songUpdateVo.getPhotoPath();

            // 透過SongID查詢是否有這首歌曲
            Song song = entityManager.find(Song.class, songID);
            // 如果有就進入修改
            if (song != null) {
                // 判斷Title是否為null or 空值，若是空值則不修改
                if (songTitle != null && !songTitle.trim().isEmpty()) {
                    song.setSongTitle(songTitle);
                }
                // 判斷ArtistID是否為null or 空值，若是空值則不修改
                if (artistID != null && artistID > 0) {
                    // 若非空則查找這筆artistID
                    Artist artist = entityManager.find(Artist.class, artistID);
                    // 有找到就覆蓋
                    if (artist != null) {
                        song.setArtist(artist);
                        // 沒有找到就拋出異常
                    } else {
                        throw new RuntimeException("can't find artist");
                    }
                }
                // 若genreName為空則不進行修改
                if (songGenreName != null && !songGenreName.trim().isEmpty()) {
                    //若不為空則查找這筆資料
                    String hql = "from Genre where genreName=:name";
                    List<Genre> genreList = entityManager.createQuery(hql, Genre.class)
                            .setParameter("name", songGenreName)
                            .getResultList();
                    // 有找到則直接使用
                    if (!genreList.isEmpty()) {
                        song.setGenre(genreList.get(0));
                    }
                    // 沒有找到就新增 + 使用
                    else {
                        Genre newGenre = new Genre();
                        newGenre.setGenreName(songGenreName);
                        song.setGenre(entityManager.merge(newGenre));
                    }
                }
                // 判斷有無country
                if (countryName != null && !countryName.trim().isEmpty()) {
                    String hql = "from Country where countryName=:name";
                    List<Country> countryList = entityManager.createQuery(hql, Country.class)
                            .setParameter("name", countryName)
                            .getResultList();
                    // 若有查到這筆country直接使用
                    if (!countryList.isEmpty()) {
                        song.setCountry(countryList.get(0));
                    }
                    // 若沒有查到這筆則新增 + 使用
                    else {
                        Country newCountry = new Country();
                        newCountry.setCountryName(countryName);
                        song.setCountry(entityManager.merge(newCountry));
                    }
                }

                // 判斷有無albumID
                if (albumID != null && albumID > 0) {
                    // 若非空則查找這筆artistID
                    Album album = entityManager.find(Album.class, albumID);
                    // 有找到就覆蓋
                    if (album != null) {
                        song.setAlbum(album);
                        // 沒有找到就拋出異常
                    } else {
                        throw new RuntimeException("can't find album");
                    }
                }

                // 判斷有無songInfo
                if (songInfo != null && !songInfo.trim().isEmpty()) {
                    song.setSongInfo(songInfo);
                }
                // 判斷有無photoPath
                if (photoPath != null && !photoPath.trim().isEmpty()) {
                    song.setCoverImagePath(photoPath);
                }
                song.setSongLastUpdate(new Date());
                entityManager.merge(song);
            } else {
                throw new RuntimeException("can't find song");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }


    public boolean updateArtist(ArtistUpdateVo artistUpdateVo){
        try{

            Integer artistID = artistUpdateVo.getArtistID();
            String artistName = artistUpdateVo.getArtistName();
            String countryName = artistUpdateVo.getCountryName();
            Date birthDate = artistUpdateVo.getBirthDate();
            String imagePath = artistUpdateVo.getImagePath();
            Artist artist = entityManager.find(Artist.class, artistID);
            if (artist != null) {
                if (artistName != null && !artistName.trim().isEmpty()) {
                    artist.setArtistName(artistName);
                }
                if (countryName != null && !countryName.trim().isEmpty()) {
                    String hql = "from Country where countryName=:name";
                    Country country = entityManager.createQuery(hql, Country.class).setParameter("name", countryName).getSingleResult();
                    if (country != null) {
                        artist.setCountry(country);
                    }
                    else{
                        Country newCountry = new Country();
                        newCountry.setCountryName(countryName);
                        artist.setCountry(entityManager.merge(newCountry));
                    }
                }
                if (birthDate != null) {
                    artist.setBirthDate(birthDate);
                }
                if (imagePath!=null && !imagePath.trim().isEmpty()) {
                    artist.setArtistPhoto(imagePath);
                }
            }else{
                throw new RuntimeException("can't find artist");
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 根據profilesID查詢playlist
     */
    public List<PlaylistDTO> getPlaylistByProfileID(int pageIndex, int pageSize, int profileID){
        try{
            String jpql = "select new team5.song.model.dto.PlaylistDTO(p.playlistID,p.playlistName,p.playlistDescription)" +
                    " from Playlist p where profile.profileId=:profileID";
            return entityManager.createQuery(jpql, PlaylistDTO.class)
                    .setParameter("profileID", profileID)
                    .setMaxResults(pageSize)
                    .setFirstResult((pageIndex - 1) * pageSize)
                    .getResultList();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Long getPlaylistCountByProfileID(int profileID){
        try{
            String hql = "select count (*) from Playlist where profile.profileId=:profileID";
            return entityManager.createQuery(hql, long.class)
                    .setParameter("profileID", profileID)
                    .getSingleResult();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 傳入playlistID返回所有歌曲
     * @param playlistID
     * @return
     */
    public List<PlaylistSongDTO> getPlaylistSongByPlaylistID(int playlistID) {
        try {
            String jpql =
                    "select new team5.song.model.dto.PlaylistSongDTO (" +
                            "s.songID, s.songTitle, s.songDuration, " +
                            "coalesce(s.genre.genreName, ''), " +
                            "coalesce(s.country.countryName, ''), " +
                            "coalesce(s.album.albumName, ''), " +
                            "coalesce(s.artist.artistName, ''), " +
                            "s.songPath, s.coverImagePath, " +
                            "s.songInfo, s.songCreateAt, p.songPosition) " +
                            "from PlaylistSong p " +
                            "left join Song s on p.song.songID = s.songID " +
                            "left join s.genre " +
                            "left join s.country " +
                            "left join s.album " +
                            "left join s.artist " +
                            "where s.songIsDeleted = false and p.playlist.playlistID = :playlistID order by p.songPosition";

            return entityManager.createQuery(jpql, PlaylistSongDTO.class)
                    .setParameter("playlistID", playlistID)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching playlist songs by playlist ID", e);
        }
    }

    public boolean deletePlaylistSong(int playlistID, List<Integer> songIDs){
        try{
            Playlist playlist = entityManager.find(Playlist.class, playlistID);
            if (playlist != null){
                String hql = "delete from PlaylistSong p where p.song.songID in (:songIDs) and p.playlist.playlistID = :playlistID";
                int i = entityManager.createQuery(hql)
                        .setParameter("playlistID",playlistID)
                        .setParameter("songIDs", songIDs)
                        .executeUpdate();
                return i>0;
            }else{
                throw new RuntimeException("找不到playlist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 傳入條件查詢參數，返回排行榜
     * @param songRankQueryVo
     * @return
     */
    public List<SongRankDTO> getSongRank(SongRankQueryVo songRankQueryVo){
        try {
            String genreName = songRankQueryVo.getGenreName();
            String countryName = songRankQueryVo.getCountryName();
            Integer day = songRankQueryVo.getDay();
            Integer count = songRankQueryVo.getCount();  // 用來限制資料的數量
            String artistName = songRankQueryVo.getArtistName();

            StringBuilder whereClause = new StringBuilder("WHERE s.songIsDeleted = false ");

            // 動態增加條件
            if (genreName != null && !genreName.isEmpty()) {
                whereClause.append("AND s.genre.genreName = :genreName ");
            }
            if (countryName != null && !countryName.isEmpty()) {
                whereClause.append("AND s.country.countryName = :countryName ");
            }
            if (artistName != null && !artistName.isEmpty()) {
                whereClause.append("AND s.artist.artistName = :artistName ");
            }
            if (day != null) {
                whereClause.append("AND DATEDIFF(hour, sc.songClickDate, CURRENT_TIMESTAMP) <= :hour ");
            }

            String jpql =
                    "SELECT new team5.song.model.dto.SongRankDTO(" +
                            "sc.song.songID, " +
                            "s.songTitle, " +
                            "s.songDuration, " +
                            "COALESCE(s.genre.genreName, ''), " +
                            "COALESCE(s.country.countryName, ''), " +
//                            "COALESCE(s.album.albumName, ''), " +
                            "COALESCE(s.artist.artistName, ''), " +
                            "s.songPath, " +
                            "s.coverImagePath, " +
                            "s.songInfo, " +
                            "s.songCreateAt, " +
                            "COUNT(sc.song.songID)) " +
                            "FROM SongClick sc " +
                            "LEFT JOIN sc.song s " +
                            whereClause +  // 添加 WHERE 子句
                            "GROUP BY sc.song.songID, " +
                            "s.songTitle, " +
                            "s.songDuration, " +
                            "COALESCE(s.genre.genreName, ''), " +
                            "COALESCE(s.country.countryName, ''), " +
//                            "COALESCE(s.album.albumName, ''), " +
                            "COALESCE(s.artist.artistName, ''), " +
                            "s.songPath, " +
                            "s.coverImagePath, " +
                            "s.songInfo, " +
                            "s.songCreateAt " +
                            "ORDER BY COUNT(sc.song.songID) DESC";

            var query = entityManager.createQuery(jpql, SongRankDTO.class);

            // 設置條件參數
            if (genreName != null && !genreName.isEmpty()) {
                query.setParameter("genreName", genreName);
            }
            if (countryName != null && !countryName.isEmpty()) {
                query.setParameter("countryName", countryName);
            }
            if (artistName != null && !artistName.isEmpty()) {
                query.setParameter("artistName", artistName);
            }
            if (day != null) {
                query.setParameter("hour", day*24);
            }

            // 設定結果數量限制
            if (count != null && count > 0) {
                query.setMaxResults(count);
            }

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching song ranks by song clicks", e);
        }

    }

    /**
     * 傳入profileID，返回近10次觀看紀錄
     */
    public List<SongPlayHistoryDTO> getHistoryByProfileID(int profileID) {
        try {
            String hql =
                    "select new team5.song.model.dto.SongPlayHistoryDTO(" +
                            "s.songID, " +
                            "s.songTitle, " +
                            "s.songDuration, " +
                            "COALESCE(s.genre.genreName, ''), " +
                            "COALESCE(s.country.countryName, ''), " +
                            "s.artist.artistName, " +
                            "s.songPath, " +
                            "s.coverImagePath, " +
                            "s.songInfo, " +
                            "s.songCreateAt, " +
                            "MAX(sc.songClickDate)) " +  // 使用 MAX 來獲取最新的播放紀錄日期
                            "from SongClick sc " +
                            "left join sc.song s " +
                            "where sc.profile.profileId = :profileID " +
                            "group by s.songID, s.songTitle, s.songDuration, s.genre.genreName, s.country.countryName, " +
                            "s.artist.artistName, s.songPath, s.coverImagePath, " +
                            "s.songInfo, s.songCreateAt " +
                            "order by MAX(sc.songClickDate) desc ";  // 按最新的播放紀錄日期排序

            return entityManager.createQuery(hql, SongPlayHistoryDTO.class)
                    .setParameter("profileID", profileID)
                    .setMaxResults(10)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

