package cmov.pa;

public class User {
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public boolean isDoctor() {
		return isDoctor;
	}


	public void setDoctor(String type) {
		if(type.equals("Doctor"))
			this.isDoctor = true;
		else
			this.isDoctor = false;
				
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public String getUsername() {
		return username;
	}


	public String getName() {
		return name;
	}


	public String getAddress() {
		return address;
	}


	public String getBirthDate() {
		return birthDate;
	}


	public String getPhoto() {
		return photo;
	}
	
	
	public String toString(){
		return name;
	}


	private String sex;
	private String username;
	private String name;
	private String address;
	private String birthDate;
	private String photo;
	private boolean isDoctor;
	private String id;
	
}
