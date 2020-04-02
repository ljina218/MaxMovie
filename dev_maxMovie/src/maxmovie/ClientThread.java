package maxmovie;

import java.util.StringTokenizer;

public class ClientThread extends Thread{
	
	MaxMovieView mmv = null;
	
	boolean stop = false;

	public ClientThread(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	//화면전환 할 때 사용하는 제어 메소드
		public void display(boolean lv, boolean mrv, boolean mcv, boolean scv, boolean pv,
							boolean mv, boolean thv, boolean miv, boolean muv, boolean rv) {
			mmv.jp_lv.setVisible(lv);
			mmv.jp_mrv.setVisible(mrv);
			mmv.jp_mrv.jp_mcv.setVisible(mcv);
			mmv.jp_mrv.jp_scv.setVisible(scv);
			mmv.jp_mrv.jp_pv.setVisible(pv);
			mmv.jp_mv.setVisible(mv);
			mmv.jp_mv.jp_thv.setVisible(thv);
			mmv.jp_mv.jp_miv.setVisible(miv);
			mmv.jp_mv.jp_muv.setVisible(muv);
			mmv.jp_rv.setVisible(rv);
		}
	
	
	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		String msg = null;
		int protocol = 0;
		try {
			while(!stop) {
				//msg = (String)mmv.ois.readObject();
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer("","#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				//프로토콜에 따라 send 파라미터 달라지는...
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
}
