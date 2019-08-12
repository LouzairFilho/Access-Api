package br.com.access.api.model.DTO;

public class DashboardDTO {

	private String ip;
	private String userAgent;
	private Long quantidade;

	
	public DashboardDTO() {
		
	}

	public DashboardDTO(String ip, String userAgent, Long quantidade) {
		this.ip = ip;
		this.userAgent = userAgent;
		this.quantidade = quantidade;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	
	
}
