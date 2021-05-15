package com.test.teamlog.controller;

import com.test.teamlog.entity.User;
import com.test.teamlog.payload.ApiResponse;
import com.test.teamlog.payload.ProjectDTO;
import com.test.teamlog.payload.ProjectJoinDTO;
import com.test.teamlog.payload.UserDTO;
import com.test.teamlog.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(description = "프로젝트 초대 및 멤버 관리 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProjectJoinMemberController {
    private final ProjectService projectService;

    // -------------------------------
    // ----- 프로젝트 멤버 신청 관리 -----
    // -------------------------------
    @ApiOperation(value = "프로젝트 멤버 초대(userId 필요) 및 신청")
    @PostMapping("/projects/{projectId}/joins")
    public ResponseEntity<ApiResponse> inviteUserForProject(@PathVariable("projectId") long projectId,
                                                            @RequestParam(value = "userId", required = false) String userId,
                                                            @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = null;
        // userId 있으면 초대, 없으면 신청
        if (userId != null) {
            apiResponse = projectService.inviteUserForProject(projectId, userId);
        } else {
            apiResponse = projectService.applyForProject(projectId, currentUser);
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

//    @ApiOperation(value = "프로젝트 멤버 신청")
//    @PostMapping("/{id}/apply")
//    public ResponseEntity<ApiResponse> inviteUserForProject(@PathVariable("id") long projectId,
//                                                            @AuthenticationPrincipal User currentUser) {
//        ApiResponse apiResponse = projectService.applyForProject(projectId, currentUser);
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }

    @ApiOperation(value = "프로젝트 멤버 신청 삭제")
    @DeleteMapping("/project-joins/{joinId}")
    public ResponseEntity<ApiResponse> deleteProjectJoin(@PathVariable("joinId") Long joinId) {
        ApiResponse apiResponse = projectService.deleteProjectJoin(joinId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 멤버 신청 목록 조회")
    @GetMapping("/projects/{id}/joins")
    public ResponseEntity<List<ProjectJoinDTO.ProjectJoinResponse>> getProjectApplyList(@PathVariable("id") Long id) {
        List<ProjectJoinDTO.ProjectJoinResponse> response = projectService.getProjectApplyList(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "유저가 받은 프로젝트 초대 조회")
    @GetMapping("users/project-invitation")
    public ResponseEntity<List<ProjectJoinDTO.ProjectJoinResponse>> getProjectInvitationList(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        List<ProjectJoinDTO.ProjectJoinResponse> response = projectService.getProjectInvitationList(currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ---------------------------
    // ----- 프로젝트 멤버 관리 -----
    // ---------------------------
    @ApiOperation(value = "프로젝트 초대 및 신청을 수락")
    @PostMapping("/project-joins/{joinId}")
    public ResponseEntity<ApiResponse> acceptProjectInvitation(@PathVariable("joinId") Long joinId) {
        ApiResponse apiResponse = projectService.acceptProjectInvitation(joinId);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "프로젝트 멤버 탈퇴/ 추방(userId 필요) : 임시")
    @DeleteMapping("/projects/{projectId}/members")
    public ResponseEntity<ApiResponse> leaveProject(@PathVariable("projectId") long projectId,
                                                    @RequestParam(value = "userId", required = false) String userId,
                                                    @ApiIgnore  @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = null;
        // userId 없으면 탈퇴 있으면 추방
        if(userId == null) {
            apiResponse = projectService.leaveProject(projectId, currentUser);
        } else {
            apiResponse = projectService.expelMember(projectId, userId, currentUser);
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 멤버 삭제 By ProjectMember key")
    @DeleteMapping("/project-members/{id}")
    public ResponseEntity<ApiResponse> leaveProject(@PathVariable("id") long id) {
        ApiResponse apiResponse = projectService.deleteProjectMemeber(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}