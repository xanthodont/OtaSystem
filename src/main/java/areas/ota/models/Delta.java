package areas.ota.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	@Column(name="size")
	private int size;
	
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
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
}
