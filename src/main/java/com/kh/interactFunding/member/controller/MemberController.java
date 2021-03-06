package com.kh.interactFunding.member.controller;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.kh.interactFunding.funding.model.service.FundingService;
import com.kh.interactFunding.funding.model.vo.Funding;
import com.kh.interactFunding.member.model.service.MemberService;
import com.kh.interactFunding.member.model.vo.Coupon;
import com.kh.interactFunding.member.model.vo.Member;
import com.kh.interactFunding.member.model.vo.Msg;
import com.kh.interactFunding.member.model.vo.Point;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/member")
@SessionAttributes({"loginMember","next","receive","send"})
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FundingService fundingService;

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	final String username = "interact.funding";
	final String password = "if1234!!!";
	
	private final String KAKAO_API_KEY = "df9569096cc5618b81581186dfe78bb2";
	private String KAKAO_REDIRECT_URL = "http://interact-funding.kro.kr/member/auth/kakao";
	//?????????
	@GetMapping("/login")
	public void login(@SessionAttribute(required = false) String next ,
					  @RequestHeader (name = "Referer", required = false) String referer, 
					  Model model) {
		log.info("referer@login = {}", referer);
		log.debug("next@login = {}",next);
		//????????? ????????? ???????????? ???????????? ????????????, ???????????? ????????? ????????? ??????????????????
		if(referer != null && next==null) {
			model.addAttribute("next", referer);
			log.debug("next ????????? ??????");
		}
	}
	
	@ResponseBody
	@GetMapping("saveEmail")
	public String saveEmail(@RequestParam Boolean saveEmail, @RequestParam String email, HttpServletResponse response, HttpServletRequest request) {
		String data;
		//????????? ???????????? ??????
		Cookie c = new Cookie("saveEmail", email);
		c.setPath(request.getContextPath()+"/"); //path ????????? ????????? url
		if(saveEmail) {
			c.setMaxAge(60 * 60 * 24 * 7); //7????????? ??????????????? ?????? 
			data = "????????? ?????? ??????";
		}else {
			c.setMaxAge(0); //0?????? ???????????? ?????? ??????, ????????? ???????????? session????????? ?????? 
			data = "????????? ?????? ??????";
		}
		response.addCookie(c);
		return data;
	}
	
	@GetMapping("findid")
	public void findid() {
	}
	
	@PostMapping("findid")
	public String findid(Member member, RedirectAttributes redirectAttr, HttpServletRequest request) {
		log.debug("member@findid = {}", member);
		String name = member.getName(); 
		member = memberService.selectOneMember(member);
		if(member==null || !member.getName().equals(name)) {
			redirectAttr.addFlashAttribute("msg","????????? ????????? ????????????.");
			return "redirect:/member/findid";
		}
		
		
		//???????????? ??????
		int ran = (int)(Math.random()*1000000);
		DecimalFormat df = new DecimalFormat("000000");
		String code = df.format(ran);
		
		//??????????????? ????????? ??????????????? ?????? ??????
		Map<String, String> check = memberService.selectOneCertification(member);
		if(check!=null) {
			code = check.get("certificationCode");
			log.debug("?????????????????? ?????? ?????? ??????????????? = {}",code);
		}else {
			//???????????? db??? ??????
			Map<String, Object> param = new HashMap<>();
			param.put("member", member);
			param.put("code", code);
			int result = memberService.insertCertificationCode(param);
		}
		//???????????? ????????? ????????? ?????? email??????
		String url = "http://interact-funding.kro.kr";
		Properties props = new Properties();
		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "25");
		props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.EnableSSL.enable", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("if"));//
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(member.getEmail()));//????????????????????? ???????????????
			message.setSubject("[??????] ???????????? ?????????????????????.","utf-8");//??????
			message.setContent(new MimeMultipart());
			Multipart mp = (Multipart) message.getContent();
			mp.addBodyPart(
					getContents("<a href='"+url+"/member/newPassword?memberNo="+member.getMemberNo()+"&code="+code+"'>???????????? ????????????</a>"));
			Transport.send(message);
			log.debug("????????? ?????? ??????");
			redirectAttr.addFlashAttribute("msg","???????????? ?????? ??????????????????????????????!");

		} catch (Exception e) {
			log.error("??????????????? ??????");
			redirectAttr.addFlashAttribute("msg","?????? ????????? ?????? ???????????????");
			return "redirect:/member/findid";
		}
		return "redirect:/member/login";
	}
	
	@GetMapping("/newPassword")
	public String newPassword(@RequestParam(required = false) String code, @RequestParam(required = false) String memberNo, RedirectAttributes redirect, Model model){
		log.debug("code = {}", code);
		log.debug("memberNo = {}", memberNo);
		if(code==null || memberNo==null) {
			redirect.addFlashAttribute("msg","???????????? ?????? ???????????????.");
			return "redirect:/";
		}
		Member member = memberService.selectOneMemberUseNo(Integer.parseInt(memberNo));
		Map<String, String> check = memberService.selectOneCertification(member);
		if(check==null) {
			redirect.addFlashAttribute("msg","???????????? ?????? ???????????????.");
			return "redirect:/";
		}
		
		if(Integer.parseInt(code)!=Integer.parseInt(check.get("certificationCode"))) {
			redirect.addFlashAttribute("msg","???????????? ?????? ???????????????.");
			return "redirect:/";
		}
		
		model.addAttribute("code",code);
		model.addAttribute("memberNo",memberNo);
		return "/member/newPassword";
	}
	
	@PostMapping("/newPassword")
	public String newPassword2(String memberNo, String password, RedirectAttributes redirect, Model model) {
		int memberNoo = Integer.parseInt(memberNo);
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", memberNoo);
		Member member = memberService.selectOneMemberUseNo(memberNoo);
		member.setPassword(password);
		map.put("password", bCryptPasswordEncoder.encode(member.getPassword()));
		
		//changePassword??? ???????????????????????? ???????????? ??????, ???????????? ??????
		int result = memberService.changePassword(map);
		if(result==0) {
			redirect.addFlashAttribute("msg","????????????");
			return "redirect:/";
		}
		redirect.addFlashAttribute("msg","???????????? ????????? ?????? ???????????????.");
		return "redirect:/member/login";
	}
	
	//??????????????? ???????????? MyCustomLoginSuccessHandler ??? @GetMapping("saveEmail")-ajax??? ????????? ????????????
