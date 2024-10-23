package com.example.sociallogintest.member.controller;

import com.example.sociallogintest.member.entity.Member;
import com.example.sociallogintest.member.repository.MemberRepository;
import com.example.sociallogintest.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 수정페이지
    @GetMapping("/member/modify")
    public String showModifyPage(Authentication authentication, Model model) {
        AuthMemberDTO authMember = (AuthMemberDTO) authentication.getPrincipal();
        model.addAttribute("member", authMember);

         Member member = memberRepository.findByEmail(authMember.getEmail(),true)
                 .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

         model.addAttribute("member", member);
        return "member/modify";
    }

    // 수정
    @PostMapping("/member/update")
    public String updateMember(Authentication authentication, String name, String password) {
        AuthMemberDTO authMember = (AuthMemberDTO) authentication.getPrincipal();
        Member member = memberRepository.findByEmail(authMember.getEmail(), true).orElseThrow();

        // 이름 수정
        member.setName(name);

        // 비밀번호 수정
        if (password != null && !password.isEmpty()) {
            member.setPassword(passwordEncoder.encode(password));
        }

        memberRepository.save(member);
        return "redirect:/sample/member"; // 수정 완료 후 리다이렉트
    }
}

//@RestController
//@RequestMapping("/api/members")
//@Controller
//@RequiredArgsConstructor
//public class MemberController {
//
//    private final MemberService memberService;
//    private final MemberRepository memberRepository;
//
//    @GetMapping("/")
//    public String index() {
//        return "/sample/member";
//    }
//
//    @GetMapping("/member/myPage")
//    public String myPage(Model model, HttpSession session) {
//        String userId = (String) session.getAttribute("userId"); // 세션에서 사용자 ID 가져오기
//
//        if (userId == null) {
//            return "redirect:/member/login"; // 로그인하지 않은 경우 로그인 페이지로 리디렉션
//        }
//
//        // 현재 회원 정보 가져오기
//        MemberDTO memberDTO = memberService.memberInfo(userId);
//        if (memberDTO == null) {
//            model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
//            return "/member/login"; // 오류 발생 시 로그인 페이지로 리디렉션
//        }
//
//        model.addAttribute("member", memberDTO);
//        return "/member/myPage"; // 회원 정보를 보여주는 페이지로 이동
//    }
//
//
//    // 회원가입
//    @GetMapping("/member/register")
//    public String createMemberForm(Model model) {
//        model.addAttribute("memberDTO", new MemberDTO()); // 폼에 바인딩할 MemberDTO 생성
//        return "/member/register";
//    }
//
//    @PostMapping("/member/register")
//    public String createMember(@Valid @ModelAttribute MemberDTO memberDTO, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "/member/register"; // 오류가 있으면 폼으로 돌아감
//        }
//
//        memberDTO.setRole("ROLE_USER"); // 기본 역할 설정
//        memberDTO.setScore(0.0); // 기본 점수 설정
//
//        Member member = memberService.registerMember(memberDTO);
//        return "redirect:/member/login";
//    }
//
//    // 로그인
//    @GetMapping("/member/login")
//    public String login() {
//        return "/member/login";
//    }
//
//    @PostMapping("/member/login")
//    public String login(@RequestParam String id, @RequestParam String password, Model model , HttpSession session) {
//        Member member = memberRepository.findById(id).orElse(null);
//
//        // 사용자 존재 여부 및 비밀번호 확인
//        if (member != null && member.getPassword().equals(password)) {
//            session.setAttribute("userId", id); // 세션에 사용자 ID 저장
//            return "redirect:/member/myPage"; // 로그인 성공 시 리디렉션
//        }
//
//
//        // 실패 시 오류 메시지 추가
//        model.addAttribute("errorMessage", "ID 또는 비밀번호가 잘못되었습니다.");
//        return "/member/login"; // 로그인 페이지로 돌아감
//    }
//
//
//    // 회원 목록 페이지 (어드민만 가능)
//    @GetMapping("/member/list")
//    public String list(Model model) {
//
//        List<MemberDTO> memberList = memberService.getList();
//        model.addAttribute("list", memberList);
//        return "member/list"; // 회원 목록 보기
//    }
//
//    // 회원 삭제
//    @PostMapping("/member/remove")
//    public String remove(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
//        try {
//            memberService.deleteMember(id);
//            redirectAttributes.addFlashAttribute("successMessage", "회원이 삭제되었습니다.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "회원 삭제에 실패했습니다.");
//        }
//        return "redirect:/";
//    }
//}









//    @Autowired
//    private MemberService memberService;
//
//    // 모든 회원 조회
//    @GetMapping
//    public ResponseEntity<List<Member>> getAllMembers() {
//        List<Member> members = memberService.getAllMembers();
//        return new ResponseEntity<>(members, HttpStatus.OK);
//    }
//
//    // 특정 회원 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<Member> getMemberById(@PathVariable String id) {
//        Optional<Member> member = memberService.getMemberById(id);
//        if (member.isPresent()) {
//            return new ResponseEntity<>(member.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // 회원 등록
//    @PostMapping
//    public ResponseEntity<Member> createMember(@RequestBody Member member) {
//        Member createdMember = memberService.saveMember(member);
//        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
//    }
//
//    // 회원 정보 수정
//    @PutMapping("/{id}")
//    public ResponseEntity<Member> updateMember(@PathVariable String id, @RequestBody Member updatedMember) {
//        try {
//            Member member = memberService.updateMember(id, updatedMember);
//            return new ResponseEntity<>(member, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // 회원 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMember(@PathVariable String id) {
//        memberService.deleteMember(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }



