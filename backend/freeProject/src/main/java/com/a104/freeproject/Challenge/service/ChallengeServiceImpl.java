package com.a104.freeproject.Challenge.service;

import com.a104.freeproject.Category.entity.Category;
import com.a104.freeproject.Category.entity.DetailCategory;
import com.a104.freeproject.Category.repository.CategoryRepository;
import com.a104.freeproject.Category.repository.DetailCategoryRepository;
import com.a104.freeproject.Challenge.entity.Challenge;
import com.a104.freeproject.Challenge.repository.ChallengeRepository;
import com.a104.freeproject.Challenge.request.registerRequest;
import com.a104.freeproject.HashTag.service.ChltagServiceImpl;
import com.a104.freeproject.Member.entity.Member;
import com.a104.freeproject.Member.service.MemberServiceImpl;
import com.a104.freeproject.PrtChl.service.PrtChlServiceImpl;
import com.a104.freeproject.advice.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService{

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final DetailCategoryRepository detailRepository;
    private final ChltagServiceImpl chltagService;
    private final MemberServiceImpl memberService;
    private final ChlTimeServiceImpl chlTimeService;
    private final PrtChlServiceImpl prtChlService;

    @Override
    public boolean register(registerRequest input, HttpServletRequest req) throws NotFoundException {

        Member member;

        try{
            member = memberService.findEmailbyToken(req);
        } catch (Exception e){
            throw e;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        input.setStartTime(Timestamp.valueOf(sdf.format(input.getStartTime())));
        input.setEndTime(Timestamp.valueOf(sdf.format(input.getEndTime())));

        if(!categoryRepository.existsById(input.getCategoryId())) throw new NotFoundException("카테고리를 다시 입력해주세요.");
        Category category = categoryRepository.findById(input.getCategoryId()).get();
        Challenge challenge;

        if(category.getName().equals("기타")){ // 세부 카테고리가 없음
            challenge = Challenge.builder().title(input.getTitle()).contents(input.getContents()).imgurl(input.getImgurl()).
                    isWatch(input.isWatch()).roomtype(input.getRoomtype()).password(input.getPassword()).writer(member.getId())
                    .limitPeople(input.getLimitPeople()).alarm(input.getAlarm())
                    .goal(input.getGoal()).unit(input.getUnit()).category(category)
                    .build();
        }
        else{ // 세부 카테고리가 있음
            if(!detailRepository.existsByCategoryAndId(category,input.getDetailCategoryId())) throw new NotFoundException("세부 카테고리를 다시 입력해주세요.");
            DetailCategory detail = detailRepository.findById(input.getDetailCategoryId()).get();

            challenge = Challenge.builder().title(input.getTitle()).contents(input.getContents()).imgurl(input.getImgurl()).
                    isWatch(input.isWatch()).roomtype(input.getRoomtype()).password(input.getPassword()).writer(member.getId())
                    .limitPeople(input.getLimitPeople()).alarm(input.getAlarm())
                    .goal(input.getGoal()).unit(input.getUnit()).category(category).detailCategory(detail)
                    .build();
        }

        // 시간 체크하고 지금시간보다 전이면(입력하느라 시간 지난거 등을 생각해서)
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Timestamp now = Timestamp.valueOf(sdf.format(time));

        if(input.getStartTime().equals(now) || input.getStartTime().before(now)) challenge.setStatus(1);

        Long cid = challengeRepository.save(challenge).getId();
        Challenge c = challengeRepository.findById(cid).get();

        //해시태그 추가
        chltagService.addChallengeTag(c,input.getTagList());

        //ChlTime 추가
        chlTimeService.addRow(c,input.getStartTime(), input.getEndTime());

        //PrtChl 추가
        prtChlService.participate(c.getId(),req);

        return true;
    }

}