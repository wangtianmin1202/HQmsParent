package com.hand.spc.repository.service;

import java.util.List;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.MessageTypeR;
import com.hand.spc.repository.dto.MessageTypeUploadRelDTO;

public interface IMessageTypeRService extends IBaseService<MessageTypeR>, ProxySelf<IMessageTypeRService> {

	/**
	 * 查询消息类型维护的消息触发配置
	 *
	 * @param messageType
	 * @return List<MessageTypeUploadRelDTO>
	 * @throws Exception
	 */
	public List<MessageTypeUploadRelDTO> selectMessageUploadConfig(MessageTypeR messageType) throws Exception;

	/**
	 * 消息类型查询
	 *
	 * @param messageTypeQuery
	 * @return
	 * @throws Exception
	 */
	public MessageTypeR listMessageType(MessageTypeR messageTypeQuery) throws Exception;

	/**
	 * 消息类型保存
	 *
	 * @param messageType
	 * @return
	 */
	public MessageTypeR saveMessageType(MessageTypeR messageType);

	/**
	 * 消息类型保存 byhap规范
	 *
	 * @param messageType
	 * @return
	 */
	public MessageTypeR saveMessageTypebyhap(MessageTypeR messageType);
	
	public List<MessageTypeR> batchUpdate(IRequest request, List<MessageTypeR> messageTypeList);

}