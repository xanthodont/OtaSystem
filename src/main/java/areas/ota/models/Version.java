package areas.ota.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="version")
public class Version {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(name="buildNumber", length=100)
	private String buildNumber;
	
	@Column(name="versionName", length=100)
	private String versionName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="createTime") 
	private long createTime;
	@Transient
	private String createTimeStr;
	
	@Column(name="publishTime")
	private long publishTime;
	
	@Column(name="status")
	private int status;
	
	@Column(name="androidVersion", length=20)
	private String androidVersion;
	
	@Column(name="fingerprint", length=255)
	private String fingerprint;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="projectId", referencedColumnName="id")
	private Project project; 
	
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="fromVersionId", referencedColumnName="id", updatable=false)
	private Set<Delta> fromDeltas = new HashSet<Delta>();
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="toVersionId", referencedColumnName="id", updatable=false)
	private Set<Delta> toDeltas = new HashSet<Delta>();
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


	public Set<Delta> getFromDeltas() {
		return fromDeltas;
	}

	public void setFromDeltas(Set<Delta> fromDeltas) {
		this.fromDeltas = fromDeltas;
	}

	public Set<Delta> getToDeltas() {
		return toDeltas;
	}

	public void setToDeltas(Set<Delta> toDeltas) {
		this.toDeltas = toDeltas;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getCreateTimeStr() {
		Date d = new Date(this.createTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(d);
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
}
