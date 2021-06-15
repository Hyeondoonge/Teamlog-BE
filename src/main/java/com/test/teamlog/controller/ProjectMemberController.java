package com.test.teamlog.controller;

import com.test.teamlog.entity.User;
import com.test.teamlog.payload.ApiResponse;
import com.test.teamlog.payload.UserDTO;
import com.test.teamlog.service.ProjectMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "프로젝트 멤버 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @ApiOperation(value = "프로젝트 초대(신청) 수락")
    @PostMapping("/project-joins/{joinId}")
    public ResponseEntity<ApiResponse> acceptProjectInvitation(@PathVariable("joinId") Long joinId) {
        ApiResponse apiResponse = projectMemberService.acceptProjectInvitation(joinId);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "프로젝트 초대 수락")
    @PostMapping("/projects/{projectId}/members")
    public ResponseEntity<ApiResponse> createProjectMember(@PathVariable("projectId") Long projectId,
                                                           @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = projectMemberService.createProjectMember(projectId, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "프로젝트 멤버 삭제")
    @DeleteMapping("/projects/{projectId}/members")
    public ResponseEntity<ApiResponse> leaveProject(@PathVariable("projectId") long projectId,
                                                    @RequestParam(value = "userId", required = false) String userId,
                                                    @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = null;
        // userId 없으면 탈퇴 있으면 추방
        if (userId == null) {
            apiResponse = projectMemberService.leaveProject(projectId, currentUser);
        } else {
            apiResponse = projectMemberService.expelMember(projectId, userId, currentUser);
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 멤버 조회")
    @GetMapping("/projects/{projectId}/members")
    public ResponseEntity<List<UserDTO.UserSimpleInfo>> getProjectMembers(@PathVariable("projectId") Long projectId) {
        List<UserDTO.UserSimpleInfo> response = projectMemberService.getProjectMembers(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 멤버가 아닌 유저 조회")
    @GetMapping("/projects/{projectId}/not-members")
    public ResponseEntity<List<UserDTO.UserSimpleInfo>> getUsersNotInProjectMember(@PathVariable("projectId") Long projectId) {
        List<UserDTO.UserSimpleInfo> response = projectMemberService.getUsersNotInProjectMember(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}