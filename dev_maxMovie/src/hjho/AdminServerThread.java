package hjho;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class AdminServerThread  extends Thread {
	public AdminServer as = null;
	Socket admin = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	String adminID = null;//현재서버에 입장한 클라이언트 스레드 ID 저장
	List<AdminShowtimeVO> astVO = null;
	AdminDao aDao = new AdminDao();
	
	/***************************************************************
	 * 생성자 : admin이 들어오면 그 admin에 상영시간표를 보내주고 
	 * 		List에 추가한다.
	 * @param as
	 */
	public AdminServerThread(AdminServer as) {
		this.as = as;
		this.admin = as.socket;
		
		try {
			oos = new ObjectOutputStream(admin.getOutputStream());
			ois = new ObjectInputStream(admin.getInputStream());
			String msg = (String)ois.readObject();
			String time = as.setTimer()+" "+as.setHMS();
			as.jta_log.append("*LOGIN*"+time+" : "+msg+"\n");
			StringTokenizer st = new StringTokenizer(msg,"#");
			st.nextToken(); // 프로토콜
			adminID = st.nextToken();
			//처음 로그인 성공시 현재 상영하는 영화정보와 지정의 상영과을 보내준다.
			List<Map<String, Object>> rList = null;
			rList = aDao.ins(adminID);
			for(Map<String, Object> map: rList) {
				this.send(AdminProtocol._DIAL 
						 +"#"+map.get("First")
						 +"#"+map.get("Second"));
			}
			//쓰레드를 리스트에 추가
			as.adminList.add(this);
			int inwon = as.adminList.size();
			as.jta_log.append("    - 현재 인원 수  : "+inwon+" 명 \n\n");
			as.jta_log.setCaretPosition(as.jta_log.getDocument().getLength());
		} catch (NullPointerException ne) {
			System.out.println("오라클서버에 접속할수 없습니다.");
			ne.printStackTrace();
		} catch (Exception e) {
			System.out.print("[AdminServerThread]-생성자:");
			e.printStackTrace();
		}
	}
	//현재입장해 있는 친구들 모두에게 메시지 전송하기 구현
	public void broadCasting(String msg) {
		synchronized(this) { //adminList 인터셉트 금지!!!!
			for (AdminServerThread ast : as.adminList) {
				ast.send(msg);
			}
		}
	}
	//클라이언트에게 말하기 구현
	public void send(String msg) {
		try {
			oos.writeObject(msg);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}	
	public void run() {
		boolean isStop = false;
		String msg = null;
		try {
			//while(true) { //무한루프에 빠질 수 있다.
			run_start:
			while(!isStop) {
				msg = (String)ois.readObject();
				as.jta_log.append("*LOG*"+as.setHMS()+" : "+msg+"\n");
				StringTokenizer st = null;
				// 100:LOGIN|200:n-n|201:1-1|202:닉네임|500:LOGOUT;
				int protocol = 0; 
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				//로그인 클라이언트에서 보내지도 않는다.*********************************
					case AdminProtocol._LOGIN: {

					} break;
				//**********************************************************
					//서버에 등록된 그 지점의 전체 상영시간표를 보내준다.
					case AdminProtocol._REFRESH: {
						adminID = st.nextToken();
						astVO = aDao.refreshData(adminID);
						for(AdminShowtimeVO mVO: astVO) {
							this.send(AdminProtocol._REFRESH
									 +"#"+mVO.getScrName()
									 +"#"+mVO.getMovieTitle()
									 +"#"+mVO.getDate()
									 +"#"+mVO.getTime());
						}
					} break;
				//**********************************************************
					//오늘날짜의 상영시간표를 보내준다.
					case AdminProtocol._SEL: {
						adminID = st.nextToken();
						String currentYMD = st.nextToken();
						astVO = aDao.selectData(adminID, currentYMD);
						for(AdminShowtimeVO mVO: astVO) {
							this.send(AdminProtocol._REFRESH
									 +"#"+mVO.getScrName()
									 +"#"+mVO.getMovieTitle()
									 +"#"+mVO.getDate()
									 +"#"+mVO.getTime());
						}
					} break;
				//**********************************************************
					//현재 상영중인 영화 정보와 요청한 지점의 상영관을 보내준다.
					case AdminProtocol._DIAL: {
						adminID = st.nextToken();
						List<Map<String, Object>> rList = null;
						rList = aDao.ins(adminID);
						for(Map<String, Object> map: rList) {
							this.send(AdminProtocol._DIAL 
									 +"#"+map.get("First")
									 +"#"+map.get("Second"));
						}
					} break;
				//**********************************************************
					//상영시간표 추가해주고 처리결과 메시지를 반환 해준다.
					case AdminProtocol._INS: {
						AdminShowtimeVO astVO = new AdminShowtimeVO();
						astVO.setId(st.nextToken());
						astVO.setMovieTitle(st.nextToken());
						astVO.setScrName(st.nextToken());
						astVO.setYy(st.nextToken());
						astVO.setMm(st.nextToken());
						astVO.setDd(st.nextToken());
						astVO.setHh24(st.nextToken());
						astVO.setMi(st.nextToken());
						String r_msg = aDao.insertShowtime(astVO);
							this.send(AdminProtocol._INS 
									 +"#"+r_msg);
					} break;
				//**********************************************************
					//상영시간표 삭제해주고 처리결과 메시지를 반환 해준다
					case AdminProtocol._DEL: {
						AdminShowtimeVO astVO = new AdminShowtimeVO();
						astVO.setId(st.nextToken());
						astVO.setMovieTitle(st.nextToken());
						astVO.setScrName(st.nextToken());
						astVO.setYy(st.nextToken());
						astVO.setMm(st.nextToken());
						astVO.setDd(st.nextToken());
						astVO.setHh24(st.nextToken());
						astVO.setMi(st.nextToken());
						String r_msg = aDao.deleteShowtime(astVO);
							this.send(AdminProtocol._DEL 
									 +"#"+r_msg);
					} break;
				//**********************************************************
					//클라이언트가 종료를 누르면 로그애 띄운다.
					case AdminProtocol._EXIT: {
						String nickName = st.nextToken();
						//String message = st.nextToken();
						as.jta_log.append("   -INFO : "+as.socket+"\n");
						as.adminList.remove(this);
						as.jta_log.append("    - 현재 인원 수  : "+as.adminList.size()+" 명 \n\n");
					} break run_start;
				}///end of switch
				//JTextArea의 커서를 맨 밑으로 내려 스크롤을 맨 아래를 보게 한다.
				as.jta_log.setCaretPosition(as.jta_log.getDocument().getLength());
			}			
		} catch (Exception e) {
			System.out.print("[AdminServerThread]-run():");
			e.printStackTrace();
		}
	}

}
