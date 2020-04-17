package com.hand.spc.repository.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.hand.spc.repository.dto.ClassifyR;
import com.hand.spc.repository.dto.ClassifyGroupR;

public interface IClassifyRService extends IBaseService<ClassifyR>, ProxySelf<IClassifyRService>  {

    //分类组-保存-仅保存分类组
    public ClassifyGroupR saveClassifyGroup(ClassifyGroupR classifyGroup);

    //分类组-删除-删除分类组、分类项与分类组关系、控制要素与分类组关系
    public ClassifyGroupR delClassifyGroup(ClassifyGroupR classifyGroup);

    //分类项-保存-生成分类项、分类项与分类组关系
    public ClassifyGroupR saveClassify(ClassifyGroupR classifyGroup);

    //分类项-删除-删除分类项、分类项与分类组关系
    public ClassifyGroupR delClassify(ClassifyGroupR classifyGroup);

    //控制要素-保存-生成控制要素、控制要素与分类组关系
    public ClassifyGroupR saveCeParameter(ClassifyGroupR classifyGroup);

    //控制要素-删除-删除控制要素、控制要素与分类组关系
    public ClassifyGroupR delCeParameter(ClassifyGroupR classifyGroup);

}
