package maxmovie;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MemInfoView extends JPanel {


	JLabel					jl_pageInfoLeft 	= new JLabel();
	JLabel					jl_pageInfoRight	= new JLabel("님의 회원정보");
	JLabel					jl_id				= new JLabel("아이디");
	JLabel					jl_mem_id			= new JLabel();
	JLabel					jl_pw				= new JLabel("비밀번호");	
	JPasswordField 			jpf_pw 				= new JPasswordField();
	JLabel					jl_pw_warning		= new JLabel("비밀번호가 일치하지 않습니다.");
	JButton 				jbt_modified 		= new JButton("회원정보수정");
	EventMapping 			em 					= null;
	
	public MemInfoView(EventMapping em) {
		this.em = em;
		initDisplay();
	}
	public void initDisplay() {
		this.setLayout(null);
		this.setBackground(new Color(215, 215, 215));
//		현접속유저아이디
//		jl_pageInfoLeft.setText(em.mmv.mem_nick);
		jl_pageInfoLeft.setText("kong");
		jl_mem_id.setText("cloudsky7");
		
		/*************************************************
		 * 아이디랑 비번 불일치시
		 * jl_pw_warning.setVisible(false);
		 * 
		 * 일치시
		 * jl_pw_warning.setVisible(true);
		 *************************************************/
		jl_pw_warning.setVisible(false);
		jpf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jl_pageInfoLeft.setBounds(260, 110, 160, 100);
		jl_pageInfoRight.setBounds(430, 110, 170, 100);
		jl_id.setBounds(260, 170, 50, 40);
		jl_mem_id.setBounds(320, 170, 200, 40);
		jl_pw.setBounds(260, 212, 50, 40);
		jpf_pw.setBounds(312, 212, 296, 40);
		jbt_modified.setBounds(314, 254, 292, 35);
		jl_pw_warning.setBounds(612, 220, 300, 28);

		jl_id.setHorizontalAlignment(JLabel.RIGHT);
		jl_pageInfoLeft.setHorizontalAlignment(JLabel.RIGHT);
		jl_pageInfoLeft.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoLeft.setForeground(Color.black);
		jl_pageInfoRight.setFont(new Font("굴림체", Font.BOLD, 24));
		jl_pageInfoRight.setForeground(Color.black);
		jl_id.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_id.setForeground(Color.black);
		jl_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_pw.setForeground(Color.black);
		jpf_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
		jpf_pw.setForeground(new Color(142, 142, 142));
		jl_pw_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_pw_warning.setForeground(Color.red);
		jbt_modified.setBackground(new Color(52, 152, 219));
		jbt_modified.setForeground(Color.white);
		
		this.add(jl_pageInfoLeft);
		this.add(jl_pageInfoRight);
		this.add(jl_id);
		this.add(jl_mem_id);
		this.add(jl_pw);
		this.add(jpf_pw);
		this.add(jl_pw_warning);
		this.add(jbt_modified);
		jpf_pw.addActionListener(em);
		jbt_modified.addActionListener(em);
	}

	

}
