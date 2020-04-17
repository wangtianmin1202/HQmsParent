package com.hand.spc.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.hand.spc.pspc_entity.view.AndersonDarlingChartDataVo;

/**
 * description
 *
 * @author 60201 2019/08/28 16:58
 */
public abstract class CoordinateUtils {


    public static List<AndersonDarlingChartDataVo> getCoordinate(List<BigDecimal> xList, List<BigDecimal> yList, List<BigDecimal> zList) {

        if (CollectionUtils.isEmpty(xList)) {
            return null;
        }
        List<AndersonDarlingChartDataVo> andersonDarlingChartDataVos = new ArrayList<>(xList.size());
        if (CollectionUtils.isNotEmpty(yList)) {
            boolean flag = true;
            int count = 0;
            while (flag) {
                AndersonDarlingChartDataVo darlingChartDataVo = new AndersonDarlingChartDataVo(xList.get(count).setScale(2, RoundingMode.HALF_UP), yList.get(count).setScale(2, RoundingMode.HALF_UP));
                andersonDarlingChartDataVos.add(darlingChartDataVo);
                if (++count == xList.size()) {
                    flag = false;
                }
            }
            return andersonDarlingChartDataVos;
        }
        if (CollectionUtils.isNotEmpty(zList)) {
            boolean flag = true;
            int count = 0;
            while (flag) {
                AndersonDarlingChartDataVo darlingChartDataVo =
                        new AndersonDarlingChartDataVo(xList.get(count).setScale(2, RoundingMode.HALF_UP),
                                null, zList.get(count).setScale(2, RoundingMode.HALF_UP));
                andersonDarlingChartDataVos.add(darlingChartDataVo);
                if (++count == xList.size()) {
                    flag = false;
                }
            }
        }
        return andersonDarlingChartDataVos;
    }
}
