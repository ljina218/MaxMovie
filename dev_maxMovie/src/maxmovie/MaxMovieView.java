package maxmovie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

//진아수정
public class MaxMovieView extends JFrame{
	//로고들에 사용한 폰트    : Futura XBlk BT
	//텍스트들에 사용한 폰트 : 굴림체
	//기본 메인 규격 res.width = 모니터해상도 가로길이,  res.height = 모니터해상도 세로길이
	Dimension 				res 			= Toolkit.getDefaultToolkit().getScreenSize();
	
	//모든패널의 이벤트를 맞는 클래스선언
	EventMapping 			event 			= new EventMapping(this);
	
	//클라이언트 스레드를 생성하기 위한 전변
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;

	//**메인프레임의 틀 메인프레임의 고정된 북쪽패널과 남쪽패널
	JPanel 					jp_north 	= new JPanel();
	JPanel 					jp_south 	= new JPanel();
	JPanel 					jp_west 	= new JPanel();
	JPanel 					jp_east 	= new JPanel();
	
	JTextPane 				jtp_south_south	= new JTextPane();
	
	JLabel					jl_logo_small	= new JLabel() {
		public void paint(Graphics g) {
			Image img = null;
			try {
				img = ImageIO.read(new File("src\\maxmovie\\maxmovielogos.png"));
				g.drawImage(img, 0, 0, 200, 94, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	//로그인뷰
	JPanel 					jp_lv 			= new LoginView(this);
	//마이페이지뷰
	JPanel 					jp_mv 			= new MyPageView(this);
	//무비초이스뷰
	JPanel 					jp_mcv 			= new MovieChoiceView(this);
	//시트초이스뷰
	JPanel 					jp_sc 			= new SeatChoiceView(this);
	//리절트뷰
	JPanel 					jp_rv 			= new ResultView(this);

	public MaxMovieView() {
		initDisplay();
		eventMapping();//이벤트 맵핑 메소드
		connect();//클라이언트 스레드를 생성하기 위한 메소드
	}
	
	private void initDisplay(){
		this.add("Center", jp_mv);
		this.add("Center", jp_mcv);
		this.add("Center", jp_sc);
		this.add("Center", jp_rv);
		this.add("North", jp_north);
		this.add("South", jp_south);
		this.add("Center", jp_lv);
		this.add("West", jp_west);
		this.add("East", jp_east);
		
		jp_north.setLayout(null);
		//예매화면으로 이동시 jl_logo_small.setVisible(true); 로 변환
		jl_logo_small.setVisible(false);
		
		jl_logo_small.setBounds(790, 30, 200, 94);
		
		
		jtp_south_south.setText("회사소개 (930307)경기도 고양시 덕양구 토당동 양우아파트 102동 502호 대표이사 : 이진아 박미경 이정훈 사업자등록번호 : 501-39-76677-7 호스팅사업자 : CJ올리브네트웍스개인정보보호  \n"
							  + "책임자 : 이진아 대표이메일 : ljina0218@naver.com 고객센터 : 1588-1588 (04377)서울특별시 용산구 한강대로 23길 55, 아이파크몰 6층(한강로동)\n"
							  + "호스팅사업자 : CJ 올리브 네트웍스 개인정보 보호 책임자 : 정종민 대표이메일 : cjcgvmaster@cj.netCGV고객센터 : 1544-1122 \n"
							  + "대표이사 : 곽두팔 사업자 등록번호 : 157-81-452390통신판매업신고번호 : 2019-독도 울릉도-0662 사업자정보확인 \n"
							  + "통신판매업신고번호 : 2019-독도-05 사업자정보확인 ©2020 MAXMOVIE Corp. All right Reserved \n");
		
		jp_north.add(jl_logo_small);
		jp_south.add(jtp_south_south);
		
	
		jp_north.setPreferredSize(new Dimension(1980, 140));
		jp_south.setPreferredSize(new Dimension(1980, 140));
		jp_west.setPreferredSize(new Dimension(190, 1080));
		jp_east.setPreferredSize(new Dimension(190, 1080));
		
//		jp_north.setBackground(Color.white);
//		jp_south.setBackground(Color.white);
//		jp_west.setBackground(Color.white);
//		jp_east.setBackground(Color.white);
		
		jp_lv.setVisible(true);
		jp_mv.setVisible(false);
		jp_mcv.setVisible(false);
		jp_sc.setVisible(false);
		jp_rv.setVisible(false);

		/***********************************************
		 * 기본 메인 규격 res.width = 모니터해상도 가로길이,  res.height = 모니터해상도 세로길이  
		 * border여서 전체길이를 매개변수로 넘겼음
		 ***********************************************/
		this.setSize(res.width, res.height);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void eventMapping() {
		//.addActionlistener(event);
	}
	
	public static void main(String[] args) {
		new MaxMovieView();
	}
	
	public void connect() {//클라이언트 스레드를 생성하기 위한 메소드
		try {
			socket = new Socket("192.168.0.244",5000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			ClientThread ct = new ClientThread(this);
			ct.start();
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}

}
