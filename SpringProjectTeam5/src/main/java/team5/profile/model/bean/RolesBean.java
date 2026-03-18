package team5.profile.model.bean;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "roles")
@Component
@JsonIgnoreProperties({"roleDescr", "roleStatus","access"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roleName")
public class RolesBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "roleName", unique = true, nullable = false)
    private String roleName;

    @Column(name = "roleDescr")
    private String roleDescr;

    @Column(name = "roleStatus", nullable = false)
    private Integer roleStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rolesBean", cascade = CascadeType.ALL)
//    @JsonBackReference // 反向關聯，這是 "子" 端,防止循環依賴
    private Set<ProfilesBean> profiles = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
    	    name = "role_access",
    	    joinColumns = @JoinColumn(name = "roleName", referencedColumnName = "roleName"),
    	    inverseJoinColumns = @JoinColumn(name = "accessType", referencedColumnName = "accessType")
    	)
    private Set<AccessBean> access = new HashSet<>();
    
    // 默認構造函數
    public RolesBean() {
    }

    // 帶有 roleName 和 roleDescr 參數的構造函數
    public RolesBean(String roleName, String roleDescr) {
        this.roleName = roleName;
        this.roleDescr = roleDescr;
        this.roleStatus = 1;  // 默認 roleStatus 為 1 (active)
    }
    
    // Getters and setters
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescr() {
        return roleDescr;
    }

    public void setRoleDescr(String roleDescr) {
        this.roleDescr = roleDescr;
    }

    public Integer getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(Integer roleStatus) {
        this.roleStatus = roleStatus;
    }

    public Set<ProfilesBean> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<ProfilesBean> profiles) {
        this.profiles = profiles;
    }

    public Set<AccessBean> getAccess() {
        return access;
    }

    public void setAccess(Set<AccessBean> access) {
        this.access = access;
    }
}
