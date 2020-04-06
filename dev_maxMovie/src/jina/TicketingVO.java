package jina;
/********************************************************************************
 * TicketingVO : 클라이언트에서 결제할 때 Database에 정보를 주기 위한 VO
 * @author kosmo_06
 ********************************************************************************/
public class TicketingVO {
	private String command      	= null;
	private String mem_id 			= null;//아이디
	private String movie_name		= null;//영화 이름
	private String movie_age		= null;//영화 연령
	private String loc 				= null;//지역
	private String theater			= null;//지점
	private String movie_screen		= null;//상영관
	private String movie_date		= null;//날짜
	private String movie_time 		= null;//시간
	private String screen_seat		= null;//좌석
	private String pay_status    	= null;//결제 상태 
	private String result 		 	= null;
	private int    ticketing_code = 0; //예매코드

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
	public int getTicketing_code() {
		return ticketing_code;
	}
	public void setTicketing_code(int ticketing_code) {
		this.ticketing_code = ticketing_code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
