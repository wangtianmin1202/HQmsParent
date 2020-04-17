package com.hand.hqm.hqm_dfmea_detail.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hap.util.SystemApiMethod;
import com.hand.hcm.hcm_plant.dto.Plant;
import com.hand.hcs.hcs_supply_demand.dto.SupplyDemand;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.DateUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hand.hqm.hqm_dfmea_detail.dto.HqmpdbMenuItem;
import com.hand.hqm.hqm_control_plan.dto.ControlPlanFeatures;
import com.hand.hqm.hqm_db_management.dto.HQMInvalid;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_db_management.mapper.HqmInvalidTreeMapper;
import com.hand.hqm.hqm_dfmea_detail.dto.DfmeaDetail;
import com.hand.hqm.hqm_dfmea_detail.mapper.DfmeaDetailMapper;
import com.hand.hqm.hqm_dfmea_detail.service.IDfmeaDetailService;
import com.hand.hqm.hqm_fmea.dto.Fmea;
import com.hand.hqm.hqm_fmea.mapper.FmeaMapper;
import com.hand.hqm.hqm_fmea_version.dto.FmeaVersion;
import com.hand.hqm.hqm_fmea_version.mapper.FmeaVersionMapper;
import com.hand.hqm.hqm_sample.dto.Sample;
import com.hand.sys.sys_user.mapper.UserSysMapper;
import com.mysql.fabric.xmlrpc.base.Array;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(rollbackFor = Exception.class)
public class DfmeaDetailServiceImpl extends BaseServiceImpl<DfmeaDetail> implements IDfmeaDetailService {

	@Autowired
	private DfmeaDetailMapper dfmeaDetailMapper;
	@Autowired
	FmeaMapper fmeaMapper;
	@Autowired
	FmeaVersionMapper fmeaVersionMapper;
	@Autowired
	HqmInvalidTreeMapper hqmInvalidTreeMapper;

	/**
	 * @Author ruifu.jiang
	 * @Description 查询附着对象层级维护 树形图数据
	 * @Date 11:40 2019/8/26
	 * @Param [requestContext, dto]
	 */
	@Override
	public List<HqmpdbMenuItem> queryTreeData(IRequest requestContext, DfmeaDetail dto) {
		// 查询根数据
		List<DfmeaDetail> dfmeaDetail = dfmeaDetailMapper.selectParentInvalid(dto);
		// 查询下层数据
		List<HqmpdbMenuItem> menuItems = castToMenuItem(dfmeaDetail);
		return menuItems;
	}

