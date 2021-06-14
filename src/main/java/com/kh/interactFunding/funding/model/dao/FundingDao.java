package com.kh.interactFunding.funding.model.dao;

import java.util.List;

import java.util.Map;

import com.kh.interactFunding.funding.model.vo.Attachment;
import com.kh.interactFunding.funding.model.vo.Funding;

import com.kh.interactFunding.funding.model.vo.Funding_reward;
import com.kh.interactFunding.member.model.vo.Member;
import com.kh.interactFunding.funding.model.vo.FundingExt;


public interface FundingDao {

	//김윤수
	
	//김경태
	
	//김주연
	List<Funding> statusYList(Member loginMember);
	int ready1FundingInsertNo(Funding funding);
	int saveCharge(Map<String, Object> param);
	int saveBasicInfo(FundingExt funding);
	int insertAttachment(Attachment attach);
	int saveStory(Funding funding);
	int finalSubmit(Funding funding);
	
	//박요한
	
	//배기원
	public List<Funding> indexfundingList();
	public List<Funding> indexfundinglike();
	public List<Funding_reward> indexfuding_rewardList();

	//이승우
	public List<Funding> fundingList(Map<String, Object> map);

	public List<Funding> fundingSearchList();
	//천호현
	public Funding selectOneFunding(int funding_no);
	
	public List<Funding> selectFunding(int funding_no);

}
