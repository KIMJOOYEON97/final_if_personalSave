package com.kh.interactFunding.funding.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.interactFunding.funding.model.dao.FundingDao;
import com.kh.interactFunding.funding.model.vo.Attachment;
import com.kh.interactFunding.funding.model.vo.Funding;
import com.kh.interactFunding.funding.model.vo.FundingExt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FundingServiceImpl implements FundingService{
	
	@Autowired
	private FundingDao fundingDao;
	
	//김윤수
	
	//김경태
	
	//김주연
	@Override
	public int ready1FundingInsertNo(Funding funding) {
		return fundingDao.ready1FundingInsertNo(funding);
	}
	@Override
	public int saveCharge(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return fundingDao.saveCharge(param);
	}
	@Override
	public int saveBasicInfo(FundingExt funding) {
		int result = 0;
		result = fundingDao.saveBasicInfo(funding);
		log.debug("funding = {}",funding);
		
		//attachment 등록
		if(funding.getAttachList().size() > 0) {
			for(Attachment attach: funding.getAttachList()) {
				attach.setFunding_no(funding.getFunding_no()); //이번에 발급받은 funindg pk|  attach no fk세팅
				result = insertAttachment(attach);
			}
		}	
		return result;
	}
	@Override
	public int insertAttachment(Attachment attach) {
		return fundingDao.insertAttachment(attach);
	}
	@Override
	public int saveStory(Funding funding) {
		// TODO Auto-generated method stub
		return fundingDao.saveStory(funding);
	}
	
	
	//박요한
	
	//배기원
	
	//이승우
	@Override
	public List<Funding> fundingList() {
		return fundingDao.fundingList();
	}
	//천호현




	
}