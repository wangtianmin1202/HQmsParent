package com.hand.hqm.hqm_ng_group_member.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.hand.hqm.hqm_ng_group_member.dto.NgGroupMember;
import com.hand.hqm.hqm_ng_group_member.service.INgGroupMemberService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class NgGroupMemberServiceImpl extends BaseServiceImpl<NgGroupMember> implements INgGroupMemberService{

}