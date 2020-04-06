package hjho;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class AdminLoginView extends JFrame implements ActionListener{
	JLabel 			jl_logo_m 		= new JLabel() {
		public void paint(Graphics g) {
			Image img = null;
			try {
				img = ImageIO.read(new File("src\\maxmovie\\maxmovielogom.png"));
				g.drawImage(img, 0, 0, 300, 140, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};	
	String nickName = null; //닉네임 등록
	String id = null;
	AdminDao aDao = new AdminDao();
	Dimension 		res 			= Toolkit.getDefaultToolkit().getScreenSize();
	JPanel			jp 					= new JPanel();
	JLabel			jl_id				= new JLabel("아이디");
	JLabel			jl_pw				= new JLabel("비밀번호");	
	JTextField 		jtf_id 				= new JTextField();
	JPasswordField 	jpf_pw 				= new JPasswordField();
	JLabel			jl_id_warning		= new JLabel();
	JLabel			jl_pw_warning		= new JLabel();
	//JButton 		jbt_join 			= new JButton("회원가입");
	JButton 		jbt_login 			= new JButton("로그인");
	
	public AdminLoginView() {
		initDisplay();
	}
	private void initDisplay() {
		jp.setLayout(null);
		
		jtf_id.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jpf_pw.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
		jl_id_warning.setText("아이디가 존재하지 않습니다.");
		jl_pw_warning.setText("비밀번호가 일치하지 않습니다.");
		jl_id.setHorizontalAlignment(JLabel.RIGHT);
		jl_pw.setHorizontalAlignment(JLabel.RIGHT);
		jl_id_warning.setVisible(false);
		jl_pw_warning.setVisible(false);
		
		jl_logo_m.setBounds(612, 170, 300, 140);
		jl_id.setBounds(560, 310, 50, 40);
		jl_pw.setBounds(560, 352, 50, 40);
		jtf_id.setBounds(612, 310, 296, 40);
		jpf_pw.setBounds(612, 352, 296, 40);

		//jbt_join.setBounds(612, 394, 146, 40);
		jbt_login.setBounds(612, 394, 300, 40);
		jl_id_warning.setBounds(912, 318, 300, 28);
		jl_pw_warning.setBounds(912, 360, 300, 28);
		
		jl_id.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_id.setForeground(Color.black);

		jtf_id.setFont(new Font("굴림체", Font.PLAIN, 12));
		jtf_id.setForeground(new Color(142, 142, 142));
				
		jl_id_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_id_warning.setForeground(Color.red);

		jl_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
		jl_pw.setForeground(Color.black);
		
		jpf_pw.setFont(new Font("굴림체", Font.PLAIN, 12));
		jpf_pw.setForeground(new Color(142, 142, 142));

		jl_pw_warning.setFont(new Font("굴림체", Font.PLAIN, 11));
		jl_pw_warning.setForeground(Color.red);
		
//		jbt_join.setBackground(new Color(52, 152, 219));
//		jbt_join.setForeground(Color.white);
		
		jbt_login.setBackground(new Color(52, 152, 219));
		jbt_login.setForeground(Color.white);
		
		jp.add(jl_logo_m);
		jp.add(jl_id);
		jp.add(jl_pw);
		jp.add(jtf_id);
		jp.add(jpf_pw);
		jp.add(jl_id_warning);
		jp.add(jl_pw_warning);
		//jp.add(jbt_join);
		jp.add(jbt_login);
		this.setTitle("MaxMovie 지점용 로그인 창");
		this.setBackground(Color.white);
		this.add(jp);
		this.setSize(1500, 800);
		this.setVisible(true);
/*******************************************************
 * 		●#login 13	jl_id_warning.setVisible(false);
 * 		●#login 14	jl_id_warning.setVisible(true);
 * 		○#login 17	jl_pw_warning.setVisible(false);
 * 		○#login 18	jl_pw_warning.setVisible(true);
 *******************************************************/
		jtf_id.addActionListener(this);
		jpf_pw.addActionListener(this);
		jbt_login.addActionListener(this);
//		jbt_join.addActionListener(this);
	}
	
	public static void main(String[] args) {
		new AdminLoginView();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(jbt_login==obj
		 ||jtf_id==obj||jpf_pw==obj) {
			if("".equals(jtf_id.getText()) || "".equals(jpf_pw.getText())) {
				JOptionPane.showMessageDialog(this, "아이디와 비번을 확인하세요");
				return; //더 이상 진행을 할 필요가 없다.
			}
			try {
				String admin_id = jtf_id.getText();
				String admin_pw = jpf_pw.getText();
				String admin_name = aDao.login(admin_id, admin_pw);
				//System.out.println("result:"+result);
				if("비밀번호가 맞지 않습니다.".equals(admin_name)) {
					JOptionPane.showMessageDialog(this, "비밀번호가 맞지 않습니다.");
					jpf_pw.setText("");
					return;
				}
				else if("아이디가 존재하지 않습니다.".equals(admin_name)) {
					JOptionPane.showMessageDialog(this, "아이디가 존재하지 않습니다.");
					jtf_id.setText("");
					jpf_pw.setText("");
					return;
				}
				else {
					nickName = admin_name;
					id = admin_id;
					this.setVisible(false); //로그인 화면은 비 활성화
					jtf_id.setText(""); 	//입력한 아이디 초기화
					jpf_pw.setText("");		//입력한 비번도 초기화
					
					AdminClient tc = new AdminClient(this, nickName, id); //생성자 호출
				}
				System.out.println("admin_name:"+admin_name);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
}
