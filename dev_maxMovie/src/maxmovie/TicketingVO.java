package maxmovie;
/********************************************************************************
 * TicketingVO : 클라이언트에서 결제할 때 Datebase에 정보를 주기 위한 VO
 * 
 * @author kosmo_06
 *
 ********************************************************************************/
public class TicketingVO {
	String command      = null;
	String protocol     = null;
	String mem_id         = null; 
	int    scr_code       = 0; 
	int    theater_code   = 0; 
	int    movie_code     = 0; 
	String show_date      = null; 
	String show_time      = null; 
	int    ticketing_code = 0; 
	String pay_status     = null; 
	String seat_code      = null;
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
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public int getScr_code() {
		return scr_code;
	}
	public void setScr_code(int scr_code) {
		this.scr_code = scr_code;
	}
	public int getTheater_code() {
		return theater_code;
	}
	public void setTheater_code(int theater_code) {
		this.theater_code = theater_code;
	}
	public int getMovie_code() {
		return movie_code;
	}
	public void setMovie_code(int movie_code) {
		this.movie_code = movie_code;
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
	public int getTicketing_code() {
		return ticketing_code;
	}
	public void setTicketing_code(int ticketing_code) {
		this.ticketing_code = ticketing_code;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getSeat_code() {
		return seat_code;
	}
	public void setSeat_code(String seat_code) {
		this.seat_code = seat_code;
	}
}
