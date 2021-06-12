package com.kh.interactFunding.funding.controller;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.interactFunding.common.util.HelloSpringUtils;
import com.kh.interactFunding.funding.model.service.FundingService;
import com.kh.interactFunding.funding.model.vo.Attachment;
import com.kh.interactFunding.funding.model.vo.Funding;
import com.kh.interactFunding.funding.model.vo.FundingExt;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/funding")
@Slf4j
public class FundingController {
	@Autowired
	private FundingService fundingService;
	
	//파일 저장시 사용 
	@Autowired
	private ServletContext application;
	
	//김윤수(test)
	
	//김경태 졸리다
	
	//김주연
	@GetMapping("/fundingStart1")
	public void fundingStart1() {
		log.debug("fundingStart1");
	}

	
	@PostMapping("/savePhone")
	public String savePhone() {
		
		return "redirect:/funding/fundingStart2";
	}
	
	@GetMapping("/fundingStart2")
	//public void fundingStart2(Funding funding, HttpSession session) {
	public void fundingStart2(HttpSession session) {
		//ready1FundingInsertNo(funding);
		log.debug("fundingStart2");
		//log.debug("funding_no={}",funding.getFunding_no());
		//session.setAttribute("funding", funding);
		Funding funding = new Funding();
		funding.setFundingNo(18);
		log.debug("funding_no={}",funding.getFundingNo());
		session.setAttribute("funding", funding);
	}
	@ResponseBody
	public void ready1FundingInsertNo(Funding funding) {
		try {
			log.debug("ready1Funding");
			int result =  fundingService.ready1FundingInsertNo(funding);
			log.debug("result={}",result);
		} catch (Exception e) {
			log.error("펀딩 start 에러(funding_no 부여)",e);
		}
	}
	
	@GetMapping("/fundingStart3")
	public void fundingStart3() {
		log.debug("fundingStart3");
	}
	@GetMapping("/fundingStart4")
	public void fundingStart4() {
		log.debug("fundingStart4");
	}
	@GetMapping("/fundingStart5")
	public void fundingStart5() {
		log.debug("fundingStart5");
	}
	@GetMapping("/ready1Funding")
	public void ready1Funding() {
		log.debug("ready1Funding");
	}
	
