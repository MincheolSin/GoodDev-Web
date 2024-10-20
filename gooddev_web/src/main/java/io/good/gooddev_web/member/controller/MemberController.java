package io.good.gooddev_web.member.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.good.gooddev_web.board.service.BoardService;
import io.good.gooddev_web.member.dto.MemberDTO;
import io.good.gooddev_web.member.service.MemberService;
import io.good.gooddev_web.member.vo.MemberVO;
import io.good.gooddev_web.search.dto.PageRequestDTO;
import io.good.gooddev_web.util.EmailValidator;
import io.good.gooddev_web.util.IdMasker;
import io.good.gooddev_web.util.IdValidator;
import io.good.gooddev_web.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final MapperUtil mapperUtil;
	private final BoardService boardService; 

//---------------- 회원 CRUD ------------------ 1) Read 2) Create 3) Update 4) Delete  
    // 회원 목록 GET
    @GetMapping("list")
    public String getlist(@Validated PageRequestDTO pageRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
          pageRequestDTO = new PageRequestDTO();
        }
        model.addAttribute("pageResponseDTO", memberService.getList(pageRequestDTO));
        return "member/list";
    }

    // 회원 가입 처리 POST
    @PostMapping("register")
    public ResponseEntity<Map<String,Boolean>> register(@RequestBody MemberDTO memberDTO) {
        log.info("회원가입 요청 처리 중");
        Map<String,Boolean> response = new HashMap<>();
        int result = memberService.register(mapperUtil.map(memberDTO, MemberVO.class));
        response.put("success",result==1);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정 GET
    @GetMapping("modify/{mid}")
    public String modifyMemberForm(@PathVariable String mid, Model model) {
        MemberDTO member = memberService.getRead(mid);
        model.addAttribute("memberDTO", member);
        return "member/modify";
    }
    
    // 회원 정보 수정 POST처리
    @PostMapping("modify")
    public String modifyMember(@Validated @ModelAttribute("memberVO") MemberDTO memberDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.memberVO", bindingResult);
            redirectAttributes.addFlashAttribute("memberVO", memberDTO);
            return "redirect:/member/modify/" + memberDTO.getMid();
        } 
        // MemberDTO 객체 생성 및 매핑
        memberService.modifyMemberInfo(mapperUtil.map(memberDTO, MemberVO.class));
        return "redirect:/member/list";
    }
   
   
      //---------------- 아이디/비밀번호 찾기 ------------------
    // 아이디 찾기 : 사용자가 아이디 찾기 페이지를 요청할 때 호출됨
       // 아이디 찾기 폼 GET 요청 처리
       @GetMapping("findid")
       public String findIdForm(Model model) {
           model.addAttribute("memberDTO", new MemberDTO());
           return "member/findid"; // 아이디 찾기 페이지로 이동
       }
   
    // 아이디 찾기 POST 요청 처리
    @PostMapping("findid")
    public String findIdByEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        // 이메일 유효성 검사
        if (email == null || email.isEmpty())
         {
            redirectAttributes.addFlashAttribute("message", "이메일을 입력해 주세요.");
            return "redirect:/member/findid";
        }
        if (!EmailValidator.isValidEmailREGEX(email)) {
            redirectAttributes.addFlashAttribute("message", "유효하지 않은 이메일 형식입니다.");
            return "redirect:/member/findid";
        }
            String foundId = memberService.findIdByEmail(email);     // 이메일로 아이디 찾기
         // 아이디 마스킹 처리 (ex: ab****cd) 후 사용자에게 알려줌
            if (foundId != null) {
            String maskedId = IdMasker.maskId(foundId);
            redirectAttributes.addFlashAttribute("message", "찾은 아이디: " + maskedId);
        } else {
            redirectAttributes.addFlashAttribute("message", "해당 이메일로 등록된 아이디가 없습니다.");
        }
        return "redirect:/member/findid"; 
    }
    // 이메일 중복 체크 : 비동기 통신 사용
    @GetMapping("/checkEmailDuplicate/{email}")
    @ResponseBody
    public String checkEmailDuplicate(@PathVariable String email) {
        // 이메일 형식 유효성 검사
        if (!EmailValidator.isValidEmailREGEX(email)) {
            return "invalid";
        }
        // 서비스 메서드를 사용하여 이메일 중복 여부 확인
        boolean isDuplicate = memberService.checkEmailDuplicate(email);
        // 중복된 이메일이 있으면 "duplicate" 반환, 없으면 "available" 반환
        return isDuplicate ? "duplicate" : "available";
    }

    // 아이디 중복 체크 : 비동기 통신 사용
    @GetMapping("/checkIdDuplicate/{mid}")
    @ResponseBody // json 형식으로 데이터를 반환
    public String checkIdValid(@PathVariable String mid) {
        
        // 먼저 유효성 검사 (아이디 형식에 대한 정규식 검사) 수행
        if (!IdValidator.isValidIdREGEX(mid)) {
            return "invalid";
        }
        // 서비스 메서드 활용하여 중복 여부 확인
        boolean isDuplicate = memberService.checkIdDuplicate(mid);
        // 중복된 아이디가 있으면 "duplicate" 반환, 없으면 "available" 반환
        if (isDuplicate) {
            return "duplicate";
        } else {
            return "available";
        }
    } 
        
    @PostMapping("/checkIdAndEmail")    
    @ResponseBody
    public ResponseEntity<Map<String,Boolean>> checkIdAndEmail(@RequestParam("mid") String mid,@RequestParam("email") String email) {
        Map<String,Boolean> response = new HashMap<>();
        if (!EmailValidator.isValidEmailREGEX(email)||!IdValidator.isValidIdREGEX(mid)|| !memberService.checkIdAndEmail(mid,email)) {
            response.put("success",false);
            return ResponseEntity.ok(response);
        }
        response.put("success",true);
        return ResponseEntity.ok(response);
    }

    // 비밀번호 찾기 페이지로 이동 GET
    @GetMapping("findpwd")
    public String FindPwdForm() {
        return "member/findpwd"; // findpwd.jsp로 이동
    }

   // 비밀번호 찾기 처리 POST
   @PostMapping("findpwd")
   public String findPwdPost(@RequestParam("mid") String mid,
                             @RequestParam("email") String email,
                             @RequestParam("newPassword") String newPassword,
                             RedirectAttributes redirectAttributes) {
       if (!EmailValidator.isValidEmailREGEX(email)) {
           redirectAttributes.addFlashAttribute("message", "유효하지 않은 이메일 형식입니다.");
           redirectAttributes.addFlashAttribute("mid", mid);
           redirectAttributes.addFlashAttribute("email", email);
           return "redirect:/member/findpwd";
       }
   
       int success = memberService.findPassword(mid, email, newPassword);
       if (success==1) {
           redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
           redirectAttributes.addFlashAttribute("mid", mid);
           return "redirect:/";
       } else {
           redirectAttributes.addFlashAttribute("message", "아이디 또는 이메일이 일치하지 않습니다.");
           redirectAttributes.addFlashAttribute("mid", mid);
           redirectAttributes.addFlashAttribute("email", email);
           return "redirect:/member/findpwd";
       }
   }


 //---------------- 로그인/로그아웃 ------------------
    //로그아웃
    @RequestMapping("logout")
    public String logout(HttpServletRequest request,HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("loginInfo");
        member.setAuto_Login("");
        memberService.modify_Auto_Login(mapperUtil.map(member, MemberVO.class));
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("login")
	public String loginGet(HttpServletRequest request, HttpServletResponse response,Model model) {
        try{
            String link = request.getParameter("redirect");
            log.info("link는"+link);    
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("autoLoginTrue")) {
                        MemberDTO member = memberService.getRead_Auto_Login(cookie.getValue());
                        if (member != null) {
                            HttpSession session = request.getSession();
                            session.setAttribute("mid", member.getMid());
                            session.setAttribute("loginInfo", member);
                            cookie.setMaxAge(60 * 10);
                            response.addCookie(cookie);
                            return "redirect:"+(link != null&& !link.equals("null")? URLDecoder.decode(link, "UTF-8") : "/");
                        }
                    }
                }
            }
            String redirectLink = link != null && !link.equals("null") ? link : "/";
            model.addAttribute("redirect", URLEncoder.encode(redirectLink,"UTF-8"));
            return "member/login";
        }
        catch(Exception e){
            e.getStackTrace();
            return "redirect:/";
        }
	}
    
    //로그인 POST처리
    @PostMapping("/login")
    public ResponseEntity<Map<String,MemberDTO>> loginPost(HttpServletRequest request, HttpServletResponse response,@RequestBody MemberDTO memberDTO) {
        try {
            HttpSession session = request.getSession();
            String auto_login_check = request.getParameter("auto_login_check");
            if (auto_login_check == null) auto_login_check = "false";
            MemberDTO member = memberService.login(mapperUtil.map(memberDTO, MemberVO.class), auto_login_check);
            if (member != null) {
                if (auto_login_check.equals("on")) {
                    Cookie cookie = new Cookie("autoLoginTrue", member.getAuto_Login());
                    cookie.setMaxAge(60 * 10);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                session.setAttribute("mid", member.getMid());
                session.setAttribute("loginInfo", member);
                // isAdminYn 값을 세션에 추가
                session.setAttribute("isAdminYn", String.valueOf(member.getIsAdminYn()));
                Map<String, MemberDTO> map = new HashMap<>();
                map.put("loginInfo",member);
                return ResponseEntity.ok(map);

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 오류 기록
            return null;
        }
    }

//---------------- 마이페이지 ------------------
    
    //회원 마이페이지
    @RequestMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginInfo");
        
        if (member == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        // DB에서 사용자 정보를 가져오는 서비스 호출
        MemberDTO memberInfo = memberService.getMemberInfo(member.getMid());
        log.info("mid 정보 보기" + member.getMid());
        model.addAttribute("member", memberInfo); // JSP에서 사용하기 위해 모델에 추가
        
        return "member/mypage/myPage";
    }
        
    //나의 게시물 가져오기
    @GetMapping("/myBoardList")
    public String myBoardList(PageRequestDTO pageRequestDTO, Model model, HttpServletRequest request) {
    	HttpSession session = request.getSession();
        MemberDTO member= (MemberDTO) session.getAttribute("loginInfo");

        if (member == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        pageRequestDTO.setMid(member.getMid());
        model.addAttribute("pageResponseDTO", boardService.getList(pageRequestDTO));

        return "member/mypage/myBoardList";
    }
    
    //회원 상세보기
    @GetMapping("/detailMember")
	public String detailMember(@RequestParam String mid, Model model) {
		
	    MemberDTO member = memberService.getRead(mid);
	    model.addAttribute("member", member);
	    
	    return "member/mypage/detailMember";
	}
    
    //회원 정보 수정
    @GetMapping("/updateMember")
    public String updateMember(Model model, HttpServletRequest request) {
    	
    	String mid = request.getParameter("mid");
    	log.info("Received mid: " + mid);
    	
        MemberDTO member = memberService.getRead(mid); // 회원 정보 조회
        model.addAttribute("member", member); // 모델에 추가
        
        log.info("updateMid() 실행" + mid);
    	log.info("updateMember() 실행" + member);
    	return "member/mypage/updateMember";
    }
    
    //회원 정보 수정
    @PostMapping("/updateMember")
    public ResponseEntity<Map<String, Boolean>> updateMemberForm(@RequestParam String newPassword,HttpSession session) {
    	Map<String, Boolean> response = new HashMap<>();
    	MemberDTO member = (MemberDTO) session.getAttribute("loginInfo");
    	member.setPassword(newPassword);
    	int result = memberService.modifyMemberInfo(mapperUtil.map(member,MemberVO.class)); // 회원 정보 수정
    	if(result>0) {
    		response.put("success",true);
    	}
    	else response.put("success",false);
    	return ResponseEntity.ok(response);
    }
    //회원 탈퇴
    @RequestMapping("/removeMember")
    public String removeMember(HttpServletRequest request) {
    	HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginInfo");
        
        if (member == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        
        memberService.remove(member.getMid());
        
        // 세션 무효화
        session.invalidate();
    	
    	return "member/mypage/removeMember";
    }
    
}