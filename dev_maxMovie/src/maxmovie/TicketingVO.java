package maxmovie;
/********************************************************************************
 * TicketingVO : 클라이언트에서 결제할 때 Datebase에 정보를 주기 위한 VO
 * 
 * @author kosmo_06
 *
 */
public class TicketingVO {
	String command      	= null;
	String mem_id 			= null;//아이디
	String movie_name		= null;//영화 이름
	String movie_age		= null;//영화 연령
	String loc 				= null;//지역
	String theater			= null;//지점
	String movie_screen		= null;//상영관
	String movie_date		= null;//날짜
	String movie_time 		= null;//시간
	String screen_seat		= null;//좌석
	String pay_status    	= null;//결제 상태 
	String ticketting_code	= null;//예매코드
	String result 		 	= null;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getMovie_name() {
		return movie_name;
	}
	public void setMovie_name(String movie_name) {
		this.movie_name = movie_name;
	}
	public String getMovie_age() {
		return movie_age;
	}
	public void setMovie_age(String movie_age) {
		this.movie_age = movie_age;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getTheater() {
		return theater;
	}
	public void setTheater(String theater) {
		this.theater = theater;
	}
	public String getMovie_screen() {
		return movie_screen;
	}
	public void setMovie_screen(String movie_screen) {
		this.movie_screen = movie_screen;
	}
	public String getMovie_date() {
		return movie_date;
	}
	public void setMovie_date(String movie_date) {
		this.movie_date = movie_date;
	}
	public String getMovie_time() {
		return movie_time;
	}
	public void setMovie_time(String movie_time) {
		this.movie_time = movie_time;
	}
	public String getScreen_seat() {
		return screen_seat;
	}
	public void setScreen_seat(String screen_seat) {
		this.screen_seat = screen_seat;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getTicketting_code() {
		return ticketting_code;
	}
	public void setTicketting_code(String ticketting_code) {
		this.ticketting_code = ticketting_code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
