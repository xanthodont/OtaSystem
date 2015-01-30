package models;

public class LoginResponse {
	public String status;
	public String serverVersion;
	public String rand;
	public String sessionId;
	
	public LoginResponse() {}
	public LoginResponse(String status, String serverVersion, String rand, String sessionId) {
		this.status = status;
		this.serverVersion = serverVersion;
		this.rand = rand;
		this.sessionId = sessionId;
	}
	
}
