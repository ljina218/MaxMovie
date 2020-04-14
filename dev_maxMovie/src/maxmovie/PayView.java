package maxmovie;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class PayView extends JPanel{
	EventMapping				em						= null;
	
	JPanel						jp_center				= new JPanel();
	JLabel						jl_seatHeader			= new JLabel("결제 선택");
	
	JLabel						jl_mid_header			= new JLabel("     결제수단");

	JRadioButton 				radio[] 				= new JRadioButton[2];
	String 						radio_name[]			= {"카드결제", "계좌이체"};
	ButtonGroup					group					= new ButtonGroup();

	JPanel						jp_mid_body				= new JPanel();
	JLabel						jl_infoL1				= new JLabel("※ 계좌이체 취소 시 다음 사항을 꼭 확인해주세요.");
	JLabel						jl_infoL2				= new JLabel("1)예매일 이후 7일 이내 취소시 - 자동환불은행 : 추소 후 즉시 처리 가능\n");
	JLabel						jl_infoL3				= new JLabel("2)예매일 7일 이후 취소 시 - 화불 요청일로부터 7일 이상 소요됨\n");
	JLabel						jl_infoR1				= new JLabel("※ 계좌이체 진행 도중 취소 시, 선택하신 좌석의 재선택이 일시 제한될 수 있습니다.(약 10분간)");
	JLabel						jl_infoR2				= new JLabel("인터넷 예매는 온라인상으로 영화상영 시간 20분 전 까지 취소 가능하며 20분 이후에는 현장에서 취소를 하셔야 합니다.");
	JLabel						jl_infoR3				= new JLabel("현장 취소를 하는 경우 상영시간 이전까지만 가능하며 영화 상영 시작 시간 이후 취소나 환불은 되지 않습니다.");
	
	public PayView(EventMapping em) {
		this.em = em;
		initDisplay();
	}
	//0빈자리  1결제 진행중  2결제완료
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(Color.white);
		
		 for(int i=0; i<radio.length; i++){
	            radio[i] = new JRadioButton(radio_name[i]);
	            group.add(radio[i]);
	            radio[i].setBounds(440+i*120, 146, 100, 15);
	            this.add(radio[i]);
	     }
		jl_seatHeader.setBounds(200, 25, 1146, 32);
		jp_center.setBounds(200, 60, 1146, 600);
		jl_mid_header.setBounds(400, 100, 800, 25);
		jp_mid_body.setBounds(400, 126, 800, 500);
		jl_infoL1.setBounds(440, 508, 700, 15);
		jl_infoL2.setBounds(440, 524, 700, 15);
		jl_infoL3.setBounds(440, 540, 700, 15);
		jl_infoR1.setBounds(440, 556, 700, 15);
		jl_infoR2.setBounds(440, 572, 700, 15);
		jl_infoR3.setBounds(440, 588, 700, 15);
		jp_mid_body.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0),1)));
		jl_infoL1.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_infoL1.setForeground(new Color(80, 80, 80));
		jl_infoL2.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_infoL2.setForeground(new Color(80, 80, 80));
		jl_infoL3.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_infoL3.setForeground(new Color(80, 80, 80));
		jl_infoR1.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_infoR1.setForeground(new Color(80, 80, 80));
		jl_infoR2.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_infoR2.setForeground(new Color(80, 80, 80));
		jl_infoR3.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_infoR3.setForeground(new Color(80, 80, 80));
		jl_seatHeader.setOpaque(true);
		jl_seatHeader.setBackground(new Color(190, 190, 190));
		jl_seatHeader.setHorizontalAlignment(JLabel.CENTER);
		jl_mid_header.setBackground(Color.black);
		jl_mid_header.setForeground(Color.white);
		jl_mid_header.setOpaque(true);

		this.add(jl_seatHeader);
		this.add(jl_mid_header);
		this.add(jl_infoL1);
		this.add(jl_infoL2);
		this.add(jl_infoL3);
		this.add(jl_infoR1);
		this.add(jl_infoR2);
		this.add(jl_infoR3);
		this.add(jp_mid_body);
		this.add(jp_center);
	}
	
	public static void main(String[] args) {
		MovieServer ms = new MovieServer();
		ms.display();
		Thread th = new Thread(ms);
		th.start();
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
		
		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
		
		//라디오버튼 클릭 안되어있다면
		mmv.jp_mrv.jbt_backSeatChoice.setBackground(new Color(52, 152, 219));
		mmv.jp_mrv.jbt_backSeatChoice.setForeground(Color.white);	
		mmv.jp_mrv.jbt_backSeatChoice.setEnabled(true);
		mmv.jp_mrv.jbt_backSeatChoice.setVisible(true);

		mmv.jp_mrv.jbt_goPayInfo.setBackground(new Color(230, 230, 230));
		mmv.jp_mrv.jbt_goPayInfo.setForeground(Color.gray);
		mmv.jp_mrv.jbt_goPayInfo.setVisible(true);
		mmv.jp_mrv.jbt_goPayInfo.setEnabled(false);

		mmv.jp_mrv.jbt_goSeatChoice.setVisible(false);
		/******************************************************************
		 * 얘네 되어있어야함
		 * mmv.jp_mrv.jbt_backMovieChoice.setVisible(true);
		 * mmv.jp_mrv.jbt_backSeatChoice.setVisible(false);
		 * mmv.jp_mrv.jbt_goSeatChoice.setVisible(false);
		 * mmv.jp_mrv.jbt_goPayChoice.setVisible(true);
		 * mmv.jp_mrv.jbt_goPayInfo.setVisible(false);	
		******************************************************************/

		//라디오버튼 하나라도 클릭한다면
//		mmv.jp_mrv.jbt_backSeatChoice.setBackground(new Color(52, 152, 219));
//		mmv.jp_mrv.jbt_backSeatChoice.setForeground(Color.white);
//		mmv.jp_mrv.jbt_backSeatChoice.setEnabled(true);
//		mmv.jp_mrv.jbt_backSeatChoice.setVisible(true);
//
//		mmv.jp_mrv.jbt_goPayInfo.setBackground(new Color(52, 152, 219));
//		mmv.jp_mrv.jbt_goPayInfo.setForeground(Color.white);
//		mmv.jp_mrv.jbt_goPayInfo.setVisible(true);
//		mmv.jp_mrv.jbt_goPayInfo.setEnabled(true);
	}
}