//	@PostMapping("/login_if")
//	public String login_if(
//			Member member, 
//			@RequestParam(required = false) String remember,
//			Model model, 
//			@SessionAttribute(required = false) String next,
//			RedirectAttributes redirectAttr,
//			HttpServletRequest request, 
//			HttpServletResponse response) {
//		
//		log.debug("member = {} ", member);
//		log.debug("remember = {}", remember);
//		log.debug("next = {}",next);
//		
//		//????????? ??????
//		Member login = memberService.selectOneMember(member);
//		
//		if(login != null && bCryptPasswordEncoder.matches(member.getPassword(), login.getPassword())) {
//			model.addAttribute("next",null);
//			model.addAttribute("loginMember",login);
//			redirectAttr.addFlashAttribute("msg","????????? ??????");
//			
//			//????????? ???????????? ??????
//			Cookie c = new Cookie("saveEmail", member.getEmail());
//			c.setPath(request.getContextPath()+"/"); //path ????????? ????????? url
//			
//			if(remember != null) {
//				c.setMaxAge(60 * 60 * 24 * 7); //7????????? ??????????????? ?????? 
//			}
//			else {
//				//saveId ???????????????
//				c.setMaxAge(0); //0?????? ???????????? ?????? ??????, ????????? ???????????? session????????? ?????? 
//			}
//			response.addCookie(c);
//		}
//		else {
//			redirectAttr.addFlashAttribute("msg","????????? ??????");
//			return "redirect:/member/login";
//		}
//		return "redirect:"+ (next != null ? next : "/");
//	}
	
	//??????????????? ???????????? ???????????? ??????
