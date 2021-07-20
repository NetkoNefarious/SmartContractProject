package faks.Jakov.SmartContractProject.Model;

public class TransferRequest {
	private String destAddress;
	
	// Ether
	private String ether = "0";
	private String szabo = "0";
	private String mwei = "0";
	private String wei = "0";
	
	// Getters and setters
	public String getDestAddress() {
		return destAddress;
	}
	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}
	public String getEther() {
		return ether;
	}
	public void setEther(String ether) {
		this.ether = ether;
	}
	public String getSzabo() {
		return szabo;
	}
	public void setSzabo(String szabo) {
		this.szabo = szabo;
	}
	public String getMwei() {
		return mwei;
	}
	public void setMwei(String mwei) {
		this.mwei = mwei;
	}
	public String getWei() {
		return wei;
	}
	public void setWei(String wei) {
		this.wei = wei;
	}
}
