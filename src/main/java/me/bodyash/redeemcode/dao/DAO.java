package me.bodyash.redeemcode.dao;

public interface DAO {
	
	public void addCode(String type, String code);
	public boolean removeCode(String code);
	public boolean checkCode(String code);
	public void removeAllCodes();
	public String getCodeType(String code);
	

}
