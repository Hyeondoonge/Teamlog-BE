package com.test.teamlog.controller;

import com.test.teamlog.entity.User;
import com.test.teamlog.payload.ApiResponse;
import com.test.teamlog.payload.TeamJoinDTO;
import com.test.teamlog.payload.UserDTO;
import com.test.teamlog.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(description = "팀 멤버 관리 ( 팀 초대 수락은 여기있음 )")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TeamMemberController {
    private final TeamService teamService;

    @ApiOperation(value = "팀 초대 및 신청을 수락")
    @PostMapping("/team-joins/{joinId}")
    public ResponseEntity<ApiResponse> acceptTeamInvitation(@PathVariable("joinId") Long joinId) {
        ApiResponse apiResponse = teamService.acceptTeamInvitation(joinId);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "팀 멤버 탈퇴/ 추방(userId 필요) : 임시")
    @DeleteMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse> leaveTeam(@PathVariable("teamId") long teamId,
                                                    @RequestParam(value = "userId", required = false) String userId,
                                                    @ApiIgnore  @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = null;
        // userId 없으면 탈퇴 있으면 추방
        if(userId == null) {
            apiResponse = teamService.leaveTeam(teamId, currentUser);
        } else {
            apiResponse = teamService.expelMember(teamId, userId, currentUser);
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "팀 멤버 삭제 By TeamMember key")
    @DeleteMapping("/team-members/{id}")
    public ResponseEntity<ApiResponse> leaveTeam(@PathVariable("id") long id) {
        ApiResponse apiResponse = teamService.deleteTeamMemeber(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "팀 멤버 조회")
    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<List<UserDTO.UserSimpleInfo>> getTeamMemberList(@PathVariable("id") Long id) {
        List<UserDTO.UserSimpleInfo> response = teamService.getTeamMemberList(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}