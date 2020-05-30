package Items;

public class User {

	
	private String password,lastname,firstname,username;
	

	public User(String username, String firstname, String lastname, String password) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		
	}
	
	public String toString() {	
		return "Username: "+this.username +" Firstname: "+this.firstname +" Lastname: "+this.lastname +" Password: "+ this.password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public String getPassword() {
		return this.password;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass()!=o.getClass()) {
			return false;
		}
		
		User user = (User) o;
		
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(username, user.username)
				.append(firstname, user.firstname)
				.append(lastname, user.lastname)
				.append(password, user.password)
				.isEquals();
		
	}	
}
