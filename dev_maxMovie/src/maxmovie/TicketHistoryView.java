package maxmovie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TicketHistoryView extends JPanel  {

	JLabel					jl_pageInfoLeft 		= new JLabel();
	JLabel					jl_pageInfoRight		= new JLabel("님의 회원정보");
	JLabel					jl_pageInfo				= new JLabel("님의 예매 내역");
	String 					col_history[] 			= {"영화이름", "지점/지역", "상영날짜/시간", "상영관/좌석", "예매번호"};
	String 					data_history[][] 		= {{"이보다 더 좋을 순 없다.", "서울/강남", "2020-04-01  18:00:00", "1관/A2", "9303-2048-5189615"},{"좋은 아씨들", "부산/해운대", "2020-04-02  18:00:00", "2관/F4", "9303-2048-5189616"}};
	DefaultTableModel 		dtm_history				= new DefaultTableModel(data_history, col_history);
	JTable 					jt_history				= new JTable(dtm_history);
	JScrollPane 			jsp_history				= new JScrollPane(jt_history);
	EventMapping 			em 						= null;
	
	public TicketHistoryView(EventMapping em) {
		this.em = em;
		initDisplay();
	}
	
	public void initDisplay(){
		this.setLayout(null);
		this.setBackground(new Color(215, 215, 215));
//		jl_pageInfoLeft.setText(em.mmv.mem_nick);
		jl_pageInfoLeft.setText("kong");
		
		DefaultTableCellRenderer dtcr_local = new DefaultTableCellRenderer();
		dtcr_local.setHorizontalAlignment(JLabel.CENTER);
		jt_history.getColumn("영화이름").setCellRenderer(dtcr_local);
		jt_history.getColumn("지점/지역").setCellRenderer(dtcr_local);
		jt_history.getColumn("상영날짜/시간").setCellRenderer(dtcr_local);
		jt_history.getColumn("상영관/좌석").setCellRenderer(dtcr_local);
		jt_history.getColumn("예매번호").setCellRenderer(dtcr_local);
		jt_history.setRowHeight(35);
		jt_history.setBackground(new Color(200, 200, 200));
		
		jl_pageInfoLeft.setHorizontalAlignment(JLabel.RIGHT);
		jl_pageInfoLeft.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoLeft.setForeground(Color.black);
		jl_pageInfoRight.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoRight.setForeground(Color.black);
		jl_pageInfoLeft.setBounds(260, 0, 160, 100);
		jl_pageInfoRight.setBounds(430, 0, 170, 100);
		jsp_history.setBounds(40, 80, 840, 330);
		
		this.add(jl_pageInfoLeft);
		this.add(jl_pageInfoRight);
		this.add(jsp_history);
	}

	
	public static void main(String[] args) {
		MaxMovieView mmv = new MaxMovieView();
		mmv.jp_lv.setVisible(false);
		mmv.jp_mrv.setVisible(false);
		mmv.jp_mrv.jp_mcv.setVisible(false);
		mmv.jp_mrv.jp_scv.setVisible(false);
		mmv.jp_mrv.jp_pv.setVisible(false);
		mmv.jp_mv.setVisible(true);
		mmv.jp_mv.jp_thv.setVisible(true);
		mmv.jp_mv.jp_miv.setVisible(false);
		mmv.jp_mv.jp_muv.setVisible(false);
		mmv.jp_rv.setVisible(false);
		
		
		mmv.jl_logo_small.setVisible(true);
		mmv.jl_nickInfo.setVisible(true);
		mmv.jl_nickInfoEnd.setVisible(true);
		mmv.jbt_logout.setVisible(true);
		mmv.jbt_myPage.setVisible(true);
		mmv.jbt_ticketing.setVisible(true);
		
	}

}
