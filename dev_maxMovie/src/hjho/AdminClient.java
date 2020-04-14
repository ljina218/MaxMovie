package hjho;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class AdminClient extends JFrame implements ActionListener{
	//처음 인스턴스 될때 정보 받아오기 
	AdminDialog ad = null;
	AdminShowtimeVO astVO = null;
	List<String> first = new Vector<String>();    //영화제목과 상영관 구분자
	List<String> second = new Vector<String>();   //실직적인 영화제목, 상영관이름
	//소켓 통신을 위한 전역변수 
	Socket   		   socket 	= null;
	ObjectOutputStream oos 		= null; //서버에게 말하기
	ObjectInputStream  ois 		= null; //서버가 말한걸 듣기
	//화면 그리기 위한 변수
	JPanel jp_north = new JPanel();
	JPanel jp_center = new JPanel();
	//각종 폰트
	Font thFont = new Font("맑은 고딕", Font.BOLD, 15); //테이블헤더 폰트 
	Font tbFont = new Font("맑은 고딕", Font.PLAIN, 14); //테이블 폰트 
	Font btfont = new Font("맑은 고딕", Font.BOLD, 13);  //버튼 폰트
	//각종 버튼
	String imgPath = "src\\hjho\\";  //버튼 이미지 경로
	JButton jbtn_all = new JButton("상영시간표 조회");
	JButton jbtn_sel = new JButton(new ImageIcon(imgPath+"detail.gif"));//,
	JButton jbtn_ins = new JButton(new ImageIcon(imgPath+"new.gif"));
	JButton jbtn_del = new JButton(new ImageIcon(imgPath+"delete.gif"));
	JButton jbtn_exit = new JButton("나가기");
	//테이블
	String cols[] = {"상영관","영화","날짜","시간"};  //컬럼내용
	String data[][] = new String[0][4];         //컬럼
	DefaultTableModel dtm_movie = new DefaultTableModel(data, cols);  //테이블 데이터
	JTable jtb_movie = new JTable(dtm_movie); 	           //테이블
	JScrollPane jsp_movie  = new JScrollPane(jtb_movie);   //테이블 스크롤
	JTableHeader jth_movie = jtb_movie.getTableHeader();   //테이블 헤더
	
/******************************************************************************************************
 * 로그인하고 받은 이름 지역 지점 초기화, 화면 initDisplay()호출 및 서버접속을 위한 함수 connect_process()호출
 * @param id    : 로그인 아이디
 * @param name  : 로그인한 이름 
 * @param tLoc  : 로그인한 지점의 지역
 * @param tName : 로그인한 지점의 지점명 
 */
	public AdminClient(String id, String name, String tLoc, String tName) {
		astVO = new AdminShowtimeVO();
		astVO.setId(id);
		astVO.setName(name);
		astVO.setTheaterloc(tLoc);
		astVO.setTheatername(tName);
		//JOptionPane.showMessageDialog(this, name); //전달 테스트
		initDisplay();  //화면 함수 호출
		//plus(); // 테스트 함수 (테이블 내용 추가) 
		jbtn_sel.addActionListener(this);
		jbtn_ins.addActionListener(this);
		jbtn_del.addActionListener(this);
		jbtn_all.addActionListener(this);
		jbtn_exit.addActionListener(this);
		connect_process(); //서버연결 함수 호출
	}
/*******************************************************************************************************
 * initDisplay() : 화면 그리는 함수
 */
	public void initDisplay() {
		this.setLayout(new BorderLayout());
		jp_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		jbtn_sel.setToolTipText("당일 상영시간표 조회");
		jbtn_ins.setToolTipText("상영시간표 추가");
		jbtn_del.setToolTipText("상영시간표 삭제");
		//버튼 폰트 설정 
		jbtn_all.setFont(btfont);
		jbtn_exit.setFont(btfont);
		//테이블헤더 설정
		jth_movie.setFont(thFont); 					  //테이블 헤더 폰트 변경
		//jth_movie.setForeground(Color.white);       //테이블 헤더 폰트 색상 변경
		jth_movie.setBackground(Color.LIGHT_GRAY);    //테이블 헤더 백그라운드 색상
		jth_movie.setReorderingAllowed(false);        //테이블 헤더 위치변경 꺼두기
		//테이블 설정
		jtb_movie.setFont(tbFont);   				  //테이블 폰트 변경
		//jtb_movie.setForeground(Color.BLUE);	      //테이블 폰트 색상 변경
		//jtb_movie.setBackground(Color.GRAY);        //테이블 백그라운드 색상
		jtb_movie.setGridColor(Color.LIGHT_GRAY);     //테이블 선 색상
		jtb_movie.setSelectionBackground(Color.GRAY); //테이블 클릭시 배경 색상 변경
		//테이블 가운데 정렬  : DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer dtcr_movie = new DefaultTableCellRenderer();
		dtcr_movie.setHorizontalAlignment(SwingConstants.CENTER); // dtcr_movie의 정렬을 가운데 정렬로 지정
		TableColumnModel tcm_movie = jtb_movie.getColumnModel(); //정렬할 테이블의 ColumnModel을 가져옴
		
		//테이블 너비 및 가운데 정렬
		int tbWidth[] = {100, 300, 120, 80};  //테이블너비 = {0컬럼, 1컬럼, 2컬럼, 3컬럼 } 
		jtb_movie.setRowHeight(25);			  //테이블 높이 지정
		for (int i = 0; i < tcm_movie.getColumnCount(); i++) {
			tcm_movie.getColumn(i).setCellRenderer(dtcr_movie);      //테이블을 가운데 정렬로 지정
			TableColumn tc = jtb_movie.getColumnModel().getColumn(i);//테이블을 너비 지정
			tc.setPreferredWidth(tbWidth[i]);
		}
		//버튼 추가
		jp_north.add(jbtn_all);
		jp_north.add(jbtn_sel);
		jp_north.add(jbtn_ins);
		jp_north.add(jbtn_del);
		jp_north.add(jbtn_exit);
		
		this.add("North", jp_north);
		this.add("Center", jsp_movie);
		//ex) "서울 강남점 상영시간표 정보"
		this.setTitle(astVO.getTheaterloc()+" "+astVO.getTheatername()+" 상영시간표 정보");
		this.setSize(600, 500);
		this.setVisible(true);
	}
/*******************************************************************************************************
 * init() : 서버의 통신을 위한 함수 
 */
	public void connect_process() {
		try {
			//서버측의 ip주소 작성하기
			socket = new Socket("127.0.0.1",5100);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			//로그인 시 현재 상영되고있는 영화정보와
			//지점의 상영관 정보를 요청한다.
			oos.writeObject(AdminProtocol._DIAL+"#"+astVO.getId());
			//클라이언트 쓰레드 ON
			AdminClientThread act = new AdminClientThread(this);
			act.start();
		} catch (ConnectException ce) {
			//서버가 켜져있지 않으면 자동으로 꺼진다.
			JOptionPane.showMessageDialog(this
					, "서버와의 연결이 끊어졌습니다."
					, "에러"
					, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			ce.printStackTrace();
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}//end of connect_process
/**************************************************************************************************
 * 									actionPerformed()
 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		//***************************************************
		if(obj == jbtn_all) {
			JOptionPane.showMessageDialog(this, "전체 상영시간표 조회");
			try {
				while(dtm_movie.getRowCount()>0) {
					dtm_movie.removeRow(0);
				}
				oos.writeObject(AdminProtocol._REFRESH
						      +"#"+astVO.getId());
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		//***************************************************
		//상영시간표 조회
		//당일에 상영하는 상영시간표를 가져온다.
		else if(obj == jbtn_sel) {
			JOptionPane.showMessageDialog(this, "당일 상영시간표 조회");
			try {
				while(dtm_movie.getRowCount()>0) {
					dtm_movie.removeRow(0);
				}
				oos.writeObject(AdminProtocol._SEL
						      +"#"+astVO.getId()
						      +"#"+setYMD()
						      );
			} catch (Exception ec) {
				ec.printStackTrace();
			}
		}
		//***************************************************
		//상영시간표 추가
		//다이얼로그 창이 뜬다.
		else if(obj == jbtn_ins) {
			JOptionPane.showMessageDialog(this, "상영시간표 추가");
			try {
				if(ad==null) {
					ad = new AdminDialog(this);
				}
				ad.initDisplay();
			} catch (Exception ec) {
				ec.printStackTrace();
			}
			
		}
		//***************************************************
		//상영시간표 삭제
		else if(obj == jbtn_del) {
			JOptionPane.showMessageDialog(this, "상영시간표 삭제");
			int indexs[] = jtb_movie.getSelectedRows();
			if(indexs.length==0) {
				JOptionPane.showMessageDialog(this, "삭제할 로우를 선택하세요.");
				return;
			} 
			else {
				//클라이언트에 있는 AdminShowtimeVO를 건드리지 않고 
				//삭제하기 위한 정보들은 따로 객체 생성해서 담는다.
				AdminShowtimeVO del_astVO = null;
				for (int i=0; i<dtm_movie.getRowCount(); i++) {
					if(jtb_movie.isRowSelected(i)) { //i가 선택이 된거야?
						//선택한 로우의 상영관, 영화, 날짜, 시간 정보 저장 
						del_astVO = new AdminShowtimeVO();
						del_astVO.setScrName(dtm_movie.getValueAt(i,0).toString()); 
						del_astVO.setMovieTitle(dtm_movie.getValueAt(i,1).toString()); 
						del_astVO.setDate(dtm_movie.getValueAt(i,2).toString()); 
						del_astVO.setTime(dtm_movie.getValueAt(i,3).toString()); 
						break;
					}
				}
				//오라클 프로시저 맞는 형식으로 전환하기 위해 스트링 쪼개기
				del_astVO.setYy(del_astVO.getDate().substring(0,4));  //연도
				del_astVO.setMm(del_astVO.getDate().substring(4,6));  //월
				del_astVO.setDd(del_astVO.getDate().substring(6));    //일
				del_astVO.setHh24(del_astVO.getTime().substring(0,2));//시
				del_astVO.setMi(del_astVO.getTime().substring(3));    //분
				del_astVO.setId(this.astVO.getId());                  //지점 아이디
				try {
					oos.writeObject(AdminProtocol._DEL
								+"#"+del_astVO.getId()
								+"#"+del_astVO.getMovieTitle()
								+"#"+del_astVO.getScrName()
								+"#"+del_astVO.getYy()
								+"#"+del_astVO.getMm()
								+"#"+del_astVO.getDd()
								+"#"+del_astVO.getHh24()
								+"#"+del_astVO.getMi()
							   );
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			}
		}
		//***************************************************
			//나가기 
		else if(obj == jbtn_exit) {
			int confirm = JOptionPane.showConfirmDialog(
					this, "나가시겠습니까?"
					, "나가기", JOptionPane.YES_NO_OPTION
					, JOptionPane.INFORMATION_MESSAGE);
			if(confirm==0) {
				try {
					oos.writeObject(AdminProtocol._EXIT
							+"#"+astVO.getId());
					//+"#"+loginForm.nickName+"님이 퇴장하였습니다.");
					//자바가상머신과 연결 고리를 끊는다.
					if(ois != null) ois.close();
					if(oos != null) oos.close();
					if(socket != null) socket.close();
					System.exit(0);
				} catch (Exception ec) {
					ec.printStackTrace();
				}
			}
		}
		
	}
	/*
	public static void main(String[] args) {
		//테스트용 메인 
		new AdminClient(null, "허준호", "gangnam");
	}
	public void plus() {
		//테스트용 함수
		Vector<String> v = new Vector<String>();
		v.add("1관");
		v.add("그녀");
		v.add("20200403");
		v.add("19:00");
		dtm_movie.addRow(v);
	}*/
/********************************************************************
 * 상영시간표를 추가하거나 삭제 할 때 현재 날짜 정보를 받아온다.
 * 사용자가 입력한 날짜를 비교 할 때 사용한다.   
 * @return
 */
	public String setYMD() {
		Calendar cal = Calendar.getInstance();
		int yyyy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return yyyy
			+(mm < 10 ? "0"+mm:""+mm)
			+(day < 10 ? "0"+day:""+day);
	}//end of setTimer
/********************************************************************
 * 상영시간표를 추가하거나 삭제 할 때 현재 시간 정보를 받아온다.
 * 사용자가 입력한 시간를 비교 할 때 사용한다.  
 * @return
 */
	public String setHMS() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		return (hour < 10 ? "0"+hour:""+hour)
			   +(min < 10 ? "0"+min:""+min);
	}//end of setTimer
}
