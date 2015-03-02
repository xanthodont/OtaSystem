package areas.ota.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="delta")
public class Delta {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name="fromVersionId", referencedColumnName="id")
	private Version fromVersion;
	
	@ManyToOne
	@JoinColumn(name="toVersionId", referencedColumnName="id")
	private Version toVersion;
	
	@Column(name="updateTime")
	private long updateTime;
	@Transient
	private String updateTimeStr;
	
	@Column(name="size")
	private long size;
	
	@Column(name="status")
	private int status;
	
	@Column(name="filePath", length=255)
	private String filePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Version getFromVersion() {
		return fromVersion;
	}

	public void setFromVersion(Version fromVersion) {
		this.fromVersion = fromVersion;
	}

	public Version getToVersion() {
		return toVersion;
	}

	public void setToVersion(Version toVersion) {
		this.toVersion = toVersion;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getUpdateTimeStr() {
		Date d = new Date(this.updateTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(d);
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	
	public class Status {
		public static final int INIT = 0;
		public static final int TESTING = 2;
		public static final int PUBLISHING = 4;
		public static final int PUBLISHED = 5;
	} 
}
