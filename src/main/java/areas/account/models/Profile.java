package areas.account.models;

import javax.persistence.*;

@Entity
@Table(name="profile")
public class Profile {
	@Id
	private String id;
	
	@Column(name="nickName")
	private String nickName;
	
	@Column(name="realName")
	private String realName;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="country")
	private String country;
	
	@Column(name="about")
	private String about;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	
}
