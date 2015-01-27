package areas.user.models;



import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;




@Entity
@Table(name="role")
public class Role {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="roleName", length=50)
	private String roleName;
	
	@Column(name="privilege")
	private int privilege;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="account_roleId", referencedColumnName="id")
	private Set<Account> accounts; 
	
 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	
	
	public Set<Account> getAccounts() {
		return this.accounts;
	}
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
}
