package hjho;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class AdminServer extends JFrame implements Runnable{
	//Center부분 화면 선언부
	AdminServerThread 		ast = null;
	List<AdminServerThread> adminList = null;
	
	ServerSocket 			server = null;
	Socket 					socket = null;
	
	JTextArea 				jta_log = new JTextArea(10, 30); //(rows, columns)
	JScrollPane 			jsp_log = new JScrollPane(jta_log
													 ,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
													 ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	//추가로 버튼 만드는 선언부
	JPanel 	jp_north = new JPanel();
	JButton jbtn_log = new JButton("로그 저장");
	String	logPath	 = "src\\hjho\\";
	JButton jbtn_exit = new JButton("나가기");
	
	public void initDisplay() {
		//익명 메소드*************************************************************
		jbtn_log.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				//로그저장 눌렀니?
				if(obj==jbtn_log) {
					String fileName = "log_"+setTimer()+".txt";
					System.out.println(fileName); //log_2020_03_13.txt
					try {
					//자바는 모든 기능 사물 들을 클래스로 설계하도록 유도한다.
					//파일명을 클래스로 만들어주는 API가 있다. - File
						File f = new File(logPath+fileName);
					//파일명만 생성될 뿐 내용까지 만들어 주진 않는다.
					//내용 부분을 담는 별도의 클래스가 필요하다.
						PrintWriter pw = 
								new PrintWriter(
										new BufferedWriter( //필터클래스-카메라 필터
												new FileWriter(f.getAbsoluteFile())));
					//io패키지에는 단독으로 파일을 컨트롤 할 수있는 클래스가 있고
					//그 클래스에 연결해서 사용하는 필터 클래스가 존재함.(기능을 향상해줌)
						pw.write(jta_log.getText());
						pw.close();//사용한 입출력 클래스는 반드시 닫아줌.
					} catch (Exception e2) {
						//예외가 발생했을 때 출력함
						//예외가 발생하지 않으면 실행이 안됨.
						System.out.println(e2.toString());
					}
				}
			}
		});
		//익명 메소드*************************************************************
		jbtn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				//나가기를 눌렀니?
				if(obj==jbtn_exit) {
					try {
						if(socket != null) socket.close();
						if(server != null) server.close();
						System.exit(0);
					} catch (Exception e2) {
						System.out.println(e2.toString());
						e2.printStackTrace();
					} finally {
					}
				}
			}
		});
		//initDisplay*************************************************************
		//Center 화면 구성
		jta_log.setBackground(Color.LIGHT_GRAY);
		Font font = new Font("맑은 고딕", Font.BOLD, 15);
		jta_log.setFont(font);
		//North 화면 구성
		jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp_north.add(jbtn_log);
		jp_north.add(jbtn_exit);
		
		this.add("North", jp_north);
		this.add("Center", jsp_log);
		this.setTitle("admin log 기록창");
		this.setSize(600,400);
		this.setVisible(true);
	}
	
	//서버 소켓과 클라이언트측 소켓을 연결하기
	//initDispaly를 먼저 한 후 스레드  On
	@Override
	public void run() {
		//서버에 접속해온 클라이언트 정보를 관리할 스레드
		adminList = new Vector<>();
		boolean isStop = false;
		try {
			server = new ServerSocket(5100);
			jta_log.append("*Start* Server Ready Complet\n\n");
			while(!isStop) {
				socket = server.accept();
				jta_log.append("*ACCEPT* Client info: "+socket+" \n");
				AdminServerThread ast = new AdminServerThread(this);
				ast.start();
			}
		} catch (Exception e) {
			System.out.print("[TalkServer]-run():");
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		AdminServer as = new AdminServer();
		as.initDisplay();
		Thread th = new Thread(as);
		th.start();
	}
	
//	@Override
//	public void actionPerformed(ActionEvent e) {
//
//	}//end of actionPerformed
/**************************************************************
 * setTimer() : 시스템의 오늘 날짜 정보 가져오기
 * @param 해당사항 없음.
 * @return 2020_03_13
 */
	public String setTimer() {
		Calendar cal = Calendar.getInstance();
		int yyyy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return yyyy+"년"+
				(mm < 10 ? "0"+mm:""+mm)+"월"+
				(day < 10 ? "0"+day:""+day)+"일";
	}//end of setTimer
	public String setHMS() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		return (hour < 10 ? "0"+hour:""+hour)+"시"+
			   (min < 10 ? "0"+min:""+min)+"분";
	}//end of setTimer
}
