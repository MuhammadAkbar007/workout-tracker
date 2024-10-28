package uz.akbar.workoutTracker.payload;

/**
 * JwtDto
 */
public class JwtDto {

	private String username;
	private String role;

	public JwtDto(String username, String role) {
		this.username = username;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role;
	}
}
