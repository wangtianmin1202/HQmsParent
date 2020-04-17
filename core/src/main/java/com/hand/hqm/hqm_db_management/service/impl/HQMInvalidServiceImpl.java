package com.hand.hqm.hqm_db_management.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.hand.hqm.hqm_db_management.dto.HQMFunction;
import com.hand.hqm.hqm_db_management.dto.HQMInvalid;
import com.hand.hqm.hqm_db_management.dto.HQMInvalidTree;
import com.hand.hqm.hqm_db_management.dto.HQMStructure;
import com.hand.hqm.hqm_db_management.mapper.HQMFunctionMapper;
import com.hand.hqm.hqm_db_management.mapper.HQMInvalidMapper;
import com.hand.hqm.hqm_db_management.mapper.HQMStructureMapper;
import com.hand.hqm.hqm_db_management.mapper.HqmInvalidTreeMapper;
import com.hand.hqm.hqm_db_management.service.IHQMFunctionService;
import com.hand.hqm.hqm_db_management.service.IHQMInvalidService;
import com.hand.hqm.hqm_db_management.service.IHQMStructureService;
import com.hand.hqm.hqm_db_management.service.IHqmInvalidTreeService;

@Service
@Transactional(rollbackFor = Exception.class)
public class HQMInvalidServiceImpl extends BaseServiceImpl<HQMInvalid> implements IHQMInvalidService {

	@Autowired
	private HQMInvalidMapper hQMInvalidMapper;

	@Autowired
	private HqmInvalidTreeMapper hqmInvalidTreeMapper;

	@Autowired
	private IHqmInvalidTreeService hqmInvalidTreeService;

	@Autowired
	private IHQMStructureService hQMStructureService;

	@Autowired
	private IHQMFunctionService hQMFunctionService;

	@Autowired
	private HQMStructureMapper hQMStructureMapper;

	@Autowired
	private HQMFunctionMapper hQMFunctionMapper;

	@Override
	public List<HQMInvalid> query() {

		return hQMInvalidMapper.query();
	}

	@Override
	public List<HQMInvalid> save(IRequest requestCtx, List<HQMInvalid> list) {

		List<HQMInvalid> hQMInvalids = new ArrayList<HQMInvalid>();
		// List<HQMFunction> hQMFunctions = new ArrayList<HQMFunction>();
		// List<HQMStructure> hQMStructures = new ArrayList<HQMStructure>();

		for (int i = 0; i < list.size(); i++) {

			// 保存结构表
			HQMStructure hQMStructureq = new HQMStructure();
			hQMStructureq = hQMStructureMapper.structureNamecount(list.get(i).getStructureName());
			if (hQMStructureq != null) {// 判断结构表里是否已有此结构
				list.get(i).setStructureId(hQMStructureq.getStructureId());
			} else {
				/*
				 * HQMStructure hQMStructured = new HQMStructure();
				 * hQMStructured.setStructureId(list.get(i).getStructureId());
				 * hQMStructureService.deleteByPrimaryKey(hQMStructured);
				 */

				HQMStructure hQMStructurei = new HQMStructure();
				hQMStructurei.setStructureName(list.get(i).getStructureName());
				if ("add".equals(list.get(i).get__status())) {
					hQMStructurei = hQMStructureService.insertSelective(requestCtx, hQMStructurei);
					list.get(i).setStructureId(hQMStructurei.getStructureId());
				} else {
					hQMStructurei.setStructureId(list.get(i).getStructureId());
					hQMStructureService.updateByPrimaryKeySelective(requestCtx, hQMStructurei);
				}

			}

			// 保存功能表
			HQMFunction hQMFunctionq = new HQMFunction();
			hQMFunctionq = hQMFunctionMapper.functionNamecount(list.get(i).getFunctionName());
			if (hQMFunctionq != null) {// 判断功能表里是否已有此功能
				list.get(i).setFunctionId(hQMFunctionq.getFunctionId());
			} else {
				HQMFunction hQMFunctioni = new HQMFunction();
				hQMFunctioni.setFunctionName(list.get(i).getFunctionName());
				if ("add".equals(list.get(i).get__status())) {
					hQMFunctioni = hQMFunctionService.insertSelective(requestCtx, hQMFunctioni);
					list.get(i).setFunctionId(hQMFunctioni.getFunctionId());

					HQMFunction hQMFunctionii = new HQMFunction();
					hQMFunctionii.setFunctionId(hQMFunctioni.getFunctionId());
					hQMFunctionii.setStructureId(list.get(i).getStructureId());
					hQMFunctionService.updateByPrimaryKeySelective(requestCtx, hQMFunctionii);
				} else {
					hQMFunctioni.setFunctionId(list.get(i).getFunctionId());
					hQMFunctionService.updateByPrimaryKey(requestCtx, hQMFunctioni);
				}

			}

		}

		// 保存失效表
		hQMInvalids = self().batchUpdate(requestCtx, list);

		// 回写功能表 更新功能关联的失效id
		for (int j = 0; j < hQMInvalids.size(); j++) {
			HQMFunction hQMFunctionii = new HQMFunction();
			hQMFunctionii.setInvalidId(hQMInvalids.get(j).getInvalidId());
			hQMFunctionii.setFunctionId(list.get(j).getFunctionId());
			hQMFunctionService.updateByPrimaryKeySelective(requestCtx, hQMFunctionii);
		}
		return hQMInvalids;
	}

