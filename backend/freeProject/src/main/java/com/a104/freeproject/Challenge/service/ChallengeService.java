package com.a104.freeproject.Challenge.service;

import com.a104.freeproject.Challenge.request.registerRequest;
import com.a104.freeproject.Challenge.response.*;
import com.a104.freeproject.HashTag.entity.ChlTag;
import com.a104.freeproject.advice.exceptions.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ChallengeService {
    boolean register(registerRequest input, HttpServletRequest req) throws NotFoundException;

    List<ChlUserNameResponse> getUserInfo(Long chlId) throws NotFoundException;

    List<ChlUserSimpleStatResponse> getUserSimpleStatInfo(Long chlId) throws NotFoundException;

    List<ChallengeListResponse> getChallengePageList(int page) throws NotFoundException;

    List<ChallengeListResponse> getChallengePageListByCategory(int page, Long id) throws NotFoundException;

    List<ChallengeListResponse> getChallengePageListByDetailCategory(int page, Long id) throws NotFoundException;

    List<ChallengeListResponse> getChallengePageListByTitle(int page, String word) throws NotFoundException;

    List<ChallengeListResponse> getChallengePageListByNickName(int page, String nickName) throws NotFoundException;

    List<ChallengeListResponse> getChallengePageListByTag(int page, String tag) throws NotFoundException;

    ChallengeListResponse getChallenge(Long id) throws NotFoundException;

    boolean checkPassword(Long id, String password) throws NotFoundException;

    int getChallengePageCnt(Pageable pageable);

    List<String> findDoneDate(long chlId, int year, int month, HttpServletRequest req) throws NotFoundException;
    List<useWatchResponse> findWatchUse(boolean useWatch, Pageable pageable) throws NotFoundException;

    boolean participateChl(Long chlId, int alarmType, HttpServletRequest req) throws NotFoundException;
}