package team5.profile.model.bean;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "access")
@Component
public class AccessBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "accessType", unique = true, nullable = false)
    private String accessType;

    @Column(name = "accessDescr", nullable = false)
    private String accessDescr;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "role_access",
        joinColumns = @JoinColumn(name = "accessType", referencedColumnName = "accessType"),
        inverseJoinColumns = @JoinColumn(name = "roleName", referencedColumnName = "roleName")
    )
    private Set<RolesBean> roles = new HashSet<>();

    // Getters and setters
    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getAccessDescr() {
        return accessDescr;
    }

    public void setAccessDescr(String accessDescr) {
        this.accessDescr = accessDescr;
    }

    public Set<RolesBean> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesBean> roles) {
        this.roles = roles;
    }
}
