package team5.profile.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team5.profile.model.bean.RolesBean;

@Repository
public interface RolesRepository extends JpaRepository<RolesBean, String> {

	RolesBean findByRoleName(String roleName);
	
}
