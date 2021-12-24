package org.wipf.jasmarty.datatypes;

/**
 * Basis für alles weitere
 * 
 * @author Wipf
 *
 */
public class BaseType {

	private boolean isCrypt;
	private Integer nChange;
	private Integer nCreate;
	private String sChangeName;
	private String sCreateName;

	/**
	 * @return
	 */
	public boolean isCrypt() {
		return isCrypt;
	}

	/**
	 * @param isCrypt
	 */
	public void setCrypt(boolean isCrypt) {
		this.isCrypt = isCrypt;
	}

	/**
	 * @return
	 */
	public Integer getChange() {
		return nChange;
	}

	/**
	 * @param change
	 */
	public void setChange(Integer change) {
		this.nChange = change;
	}

	/**
	 * @return
	 */
	public Integer getCreate() {
		return nCreate;
	}

	/**
	 * @param create
	 */
	public void setCreate(Integer create) {
		this.nCreate = create;
	}

	public String getChangeName() {
		return sChangeName;
	}

	public void setChangeName(String changeName) {
		this.sChangeName = changeName;
	}

	public String getCreateName() {
		return sCreateName;
	}

	public void setCreateName(String createName) {
		this.sCreateName = createName;
	}

}
