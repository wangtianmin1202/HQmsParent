package com.hand.hqm.hqm_pfmea_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_db_management.mapper.HqmInvalidTreeMapper;
import com.hand.hqm.hqm_db_p_management.dto.HQMPInvalidTree;
import com.hand.hqm.hqm_db_p_management.mapper.HqmpInvalidTreeMapper;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea.mapper.FmeaMapper;
import com.hand.hqm.hqm_fmea_version.dto.FmeaVersion;
import com.hand.hqm.hqm_fmea_version.mapper.FmeaVersionMapper;
import com.hand.hqm.hqm_pfmea_detail.dto.HqmpdbMenuItemP;
import com.hand.hqm.hqm_pfmea_detail.dto.PfmeaDetail;
import com.hand.hqm.hqm_pfmea_detail.mapper.PfmeaDetailMapper;
import com.hand.hqm.hqm_pfmea_detail.service.IPfmeaDetailService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PfmeaDetailServiceImpl extends BaseServiceImpl<PfmeaDetail> implements IPfmeaDetailService{
	 @Autowired
	 private PfmeaDetailMapper pfmeaDetailMapper;
	 @Autowired
	 FmeaMapper fmeaMapper;
	 @Autowired
	 FmeaVersionMapper fmeaVersionMapper;
	 @Autowired
	 HqmpInvalidTreeMapper hqmpInvalidTreeMapper;
	 
	 /**
		 * @Author ruifu.jiang
		 * @Description 查询附着对象层级维护 树形图数据
		 * @Date 11:40 2019/8/26
		 * @Param [requestContext, dto]
		 */
		@Override
		public List<HqmpdbMenuItemP> queryTreeData(IRequest requestContext, PfmeaDetail dto) {
			// 查询根数据
			List<PfmeaDetail> pfmeaDetail = pfmeaDetailMapper.selectParentInvalid(dto);
			// 查询下层数据
			List<HqmpdbMenuItemP> menuItems = castToMenuItem(pfmeaDetail);
			return menuItems;
		}
 
		/**
		 * @Author han.zhang
		 * @Description 更新或者保存附着对象
		 * @Date 19:40 2019/8/19
		 * @Param [requestCtx, dto]
		 */
		@Override
		public ResponseData updateOrAdd(IRequest requestCtx, PfmeaDetail dto) {
			// HQMPInvalidTree hQMInvalid = hqmpInvalidTreeMapper.selectByPrimaryKey(dto);
			ResponseData responseData = new ResponseData();
			// id没有是新增
			if (null == dto.getBranchId()) {// 新插入值
				if (dto.getParentBranchId() != null) {// 有父级id
					PfmeaDetail pfmeaDetail = new PfmeaDetail();
					pfmeaDetail.setBranchId(dto.getParentBranchId());
					pfmeaDetail = pfmeaDetailMapper.selectByPrimaryKey(pfmeaDetail);
					if (pfmeaDetail.getParentBranchId() != null) {
						dto.setRanks(3L);
					} else {
						dto.setRanks(2L);
					}

				} else {
					dto.setRanks(1L);
				}
				// 校验
//				checkInvalid(dto, dto.getRanks(), responseData);
				if (responseData.isSuccess()) {
					// 新增
					pfmeaDetailMapper.insertSelective(dto);
					return responseData;
				} else {
					return responseData;
				}

			} else {
				// 校验
//				checkInvalid(dto, dto.getRanks(), responseData);
				if (responseData.isSuccess()) {
					// 更新
					pfmeaDetailMapper.updatePfmeaDetail(dto);
					return responseData;
				} else {
					return responseData;
				}
			}
		}
		/**
		 * @Author han.zhang
		 * @Description 删除附着对象及其后代
		 * @Date 11:49 2019/8/20
		 * @Param [dto]
		 */
		@Override
		public void deleteRow(PfmeaDetail dto) {

			// 查询后代所有需要删除的附着对象
			List<PfmeaDetail> delList = new ArrayList<>();
			delList.add(dto);
			queryDownAttachment(delList, Collections.singletonList(dto));
			// 删除
			self().batchDelete(delList);
		}
		/**
		 * @Author han.zhang
		 * @Description 将list转换成目录菜单
		 * @Date 11:52 2019/8/19
		 * @Param [spcAttachments]
		 */
		private List<HqmpdbMenuItemP> castToMenuItem(List<PfmeaDetail> pfmeaDetail) {
			// 根
			List<HqmpdbMenuItemP> menuItems = new ArrayList<>();
			pfmeaDetail.stream().forEach(pfmea -> {
				if (pfmea.getParentBranchId() == null) {
					menuItems.add(createMenuItem(pfmea));
				}
			});
			// 添加子
			menuItems.stream().forEach(item -> {
				additem(item);
			});
			return menuItems;
		}
		
		/**
		 * @Author han.zhang
		 * @Description 将SpcAttachment对象转换成菜单
		 * @Date 13:50 2019/8/19
		 * @Param [attachment]
		 */
		private HqmpdbMenuItemP createMenuItem(PfmeaDetail pfmeaDetail) {
			HqmpdbMenuItemP menu = new HqmpdbMenuItemP();
			menu.setFunctionCode(pfmeaDetail.getBranchName());
			menu.setText(pfmeaDetail.getBranchName());
			menu.setType(pfmeaDetail.getBranchName());
			menu.setId(pfmeaDetail.getBranchId());
			menu.setBranchId(pfmeaDetail.getBranchId());
			menu.setParentBranchId(pfmeaDetail.getParentBranchId());
			menu.setRanks(pfmeaDetail.getRanks());
			menu.setBranchName(pfmeaDetail.getBranchName());
			//menu.setRangeName(dfmeaDetail.getRangeName());
			menu.setClaim(pfmeaDetail.getClaim());
			menu.setPotentialFailureMode(pfmeaDetail.getPotentialFailureMode());
			menu.setInvalidConsequence(pfmeaDetail.getInvalidConsequence());
			menu.setSeverity(pfmeaDetail.getSeverity());
			menu.setSpecialCharacteristicType(pfmeaDetail.getSpecialCharacteristicType());
			menu.setFailureReason(pfmeaDetail.getFailureReason());
			menu.setPreventiveMeasure(pfmeaDetail.getPreventiveMeasure());
			menu.setDetectionMeasure(pfmeaDetail.getDetectionMeasure());
			menu.setOccurrence(pfmeaDetail.getOccurrence());
			menu.setDetection(pfmeaDetail.getDetection());
			menu.setRpn(pfmeaDetail.getRpn());
			menu.setSuggestMeasure(pfmeaDetail.getSuggestMeasure());
			menu.setChargeId(pfmeaDetail.getChargeId());
			menu.setEstimatedFinishTime(pfmeaDetail.getEstimatedFinishTime());
			menu.setMeasureResult(pfmeaDetail.getMeasureResult());
			menu.setPostOccurrence(pfmeaDetail.getPostOccurrence());
			menu.setPostDetection(pfmeaDetail.getPostDetection());
			menu.setPostRpn(pfmeaDetail.getPostRpn());	
			menu.setPostSeverity(pfmeaDetail.getPostSeverity());
			menu.setUseName(pfmeaDetail.getUseName());
			return menu;
		}
		
		/**
		 * @Author han.zhang
		 * @Description 有子元素则添加
		 * @Date 13:51 2019/8/19
		 * @Param [menuItem]
		 */
		private void additem(HqmpdbMenuItemP menuItem) {
			// 定义二层菜单
			List<HqmpdbMenuItemP> children = new ArrayList<>();
			// 查询子
			PfmeaDetail pfmeaDetail = new PfmeaDetail();
			pfmeaDetail.setParentBranchId(menuItem.getBranchId());
			List<PfmeaDetail> pfmeaDetails = pfmeaDetailMapper.selectInvalidByParent(pfmeaDetail);
			// 添加子
			pfmeaDetails.stream().forEach(item -> {
				children.add(createMenuItem(item));
			});
			// 设定子菜单
			if (children.size() > 0) {
				menuItem.setChildren(children);
				// 递归，有子 继续添加

				children.stream().forEach(childrenItem -> {
					additem(childrenItem);

				});

			}
			menuItem.setChildren(children);
		}
		
		/**
		 * @Author han.zhang
		 * @Description 查询后代子附着对象
		 * @Date 11:52 2019/8/20
		 * @Param
		 */
		public void queryDownAttachment(List<PfmeaDetail> delList, List<PfmeaDetail> dtos) {
			dtos.stream().forEach(dto -> {
				PfmeaDetail parnethQMInvalid = new PfmeaDetail();
				parnethQMInvalid.setParentBranchId(dto.getBranchId());
				List<PfmeaDetail> hQMInvalids = pfmeaDetailMapper.select(parnethQMInvalid);
				if (hQMInvalids.size() > 0) {
					delList.addAll(hQMInvalids);
					queryDownAttachment(delList, hQMInvalids);
				}
			});
		}
		/**
		 * @Description 校验结构不重复 同一结构下功能不重复 同一功能下失效不重复
		 * @Param [dtos]
		 */
		public void checkInvalid(PfmeaDetail dto, Long ranks, ResponseData responseData) {
			if (ranks == 1&&dto.getParentBranchId()==null) {
				// 结构校验
				int count1 = pfmeaDetailMapper.checkStructure(dto);
				if (count1 > 0) {
					responseData.setSuccess(false);
					responseData.setMessage("结构不能重复！");

				}
			} else if (ranks == 2||(ranks == 1&&dto.getParentBranchId()!=null)) {
				// 功能校验
				int count2 = pfmeaDetailMapper.checkFunctionAndInvalid(dto);
				if (count2 > 0) {
					responseData.setSuccess(false);
					responseData.setMessage("同一结构下功能不能重复！");
				}
			}/* else {
				// 失效校验
				int count3 = dfmeaDetailMapper.checkFunctionAndInvalid(dto);
				if (count3 > 0) {
					responseData.setSuccess(false);
					responseData.setMessage("同一功能下失效不能重复！");
				}
			}*/
		}
		
		 @Override
			public ResponseData confirm(IRequest requestContext, List<Float> headList) {
				//List<PoHeaders> headList1 = new ArrayList();
			   /* ResponseData responseData= new ResponseData();		    
			    Long id_parent=0L;
				Long id_son=0L;
				Float id = headList.get(0);
					PfmeaDetail pfmeaDetail = new PfmeaDetail();
					pfmeaDetail.setFmeaId(id);				
					List<PfmeaDetail> pfmeaDetails =pfmeaDetailMapper.select(pfmeaDetail);
					//提交校验
					for(PfmeaDetail meaDetail:pfmeaDetails) {
						if(meaDetail.getPostRpn()!=null)
						{
							if(meaDetail.getPostRpn()>80)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高RPN值失效，不能提交！");
								return responseData;
							}
						}
						if(meaDetail.getPostSeverity()!=null)
						{
							if(meaDetail.getPostSeverity()>8)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高严重度失效，不能提交！");
								return responseData;
							}
						}
						if(meaDetail.getPostOccurrence()!=null)
						{
							if(meaDetail.getPostOccurrence()>8)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高频度失效，不能提交！");
								return responseData;
							}
						}
				   if(meaDetail.getPostRpn()==null||meaDetail.getPostOccurrence()==null||meaDetail.getPostSeverity()==null)
				   {
						if(meaDetail.getRpn()!=null)
						{
							if(meaDetail.getRpn()>80)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高RPN值失效，不能提交！");
								return responseData;
							}
						}
									if(meaDetail.getSeverity()!=null)
									{
										if(meaDetail.getSeverity()>8)
										{
											responseData.setSuccess(false);
											responseData.setMessage("仍存在高严重度失效，不能提交！");
											return responseData;
										}
									}
									if(meaDetail.getOccurrence()!=null)
									{
										if(meaDetail.getOccurrence()>8)
										{
											responseData.setSuccess(false);
											responseData.setMessage("仍存在高频度失效，不能提交！");
											return responseData;
										}
									}
				   }
						
					}
					//加逻辑获取明细表中存在而库中不存在的结构 
					List<PfmeaDetail> pfmeaDetail_cs =pfmeaDetailMapper.commitSelect(pfmeaDetail);  
					if(pfmeaDetail_cs.size()>0)
					{
						for(PfmeaDetail pfmeaDetail_c :pfmeaDetail_cs)
						{
												
						    List<PfmeaDetail>pfmeaDetail_t = pfmeaDetailMapper.Selectbyparentbranch(pfmeaDetail_c);
						    for(PfmeaDetail dfmeaDetail_f :pfmeaDetail_t)
						    {
							HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
							hQMInvalidTree.setInvalidConsequence(dfmeaDetail_f.getFailureConsequences());
							hQMInvalidTree.setInvalidName(dfmeaDetail_f.getBranchName());
							if(dfmeaDetail_f.getSeverity()!=null)					
							{	
								hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(dfmeaDetail_f.getSeverity())));
							}
							hQMInvalidTree.setSpecialCharacterType(dfmeaDetail_f.getSpecialCharacteristicType()) ;
							hQMInvalidTree.setInvalidReason(dfmeaDetail_f.getFailureReason());
							hQMInvalidTree.setPreventMeasure(dfmeaDetail_f.getPreventiveMeasure());
							hQMInvalidTree.setOccurrence(dfmeaDetail_f.getOccurrence());
							if( dfmeaDetail_f.getDetection()!=null)					
							{
								hQMInvalidTree.setDetection(dfmeaDetail_f.getDetection());
							}
							hQMInvalidTree.setRpn(dfmeaDetail_f.getRpn()); 
							hQMInvalidTree.setRanks(dfmeaDetail_f.getRanks());
							
							hqmInvalidTreeMapper.insertSelective(hQMInvalidTree);
							hQMInvalidTree =hqmInvalidTreeMapper.selectOne(hQMInvalidTree);
							
							if(hQMInvalidTree.getRanks()==1)
							{		
							  id_parent=hQMInvalidTree.getInvalidId();
							}
							else if(hQMInvalidTree.getRanks()==2)
							{
								id_son =hQMInvalidTree.getInvalidId();
								hQMInvalidTree.setParentInvalidId(id_parent);
								hqmInvalidTreeMapper.updateByPrimaryKey(hQMInvalidTree);
							}
							else if(hQMInvalidTree.getRanks()==3)
							{
								hQMInvalidTree.setParentInvalidId(id_son);
								hqmInvalidTreeMapper.updateByPrimaryKey(hQMInvalidTree);
							}
						    }
						}										
					}				
					Fmea head = new Fmea();
					head.setKid(id);
					head = fmeaMapper.selectByPrimaryKey(head);
					//获取当前fmeaId 下的  版本 
					Float vNum =head.getCurrentVersion();
					//更新版本
					Float newVnum =getMaxVersion(vNum);
					head.setCurrentVersion(newVnum);
					fmeaMapper.updateByPrimaryKeySelective(head);
					//新增版本表
					FmeaVersion  fmeaVersion =new FmeaVersion();
					fmeaVersion.setFmeaId(id);  
					fmeaVersion.setFmeaCode(head.getFmeaCode());
					fmeaVersion.setFmeaVersion(newVnum);
					fmeaVersionMapper.insertSelective(fmeaVersion);
								
				responseData.setSuccess(true);
				responseData.setMessage("提交成功");
				return responseData;*/
			 
			    ResponseData responseData= new ResponseData();		    
				Float id = headList.get(0);
					PfmeaDetail pfmeaDetail = new PfmeaDetail();
					pfmeaDetail.setFmeaId(id);				
					List<PfmeaDetail> pfmeaDetails =pfmeaDetailMapper.select(pfmeaDetail);
					//提交校验
					for(PfmeaDetail meaDetail:pfmeaDetails) {
						if(meaDetail.getPostRpn()!=null)
						{
							if(meaDetail.getPostRpn()>80)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高RPN值失效，不能提交！");
								return responseData;
							}
						}
						if(meaDetail.getPostSeverity()!=null)
						{
							if(meaDetail.getPostSeverity()>8)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高严重度失效，不能提交！");
								return responseData;
							}
						}
						if(meaDetail.getPostOccurrence()!=null)
						{
							if(meaDetail.getPostOccurrence()>8)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高频度失效，不能提交！");
								return responseData;
							}
						}
				   if(meaDetail.getPostRpn()==null||meaDetail.getPostOccurrence()==null||meaDetail.getPostSeverity()==null)
				   {
						if(meaDetail.getRpn()!=null)
						{
							if(meaDetail.getRpn()>80)
							{
								responseData.setSuccess(false);
								responseData.setMessage("仍存在高RPN值失效，不能提交！");
								return responseData;
							}
						}
									if(meaDetail.getSeverity()!=null)
									{
										if(meaDetail.getSeverity()>8)
										{
											responseData.setSuccess(false);
											responseData.setMessage("仍存在高严重度失效，不能提交！");
											return responseData;
										}
									}
									if(meaDetail.getOccurrence()!=null)
									{
										if(meaDetail.getOccurrence()>8)
										{
											responseData.setSuccess(false);
											responseData.setMessage("仍存在高频度失效，不能提交！");
											return responseData;
										}
									}
				   }
						
					}
					
					//加逻辑获取明细表中存在而库中不存在的结构(最上层结构) 
					List<PfmeaDetail> pfmeaDetail_cs =pfmeaDetailMapper.commitSelect(pfmeaDetail);  
					if(pfmeaDetail_cs.size()>0)
					{
	                   //新增了第一层结构
						    for(PfmeaDetail pfmeaDetail_f :pfmeaDetail_cs)
						    {
							HQMPInvalidTree hQMInvalidTree = new HQMPInvalidTree();
							hQMInvalidTree.setInvalidConsequence(pfmeaDetail_f.getFailureConsequences());
							hQMInvalidTree.setInvalidName(pfmeaDetail_f.getBranchName());
							if(pfmeaDetail_f.getSeverity()!=null)					
							{	
								hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(pfmeaDetail_f.getSeverity())));
							}
							hQMInvalidTree.setSpecialCharacterType(pfmeaDetail_f.getSpecialCharacteristicType()) ;
							hQMInvalidTree.setInvalidReason(pfmeaDetail_f.getFailureReason());
							hQMInvalidTree.setPreventMeasure(pfmeaDetail_f.getPreventiveMeasure());
							hQMInvalidTree.setOccurrence(pfmeaDetail_f.getOccurrence());
							if( pfmeaDetail_f.getDetection()!=null)					
							{
								hQMInvalidTree.setDetection(pfmeaDetail_f.getDetection());
							}
							hQMInvalidTree.setRpn(pfmeaDetail_f.getRpn()); 
							hQMInvalidTree.setRanks(pfmeaDetail_f.getRanks());
							
							hqmpInvalidTreeMapper.insertSelective(hQMInvalidTree);
							
						    }
						   //新增第二层功能
		                   
		                  for( PfmeaDetail pfmeaDetail_c22 :pfmeaDetail_cs)
		                   {
		                    List<PfmeaDetail> pfmeaDetail_c23 = pfmeaDetailMapper.Selectbyparentbranch(pfmeaDetail_c22);
		                    for(PfmeaDetail pfmeaDetail_f :pfmeaDetail_c23)
						    {
		                    if(pfmeaDetail_f.getRanks()==2)
		                    {
		                    Long perant_id =pfmeaDetailMapper.getparentIdsec(pfmeaDetail_f);
		                    HQMPInvalidTree hQMInvalidTree = new HQMPInvalidTree();
							hQMInvalidTree.setInvalidConsequence(pfmeaDetail_f.getFailureConsequences());
							hQMInvalidTree.setInvalidName(pfmeaDetail_f.getBranchName());
							if(pfmeaDetail_f.getSeverity()!=null)					
							{	
								hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(pfmeaDetail_f.getSeverity())));
							}
							hQMInvalidTree.setSpecialCharacterType(pfmeaDetail_f.getSpecialCharacteristicType()) ;
							hQMInvalidTree.setInvalidReason(pfmeaDetail_f.getFailureReason());
							hQMInvalidTree.setPreventMeasure(pfmeaDetail_f.getPreventiveMeasure());
							hQMInvalidTree.setOccurrence(pfmeaDetail_f.getOccurrence());
							if( pfmeaDetail_f.getDetection()!=null)					
							{
								hQMInvalidTree.setDetection(pfmeaDetail_f.getDetection());
							}
							hQMInvalidTree.setRpn(pfmeaDetail_f.getRpn()); 
							hQMInvalidTree.setRanks(pfmeaDetail_f.getRanks());
							hQMInvalidTree.setParentInvalidId(perant_id);
							
							hqmpInvalidTreeMapper.insertSelective(hQMInvalidTree);
							
						    }
						    }
		                    }
		                    
		                  //新增第三层功能
		                  for( PfmeaDetail pfmeaDetail_c32 :pfmeaDetail_cs)
		                   {
		                    List<PfmeaDetail> pfmeaDetail_c33 = pfmeaDetailMapper.Selectbyparentbranch(pfmeaDetail_c32);
		                    for(PfmeaDetail pfmeaDetail_f :pfmeaDetail_c33)
						    {
		                    if(pfmeaDetail_f.getRanks()==3)
		                    {
		                    //先获取他的最顶层结构的ID
		                    Long perant_id =pfmeaDetailMapper.getparentIdfromthd(pfmeaDetail_f);
		                    //在确定上一层结构的ID
		                    PfmeaDetail pfmeaDetail_3 =new 	PfmeaDetail();
		                    pfmeaDetail_3.setBranchId(pfmeaDetail_f.getBranchId());
		                    pfmeaDetail_3.setParentBranchId(perant_id);
		                    Long perant_id_i =pfmeaDetailMapper.getparentIdthd(pfmeaDetail_3);
		                    //新增
							HQMPInvalidTree hQMInvalidTree = new HQMPInvalidTree();
							hQMInvalidTree.setInvalidConsequence(pfmeaDetail_f.getFailureConsequences());
							hQMInvalidTree.setInvalidName(pfmeaDetail_f.getBranchName());
							if(pfmeaDetail_f.getSeverity()!=null)					
							{	
								hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(pfmeaDetail_f.getSeverity())));
							}
							hQMInvalidTree.setSpecialCharacterType(pfmeaDetail_f.getSpecialCharacteristicType()) ;
							hQMInvalidTree.setInvalidReason(pfmeaDetail_f.getFailureReason());
							hQMInvalidTree.setPreventMeasure(pfmeaDetail_f.getPreventiveMeasure());
							hQMInvalidTree.setOccurrence(pfmeaDetail_f.getOccurrence());
							if( pfmeaDetail_f.getDetection()!=null)					
							{
								hQMInvalidTree.setDetection(pfmeaDetail_f.getDetection());
							}
							hQMInvalidTree.setRpn(pfmeaDetail_f.getRpn()); 
							hQMInvalidTree.setRanks(pfmeaDetail_f.getRanks());
							hQMInvalidTree.setParentInvalidId(perant_id_i);
							
							hqmpInvalidTreeMapper.insertSelective(hQMInvalidTree);
							
						    }
		                    
						    }
						}
					}			
					Fmea head = new Fmea();
					head.setKid(id);
					head = fmeaMapper.selectByPrimaryKey(head);
					//获取当前fmeaId 下的  版本 
					Float vNum =head.getCurrentVersion();
					//更新版本
					Float newVnum =getMaxVersion(vNum);
					head.setCurrentVersion(newVnum);
					fmeaMapper.updateByPrimaryKeySelective(head);
					//新增版本表
					FmeaVersion  fmeaVersion =new FmeaVersion();
					fmeaVersion.setFmeaId(id);  
					fmeaVersion.setFmeaCode(head.getFmeaCode());
					fmeaVersion.setFmeaVersion(newVnum);
					fmeaVersionMapper.insertSelective(fmeaVersion);
		
				responseData.setSuccess(true);
				responseData.setMessage("提交成功");
				return responseData;
			}
		 //导入结构
		 @Override
			public ResponseData getdata(IRequest requestContext, List<Long> headList) {
				//List<PoHeaders> headList1 = new ArrayList();
			   /*    ResponseData responseData= new ResponseData();
			            
			        HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
			        hQMInvalidTree.setInvalidId(headList.get(0));		
				    if(headList.get(1)==null)
				    {
				    	responseData.setSuccess(true);
						responseData.setMessage("提交成功");
						return responseData;
				    	
				    }
					List<HQMInvalidTree> hQMInvalidTreeL = hqmInvalidTreeMapper.selectfromdata_r1(hQMInvalidTree);
			        
					Long id_parent=0L;
					Long id_son=0L;
					 
					for(HQMInvalidTree  hQMInvalidTrees : hQMInvalidTreeL)
					{
						PfmeaDetail pfmeaDetail = new PfmeaDetail();
						
						pfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
						pfmeaDetail.setBranchName(hQMInvalidTrees.getInvalidName());
						pfmeaDetail.setFailureConsequences(hQMInvalidTrees.getInvalidConsequence());
						if(hQMInvalidTrees.getSerious()!=null)					
						{	
							pfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMInvalidTrees.getSerious())));
						}
						pfmeaDetail.setSpecialCharacteristicType( hQMInvalidTrees.getSpecialCharacterType());
						pfmeaDetail.setFailureReason(hQMInvalidTrees.getInvalidReason());
						pfmeaDetail.setPreventiveMeasure(hQMInvalidTrees.getPreventMeasure());
						pfmeaDetail.setOccurrence(hQMInvalidTrees.getOccurrence());
						if(hQMInvalidTrees.getDetection()!=null)					
						{
							pfmeaDetail.setDetection(hQMInvalidTrees.getDetection());
						}
						pfmeaDetail.setRpn(hQMInvalidTrees.getRpn()); 
						pfmeaDetail.setRanks(hQMInvalidTrees.getRanks());
						//结构和功能要保证唯一性
						if(pfmeaDetail.getRanks()==1||pfmeaDetail.getRanks()==2)
						{
						PfmeaDetail pfmeaDetail_check = new PfmeaDetail();
						pfmeaDetail_check.setBranchName(hQMInvalidTrees.getInvalidName());		
						pfmeaDetail_check.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
						pfmeaDetail_check= pfmeaDetailMapper.selectOne(pfmeaDetail_check);
						if(pfmeaDetail_check!=null)
						{
							responseData.setSuccess(false);
							responseData.setMessage("结构已存在 不可重复添加");
							return responseData;
						}
						}
						pfmeaDetailMapper.insertSelective(pfmeaDetail);
						pfmeaDetail =pfmeaDetailMapper.selectOne(pfmeaDetail);	
						
						
						if(pfmeaDetail.getRanks()==1)
						{		
						 id_parent=pfmeaDetail.getBranchId();
						}
						else if(pfmeaDetail.getRanks()==2)
						{
							id_son =pfmeaDetail.getBranchId();
							pfmeaDetail.setParentBranchId(id_parent);
							pfmeaDetailMapper.updateByPrimaryKey(pfmeaDetail);
						}
						else if(pfmeaDetail.getRanks()==3)
						{
							pfmeaDetail.setParentBranchId(id_son);
							pfmeaDetailMapper.updateByPrimaryKey(pfmeaDetail);
						}
					}			
				responseData.setSuccess(true);
				responseData.setMessage("添加成功");
				return responseData;*/
			 
			 
			   ResponseData responseData= new ResponseData();
		       Long perant_id =null;  
		        HQMPInvalidTree hQMInvalidTree = new HQMPInvalidTree();
		        hQMInvalidTree.setInvalidId(headList.get(0));		

				List<HQMPInvalidTree> hQMPInvalidTreeL = hqmpInvalidTreeMapper.selectfromdata_r1(hQMInvalidTree);	 
				for(HQMPInvalidTree  hQMpInvalidTrees : hQMPInvalidTreeL)
				{
					PfmeaDetail pfmeaDetail = new PfmeaDetail();
					
					pfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
					pfmeaDetail.setBranchName(hQMpInvalidTrees.getInvalidName());
					pfmeaDetail.setFailureConsequences(hQMpInvalidTrees.getInvalidConsequence());
					if(hQMpInvalidTrees.getSerious()!=null)					
					{	
						pfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMpInvalidTrees.getSerious())));
					}
					pfmeaDetail.setSpecialCharacteristicType( hQMpInvalidTrees.getSpecialCharacterType());
					pfmeaDetail.setFailureReason(hQMpInvalidTrees.getInvalidReason());
					pfmeaDetail.setPreventiveMeasure(hQMpInvalidTrees.getPreventMeasure());
					pfmeaDetail.setOccurrence(hQMpInvalidTrees.getOccurrence());
					if(hQMpInvalidTrees.getDetection()!=null)					
					{
						pfmeaDetail.setDetection(hQMpInvalidTrees.getDetection());
					}
					pfmeaDetail.setRpn(hQMpInvalidTrees.getRpn()); 
					pfmeaDetail.setRanks(hQMpInvalidTrees.getRanks());
					//结构和功能要保证唯一性
					if(pfmeaDetail.getRanks()==1||pfmeaDetail.getRanks()==2)
					{
					PfmeaDetail pfmeaDetail_check = new PfmeaDetail();
					pfmeaDetail_check.setBranchName(hQMpInvalidTrees.getInvalidName());		
					pfmeaDetail_check.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
					pfmeaDetail_check= pfmeaDetailMapper.selectOne(pfmeaDetail_check);
					if(pfmeaDetail_check!=null)
					{
						responseData.setSuccess(false);
						responseData.setMessage("结构已存在 不可重复添加");
						return responseData;
					}
					}
					pfmeaDetailMapper.insertSelective(pfmeaDetail);
					pfmeaDetail =pfmeaDetailMapper.selectOne(pfmeaDetail);	
					
					perant_id=pfmeaDetail.getBranchId();
				}	
				
				
				List<HQMPInvalidTree> hQMpInvalidTreeL_2 = hqmpInvalidTreeMapper.selectfromdata_r2r3(hQMInvalidTree);	 
				for(HQMPInvalidTree  hQMpInvalidTrees : hQMpInvalidTreeL_2)
				{
					if(hQMpInvalidTrees.getRanks()==2)
					{
					PfmeaDetail pfmeaDetail = new PfmeaDetail();
					
					pfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
					pfmeaDetail.setBranchName(hQMpInvalidTrees.getInvalidName());
					pfmeaDetail.setFailureConsequences(hQMpInvalidTrees.getInvalidConsequence());
					if(hQMpInvalidTrees.getSerious()!=null)					
					{	
						pfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMpInvalidTrees.getSerious())));
					}
					pfmeaDetail.setSpecialCharacteristicType( hQMpInvalidTrees.getSpecialCharacterType());
					pfmeaDetail.setFailureReason(hQMpInvalidTrees.getInvalidReason());
					pfmeaDetail.setPreventiveMeasure(hQMpInvalidTrees.getPreventMeasure());
					pfmeaDetail.setOccurrence(hQMpInvalidTrees.getOccurrence());
					if(hQMpInvalidTrees.getDetection()!=null)					
					{
						pfmeaDetail.setDetection(hQMpInvalidTrees.getDetection());
					}
					pfmeaDetail.setRpn(hQMpInvalidTrees.getRpn()); 
					pfmeaDetail.setRanks(hQMpInvalidTrees.getRanks());
					pfmeaDetail.setParentBranchId(perant_id);
					//结构和功能要保证唯一性

					pfmeaDetailMapper.insertSelective(pfmeaDetail);
				 }
				}	
				for(HQMPInvalidTree  hQMPInvalidTrees : hQMpInvalidTreeL_2)
				{
					if(hQMPInvalidTrees.getRanks()==3)
					{
				    HQMPInvalidTree hQMInvalidTree_3 = new HQMPInvalidTree();	
				    hQMInvalidTree_3.setInvalidId(hQMPInvalidTrees.getInvalidId());
				    hQMInvalidTree_3.setParentInvalidId(perant_id);
				     
				    Long pra_id =hqmpInvalidTreeMapper.getparentIdfromthd(hQMInvalidTree_3);
					PfmeaDetail pfmeaDetail = new PfmeaDetail();
					
					pfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
					pfmeaDetail.setBranchName(hQMPInvalidTrees.getInvalidName());
					pfmeaDetail.setFailureConsequences(hQMPInvalidTrees.getInvalidConsequence());
					if(hQMPInvalidTrees.getSerious()!=null)					
					{	
						pfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMPInvalidTrees.getSerious())));
					}
					pfmeaDetail.setSpecialCharacteristicType( hQMPInvalidTrees.getSpecialCharacterType());
					pfmeaDetail.setFailureReason(hQMPInvalidTrees.getInvalidReason());
					pfmeaDetail.setPreventiveMeasure(hQMPInvalidTrees.getPreventMeasure());
					pfmeaDetail.setOccurrence(hQMPInvalidTrees.getOccurrence());
					if(hQMPInvalidTrees.getDetection()!=null)					
					{
						pfmeaDetail.setDetection(hQMPInvalidTrees.getDetection());
					}
					pfmeaDetail.setRpn(hQMPInvalidTrees.getRpn()); 
					pfmeaDetail.setRanks(hQMPInvalidTrees.getRanks());
					pfmeaDetail.setParentBranchId(pra_id);
					//结构和功能要保证唯一性

					pfmeaDetailMapper.insertSelective(pfmeaDetail);
				 }
				}	
				
			responseData.setSuccess(true);
			responseData.setMessage("添加成功");
			return responseData;
			}
		 /**
		     * @Author ruifu.jiang
		     * @Description 页面查询
		     * @Date 19:40 2019/8/26
		     * @Param [requestCtx, dto,page,pagesize]
		     */
		 @Override
		    public List<PfmeaDetail> myselect(IRequest requestContext, PfmeaDetail dto, int page, int pageSize) {
		        PageHelper.startPage(page, pageSize);
		        return pfmeaDetailMapper.myselect(dto);
			 }
		 /**
		     * @Author ruifu.jiang
		     * @Description 获取打印数据
		     * @Date 19:40 2019/8/26
		     * @Param [requestCtx, dto,page,pagesize]
		     */
		 @Override
		    public List<PfmeaDetail> queryprintData(IRequest requestContext, PfmeaDetail dto, int page, int pageSize) {
		        PageHelper.startPage(page, pageSize);
		        return pfmeaDetailMapper.queryprintData(dto);
			 }
		 /**
		     * @Author ruifu.jiang
		     * @Description 查询头表数据
		     * @Date 19:40 2019/8/26
		     * @Param [kid]
		     */
		 @Override
			public PfmeaDetail queryHeaderData(Float kid) {
				return pfmeaDetailMapper.queryHeaderData(kid);
			}
		 //每更新一次＋10
		 public Float getMaxVersion(Float VersionNum) {		  	
			 return VersionNum+10;
			}

		@Override
		public List<PfmeaDetail> selectCondition(IRequest requestContext, PfmeaDetail dto, int page, int pageSize) {
			if(dto.getPostDetections() != null && dto.getPostDetections() != "") {
				 dto.setPostDetectionList(Arrays.asList(dto.getPostDetections().replaceAll("\"", "").split(",")));			 
			 }
			 if(dto.getSeveritys() != null && dto.getSeveritys() != "") {
				 dto.setSeverityList(Arrays.asList(dto.getSeveritys().replaceAll("\"", "").split(",")));			 
			 }
			 if(dto.getOccurrences() != null && dto.getOccurrences() != "") {
				 dto.setOccurrenceList(Arrays.asList(dto.getOccurrences().replaceAll("\"", "").split(",")));			 
			 }
			 if(dto.getDetections() != null && dto.getDetections() != "") {
				 dto.setDetectionList(Arrays.asList(dto.getDetections().replaceAll("\"", "").split(",")));			 
			 }
			 if(dto.getPostSeveritys() != null && dto.getPostSeveritys() != "") {
				 dto.setPostSeverityList(Arrays.asList(dto.getPostSeveritys().replaceAll("\"", "").split(",")));			 
			 }
			 if(dto.getPostOccurrences() != null && dto.getPostOccurrences() != "") {
				 dto.setPostOccurrenceList(Arrays.asList(dto.getPostOccurrences().replaceAll("\"", "").split(",")));			 
			 }
			 if(dto.getSpecialCharacteristicTypes() != null && dto.getSpecialCharacteristicTypes() != "") {
				 dto.setSpecialCharacteristicTypeList(Arrays.asList(dto.getSpecialCharacteristicTypes().replaceAll("\"", "").split(",")));			 
			 }
			PageHelper.startPage(page, pageSize);
			return pfmeaDetailMapper.selectCondition(dto);
		}
}