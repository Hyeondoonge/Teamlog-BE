package com.test.teamlog.controller;

import com.test.teamlog.entity.User;
import com.test.teamlog.payload.ApiResponse;
import com.test.teamlog.payload.ProjectJoinDTO;
import com.test.teamlog.service.ProjectJoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "프로젝트 초대 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectJoinController{
    private final ProjectJoinService projectJoinService;

    @ApiOperation(value = "프로젝트 멤버 초대(신청) 추가")
    @PostMapping("/projects/{projectId}/joins")
    public ResponseEntity<ApiResponse> inviteUserForProject(@PathVariable("projectId") long projectId,
                                                            @RequestParam(value = "userId", required = false) String userId,
                                                            @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = null;
        // userId 있으면 초대, 없으면 신청
        if (userId != null) {
            apiResponse = projectJoinService.inviteUserForProject(projectId, userId);
        } else {
            apiResponse = projectJoinService.applyForProject(projectId, currentUser);
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "프로젝트 멤버 초대(신청) 삭제")
    @DeleteMapping("/project-joins/{joinId}")
    public ResponseEntity<ApiResponse> deleteProjectJoin(@PathVariable("joinId") Long joinId) {
        ApiResponse apiResponse = projectJoinService.deleteProjectJoin(joinId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 멤버 신청 목록 조회")
    @GetMapping("/projects/{id}/joins/apply")
    public ResponseEntity<List<ProjectJoinDTO.ProjectJoinForProject>> getProjectApplyListForProject(@PathVariable("id") Long id) {
        List<ProjectJoinDTO.ProjectJoinForProject> response = projectJoinService.getProjectApplyListForProject(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 멤버 초대 목록 조회")
    @GetMapping("/projects/{id}/joins/invitation")
    public ResponseEntity<List<ProjectJoinDTO.ProjectJoinForProject>> getProjectInvitationListForProject(@PathVariable("id") Long id) {
        List<ProjectJoinDTO.ProjectJoinForProject> response = projectJoinService.getProjectInvitationListForProject(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "유저가 가입 신청한 프로젝트 조회")
    @GetMapping("users/project-apply")
    public ResponseEntity<List<ProjectJoinDTO.ProjectJoinForUser>> getProjectApplyListForUser(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        List<ProjectJoinDTO.ProjectJoinForUser> response = projectJoinService.getProjectApplyListForUser(currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "유저가 받은 프로젝트 초대 조회")
    @GetMapping("users/project-invitation")
    public ResponseEntity<List<ProjectJoinDTO.ProjectJoinForUser>> getProjectInvitationListForUser(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        List<ProjectJoinDTO.ProjectJoinForUser> response = projectJoinService.getProjectInvitationListForUser(currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}