package hjho;

public class AdminShowtimeVO {
	//현재 접속한 상영관의 정보를 담기 위한 VO
	//처음 로그인 성공할때 한번 객체 생성 이후 지우지 않는다.
	private String id = null;           //지점아이디 
	private String name = null;			//사용자이름 
	private String theaterloc = null;   //지점의 지역
	private String theatername = null;  //지점이름 
	
	//상영 시간표를 넘기기위한 VO
	private String msg = null;        //메세지 전달을 위한 변수
	private String movieTitle = null; //영화제목
	private String scrName = null;    //상영관 이름
	private String date = null;       //날짜 "20200324"
	private String yy = null;         //연도 "2020"
	private String mm = null;         //월 "03"
	private String dd = null;         //일 "24"
	private String time = null;       //시간 "09:00"
	private String hh24 = null;       //시 "09"
	private String mi = null;         //분 "00"
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTheaterloc() {
		return theaterloc;
	}
	public void setTheaterloc(String theaterloc) {
		this.theaterloc = theaterloc;
	}
	public String getTheatername() {
		return theatername;
	}
	public void setTheatername(String theatername) {
		this.theatername = theatername;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public String getScrName() {
		return scrName;
	}
	public void setScrName(String scrName) {
		this.scrName = scrName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getYy() {
		return yy;
	}
	public void setYy(String yy) {
		this.yy = yy;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getDd() {
		return dd;
	}
	public void setDd(String dd) {
		this.dd = dd;
	}
	public String getHh24() {
		return hh24;
	}
	public void setHh24(String hh24) {
		this.hh24 = hh24;
	}
	public String getMi() {
		return mi;
	}
	public void setMi(String mi) {
		this.mi = mi;
	}
	
}
