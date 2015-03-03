package areas.user.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="account")
public class Account {
	@Id
	@GenericGenerator(name="systemUUID", strategy="uuid") 
	@GeneratedValue(generator="systemUUID")
	private String Id;
	
	@Column(name="username")
	//@Size(min=3, max=20)
	//@Pattern(regexp="[a-zA-Z0-9]*")
	private String username;
	
	@Column(name="password")
	//@Size(min=6, max=20)
	private String password;
	
	//@Pattern(regexp="@")
	@Column(name="email")
	private String email;
	
	@Column(name="registerTime")
	private Long registerTime;
	
	
	@Transient
	private String registerTimeStr;
	

	
	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Profile profile;
	

	@ManyToOne
	@JoinColumn(name="roleId", referencedColumnName="id")
	private Role role;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Long registerTime) {
		this.registerTime = registerTime;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	
	public Role getRole() {
		return this.role; 
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public String getRegisterTimeStr() {
		Date d = new Date(this.registerTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(d);
	}

	public void setRegisterTimeStr(String registerTimeStr) {
		this.registerTimeStr = registerTimeStr;
	}

	
	
	
}
