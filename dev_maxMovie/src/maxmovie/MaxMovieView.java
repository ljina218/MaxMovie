package maxmovie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MaxMovieView extends JFrame{
	
	List<Map<String, Object>> movieList = null;
	
	//로고들에 사용한 폰트    : Futura XBlk BT
	//텍스트들에 사용한 폰트 : 굴림체
	//기본 메인 규격 res.width = 모니터해상도 가로길이,  res.height = 모니터해상도 세로길이
	Dimension 					res 				= Toolkit.getDefaultToolkit().getScreenSize();
	
	//모든패널의 이벤트를 맞는 클래스선언
	EventMapping 				em 					= new EventMapping(this);
	
	//클라이언트 스레드를 생성하기 위한 전변
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;

	//**메인프레임의 틀 메인프레임의 고정된 북쪽패널과 남쪽패널
	JLabel						jl_logo_small		= new JLabel() {
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
	String						mem_id				= "";
	String						mem_nick			= "";

	JLabel						jl_nickInfo			=	new JLabel();
	JLabel						jl_nickInfoEnd		=	new JLabel("님");
	JButton						jbt_logout			=	new JButton("로그아웃");
	JButton						jbt_myPage			=	new JButton("마이페이지");
	JButton						jbt_ticketing		=	new JButton("예매하기");
	
	JPanel						jp_center			= new JPanel();
	
	JPanel 						jp_north 			= new JPanel();
	JPanel 						jp_south 			= new JPanel();
	JPanel 						jp_west 			= new JPanel();
	JPanel 						jp_east 			= new JPanel();
	JTextPane 					jtp_south_south		= new JTextPane();	
	//로그인뷰
	LoginView 					jp_lv 				= new LoginView(em);
	//마이페이지뷰
	MyPageView 					jp_mv 				= new MyPageView(em);
	//무비리저레이션뷰
	MovieReserationView 		jp_mrv 				= new MovieReserationView(em);
	//리절트뷰
	ResultView 					jp_rv 				= new ResultView(em);
	
	public MaxMovieView() {
		initDisplay();
		eventMapping();//이벤트 맵핑 메소드
		connect();//클라이언트 스레드를 생성하기 위한 메소드
	}

	public void initDisplay(){
		jp_north.setLayout(null);
		//예매화면으로 이동시 jl_logo_small.setVisible(true); 로 변환
		jl_logo_small.setBounds(840, 30, 200, 94);
		jl_nickInfo.setBounds(1080, 100, 150, 20);
		jl_nickInfoEnd.setBounds(1240, 100, 20, 20);
		jbt_logout.setBounds(1260, 100, 90, 20);	
		jbt_myPage.setBounds(1352, 100, 90, 20);	
		jbt_ticketing.setBounds(1444, 100, 90, 20);
		/*****************************************************************
		 * 로그인시 아이디
		 * mem_id = "???????";
		 * nickName = "???????";
 		 * jl_logo_small.setVisible(true);
 		 * jl_nickInfo.setVisible(true);
 		 * jl_nickInfoEnd.setVisible(true);
 		 * jbt_logout.setVisible(true);
 		 * jbt_myPage.setVisible(true);
 		 * jbt_ticketing.setVisible(true);
		 *****************************************************************/
		///

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jl_nickInfo.setFont(new Font("굴림체", Font.BOLD, 20));
		jl_nickInfo.setForeground(Color.black);
		jl_nickInfo.setHorizontalAlignment(JLabel.RIGHT);
		jbt_logout.setFont(new Font("굴림체", Font.PLAIN, 11));
		jbt_logout.setForeground(new Color(80, 80, 80));
		jbt_myPage.setFont(new Font("굴림체", Font.PLAIN, 11));
		jbt_myPage.setForeground(new Color(80, 80, 80));
		jbt_ticketing.setFont(new Font("굴림체", Font.PLAIN, 11));
		jbt_ticketing.setForeground(new Color(80, 80, 80));
		jbt_logout.setBackground(Color.white);
		jbt_myPage.setBackground(Color.white);
		jbt_ticketing.setBackground(Color.white);
		jbt_logout.setBorder(new TitledBorder(new LineBorder(Color.white)));
		jbt_myPage.setBorder(new TitledBorder(new LineBorder(Color.white)));
		jbt_ticketing.setBorder(new TitledBorder(new LineBorder(Color.white)));
		jl_logo_small.setVisible(false);//
		jl_nickInfo.setVisible(false);
		jl_nickInfoEnd.setVisible(false);
		jbt_logout.setVisible(false);
		jbt_myPage.setVisible(false);
		jbt_ticketing.setVisible(false);//
		jtp_south_south.setText("회사소개 (930307)경기도 고양시 덕양구 토당동 양우아파트 102동 502호 대표이사 : 이진아 박미경 이정훈 사업자등록번호 : 501-39-76677-7 호스팅사업자 : CJ올리브네트웍스개인정보보호  \n" +
							   "책임자 : 이진아 대표이메일 : ljina0218@naver.com 고객센터 : 1588-1588 (04377)서울특별시 용산구 한강대로 23길 55, 아이파크몰 6층(한강로동)\n" +
							   "호스팅사업자 : CJ 올리브 네트웍스 개인정보 보호 책임자 : 정종민 대표이메일 : cjcgvmaster@cj.netCGV고객센터 : 1544-1122 \n" +
							   "대표이사 : 곽두팔 사업자 등록번호 : 157-81-452390통신판매업신고번호 : 2019-독도 울릉도-0662 사업자정보확인 \n" +
							   "통신판매업신고번호 : 2019-독도-05 사업자정보확인 ©2020 MAXMOVIE Corp. All right Reserved \n");
		jp_north.add(jl_logo_small);
		jp_south.add(jtp_south_south);
		jp_north.setPreferredSize(new Dimension(1980, 140));
		jp_south.setPreferredSize(new Dimension(1980, 140));
		jp_west.setPreferredSize(new Dimension(190, 1080));
		jp_east.setPreferredSize(new Dimension(190, 1080));
		jp_north.setBackground(Color.white);
		jp_south.setBackground(Color.white);
		jp_west.setBackground(Color.white);
		jp_east.setBackground(Color.white);
		/**********************************************
		 * jp_lv 일때  false
		 * jl_logo_small.setVisible(false);
		 * nickName = "";
 		 * jl_nickInfo.setVisible(false);
 		 * jl_nickInfoEnd.setVisible(false);
 		 * jbt_logout.setVisible(false);
 		 * jbt_myPage.setVisible(false);
 		 * jbt_ticketing.setVisible(false);
		 * 
		 * jp_mcv, jp_mv, jp_scv, jp_rv
		 * jl_logo_small.setVisible(true);
		 * nickName = "??????";
 		 * jl_nickInfo.setVisible(true);
 		 * jl_nickInfoEnd.setVisible(true);
 		 * jbt_logout.setVisible(true);
 		 * jbt_myPage.setVisible(true);
 		 * jbt_ticketing.setVisible(true);
		 **********************************************/
		/***********************************************
		 * 기본 메인 규격 res.width = 모니터해상도 가로길이,  res.height = 모니터해상도 세로길이  
		 * border여서 전체길이를 매개변수로 넘겼음
		 ***********************************************/		

		jp_center.setLayout(null);
		jp_lv.setBounds(0, 0, 1535, 770);
		jp_mv.setBounds(0, 0, 1535, 770);
		jp_mrv.setBounds(0, 0, 1535, 770);
		jp_rv.setBounds(0, 0, 1535, 770);
		jp_center.add(jp_lv);
		jp_center.add(jp_mv);
		jp_center.add(jp_mrv);
		jp_center.add(jp_rv);
		 
		jp_mv.setVisible(false);
		jp_mrv.setVisible(false);
		jp_rv.setVisible(false);
		jp_lv.setVisible(true);
		
		this.add(jl_nickInfo);
		this.add(jl_nickInfoEnd);
		this.add(jbt_logout);
		this.add(jbt_myPage);
		this.add(jbt_ticketing);
		this.add("North", jp_north);
		this.add("South", jp_south);
		this.add("Center", jp_center);

		this.add("West", jp_west);
		this.add("East", jp_east);
		this.setSize(res.width, res.height);
		this.setResizable(false);
		this.setVisible(true);		

	}
	public void eventMapping() {
		jbt_logout.addActionListener(em);
		jbt_myPage.addActionListener(em);
		jbt_ticketing.addActionListener(em);

	}
	public static void main(String[] args) {
		new MaxMovieView();
	}
	public void connect() {//클라이언트 스레드를 생성하기 위한 메소드
		try {
			movieList = new Vector<Map<String,Object>>();//클라이언트에 저장할 영화정보 리스트 생성
			socket = new Socket("192.168.0.37",5000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(MovieProtocol.SELECT+"#");//영화정보 주세여
			ClientThread ct = new ClientThread(this);
			ct.start();
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
