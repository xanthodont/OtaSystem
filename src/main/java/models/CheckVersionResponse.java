package models;

public class CheckVersionResponse {
	public String status;
	public String name;
	public long size;
	public String android_version;
	public String release_notes;
	public long versionId;
	//public String fingerprint;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getAndroid_version() {
		return android_version;
	}
	public void setAndroid_version(String android_version) {
		this.android_version = android_version;
	}
	public String getRelease_notes() {
		return release_notes;
	}
	public void setRelease_notes(String release_notes) {
		this.release_notes = release_notes;
	}
	public long getVersionId() {
		return versionId;
	}
	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}
	
	/*
	public long getDeltaId() {
		return deltaId;
	}
	public void setDeltaId(long deltaId) {
		this.deltaId = deltaId;
	}
	public String getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	*/
	
}
