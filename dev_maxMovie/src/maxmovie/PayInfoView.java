package maxmovie;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class PayInfoView extends JDialog{
	
	//상단
	JLabel 				jl_logo_s 		= new JLabel() {
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
	JLabel 				         	jl_payInfo_header  				= new JLabel("     결제 상세정보 확인");
	
	//좌측
	JPanel							jp_payInfo						= new JPanel();
	JLabel 				            jl_payInfo                      = new JLabel("  결제정보");
	JLabel 				            jl_movie               		    = new JLabel("영화");
	JLabel 				            jl_locThe                  	    = new JLabel("극장");
	JLabel 				            jl_dateTime                  	= new JLabel("일시");
	JLabel 				            jl_screen                  	    = new JLabel("상영관");
	JLabel 				            jl_seat                  	    = new JLabel("좌석");
	
	
	JLabel 				            jl_choice_movie		           	= new JLabel("이보다 더좋을 순 없다.");
	JLabel 				            jl_choice_locThe                = new JLabel("전북/전주/강남");
	JLabel 				            jl_choice_screen                = new JLabel("1관");
	JLabel 				            jl_choice_seat                 	= new JLabel("A2, B1");
	JLabel 				            jl_choice_date                 	= new JLabel("2020년 3월 28일");
	JLabel 				            jl_choice_time                 	= new JLabel("12:00 ~ 13:45");
	
	//우측
	JPanel							jp_ticktingInfo					= new JPanel();
	JLabel 				            jl_ticktingInfo                 = new JLabel("  예매정보");
	JLabel                          jl_payWay                      	= new JLabel("결제수단");
	JLabel                          jl_totalPay                     = new JLabel("결제금액");
	                            
	JLabel                          jl_choice_payWay               	= new JLabel("카카오페이");
	JLabel                          jl_choice_totalPay              = new JLabel("18,000원");
	
	                                
	                                
	JButton				            jbt_PayGo                  	    = new JButton("결제하기");
	JButton				            jbt_cancel                      = new JButton("취소");
	EventMapping 		            em 						    	= null;

	public PayInfoView() {
		initDisplay();
	}
	
	public PayInfoView(EventMapping em) {
		this.em = em;
		initDisplay();
		eventMapping();
	}
	
	public void initDisplay(){
		this.setLayout(null);
		jp_ticktingInfo.setLayout(null);
		jp_payInfo.setLayout(null);
		this.getContentPane().setBackground(Color.white);

		jp_payInfo.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),1)));
		jp_ticktingInfo.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),1)));
		
		jp_payInfo.setBounds(10, 120, 234, 170);
		jp_ticktingInfo.setBounds(242, 120, 233,170);
		jl_logo_s.setBounds(150, 0, 200, 94);
		jl_payInfo_header.setBounds(10, 96, 465, 25);
		jl_payInfo.setBounds(0, 0, 234, 22);
		jl_movie.setBounds(10, 30, 150, 15);          
		jl_locThe.setBounds(10, 50, 150, 15);          
		jl_dateTime.setBounds(10, 70, 150, 15);           
		jl_screen.setBounds(10, 110, 150, 15);          
		jl_seat.setBounds(10, 130, 150, 15);    
		jl_choice_movie.setBounds(75, 30, 150, 15);	
		jl_choice_locThe.setBounds(75, 50, 150, 15);  
		jl_choice_date.setBounds(75, 70, 150, 15);   
		jl_choice_time.setBounds(75, 90, 150, 15);   
		jl_choice_screen.setBounds(75, 110, 150, 15);  
		jl_choice_seat.setBounds(75, 130, 150, 15);
		jl_ticktingInfo.setBounds(0, 0, 232, 22);
		jl_payWay.setBounds(10, 30, 150, 15);     
		jl_totalPay.setBounds(10, 50, 150, 15);
		jl_choice_payWay.setBounds(75, 30, 150, 15); 
		jl_choice_totalPay.setBounds(75, 50, 150, 15);
		jbt_PayGo.setBounds(153, 300, 90, 30);      
		jbt_cancel.setBounds(247, 300, 90, 30);    

		jbt_cancel.setForeground(Color.gray);
		jl_payInfo_header.setOpaque(true);
		jl_payInfo_header.setBackground(Color.black);
		jl_payInfo_header.setForeground(Color.white);
		jl_payInfo.setOpaque(true);
		jl_payInfo.setFont(new Font("굴림체", Font.BOLD, 13));
		jl_payInfo.setBackground(Color.gray);
		jl_ticktingInfo.setOpaque(true);
		jl_ticktingInfo.setFont(new Font("굴림체", Font.BOLD, 13));
		jl_ticktingInfo.setBackground(Color.gray);
		jbt_PayGo.setBackground(new Color(52, 152, 219));
		jbt_PayGo.setFont(new Font("굴림체", Font.BOLD, 13));
		jbt_PayGo.setForeground(Color.white);
		jbt_cancel.setBackground(new Color(255, 255, 255));
		
		jp_payInfo.add(jl_payInfo);
		jp_payInfo.add(jl_movie);
		jp_payInfo.add(jl_locThe);
		jp_payInfo.add(jl_screen);
		jp_payInfo.add(jl_seat);
		jp_payInfo.add(jl_dateTime);
		jp_payInfo.add(jl_choice_movie);
		jp_payInfo.add(jl_choice_locThe);
		jp_payInfo.add(jl_choice_date);
		jp_payInfo.add(jl_choice_time);           
		jp_payInfo.add(jl_choice_screen);
		jp_payInfo.add(jl_choice_seat);
		jp_ticktingInfo.add(jl_ticktingInfo);
		jp_ticktingInfo.add(jl_payWay);
		jp_ticktingInfo.add(jl_totalPay);
		jp_ticktingInfo.add(jl_choice_payWay);
		jp_ticktingInfo.add(jl_choice_totalPay);
		this.add(jl_logo_s);
		this.add(jl_payInfo_header);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.add(jp_payInfo);
		this.add(jp_ticktingInfo);
		this.add(jbt_PayGo);
		this.add(jbt_cancel);
		this.setSize(500, 400);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}

	
	public void eventMapping() {
		jbt_PayGo.addActionListener(em);      
		jbt_cancel.addActionListener(em);      
		
	}
	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jp_lv.setVisible(false);
		mmv.jp_mrv.setVisible(true);
		mmv.jp_mrv.jp_mcv.setVisible(false);
		mmv.jp_mrv.jp_scv.setVisible(false);
		mmv.jp_mrv.jp_pv.setVisible(true);
		mmv.jp_mv.setVisible(false);
		mmv.jp_mv.jp_thv.setVisible(false);
		mmv.jp_mv.jp_miv.setVisible(false);
		mmv.jp_mv.jp_muv.setVisible(false);
		mmv.jp_rv.setVisible(false);
		new PayInfoView();

		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
	
	}

}
