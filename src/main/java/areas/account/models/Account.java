package areas.account.models;

import javax.persistence.*;


import org.hibernate.annotations.GenericGenerator;

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
	
	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Profile profile;

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
	
}
