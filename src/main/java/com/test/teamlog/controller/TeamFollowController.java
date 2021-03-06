package com.test.teamlog.controller;

import com.test.teamlog.entity.User;
import com.test.teamlog.payload.ApiResponse;
import com.test.teamlog.payload.TeamDTO;
import com.test.teamlog.payload.UserDTO;
import com.test.teamlog.service.TeamFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "팀 팔로우 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamFollowController {
    private final TeamFollowService teamFollowService;

    @ApiOperation(value = "팀 팔로우")
    @PostMapping("/teams/{teamId}/followers")
    public ResponseEntity<ApiResponse> followTeam(@PathVariable("teamId") Long teamId,
                                                  @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = teamFollowService.followTeam(teamId, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "팀 언팔로우")
    @DeleteMapping("/teams/{teamId}/followers")
    public ResponseEntity<ApiResponse> unfollowTeam(@PathVariable("teamId") Long teamId,
                                                    @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = teamFollowService.unfollowTeam(teamId, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "팀 팔로워 조회")
    @GetMapping("/teams/{teamId}/followers")
    public ResponseEntity<List<UserDTO.UserSimpleInfo>> getTeamFollowerList(@PathVariable("teamId") Long teamId) {
        List<UserDTO.UserSimpleInfo> response = teamFollowService.getTeamFollowerList(teamId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "유저가 팔로우하는 팀 조회")
    @GetMapping("/users/{userId}/team-follow")
    public ResponseEntity<List<TeamDTO.TeamListResponse>> getFollowingTeamListByUser(@PathVariable("userId") String userId) {
        List<TeamDTO.TeamListResponse> response = teamFollowService.getTeamListByTeamFollower(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
