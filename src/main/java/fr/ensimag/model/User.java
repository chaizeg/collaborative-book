package fr.ensimag.model;
public class User{

    private int id;
    private String userName;

    public User(int id, String uname) {
        this.setId(id);
        this.setUserName(uname);
    }

    /**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}