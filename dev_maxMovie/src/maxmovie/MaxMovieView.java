package maxmovie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class MaxMovieView extends JFrame{
	//로고들에 사용한 폰트    : Futura XBlk BT
	//텍스트들에 사용한 폰트 : 굴림체
	//기본 메인 규격 res.width = 모니터해상도 가로길이,  res.height = 모니터해상도 세로길이
	Dimension 				res 			= Toolkit.getDefaultToolkit().getScreenSize();
	//모든패널의 이벤트를 맞는 클래스선언
	EventMapping 			em 			= new EventMapping(this);
	//**메인프레임의 틀 메인프레임의 고정된 북쪽패널과 남쪽패널
	JPanel 					jp_north 		= new JPanel();
	JPanel 					jp_south 		= new JPanel();
	JPanel 					jp_west 		= new JPanel();
	JPanel 					jp_east 		= new JPanel();
	JTextPane 				jtp_south_south	= new JTextPane();
	JLabel					jl_logo_s		= new JLabel() {
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
	LoginView 				jp_lv 			= new LoginView(em);	//로그인뷰
	MyPageView 				jp_mv 			= new MyPageView(em);	//마이페이지뷰
	MovieChoiceView 		jp_mcv 			= new MovieChoiceView(em);	//무비초이스뷰
	SeatChoiceView 			jp_sc 			= new SeatChoiceView(em);	//시트초이스뷰
	ResultView 				jp_rv 			= new ResultView(em);	//리절트뷰
	public MaxMovieView() {
		initDisplay();
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
		jl_logo_s.setVisible(false);
		
		jl_logo_s.setBounds(790, 30, 200, 94);
		
		jtp_south_south.setText("회사소개 (930307)경기도 고양시 덕양구 토당동 양우아파트 102동 502호 대표이사 : 이진아 박미경 이정훈 허준호 사업자등록번호 : 501-39-76677-7 호스팅사업자 : CJ올리브네트웍스개인정보보호  \n"
							  + "책임자 : 이진아 대표이메일 : ljina0218@naver.com 고객센터 : 1588-1588 (04377)서울특별시 용산구 한강대로 23길 55, 아이파크몰 6층(한강로동)\n"
							  + "호스팅사업자 : CJ 올리브 네트웍스 개인정보 보호 책임자 : 정종민 대표이메일 : cjcgvmaster@cj.netCGV고객센터 : 1544-1122 \n"
							  + "대표이사 : 곽두팔 사업자 등록번호 : 157-81-452390통신판매업신고번호 : 2019-독도 울릉도-0662 사업자정보확인 \n"
							  + "통신판매업신고번호 : 2019-독도-05 사업자정보확인 ©2020 MAXMOVIE Corp. All right Reserved \n");
		jp_north.add(jl_logo_s);
		jp_south.add(jtp_south_south);
		
		jp_north.setPreferredSize(new Dimension(1980, 140));
		jp_south.setPreferredSize(new Dimension(1980, 140));
		jp_west.setPreferredSize(new Dimension(190, 1080));
		jp_east.setPreferredSize(new Dimension(190, 1080));
		jp_north.setBackground(Color.white);
		jp_south.setBackground(Color.white);
		jp_west.setBackground(Color.white);
		jp_east.setBackground(Color.white);
		jp_lv.setVisible(true);
		jp_mv.setVisible(false);
		jp_mcv.setVisible(false);
		jp_sc.setVisible(false);
		jp_rv.setVisible(false);
		/***********************************************
		 * 기본 메인 규격 res.width = 모니터해상도 가로길이,  res.height = 모니터해상도 세로길이  
		 * border여서 전체길이를 매개변수로 넘겼음
		 * 
		 * 전환될 화면이 jp_lv 라면 
		 * jl_logo_s.setVisible(false); 되어야함
		 * 
		 * 전환될 화면이  jp_mv, jp_mcv, jp_sc, jp_rv 중 하나라면
		 * jl_logo_s.setVisible(true); 되어있거나 되어야함
		 ***********************************************/
		this.setSize(res.width, res.height);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void eventMapping() {
	}
	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
	}

}
