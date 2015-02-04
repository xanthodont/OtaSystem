package models;

public class LoginResponse {
	public String status;
	public String server_version;
	public String rand;
	public String sessionId;
	
	public LoginResponse() {}
	public LoginResponse(String status, String serverVersion, String rand, String sessionId) {
		this.status = status;
		this.server_version = serverVersion;
		this.rand = rand;
		this.sessionId = sessionId;
	}
	
}