//	@GetMapping("/logout")
//	public String logout(
//			Model model
//			) {
//		model.addAttribute("loginMember",null);
//		return "redirect:/";
//	}
	
	@GetMapping("/memberEnroll")
	public void memberEnroll() {
		
	}
	@GetMapping("memberEnroll_if")
	public void memberEnroll_if(){
		
	}

	@PostMapping("/memberEnroll_if")
	public String memberEnroll_if(Member member, Model model, RedirectAttributes redirectAttr) {
		//?????????, ????????????, ?????? ?????????
		log.debug("member={}",member);
		
		//???????????? ?????????
		member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
		member.setPlatform("IF");
		
		int result = memberService.insertMemberIf(member);
		redirectAttr.addFlashAttribute("msg","??????????????????");
		return "redirect:/member/login";
	}
	
	@GetMapping("enrollAuthenticationCode")
	public void enrollAuthenticationCode(@RequestParam String email, HttpServletResponse response) throws IOException {
		log.debug("email = {}",email);
		
		//?????? 6?????? ???????????? ??????
		int rnd = (int)(Math.random()*1000000);
		DecimalFormat df = new DecimalFormat("000000");
		log.debug("code = {}", df.format(rnd));
		
		//response????????? ?????? ???????????? ?????????????????? ??????
		response.setContentType("text/plain; charset=utf-8");
		
		//???????????? ???????????? ??????????????? ??????
		// 1. ????????? ?????????????????? ????????????.
		//String memberEmailId = request.getParameter("memberEamilId");
		//??????????????? ???????????? ??????
		Member blackMember = new Member();
		blackMember.setEmail(email);
		int black = memberService.selectOneBlackList(blackMember);
		if(black>0) {
			response.getWriter().print("B");
			return;
		}
		
		
		//????????? ????????????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		int result = memberService.selectEmailCheck(map);
		log.debug("result = {}",result);
		if(result>0) {
			response.getWriter().print("N");
			return;
		}
		
		response.getWriter().print(df.format(rnd));
		
		//??????????????? ??????
		
		//????????? ????????? ???????????? ???????????? ??????
		Properties props = new Properties();
		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "25");
		props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.EnableSSL.enable", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("if"));//
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));//????????????????????? ???????????????
			message.setSubject("[??????] ????????? ?????????????????????.","utf-8");//??????
			message.setContent(new MimeMultipart());
			Multipart mp = (Multipart) message.getContent();
			mp.addBodyPart(
					getContents("???????????? : "+df.format(rnd)));
			Transport.send(message);
			log.debug("????????? ?????? ??????");

		} catch (Exception e) {
			log.error("??????????????? ??????");
		}
	}
	
	private BodyPart getContents(String html) throws MessagingException {
		BodyPart mbp = new MimeBodyPart();
		// setText??? ????????? ?????? ?????? ????????? ???????????? ????????????.
//		 mbp.setText(html);
		// html ???????????? ??????
		mbp.setContent(html, "text/html; charset=utf-8");
		return mbp;
	}
	
	
	//???????????????
	@GetMapping("/memberDetails")
	public void memberDetails(@SessionAttribute(required = false) Member loginMember, Model model) {
		//???????????? ?????? ???????????? ??????????????? ????????? ??????
		if(loginMember==null) return;
		
		//?????? ?????? ?????? ????????? ????????? ??????5??? ?????? ????????????
		List<Integer> noList = fundingService.selectMyLikeNoList(loginMember.getMemberNo());
		log.debug("?????? ????????? ?????? ????????? : {}",noList);
		
		//???????????? ?????? ????????? ????????????
		List<Funding> list = new ArrayList<>();
		for(int x : noList) {
			list.add(fundingService.selectOneFundingKYS(x));
		}
		log.debug("?????? ???????????? ??????????????? = {}",list);
		model.addAttribute("list",list);
		
		//?????? ????????? ????????? ??????
		int particiCnt = fundingService.selectMyPartiCnt(loginMember.getMemberNo());
		log.debug("?????? ????????? ????????? ?????? : {}", particiCnt);
		model.addAttribute("particiCnt",particiCnt);
		
		//?????? ????????? ????????? ??????
		int createCnt = fundingService.selectMyCreateCnt(loginMember.getMemberNo());
		log.debug("?????? ????????? ????????? ?????? : {}", createCnt);
		model.addAttribute("createCnt",createCnt);
		
		//????????? ?????? ??????
		List<Point> pList = fundingService.selectMyPointList(loginMember.getMemberNo());
		log.debug("pList = {}",pList);
		model.addAttribute("pList", pList);
	}
	
	//??????????????? ???????????????
	@ResponseBody
	@PostMapping("addPoint")
	public Map<String, Object> addPoint(int memberNo, int point, String memo, @SessionAttribute Member loginMember, Model model) {
		log.debug("memberNo={}",memberNo);
		log.debug("point={}",point);
		log.debug("memo={}",memo);
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", memberNo);
		map.put("point", point);
		map.put("memo", memo);
		
		int result=memberService.insertPoint(map);
		loginMember.setPoint(loginMember.getPoint()+point);
		model.addAttribute("loginMember", loginMember);
		map.clear();
		map.put("msgg", point+"??? ?????? ??????");
		return map;
	}
	
	//???????????? ??????
	@ResponseBody
	@PostMapping("inputCoupon")
	public Map<String,Object> inputCoupon(Model model, int memberNo, String couponText, @SessionAttribute Member loginMember) {
		log.debug("memberNo = {}",memberNo);
		log.debug("couponText = {}",couponText);
		Map<String, Object> map = new HashMap<>();
		map.put("couponText", couponText);
		//1. ????????? ???????????? ??????
		Coupon c = memberService.selectOneCoupon(map);
		if(c == null) {
			map.put("status", false);
			map.put("msg", "???????????? ?????? ??????");
			return map;
		}
		
		//2. ?????? ????????? ?????? ???????????? ??????
		map.put("memberNo", memberNo);
		map.put("couponNo", c.getNo());
		int result = memberService.selectCouponRecordCheck(map);
		
		//2.1???????????? ?????????????????? = ????????????, ???????????? ?????? = ???????????? ????????? ??????
		if(result>0) {
			//????????? ?????? ??????
			map.put("status", false);
			map.put("msg", "?????? ????????? ??????");
			return map;
		}
		
		//coupon_record???????????? ???????????? - ???????????? ???????????????
		map.put("point", c.getPoint());
		result = memberService.insertCoupon(map);
		map.put("status", true);
		map.put("msg", c.getPoint()+"????????? ?????? ?????? ??????");
		
		//?????? ????????? ?????? ????????? ????????? ????????? ??????~(?????????????????? ??????)
		loginMember.setPoint(loginMember.getPoint()+c.getPoint());
		model.addAttribute("loginMember", loginMember);
		return map;
	}
	
	@ResponseBody
	@PostMapping("sendMsg")
	public Map<String, Object> sendMsg(Msg msg) {
		Map<String, Object> map = new HashMap<>();
		log.debug("msg = {}",msg);
		//???????????? ????????? ???????????? ??????
		Member toMember = memberService.selectOneMemberUseNo(msg.getToMemberNo());
		log.debug("toMember={}",toMember);
		if(toMember==null) {
			map.put("status", false);
			map.put("msgg", "????????? ???????????? ????????????");
			return map;
		}
		//???????????? ?????? ??????
		msg.setToMemberName(toMember.getName());
		//????????? ????????????
		int result = memberService.sendMsg(msg);
		log.debug("????????????={}",Boolean.parseBoolean(String.valueOf(result)));
		map.put("status", true);
		map.put("msgg", "????????? ??????????????????");
		return map;
		
	}
	
	@ResponseBody
	@PostMapping("msgReadStatusChg")
	public Map<String, Object> msgReadStatusChg(@RequestParam int no) {
		Map<String, Object> map = new HashMap<>();
		log.debug("no@controller= {}",no);
		//???????????? ??????
		int result = memberService.msgReadStatusChg(no);
		if(result==0) {
			map.put("status", false);
			return map;
		}
		map.put("status", true);
		return map;
	}
	
	
	
	@GetMapping("/auth/kakao")
	public String kakaoRequest(@RequestParam String code, Model model, RedirectAttributes redirectAttr) throws Exception {
		try {
			log.debug("code = {}", code);
			BufferedReader br = null;
			//??????????????? ?????? accessToeken????????????
			URL url = new URL("https://kauth.kakao.com/oauth/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

			conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			String urlParameters = "grant_type=authorization_code&client_id=" + KAKAO_API_KEY + "&redirect_uri="
					+ KAKAO_REDIRECT_URL + "&code=" + code;
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) { // ?????? ??????
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else { // ?????? ??????
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			Map<String, String> map = new Gson().fromJson(response.toString(), Map.class);
			log.debug("map = {}", map);
			
			
			
			//accessToken??? ?????? ????????? ?????? ????????????
			String accessToken = map.get("access_token");
			String refreshToken = map.get("refresh_token");
			log.debug("access_token = {}",accessToken);
			log.debug("refreshToken = {}",refreshToken);
			
			url = new URL("https://kapi.kakao.com/v2/user/me");
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("Authorization", "Bearer "+accessToken);
			responseCode = conn.getResponseCode();
			if (responseCode == 200) { // ?????? ??????
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else { // ?????? ??????
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			inputLine=null;
			response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			log.debug("json={}",response.toString());
			
			//??? ????????????
			Map<String, String> map2 = new Gson().fromJson(response.toString(), Map.class);
			log.debug("map = {}",map2);
			
			String temp = new Gson().toJson(map2.get("properties"));
			log.debug("temp = {}",temp);
			
			String name = (String) new Gson().fromJson(temp, Map.class).get("nickname");
			log.debug("name = {}", name);
			
			
			temp = new Gson().toJson(map2.get("kakao_account"));
			String email = (String) new Gson().fromJson(temp, Map.class).get("email");
			log.debug("email = {}",email);
			//1. ???????????? ????????? ??????
			Member m = new Member();
			m.setEmail(email);
			m.setName(name);
			m.setPlatform("KAKAO");
			Member tempMember = memberService.selectOneMemberKakao(m);
			log.debug("???????????? ?????? = {}",m);
			log.debug("????????? ?????? = {}",tempMember);
			//2. ???????????? ?????? ????????? ????????????
			if(tempMember!=null) {
				//???????????? ??????
				model.addAttribute("loginMember",tempMember);
				
				//???????????? ??????
				Authentication newAuthentication = 
						new UsernamePasswordAuthenticationToken(tempMember, null, tempMember.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
				return "redirect:/";
			}
			//3. ??????????????? ???????????? ???????????? ??????
			redirectAttr.addFlashAttribute("msg","???????????? ???????????? ??????");
			redirectAttr.addFlashAttribute("member",m);
			return "redirect:/member/memberEnroll_kakao";
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/memberEnroll_kakao")
	public void memberEnrollKakao() {
	}
	
	@PostMapping("/memberEnroll_kakao")
	public String memberEnrollKakao(Member member, Model model, RedirectAttributes redirectAttr) {
		log.debug("member = {}",member);
		member.setPassword("kakao");
		int result = memberService.insertMemberKakao(member);
		
		member = memberService.selectOneMemberKakao(member);
		//???????????? ??????
		model.addAttribute("loginMember",member);
		
		//???????????? ??????
		Authentication newAuthentication = 
				new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
		redirectAttr.addFlashAttribute("msg","??????????????????");
		return "redirect:/";
	}
	//?????????
	
	//?????????
	
	//?????????
	
	//?????????
	
	
	//?????????
	
	//?????????
	
	@ResponseBody
	@GetMapping("selectMemberPoint")
	public String selectMemberPoint(@RequestParam int memberNo) {
		String result = memberService.selectOneMemberPoint(memberNo);
		log.debug("result = {}", result);
		return result;
	}
}
