package team5.profile.model;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team5.profile.model.bean.ProfilesBean;

@Repository
public interface ProfilesRepository extends JpaRepository<ProfilesBean, Integer> {

	// 自訂查詢方法，根據 acct 欄位查詢
    public ProfilesBean findByAcctAndPwd(String acct,String pwd);

	public ProfilesBean findByAcct(String acct);

	public Optional<ProfilesBean> findByEmail(String email);

//	public ProfilesBean findByProfileId(Integer profileId);
//    @Query("FROM Profiles where acct = :acct and pwd = :pwd")
//    Optional<ProfilesBean> checkLogin(@Param("acct") String acct,@Param("pwd") String pwd);
}