	/**
	 * @Author han.zhang
	 * @Description 更新或者保存附着对象
	 * @Date 19:40 2019/8/19
	 * @Param [requestCtx, dto]
	 */
	@Override
	public ResponseData updateOrAdd(IRequest requestCtx, DfmeaDetail dto) {
		// HQMPInvalidTree hQMInvalid = hqmpInvalidTreeMapper.selectByPrimaryKey(dto);
		ResponseData responseData = new ResponseData();
		// id没有是新增
		if (null == dto.getBranchId()) {// 新插入值
			if (dto.getParentBranchId() != null) {// 有父级id
				DfmeaDetail dfmeaDetail = new DfmeaDetail();
				dfmeaDetail.setBranchId(dto.getParentBranchId());
				dfmeaDetail = dfmeaDetailMapper.selectByPrimaryKey(dfmeaDetail);
				if (dfmeaDetail.getParentBranchId() != null) {
					dto.setRanks(3L);
				} else {
					dto.setRanks(2L);
				}

			} else {
				dto.setRanks(1L);
			}
			// 校验
			checkInvalid(dto, dto.getRanks(), responseData);
			if (responseData.isSuccess()) {
				// 新增
				dfmeaDetailMapper.insertSelective(dto);
				return responseData;
			} else {
				return responseData;
			}

		} else {
			// 校验
			checkInvalid(dto, dto.getRanks(), responseData);
			if (responseData.isSuccess()) {
				// 更新
				dfmeaDetailMapper.updateDfmeaDetail(dto);
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
	public void deleteRow(DfmeaDetail dto) {

		// 查询后代所有需要删除的附着对象
		List<DfmeaDetail> delList = new ArrayList<>();
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
	private List<HqmpdbMenuItem> castToMenuItem(List<DfmeaDetail> dfmeaDetail) {
		// 根
		List<HqmpdbMenuItem> menuItems = new ArrayList<>();
		dfmeaDetail.stream().forEach(dfmea -> {
			if (dfmea.getParentBranchId() == null) {
				menuItems.add(createMenuItem(dfmea));
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
	private HqmpdbMenuItem createMenuItem(DfmeaDetail dfmeaDetail) {
		HqmpdbMenuItem menu = new HqmpdbMenuItem();
		menu.setFunctionCode(dfmeaDetail.getBranchName());
		menu.setText(dfmeaDetail.getBranchName());
		menu.setType(dfmeaDetail.getBranchName());
		menu.setId(dfmeaDetail.getBranchId());
		menu.setBranchId(dfmeaDetail.getBranchId());
		menu.setParentBranchId(dfmeaDetail.getParentBranchId());
		menu.setRanks(dfmeaDetail.getRanks());
		menu.setBranchName(dfmeaDetail.getBranchName());
		// menu.setRangeName(dfmeaDetail.getRangeName());
		menu.setFailureConsequences(dfmeaDetail.getFailureConsequences());
		menu.setInvalidConsequence(dfmeaDetail.getInvalidConsequence());
		menu.setSeverity(dfmeaDetail.getSeverity());
		menu.setSpecialCharacteristicType(dfmeaDetail.getSpecialCharacteristicType());
		menu.setFailureReason(dfmeaDetail.getFailureReason());
		menu.setPreventiveMeasure(dfmeaDetail.getPreventiveMeasure());
		menu.setDetectionMeasure(dfmeaDetail.getDetectionMeasure());
		menu.setOccurrence(dfmeaDetail.getOccurrence());
		menu.setDetection(dfmeaDetail.getDetection());
		menu.setRpn(dfmeaDetail.getRpn());
		menu.setSuggestMeasure(dfmeaDetail.getSuggestMeasure());
		menu.setChargeId(dfmeaDetail.getChargeId());
		menu.setEstimatedFinishTime(dfmeaDetail.getEstimatedFinishTime());
		menu.setMeasureResult(dfmeaDetail.getMeasureResult());
		menu.setPostOccurrence(dfmeaDetail.getPostOccurrence());
		menu.setPostDetection(dfmeaDetail.getPostDetection());
		menu.setPostRpn(dfmeaDetail.getPostRpn());
		menu.setPostSeverity(dfmeaDetail.getPostSeverity());
		menu.setUseName(dfmeaDetail.getUseName());
		return menu;
	}

	/**
	 * @Author han.zhang
	 * @Description 有子元素则添加
	 * @Date 13:51 2019/8/19
	 * @Param [menuItem]
	 */
	private void additem(HqmpdbMenuItem menuItem) {
		// 定义子菜单
		List<HqmpdbMenuItem> children = new ArrayList<>();
		// 查询子
		DfmeaDetail dfmeaDetail = new DfmeaDetail();
		dfmeaDetail.setParentBranchId(menuItem.getBranchId());
		List<DfmeaDetail> dfmeaDetails = dfmeaDetailMapper.selectInvalidByParent(dfmeaDetail);
		// 添加子
		dfmeaDetails.stream().forEach(item -> {
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
	public void queryDownAttachment(List<DfmeaDetail> delList, List<DfmeaDetail> dtos) {
		dtos.stream().forEach(dto -> {
			DfmeaDetail parnethQMInvalid = new DfmeaDetail();
			parnethQMInvalid.setParentBranchId(dto.getBranchId());
			List<DfmeaDetail> hQMInvalids = dfmeaDetailMapper.select(parnethQMInvalid);
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
	public void checkInvalid(DfmeaDetail dto, Long ranks, ResponseData responseData) {
		if (ranks == 1 && dto.getParentBranchId() == null) {
			// 结构校验
			int count1 = dfmeaDetailMapper.checkStructure(dto);
			if (count1 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("结构不能重复！");

			}
		} else if (ranks == 2 || (ranks == 1 && dto.getParentBranchId() != null)) {
			// 功能校验
			int count2 = dfmeaDetailMapper.checkFunctionAndInvalid(dto);
			if (count2 > 0) {
				responseData.setSuccess(false);
				responseData.setMessage("同一结构下功能不能重复！");
			}
		} /*
			 * else { // 失效校验 int count3 = dfmeaDetailMapper.checkFunctionAndInvalid(dto);
			 * if (count3 > 0) { responseData.setSuccess(false);
			 * responseData.setMessage("同一功能下失效不能重复！"); } }
			 */
	}

	/**
	 * @Description 提交
	 * @Param [requestContext，list 数据集]
	 */
	@Override
	public ResponseData confirm(IRequest requestContext, List<Float> headList) {
		// List<PoHeaders> headList1 = new ArrayList();
		ResponseData responseData = new ResponseData();
		Float id = headList.get(0);
		DfmeaDetail dfmeaDetail = new DfmeaDetail();
		dfmeaDetail.setFmeaId(id);
		List<DfmeaDetail> dfmeaDetails = dfmeaDetailMapper.select(dfmeaDetail);
		// 提交校验
		for (DfmeaDetail meaDetail : dfmeaDetails) {
			if (meaDetail.getPostRpn() != null) {
				if (meaDetail.getPostRpn() > 80) {
					responseData.setSuccess(false);
					responseData.setMessage("仍存在高RPN值失效，不能提交！");
					return responseData;
				}
			}
			if (meaDetail.getPostSeverity() != null) {
				if (meaDetail.getPostSeverity() > 8) {
					responseData.setSuccess(false);
					responseData.setMessage("仍存在高严重度失效，不能提交！");
					return responseData;
				}
			}
			if (meaDetail.getPostOccurrence() != null) {
				if (meaDetail.getPostOccurrence() > 8) {
					responseData.setSuccess(false);
					responseData.setMessage("仍存在高频度失效，不能提交！");
					return responseData;
				}
			}
			if (meaDetail.getPostRpn() == null || meaDetail.getPostOccurrence() == null
					|| meaDetail.getPostSeverity() == null) {
				if (meaDetail.getRpn() != null) {
					if (meaDetail.getRpn() > 80) {
						responseData.setSuccess(false);
						responseData.setMessage("仍存在高RPN值失效，不能提交！");
						return responseData;
					}
				}
				if (meaDetail.getSeverity() != null) {
					if (meaDetail.getSeverity() > 8) {
						responseData.setSuccess(false);
						responseData.setMessage("仍存在高严重度失效，不能提交！");
						return responseData;
					}
				}
				if (meaDetail.getOccurrence() != null) {
					if (meaDetail.getOccurrence() > 8) {
						responseData.setSuccess(false);
						responseData.setMessage("仍存在高频度失效，不能提交！");
						return responseData;
					}
				}
			}

		}

		// 加逻辑获取明细表中存在而库中不存在的结构(最上层结构)
		List<DfmeaDetail> dfmeaDetail_cs = dfmeaDetailMapper.commitSelect(dfmeaDetail);
		if (dfmeaDetail_cs.size() > 0) {
			// 新增了第一层结构
			for (DfmeaDetail dfmeaDetail_f : dfmeaDetail_cs) {
				HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
				hQMInvalidTree.setInvalidConsequence(dfmeaDetail_f.getFailureConsequences());
				hQMInvalidTree.setInvalidName(dfmeaDetail_f.getBranchName());
				if (dfmeaDetail_f.getSeverity() != null) {
					hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(dfmeaDetail_f.getSeverity())));
				}
				hQMInvalidTree.setSpecialCharacterType(dfmeaDetail_f.getSpecialCharacteristicType());
				hQMInvalidTree.setInvalidReason(dfmeaDetail_f.getFailureReason());
				hQMInvalidTree.setPreventMeasure(dfmeaDetail_f.getPreventiveMeasure());
				hQMInvalidTree.setOccurrence(dfmeaDetail_f.getOccurrence());
				if (dfmeaDetail_f.getDetection() != null) {
					hQMInvalidTree.setDetection(dfmeaDetail_f.getDetection());
				}
				hQMInvalidTree.setRpn(dfmeaDetail_f.getRpn());
				hQMInvalidTree.setRanks(dfmeaDetail_f.getRanks());

				hqmInvalidTreeMapper.insertSelective(hQMInvalidTree);

			}
			// 新增第二层功能

			for (DfmeaDetail dfmeaDetail_c22 : dfmeaDetail_cs) {
				List<DfmeaDetail> dfmeaDetail_c23 = dfmeaDetailMapper.Selectbyparentbranch(dfmeaDetail_c22);
				for (DfmeaDetail dfmeaDetail_f : dfmeaDetail_c23) {
					if (dfmeaDetail_f.getRanks() == 2) {
						Long perant_id = dfmeaDetailMapper.getparentIdsec(dfmeaDetail_f);
						HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
						hQMInvalidTree.setInvalidConsequence(dfmeaDetail_f.getFailureConsequences());
						hQMInvalidTree.setInvalidName(dfmeaDetail_f.getBranchName());
						if (dfmeaDetail_f.getSeverity() != null) {
							hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(dfmeaDetail_f.getSeverity())));
						}
						hQMInvalidTree.setSpecialCharacterType(dfmeaDetail_f.getSpecialCharacteristicType());
						hQMInvalidTree.setInvalidReason(dfmeaDetail_f.getFailureReason());
						hQMInvalidTree.setPreventMeasure(dfmeaDetail_f.getPreventiveMeasure());
						hQMInvalidTree.setOccurrence(dfmeaDetail_f.getOccurrence());
						if (dfmeaDetail_f.getDetection() != null) {
							hQMInvalidTree.setDetection(dfmeaDetail_f.getDetection());
						}
						hQMInvalidTree.setRpn(dfmeaDetail_f.getRpn());
						hQMInvalidTree.setRanks(dfmeaDetail_f.getRanks());
						hQMInvalidTree.setParentInvalidId(perant_id);

						hqmInvalidTreeMapper.insertSelective(hQMInvalidTree);

					}
				}
			}

			// 新增第三层功能
			for (DfmeaDetail dfmeaDetail_c32 : dfmeaDetail_cs) {
				List<DfmeaDetail> dfmeaDetail_c33 = dfmeaDetailMapper.Selectbyparentbranch(dfmeaDetail_c32);
				for (DfmeaDetail dfmeaDetail_f : dfmeaDetail_c33) {
					if (dfmeaDetail_f.getRanks() == 3) {
						// 先获取他的最顶层结构的ID
						Long perant_id = dfmeaDetailMapper.getparentIdfromthd(dfmeaDetail_f);
						// 在确定上一层结构的ID
						DfmeaDetail dfmeaDetail_3 = new DfmeaDetail();
						dfmeaDetail_3.setBranchId(dfmeaDetail_f.getBranchId());
						dfmeaDetail_3.setParentBranchId(perant_id);
						Long perant_id_i = dfmeaDetailMapper.getparentIdthd(dfmeaDetail_3);
						// 新增
						HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
						hQMInvalidTree.setInvalidConsequence(dfmeaDetail_f.getFailureConsequences());
						hQMInvalidTree.setInvalidName(dfmeaDetail_f.getBranchName());
						if (dfmeaDetail_f.getSeverity() != null) {
							hQMInvalidTree.setSerious(Long.valueOf(String.valueOf(dfmeaDetail_f.getSeverity())));
						}
						hQMInvalidTree.setSpecialCharacterType(dfmeaDetail_f.getSpecialCharacteristicType());
						hQMInvalidTree.setInvalidReason(dfmeaDetail_f.getFailureReason());
						hQMInvalidTree.setPreventMeasure(dfmeaDetail_f.getPreventiveMeasure());
						hQMInvalidTree.setOccurrence(dfmeaDetail_f.getOccurrence());
						if (dfmeaDetail_f.getDetection() != null) {
							hQMInvalidTree.setDetection(dfmeaDetail_f.getDetection());
						}
						hQMInvalidTree.setRpn(dfmeaDetail_f.getRpn());
						hQMInvalidTree.setRanks(dfmeaDetail_f.getRanks());
						hQMInvalidTree.setParentInvalidId(perant_id_i);

						hqmInvalidTreeMapper.insertSelective(hQMInvalidTree);

					}

				}
			}
		}
		Fmea head = new Fmea();
		head.setKid(id);
		head = fmeaMapper.selectByPrimaryKey(head);
		// 获取当前fmeaId 下的 版本
		Float vNum = head.getCurrentVersion();
		// 更新版本
		Float newVnum = getMaxVersion(vNum);
		head.setCurrentVersion(newVnum);
		fmeaMapper.updateByPrimaryKeySelective(head);
		// 新增版本表
		FmeaVersion fmeaVersion = new FmeaVersion();
		fmeaVersion.setFmeaId(id);
		fmeaVersion.setFmeaCode(head.getFmeaCode());
		fmeaVersion.setFmeaVersion(newVnum);
		fmeaVersionMapper.insertSelective(fmeaVersion);

		responseData.setSuccess(true);
		responseData.setMessage("提交成功");
		return responseData;
	}

	/**
	 * @Description 导入结构
	 * @Param [requestContext，list数据集]
	 */
	// 导入结构
	@Override
	public ResponseData getdata(IRequest requestContext, List<Long> headList) {
		// List<PoHeaders> headList1 = new ArrayList();
		ResponseData responseData = new ResponseData();
		Long perant_id = null;
		HQMInvalidTree hQMInvalidTree = new HQMInvalidTree();
		hQMInvalidTree.setInvalidId(headList.get(0));

		List<HQMInvalidTree> hQMInvalidTreeL = hqmInvalidTreeMapper.selectfromdata_r1(hQMInvalidTree);
		for (HQMInvalidTree hQMInvalidTrees : hQMInvalidTreeL) {
			DfmeaDetail dfmeaDetail = new DfmeaDetail();

			dfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
			dfmeaDetail.setBranchName(hQMInvalidTrees.getInvalidName());
			dfmeaDetail.setFailureConsequences(hQMInvalidTrees.getInvalidConsequence());
			if (hQMInvalidTrees.getSerious() != null) {
				dfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMInvalidTrees.getSerious())));
			}
			dfmeaDetail.setSpecialCharacteristicType(hQMInvalidTrees.getSpecialCharacterType());
			dfmeaDetail.setFailureReason(hQMInvalidTrees.getInvalidReason());
			dfmeaDetail.setPreventiveMeasure(hQMInvalidTrees.getPreventMeasure());
			dfmeaDetail.setOccurrence(hQMInvalidTrees.getOccurrence());
			if (hQMInvalidTrees.getDetection() != null) {
				dfmeaDetail.setDetection(hQMInvalidTrees.getDetection());
			}
			dfmeaDetail.setRpn(hQMInvalidTrees.getRpn());
			dfmeaDetail.setRanks(hQMInvalidTrees.getRanks());
			// 结构和功能要保证唯一性
			if (dfmeaDetail.getRanks() == 1 || dfmeaDetail.getRanks() == 2) {
				DfmeaDetail dfmeaDetail_check = new DfmeaDetail();
				dfmeaDetail_check.setBranchName(hQMInvalidTrees.getInvalidName());
				dfmeaDetail_check.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
				dfmeaDetail_check = dfmeaDetailMapper.selectOne(dfmeaDetail_check);
				if (dfmeaDetail_check != null) {
					responseData.setSuccess(false);
					responseData.setMessage("结构已存在 不可重复添加");
					return responseData;
				}
			}
			dfmeaDetailMapper.insertSelective(dfmeaDetail);
			dfmeaDetail = dfmeaDetailMapper.selectOne(dfmeaDetail);

			perant_id = dfmeaDetail.getBranchId();
		}

		List<HQMInvalidTree> hQMInvalidTreeL_2 = hqmInvalidTreeMapper.selectfromdata_r2r3(hQMInvalidTree);
		for (HQMInvalidTree hQMInvalidTrees : hQMInvalidTreeL_2) {
			if (hQMInvalidTrees.getRanks() == 2) {
				DfmeaDetail dfmeaDetail = new DfmeaDetail();

				dfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
				dfmeaDetail.setBranchName(hQMInvalidTrees.getInvalidName());
				dfmeaDetail.setFailureConsequences(hQMInvalidTrees.getInvalidConsequence());
				if (hQMInvalidTrees.getSerious() != null) {
					dfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMInvalidTrees.getSerious())));
				}
				dfmeaDetail.setSpecialCharacteristicType(hQMInvalidTrees.getSpecialCharacterType());
				dfmeaDetail.setFailureReason(hQMInvalidTrees.getInvalidReason());
				dfmeaDetail.setPreventiveMeasure(hQMInvalidTrees.getPreventMeasure());
				dfmeaDetail.setOccurrence(hQMInvalidTrees.getOccurrence());
				if (hQMInvalidTrees.getDetection() != null) {
					dfmeaDetail.setDetection(hQMInvalidTrees.getDetection());
				}
				dfmeaDetail.setRpn(hQMInvalidTrees.getRpn());
				dfmeaDetail.setRanks(hQMInvalidTrees.getRanks());
				dfmeaDetail.setParentBranchId(perant_id);
				// 结构和功能要保证唯一性

				dfmeaDetailMapper.insertSelective(dfmeaDetail);
			}
		}
		for (HQMInvalidTree hQMInvalidTrees : hQMInvalidTreeL_2) {
			if (hQMInvalidTrees.getRanks() == 3) {
				HQMInvalidTree hQMInvalidTree_3 = new HQMInvalidTree();
				hQMInvalidTree_3.setInvalidId(hQMInvalidTrees.getInvalidId());
				hQMInvalidTree_3.setParentInvalidId(perant_id);

				Long pra_id = hqmInvalidTreeMapper.getparentIdfromthd(hQMInvalidTree_3);
				DfmeaDetail dfmeaDetail = new DfmeaDetail();

				dfmeaDetail.setFmeaId(Float.valueOf(String.valueOf(headList.get(1))));
				dfmeaDetail.setBranchName(hQMInvalidTrees.getInvalidName());
				dfmeaDetail.setFailureConsequences(hQMInvalidTrees.getInvalidConsequence());
				if (hQMInvalidTrees.getSerious() != null) {
					dfmeaDetail.setSeverity(Long.valueOf(String.valueOf(hQMInvalidTrees.getSerious())));
				}
				dfmeaDetail.setSpecialCharacteristicType(hQMInvalidTrees.getSpecialCharacterType());
				dfmeaDetail.setFailureReason(hQMInvalidTrees.getInvalidReason());
				dfmeaDetail.setPreventiveMeasure(hQMInvalidTrees.getPreventMeasure());
				dfmeaDetail.setOccurrence(hQMInvalidTrees.getOccurrence());
				if (hQMInvalidTrees.getDetection() != null) {
					dfmeaDetail.setDetection(hQMInvalidTrees.getDetection());
				}
				dfmeaDetail.setRpn(hQMInvalidTrees.getRpn());
				dfmeaDetail.setRanks(hQMInvalidTrees.getRanks());
				dfmeaDetail.setParentBranchId(pra_id);
				// 结构和功能要保证唯一性

				dfmeaDetailMapper.insertSelective(dfmeaDetail);
			}
		}

		responseData.setSuccess(true);
		responseData.setMessage("添加成功");
		return responseData;
	}

	/**
	 * @Description 查询主界面数据
	 * @Param [requestContext，list数据集]
	 */
	@Override
	public List<DfmeaDetail> myselect(IRequest requestContext, DfmeaDetail dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return dfmeaDetailMapper.myselect(dto);
	}

	/**
	 * @Description 查询打印数据
	 * @Param [requestContext，list数据集]
	 */
	@Override
	public List<DfmeaDetail> queryprintData(IRequest requestContext, DfmeaDetail dto, int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		return dfmeaDetailMapper.queryprintData(dto);
	}

	/**
	 * @Description 查询数据
	 * @Param [requestContext，list数据集]
	 */
	@Override
	public List<DfmeaDetail> queryCondition(IRequest requestContext, DfmeaDetail dto, int page, int pageSize) {
		if (dto.getPostDetections() != null && dto.getPostDetections() != "") {
			dto.setPostDetectionList(Arrays.asList(dto.getPostDetections().replaceAll("\"", "").split(",")));
		}
		if (dto.getSeveritys() != null && dto.getSeveritys() != "") {
			dto.setSeverityList(Arrays.asList(dto.getSeveritys().replaceAll("\"", "").split(",")));
		}
		if (dto.getOccurrences() != null && dto.getOccurrences() != "") {
			dto.setOccurrenceList(Arrays.asList(dto.getOccurrences().replaceAll("\"", "").split(",")));
		}
		if (dto.getDetections() != null && dto.getDetections() != "") {
			dto.setDetectionList(Arrays.asList(dto.getDetections().replaceAll("\"", "").split(",")));
		}
		if (dto.getPostSeveritys() != null && dto.getPostSeveritys() != "") {
			dto.setPostSeverityList(Arrays.asList(dto.getPostSeveritys().replaceAll("\"", "").split(",")));
		}
		if (dto.getPostOccurrences() != null && dto.getPostOccurrences() != "") {
			dto.setPostOccurrenceList(Arrays.asList(dto.getPostOccurrences().replaceAll("\"", "").split(",")));
		}
		if (dto.getSpecialCharacteristicTypes() != null && dto.getSpecialCharacteristicTypes() != "") {
			dto.setSpecialCharacteristicTypeList(
					Arrays.asList(dto.getSpecialCharacteristicTypes().replaceAll("\"", "").split(",")));
		}
		PageHelper.startPage(page, pageSize);
		return dfmeaDetailMapper.queryCondition(dto);
	}

	@Override
	public DfmeaDetail queryHeaderData(Float kid) {
		return dfmeaDetailMapper.queryHeaderData(kid);
	}

	// 每更新一次＋10
	public Float getMaxVersion(Float VersionNum) {
		return VersionNum + 10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_dfmea_detail.service.IDfmeaDetailService#detailExcelImport(
	 * javax.servlet.http.HttpServletRequest, com.hand.hap.core.IRequest,
	 * org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public List<DfmeaDetail> detailExcelImport(HttpServletRequest request, IRequest requestCtx, MultipartFile forModel)
			throws Exception {
		Float fmeaId = Float.valueOf(request.getParameter("fmeaId"));
		Sheet sheet;
		XSSFWorkbook workBook;
		List<DfmeaDetail> totalList = new ArrayList<DfmeaDetail>();
		workBook = new XSSFWorkbook(forModel.getInputStream());
		sheet = workBook.getSheetAt(0);
		// 从第二行开始解析数据 第一行为标题行
		Row row;
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			for (int c = 0; c <= 19; c++) {
				if (row.getCell(c) != null && c != 14) {
					row.getCell(c).setCellType(CellType.STRING);
				}
			}
			DfmeaDetail branchsTop = new DfmeaDetail();
//			row.getCell(0).setCellType(CellType.STRING);
			branchsTop.setBranchName(row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue().trim());
			branchsTop.setRanks(1l);
			branchsTop.setFmeaId(fmeaId);
			List<DfmeaDetail> topList = mapper.select(branchsTop);
			// 判断当前第一层级如果存在 则更新层级 如果不存在 则新增一条数据
			if (topList != null && topList.size() > 0) {
				branchsTop = topList.get(0);
			} else {
				mapper.insertSelective(branchsTop);
				totalList.add(branchsTop);
			}

			DfmeaDetail branchs2 = new DfmeaDetail();
//			row.getCell(0).setCellType(CellType.STRING);
			branchs2.setBranchName(row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue().trim());
			branchs2.setRanks(2l);
			branchs2.setFmeaId(fmeaId);
			branchs2.setParentBranchId(branchsTop.getBranchId());
			List<DfmeaDetail> list2 = mapper.select(branchs2);
			// 判断当前第2层级如果存在 则更新层级 如果不存在 则新增一条数据
			if (list2 != null && list2.size() > 0) {
				branchs2 = list2.get(0);
			} else {
				mapper.insertSelective(branchs2);
				totalList.add(branchs2);
			}

			DfmeaDetail branchs3 = new DfmeaDetail();
//			row.getCell(0).setCellType(CellType.STRING);
			branchs3.setBranchName(row.getCell(2) == null ? null : row.getCell(2).getStringCellValue().trim());
			branchs3.setRanks(3l);
			branchs3.setFmeaId(fmeaId);
			branchs3.setParentBranchId(branchs2.getBranchId());
			DfmeaDetail branchDto = new DfmeaDetail();
			BeanUtils.copyProperties(branchs3, branchDto);// 对象拷贝
			List<DfmeaDetail> list3 = mapper.select(branchs3);
			branchDto.setInvalidConsequence(row.getCell(3) == null ? null : row.getCell(3).getStringCellValue());
			branchDto.setFailureConsequences(row.getCell(3) == null ? null : row.getCell(3).getStringCellValue());// 潜在失效后果
			branchDto.setSeverity(row.getCell(4) == null || row.getCell(4).getStringCellValue().equals("") ? null
					: Long.valueOf(row.getCell(4).getStringCellValue()));// 严重度
			branchDto.setSpecialCharacteristicType(row.getCell(5) == null ? null : row.getCell(5).getStringCellValue());// 分类
			branchDto.setFailureReason(row.getCell(6) == null ? null : row.getCell(6).getStringCellValue());// 潜在失效原因机理
			branchDto.setOccurrence(row.getCell(7) == null || row.getCell(7).getStringCellValue().equals("") ? null
					: Long.valueOf(row.getCell(7).getStringCellValue()));// 频度
			branchDto.setPreventiveMeasure(row.getCell(8) == null ? null : row.getCell(8).getStringCellValue());// 现行设计控制-预防
			branchDto.setDetectionMeasure(row.getCell(9) == null ? null : row.getCell(9).getStringCellValue());// 现行设计控制-探测
			branchDto.setDetection(row.getCell(10) == null || row.getCell(10).getStringCellValue().equals("") ? null
					: Long.valueOf(row.getCell(10).getStringCellValue()));
			branchDto.setRpn(row.getCell(11) == null || row.getCell(11).getStringCellValue().equals("") ? null
					: Long.valueOf(row.getCell(11).getStringCellValue()));
			branchDto.setSuggestMeasure(row.getCell(12) == null ? null : row.getCell(12).getStringCellValue());
			branchDto.setChargeId(
					getUserIdByEmployeeName(row.getCell(13) == null ? null : row.getCell(13).getStringCellValue()));
			branchDto.setEstimatedFinishTime(row.getCell(14) == null ? null : row.getCell(14).getDateCellValue());
			branchDto.setMeasureResult(row.getCell(15) == null ? null : row.getCell(15).getStringCellValue());
			branchDto.setPostSeverity(row.getCell(16) == null || row.getCell(16).getStringCellValue().equals("") ? null
					: Float.valueOf(row.getCell(16).getStringCellValue()));
			branchDto
					.setPostOccurrence(row.getCell(17) == null || row.getCell(17).getStringCellValue().equals("") ? null
							: Float.valueOf(row.getCell(17).getStringCellValue()));
			branchDto.setPostDetection(row.getCell(18) == null || row.getCell(18).getStringCellValue().equals("") ? null
					: Float.valueOf(row.getCell(18).getStringCellValue()));
			branchDto.setPostRpn(row.getCell(19) == null || row.getCell(19).getStringCellValue().equals("") ? null
					: Float.valueOf(row.getCell(19).getStringCellValue()));
			// 判断当前第3层级如果存在 则更新层级 如果不存在 则新增一条数据
			if (list3 != null && list3.size() > 0) {
				branchDto.setBranchId(list3.get(0).getBranchId());
				mapper.updateByPrimaryKeySelective(branchDto);
			} else {
				mapper.insertSelective(branchDto);
				totalList.add(branchDto);
			}

		}

		workBook.close();
		return totalList;
	}

	@Autowired
	UserSysMapper userSysMapper;

	public Float getUserIdByEmployeeName(String name) {
		Float res = null;
		res = userSysMapper.getUserIdByEmployeeName(name);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hand.hqm.hqm_dfmea_detail.service.IDfmeaDetailService#exportExcel(java.
	 * lang.Float, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void exportExcel(Float fmeaId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		response.addHeader("Content-Disposition",
				"attachment;filename=\"" + URLEncoder.encode("DFEMA_DETAIL" + ".xlsx", "UTF-8") + "\"");
		response.setContentType("application/vnd.ms-excel;charset=" + "UTF-8" + "");
		response.setHeader("Accept-Ranges", "bytes");
		ServletOutputStream outputStream = response.getOutputStream();
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		createRows(fmeaId, sheet, request);
//		Row firstRow = sheet.createRow(0);
//		Cell cell = firstRow.createCell(0);
//		cell.setCellValue("TEST");
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
		try {
			wb.close();
		} catch (Exception e) {

		}
	}

	/**
	 * @description
	 * @author tianmin.wang
	 * @date 2019年12月9日
	 * @param fmeaId
	 * @param sheet
	 * @param request
	 */
	private void createRows(Float fmeaId, Sheet sheet, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		generateTitle(sheet);
		DfmeaDetail ddSearch = new DfmeaDetail();
		ddSearch.setFmeaId(fmeaId);
		ddSearch.setRanks(3l);
		List<DfmeaDetail> list = mapper.select(ddSearch);
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(getParentName(getParentName(list.get(i))).getBranchName());
			row.createCell(1).setCellValue(getParentName(list.get(i)).getBranchName());
			row.createCell(2).setCellValue(list.get(i).getBranchName());
			row.createCell(3).setCellValue(list.get(i).getFailureConsequences());
			row.createCell(4)
					.setCellValue(list.get(i).getSeverity() == null ? "" : list.get(i).getSeverity().toString());
			row.createCell(5).setCellValue(list.get(i).getSpecialCharacteristicType());
			row.createCell(6).setCellValue(list.get(i).getFailureReason());
			row.createCell(7)
					.setCellValue(list.get(i).getOccurrence() == null ? "" : list.get(i).getOccurrence().toString());
			row.createCell(8).setCellValue(list.get(i).getPreventiveMeasure());
			row.createCell(9).setCellValue(list.get(i).getDetectionMeasure());
			row.createCell(10)
					.setCellValue(list.get(i).getDetection() == null ? "" : list.get(i).getDetection().toString());
			row.createCell(11).setCellValue(list.get(i).getRpn() == null ? "" : list.get(i).getRpn().toString());
			row.createCell(12).setCellValue(list.get(i).getSuggestMeasure());
			row.createCell(13).setCellValue(userSysMapper.getEmployeeNameByUserId(list.get(i).getChargeId()));
			row.createCell(14).setCellValue(list.get(i).getEstimatedFinishTime() == null ? ""
					: sdf.format(list.get(i).getEstimatedFinishTime()));
			row.createCell(15).setCellValue(list.get(i).getMeasureResult());
			row.createCell(16).setCellValue(list.get(i).getPostSeverity() == null ? ""
					: String.valueOf(list.get(i).getPostSeverity().intValue()));
			row.createCell(17).setCellValue(list.get(i).getPostOccurrence() == null ? ""
					: String.valueOf(list.get(i).getPostOccurrence().intValue()));
			row.createCell(18).setCellValue(list.get(i).getPostDetection() == null ? ""
					: String.valueOf(list.get(i).getPostDetection().intValue()));
			row.createCell(19).setCellValue(
					list.get(i).getPostRpn() == null ? "" : String.valueOf(list.get(i).getPostRpn().intValue()));
		}
	}

	/**
	 * 
	 * @description 父亲的分支描述 BRANCH_NAME
	 * @author tianmin.wang
	 * @date 2019年12月9日
	 * @return
	 */
	private DfmeaDetail getParentName(DfmeaDetail thisDto) {
		DfmeaDetail ddSearch = new DfmeaDetail();
		ddSearch.setFmeaId(thisDto.getFmeaId());
		ddSearch.setRanks(thisDto.getRanks() - 1);
		ddSearch.setBranchId(thisDto.getParentBranchId());
		List<DfmeaDetail> list = mapper.select(ddSearch);
		return list.get(0);
	}

	/**
	 * 
	 * @description excel的标题
	 * @author tianmin.wang
	 * @date 2019年12月9日
	 * @param sheet
	 */
	private void generateTitle(Sheet sheet) {
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("结构");
		row.createCell(1).setCellValue("功能");
		row.createCell(2).setCellValue("潜在失效模式");
		row.createCell(3).setCellValue("潜在失效后果");
		row.createCell(4).setCellValue("严重度");
		row.createCell(5).setCellValue("分类");
		row.createCell(6).setCellValue("潜在失效原因/机理");
		row.createCell(7).setCellValue("频度");
		row.createCell(8).setCellValue("现行设计控制-预防");
		row.createCell(9).setCellValue("现行设计控制-探测");
		row.createCell(10).setCellValue("探测度");
		row.createCell(11).setCellValue("风险顺序数");
		row.createCell(12).setCellValue("建议措施");
		row.createCell(13).setCellValue("责任人");
		row.createCell(14).setCellValue("目标完成日期");
		row.createCell(15).setCellValue("采取的措施(状态)");
		row.createCell(16).setCellValue("新严重度");
		row.createCell(17).setCellValue("新频度");
		row.createCell(18).setCellValue("新探测度");
		row.createCell(19).setCellValue("新风险顺序数");
	}

}