	@GetMapping("/ready2Charge")
	public void ready2Funding() {
		log.debug("ready2Charge");
	}
	@PutMapping("/saveCharge/{no}/{charge}")
	public String saveCharge(@PathVariable String no ,@PathVariable String charge, RedirectAttributes redirectAttr) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("no",no);
			param.put("charge",charge);
			log.debug("param={}",param);
			int result =  fundingService.saveCharge(param);
			redirectAttr.addFlashAttribute("msg","저장되었습니다.");
		} catch (Exception e) {
			log.error("saveCharge 등록 에러",e);
			throw e;
		}
		return "redirect:/funding/ready1Funding";
	}
	
	@GetMapping("/ready3BasicInfo")
	public void ready4Funding() {
		log.debug("ready3BasicInfo");
	}
	@PostMapping("/saveBasicInfo")
	public String  saveBasicInfo(
			@ModelAttribute FundingExt funding,
			@RequestParam(name="upFile") MultipartFile[] upFiles,
			RedirectAttributes redirectAttr
			) throws Exception {
		log.debug("funding = {}",funding);
		log.debug("upFiles = {}",upFiles);
		try {
			String saveDirectory = application.getRealPath("/resources/upload/board");
			log.debug("saveDirectory ={}",saveDirectory);
			
			//디렉토리 생성
			File dir = new File(saveDirectory);
			if(!dir.exists())
				dir.mkdirs(); //복수개의 디렉토리를 생성
			
			List<Attachment> attachList = new ArrayList<>();
			
			for(MultipartFile upfile : upFiles) {
				//input[name=upFile]로부터 비어있는 upFile이 넘어온다.
				if(upfile.isEmpty()) continue; 
				
				String renamedFilename = 
						HelloSpringUtils.getRenamedFilename(upfile.getOriginalFilename());
				
				//a. 서버컴퓨터에 저장
									// 부모디렉토리, 파일명
				File dest = new File(saveDirectory, renamedFilename);
				upfile.transferTo(dest); //파일이동
				
				//b. 저장된 데이터를 Attachment객체에 저장 및 list 추가
				Attachment attach = new Attachment();
				attach.setOriginalFilename(upfile.getOriginalFilename());
				attach.setRenamedFilename(renamedFilename);
				attachList.add(attach);
			}
			
			log.debug("attachList = {}" ,attachList);
			//board객체에 설정
			funding.setAttachList(attachList);
			
			//2. 업무로직 : DB 저장 board, attachment
			int result = fundingService.saveBasicInfo(funding);
			
			//3. 사용자피드백 & 리다이렉트
			redirectAttr.addFlashAttribute("msg","기본정보를 저장하였습니다.");
			
			}catch(Exception e){
				log.error("게시물 등록 오류", e);
				throw e;
			}
			return "redirect:/funding/ready1Funding";
	}
	
	@GetMapping("/ready4Story")
	public void ready5Funding() {
		log.debug("ready4Story");
	}
	@PostMapping("/saveStory")
	public String saveStory(Funding funding){
		try {
			log.debug("funding={}",funding);

			int result = fundingService.saveStory(funding);
			
			log.debug("funding = {}",funding);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return "redirect:/funding/ready1Funding";
	}
	@GetMapping("/ready5Reward")
	public void ready6Funding() {
		log.debug("ready5Reward");
	}
	
	
	
	//박요한 push
	@GetMapping("/news.do")
	public void news() {
		
	}
	
	@GetMapping("/community.do")
	public void community() {
		
	}
	
	@GetMapping("/supporter.do")
	public void supporter() {
		
	}
	
	@GetMapping("newsView.do")
	public void newsView() {
		
	}
	
	//배기원(test 해보겠습니다)
	@ResponseBody
	@GetMapping("fundinglike")
	public List<Funding> indexfundinglike(Model model ,HttpSession session){
		log.debug("1111");
		List<Funding> likeList=null;
		try {
		likeList =fundingService.indexfundinglike();
		log.info("likeList={}",likeList);
		}catch (Exception e) {
			log.error("메인페이지 좋아요가 안됩니다",e);
			throw e;
		}
		return likeList;
	}
	 
	 
	
	//이승우
	//흠흠
	@GetMapping("/fundingList")
	public ModelAndView fundingList(
			ModelAndView mav,
			@RequestParam(required = false, defaultValue = "") String searchKeyword,
			@RequestParam(required = false, defaultValue = "") String searchSelect1,
			@RequestParam(required = false, defaultValue = "") String searchSelect2
		) {
		Map<String, Object> map = new HashMap<>();
		map.put("searchKeyword", searchKeyword);
		map.put("searchSelect1", searchSelect1);
		map.put("searchSelect2", searchSelect2);
		log.debug("searchTitle = {}", searchKeyword);
		
		// 업무로직
		try {
			List<Funding> list = fundingService.fundingList(map);
			System.out.println("list"+list);
			log.debug("list = {}", list);
			//jsp에 위임
			mav.addObject("list", list);
			
			return mav;
		}
		catch(Exception e){
			log.error("fundingList 조회 오류");
			throw e;
		}
	}
	
	@GetMapping("/earlyList")
	public void earlyList() {
		
	}
	
	//천호현
	/*
	* @GetMapping("/fundingDetail") public void fundingDetail(@RequestParam int
	* funding_no, Model model) { //1. 업무로직 Funding funding =
	* fundingService.selectOneFunding(funding_no); log.debug("funding = {}" ,
	* funding); //2. 위임 model.addAttribute("funding", funding);
	*
	* }
	*/

	@GetMapping("/fundingDetail")
	public Map<String, Object> fundingDetail(@RequestParam int funding_no) {
		//1. 업무로직
		List<Funding> list = fundingService.selectFunding(funding_no);

		log.debug("list = {}" , list);
		//2. 위임
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		return map;

	}
	
	
	@GetMapping("/fundingReward")
	public void fundingReward() {
	}
	@GetMapping("/fundingChatMaker")
	public void fundingChatMaker() {
	}
	@GetMapping("/fundingPayment")
	public void fundingPayment() {
	}
	@GetMapping("/fundingFindAddress")
	public void fundingFindAddress() {
	}
	
	
}
	