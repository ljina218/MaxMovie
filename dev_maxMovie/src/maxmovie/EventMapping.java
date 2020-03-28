package maxmovie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class EventMapping implements ActionListener{
	
	/* 뷰 패널들의 변수이름
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
	*/
	
	MaxMovieView iv = null;
	MovieController ctrl = new MovieController();
	public EventMapping(MaxMovieView iv) {
		this.iv = iv;
	}
	
	//서버스레드에게 말하기 위한 메소드
	private void send(String msg) {
		try {
			iv.oos.writeObject(msg);
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
