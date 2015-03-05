package areas.ota.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.text.DateFormatter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import areas.user.models.Account;

@Entity
@Table(name="project")
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="projectName", length=100)
	private String projectName;
	
	@Column(name="oem", length=20)
	private String oem;
	
	@Column(name="product", length=100)
	private String product;
	
	@Column(name="language", length=20)
	private String language;
	
	@Column(name="operator", length=50)
	private String operator;
	
	@Column(name="updateTime")
	private long updateTime;
	@Transient
	private String updateTimeStr;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(targetEntity=Version.class, cascade=CascadeType.REMOVE)
	@JoinColumn(name="projectId", updatable=false)
	@Fetch(FetchMode.JOIN)
	@JsonIgnore
	private Set<Version> versions = new HashSet<Version>(); 
	
	/**
	 * 默认取值
	 */
	public Project() {
		projectName = oem = language = product = operator = description = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Version> getVersions() {
		return versions;
	}

	public void setVersions(Set<Version> versions) {
		this.versions = versions;
	}

	public String getUpdateTimeStr() {
		Date d = new Date(this.updateTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(d);
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
}
