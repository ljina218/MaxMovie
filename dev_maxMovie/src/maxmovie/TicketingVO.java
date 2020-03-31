package maxmovie;
/********************************************************************************
 * TicketingVO : 클라이언트에서 결제할 때 Database에 정보를 주기 위한 VO
 * @author kosmo_06
 ********************************************************************************/
public class TicketingVO {
	private String command      = null;
	private String protocol     = null;
	private int    ticketing_code = 0; 
	private String mem_id         = null; 
	private String movie_title  = null;
	private String theater_name = null;
	private String theater_loc  = null;
	private String scr_name     = null;
	private String show_date      = null; 
	private String show_time      = null; 
	private String pay_status     = null;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getTicketing_code() {
		return ticketing_code;
	}
	public void setTicketing_code(int ticketing_code) {
		this.ticketing_code = ticketing_code;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getMovie_title() {
		return movie_title;
	}
	public void setMovie_title(String movie_title) {
		this.movie_title = movie_title;
	}
	public String getTheater_name() {
		return theater_name;
	}
	public void setTheater_name(String theater_name) {
		this.theater_name = theater_name;
	}
	public String getTheater_loc() {
		return theater_loc;
	}
	public void setTheater_loc(String theater_loc) {
		this.theater_loc = theater_loc;
	}
	public String getScr_name() {
		return scr_name;
	}
	public void setScr_name(String scr_name) {
		this.scr_name = scr_name;
	}
	public String getShow_date() {
		return show_date;
	}
	public void setShow_date(String show_date) {
		this.show_date = show_date;
	}
	public String getShow_time() {
		return show_time;
	}
	public void setShow_time(String show_time) {
		this.show_time = show_time;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	} 
	
	
	
}
