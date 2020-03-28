package maxmovie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class MovieServerThread extends Thread{
	
	//id와 nickname 저장
	String id = null;
	String nickname = null;
	
	//컨트롤러 생성
	MovieController ctrl = new MovieController();
	
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
		
	//이벤트로부터 넘어온 메세지를 듣고 말하는 메소드
	@Override
	public void run() {
		int num = 0;
		while(!stop) {
			/****************************************************************************
			 * 리프레시 되는 곳
			 */
			if("00:00:00".equals(ms.setTimer(null,"시간"))&&num==0) {//정시가되면
				//[1]로그파일저장
				String fileName = "log_"+ms.setTimer(null,"날짜")+".txt";
				try {
					File f = new File(logpath+fileName);
					PrintWriter pw 
						= new PrintWriter(
								new BufferedWriter(
										new FileWriter(f.getAbsolutePath())));
					pw.write(ms.jta_log.getText());
					pw.close();
				} catch (Exception e) {
					System.out.println(e.toString());
					//e.printStackTrace();
				}
				//[2]영화정보 리프레쉬
				Calendar newDay = Calendar.getInstance();//새로운 오늘 날짜정보에서
				//어제날짜 뽑기[형식:20200325]
				newDay.add(Calendar.DAY_OF_MONTH, -1);
				int year = newDay.get(Calendar.YEAR);
				int month = newDay.get(Calendar.WEEK_OF_MONTH)-1;
				int day = newDay.get(Calendar.DAY_OF_MONTH);
				String yesterday = ms.setTimer(newDay, "날짜");
				//추가날짜 뽑기[형식:20200328]
				newDay.add(Calendar.DAY_OF_MONTH, +3);
				year = newDay.get(Calendar.YEAR);
				month = newDay.get(Calendar.WEEK_OF_MONTH)-1;
				day = newDay.get(Calendar.DAY_OF_MONTH);
				String addDay = ms.setTimer(newDay, "날짜");
				for(int i=0; i<ms.movieList.size(); i++) {
					/*
					if(yesterday.equals(ms.movieList.get(i).날짜)) {
						영화정보 리스트에서 제거
						ms.movieList.remove(i);
					}			
					//db에 오늘 날짜 보내서 새로운 영화정보 가져오기
					*/			
				}
				num = 1;//리프레시 조건식을 false로 만들기 위해
			}
			if("00:00:01".equals(ms.setTimer(null,"시간"))&&num==1) {//1초가 되면 
				num=0;//num을 0으로 다시 초기화 ==> while문을 안멈추면서 계속 "00:00:00" 조건을 검사할 수 있도록
			}
			/****************************************************************************
			 * 메세지 듣고 말하는 곳
			 */
			try {
				String msg = (String) ois.readObject();
				int protocol = 0;
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer("","#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				//프로토콜에 따라 send 파라미터 달라지는...
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