	@Override
	public int delete(List<HQMInvalid> list) {
		for (int i = 0; i < list.size(); i++) {

			// 删除结构表
			HQMStructure hQMStructure = new HQMStructure();
			hQMStructure.setStructureId(list.get(i).getStructureId());
			hQMStructureService.deleteByPrimaryKey(hQMStructure);

			// 删除功能表
			HQMFunction hQMFunction = new HQMFunction();
			hQMFunction.setFunctionId(list.get(i).getFunctionId());
			hQMFunctionService.deleteByPrimaryKey(hQMFunction);
		}
		int re = 0;
		re = self().batchDelete(list);
		return re;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseData inputDataFromExcel(HttpServletRequest request, IRequest requestContext, InputStream inputStream)
			throws Exception {
		ResponseData responseData = new ResponseData();
		// TODO 解析
		Sheet sheet;
		XSSFWorkbook workBook;
		// List<HQMInvalidTree> hQMInvalidTreelist = new ArrayList<HQMInvalidTree>();
		workBook = new XSSFWorkbook(inputStream);
//             if(workBook.getNumberOfSheets()>1) {
//             }
		sheet = workBook.getSheetAt(0);
		// 校验结构
		checkInport(sheet, responseData);
		if (responseData.isSuccess()) {
			try {
				// 循环结构
				for (int i = 1; i < sheet.getLastRowNum(); i++) {
					// i=1,从第二行开始
					Row rows = sheet.getRow(i);
					HQMInvalidTree hQMInvalidTrees = new HQMInvalidTree();
					// 行解析
					if (rows.getCell(0) == null) {
						continue;
					} else {
						// 结构
						hQMInvalidTrees.setInvalidName(rows.getCell(0).toString());
						hQMInvalidTrees.setRanks(1L);
						// 存表返id作为功能的parentId
						hQMInvalidTrees = hqmInvalidTreeService.insertSelective(requestContext, hQMInvalidTrees);
					}

					// 循环功能
					for (int j = i + 1; j < sheet.getLastRowNum(); j++) {
						Row rowf = sheet.getRow(j);
						HQMInvalidTree hQMInvalidTreef = new HQMInvalidTree();
						if (rowf.getCell(1) == null) {
							if (rowf.getCell(0) != null) {
								break;
							} else {
								continue;
							}
						} else {
							// 功能
							hQMInvalidTreef.setInvalidName(rowf.getCell(1).toString());
							hQMInvalidTreef.setRanks(2L);
							// 将结构id作为功能的父id
							hQMInvalidTreef.setParentInvalidId(hQMInvalidTrees.getInvalidId());
							// 功能存表返id作为失效的parentId
							hQMInvalidTreef = hqmInvalidTreeService.insertSelective(requestContext, hQMInvalidTreef);
						}

						// 循环失效
						for (int k = j + 1; k < sheet.getLastRowNum() + 1; k++) {
							Row rowi = sheet.getRow(k);
							HQMInvalidTree hQMInvalidTreei = new HQMInvalidTree();
							if (rowi.getCell(2) == null) {
								break;
							} else {
								// 失效
								hQMInvalidTreei.setInvalidName(rowi.getCell(2).toString());
								if (rowi.getCell(3) != null) {
									hQMInvalidTreei.setInvalidConsequence(rowi.getCell(3).toString());
								}
								if (rowi.getCell(4) != null) {
									hQMInvalidTreei.setSerious(StringSpliToLong(rowi.getCell(4).toString()));
								}
								if (rowi.getCell(5) != null) {
									hQMInvalidTreei.setSpecialCharacterType(rowi.getCell(5).toString());

								}
								if (rowi.getCell(6) != null) {
									hQMInvalidTreei.setInvalidReason(rowi.getCell(6).toString());

								}
								if (rowi.getCell(7) != null) {
									hQMInvalidTreei.setPreventMeasure(rowi.getCell(7).toString());

								}
								if (rowi.getCell(8) != null) {
									hQMInvalidTreei.setOccurrence(StringSpliToLong(rowi.getCell(8).toString()));
								}
								if (rowi.getCell(9) != null) {
									hQMInvalidTreei.setDetectMeasure(rowi.getCell(9).toString());

								}
								if (rowi.getCell(10) != null) {
									hQMInvalidTreei.setDetection(StringSpliToLong(rowi.getCell(10).toString()));
								}
								if (rowi.getCell(10) != null && rowi.getCell(8) != null && rowi.getCell(4) != null) {
									hQMInvalidTreei.setRpn(StringSpliToLong(rowi.getCell(10).toString())
											* StringSpliToLong(rowi.getCell(8).toString())
											* StringSpliToLong(rowi.getCell(4).toString()));// StringSpliToLong(rowi.getCell(11).toString())
								}
								hQMInvalidTreei.setRanks(3L);
								// 将功能id作为失效的父id
								hQMInvalidTreei.setParentInvalidId(hQMInvalidTreef.getInvalidId());
								// 保存失效表
								hQMInvalidTreei = hqmInvalidTreeService.insertSelective(requestContext,
										hQMInvalidTreei);
							}

						}
					}

					// hQMInvalidTrees.add(hQMInvalidTree);
				}
			} catch (Exception e) {
				e.printStackTrace();
				responseData.setSuccess(false);
				responseData.setMessage("导入结构数据重复或模板有误，请检查excel数据结构并严格按照模板进行维护！");

				// throw new Exception();
			}
			try {
				workBook.close();
			} catch (Exception e) {

			}
			return responseData;
		} else {
			try {
				workBook.close();
			} catch (Exception e) {

			}
			return responseData;
		}

		/*
		 * for (InspectionAttribute inspectionAttribute : totalList) {
		 * self().insertSelective(null, inspectionAttribute); }
		 */
	}

	public Long StringSpliToLong(String s) {
		Long item = 0L;
		if (s != null && !"".equals(s)) {
			item = Long.valueOf(s.substring(0, s.indexOf(".")));
		}
		return item;
	}

	/**
	 * @throws Exception
	 * @Description Excel导入校验
	 * @Param [dtos]
	 */
	public void checkInport(Sheet sheet, ResponseData responseData) throws Exception {
		List<HQMInvalidTree> hQMInvalidTreelist = new ArrayList<HQMInvalidTree>();
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			// i=1,从第二行开始
			Row rows = sheet.getRow(i);
			if (rows == null) {
				responseData.setSuccess(false);
				responseData.setMessage("导入模板错误，请使用正确的模板进行导入！");
			} else {
				HQMInvalidTree hQMInvalidTrees = new HQMInvalidTree();
				// 行解析
				if (rows.getCell(0) == null) {
					continue;
				} else {
					// 结构
					hQMInvalidTrees.setInvalidName(rows.getCell(0).toString());
					hQMInvalidTrees.setRanks(1L);
					hQMInvalidTreelist.add(hQMInvalidTrees);
					// 数据库结构校验
					int count1 = hqmInvalidTreeMapper.checkStructure(hQMInvalidTrees);
					if (count1 > 0) {
						responseData.setSuccess(false);
						responseData.setMessage("导入结构不能重复！" + hQMInvalidTrees.getInvalidName());
					}
				}
			}

		}

		// excel维护结构校验
		if (validateSingleInExcel(hQMInvalidTreelist).size() > 0) {
			responseData.setSuccess(false);
			responseData.setMessage("excel内结构不能重复！" + hQMInvalidTreelist.get(0).getInvalidName());
		}

	}

	private List<HQMInvalidTree> validateSingleInExcel(List<HQMInvalidTree> snids) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		List<HQMInvalidTree> reSnids = new ArrayList<HQMInvalidTree>();
		for (int i = 0; i < snids.size(); i++) {
			String tmp = snids.get(i).getInvalidName();
			int count = resultMap.get(tmp) != null ? resultMap.get(tmp) : 0;
			count = count + 1;
			resultMap.put(tmp, count);
		}
		for (String getKey : resultMap.keySet()) {
			if (resultMap.get(getKey) != 1) {
				HQMInvalidTree wss = new HQMInvalidTree();
				wss.setInvalidName(getKey);
				reSnids.add(wss);
			}
		}
		return reSnids;
	}

}