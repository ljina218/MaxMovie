package jina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


public class MovieServerThread extends Thread{
	
	//id와 nickname 저장
	//String id = null;
	//String nickname = null;
	
	//영화정보 셋팅할 변수
	TicketingVO tVO = null;
	
	//컨트롤러 생성
	//MovieController ctrl = new MovieController();
	
	//스레드 선언부
	MovieServer ms = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	boolean stop = false;
	
	//로그파일 생성을 위한 선언부
	String logpath = "src\\thread\\chat\\";
	String fileName = null;

	//생성자
	public MovieServerThread(MovieServer ms) {
		this.ms = ms;
		ms.globalist.add(this);
		ms.jta_log.append(ms.globalist.size()+"\n");
		//ms.movieList = ctrl.dao.refreshMovieAll(ms.movieList);//서버에 3일치 영화정보 저장
		try {
			oos = new ObjectOutputStream(ms.socket.getOutputStream());
			ois = new ObjectInputStream(ms.socket.getInputStream());
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
	//oos 하는 메소드
	public void send(String msg) {
		try {
			oos.writeObject(msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//영화 시간 조건 메소드
	public int checkTime (String time) {//스케줄에 있는 영화 시간 받아서
		int result = 0;
		StringTokenizer token = new StringTokenizer(time, ":");//시와 분으로 쪼개기
		char[] scheduledhour = token.nextToken().toCharArray();
		char[] scheduledmin = token.nextToken().toCharArray();
		String sethour = null;
		String setmin = null;
		if(scheduledhour[0]==0){//시 앞에 붙은 0 떼어내기
			sethour = scheduledhour[1]+"";
		}else {
			sethour = scheduledhour[0]+""+scheduledhour[1]; 
		}
		if(scheduledmin[0]==0){//분 앞에 붙은 0 떼어내기
			setmin = scheduledmin[1]+"";
		}else {
			setmin = scheduledmin[0]+""+scheduledmin[1]; 
		}
		int hour = Integer.parseInt(sethour);//시와 분을 string에서 int 형으로 바꾸기
		int min = Integer.parseInt(setmin);
		Calendar now = Calendar.getInstance();//현재 시간에서 
		now.add(Calendar.MINUTE, -30);//30분 빼기 => 30분 전까지만 예매가능
		int nowhour = now.get(Calendar.HOUR_OF_DAY);
		int nowmin = now.get(Calendar.MINUTE);
		if(hour>=nowhour) {//시가 아직 안지났고
			if(min>=nowmin) {//분이 아직 안지났다면,
				result=1;
			}
		}
		return result;
	}
	
	//영화 선택 리스트 전달 메소드
	public void choiceMoive(int MovieProtocol) {
		for(int i=0; i<ms.movieList.size(); i++) {
			String title = ms.movieList.get(i).get("M_TITLE").toString();
			String loc = ms.movieList.get(i).get("T_LOC").toString();
			String theater = ms.movieList.get(i).get("T_NAME").toString();
			String date = ms.movieList.get(i).get("S_DATE").toString();
			String time = ms.movieList.get(i).get("S_TIME").toString();
			String screen = ms.movieList.get(i).get("SC_NAME").toString();
			int result = checkTime(time);
			if(result==1) {//상영시간이 30분 전이고....
				if(tVO.getMovie_name().equals(title)||
						tVO.getLoc().equals(loc)||
							tVO.getTheater().equals(theater)||
								tVO.getMovie_date().equals(date)||
									tVO.getMovie_time().equals(time)||
										tVO.getMovie_screen().equals(screen)){
					String msg = MovieProtocol+"#"+title+"#"+
							loc+"#"+theater+"#"+date+"#"+time+"#"+screen;
					this.send(msg);	
				}
			}
		}
	}
		
	//이벤트로부터 넘어온 메세지를 듣고 말하는 메소드
	@Override
	public void run() {
		int num = 0;
		while(!stop) {
			/****************************************************************************
			 * 메세지 듣고 말하는 곳
			 */
			try {
				String msg = (String) ois.readObject();
				ms.jta_log.append(msg+"\n");
				int protocol = 0;
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				case MovieProtocol.SELECT:{//MovieChoiceView 호출
					//오늘의 영화정보 list 선언부
					/* List<Map<String, Object>> movieList = null;
					 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
						showMap.put("T_LOC", rs.getString("T_LOC"));
						showMap.put("T_NAME", rs.getString("T_NAME"));
						showMap.put("S_DATE", rs.getString("S_DATE"));
						showMap.put("S_TIME", rs.getString("S_TIME"));
						showMap.put("SC_NAME", rs.getString("SC_NAME"));
					 */
					tVO = null;
					tVO = new TicketingVO();
					ms.movieList = new Vector<Map<String,Object>>();
					Map<String, Object> showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "15");
					showMap.put("M_TITLE", "그녀");
					showMap.put("T_LOC", "서울");
					showMap.put("T_NAME", "강남");
					ms.movieList.add(showMap);
					//
					showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "15");
					showMap.put("M_TITLE", "그녀");
					showMap.put("T_LOC", "서울");
					showMap.put("T_NAME", "강남");
					ms.movieList.add(showMap);
					//
					showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "15");
					showMap.put("M_TITLE", "그녀");
					showMap.put("T_LOC", "서울");
					showMap.put("T_NAME", "강남");
					ms.movieList.add(showMap);
					//
					showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "12");
					showMap.put("M_TITLE", "이보다더 좋을 수 없다.");
					showMap.put("T_LOC", "서울");
					showMap.put("T_NAME", "강남");
					ms.movieList.add(showMap);
					//
					showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "12");
					showMap.put("M_TITLE", "이보다더 좋을 수 없다.");
					showMap.put("T_LOC", "서울");
					showMap.put("T_NAME", "명동");
					ms.movieList.add(showMap);
					//
					showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "12");
					showMap.put("M_TITLE", "이보다더 좋을 수 없다.");
					showMap.put("T_LOC", "부산");
					showMap.put("T_NAME", "해운대");
					ms.movieList.add(showMap);
					//
					showMap = new HashMap<String, Object>();
					showMap.put("M_CERTIF", "12");
					showMap.put("M_TITLE", "이보다더 좋을 수 없다.");
					showMap.put("T_LOC", "부산");
					showMap.put("T_NAME", "해운대");
					ms.movieList.add(showMap);
					//
					for(int i=0; i<ms.movieList.size(); i++) {
						String age = ms.movieList.get(i).get("M_CERTIF").toString();
						String title = ms.movieList.get(i).get("M_TITLE").toString();
						String loc = ms.movieList.get(i).get("T_LOC").toString();
						String theater = ms.movieList.get(i).get("T_NAME").toString();
						String movielist_msg = MovieProtocol.SELECT+"#"+age+"#"+title+"#"+
									loc+"#"+theater;
						System.out.println(movielist_msg);
							this.send(movielist_msg);
					}
				}break;
				case MovieProtocol.SELECT_MOVIE:{//영화선택
					String choiceMovie = st.nextToken();
					if(choiceMovie!=null){
						tVO.setMovie_name(choiceMovie);
					}
					choiceMoive(MovieProtocol.SELECT_MOVIE);
				}break;
				case MovieProtocol.SELECT_LOCAL:{//지역선택
					String choiceLoc = st.nextToken();
					if(choiceLoc!=null){
						tVO.setLoc(choiceLoc);
					}
					choiceMoive(MovieProtocol.SELECT_LOCAL);
				}break;
				case MovieProtocol.SELECT_SCR:{//지점명선택
					String choiceTheater = st.nextToken();
					if(choiceTheater!=null){
						tVO.setTheater(choiceTheater);
					}
					choiceMoive( MovieProtocol.SELECT_SCR);
				}break;
				case MovieProtocol.SELECT_DATE:{//관, 시간선택
					String choiceScr = st.nextToken();
					String choiceTime = st.nextToken();
					if(choiceTime!=null&&choiceScr!=null){
						tVO.setMovie_time(choiceTime);
						tVO.setMovie_screen(choiceScr);
					}
					choiceMoive(MovieProtocol.SELECT_DATE);
				}break;
				}
			} catch (ClassNotFoundException e) {
				System.out.println(e.toString());
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.println(e.toString());
				//e.printStackTrace();
			}
		}	
	}
	
	
}
