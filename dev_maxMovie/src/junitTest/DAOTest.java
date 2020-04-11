package junitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import maxmovie.MovieDao;
import maxmovie.TicketingVO;

class DAOTest {
	MovieDao dao = new MovieDao();
	List<Map<String, Object>>	v = new Vector<Map<String, Object>>();
//	@Test
//	void testGet_SeatStatus() {
//		TicketingVO ptVO = new TicketingVO();
//		ptVO.setTheater("해운대점");
//		ptVO.setMovie_screen("1관");
//		ptVO.setMovie_date("20200409");
//		ptVO.setMovie_time("23:10");
//		List<Map<String, Object>> seatList = dao.get_SeatStatus(ptVO);
//		for(Map<String, Object> rmap:seatList) {
//			System.out.println(rmap.get("좌석").toString() +", "+ rmap.get("현황").toString());
//		}
////		List someList = someClass.getSomeList();
////		assertNotNull("조회결과 null", someList);
////		assertTrue(someList.size() > 0); 
////		assertEquals(3, someList.size()); 
//	}

	@Test
	void testProc_SeatStatus() {
		TicketingVO ptVO = new TicketingVO();
		ptVO.setTheater("해운대점");
		ptVO.setMovie_screen("1관");
		ptVO.setMovie_date("20200409");
		ptVO.setMovie_time("23:10");
		List<Map<String, Object>> seatList = dao.proc_SeatStatus(ptVO);
		for(Map<String, Object> rmap:seatList) {
			System.out.println(rmap.get("좌석").toString() +", "+ rmap.get("현황").toString());
		}
	}

}
