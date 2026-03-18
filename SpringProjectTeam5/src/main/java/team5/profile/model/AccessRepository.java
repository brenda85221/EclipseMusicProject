package team5.profile.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team5.profile.model.bean.AccessBean;

@Repository
public interface AccessRepository extends JpaRepository<AccessBean, String> {

}
