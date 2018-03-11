package org.lightning.quark.db.sql;

import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.db.PKData;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2017/12/12
 */
public interface SqlProvider {

    // QUERY rows
    /**
     * 查询行(s) - [startPk, endPk)
     * 用于按 范围 获取数据, 前闭后开
     * @param startPk
     * @param endPk
     * @return
     */
    String prepareQueryRowByRange(PKData startPk, PKData endPk);

    /**
     * 查询行(s) - [startPk, endPk]
     * 用于按 范围 获取数据, 前闭后闭
     * @param startPk
     * @param endPk
     * @return
     */
    String prepareQueryRowByRangeClosed(PKData startPk, PKData endPk);

    /**
     * 查询行(s) - [(pageNo-1) * pageSize, pageNo * pageSize)
     * 分页模式 - pageNo
     * @param pageNo - 1-baseed
     * @param pageSize
     * @param maxPk limit max-pk
     * @return
     */
    String prepareQueryRowByPage(int pageNo, int pageSize, PKData maxPk);

    /**
     * 查询行(s) - [offset, offset + pageSize)
     * 分页模式 - pageNo
     * @param offset
     * @param pageSize
     * @param maxPk
     * @return
     */
    String prepareQueryRowByOffset(long offset, int pageSize, PKData maxPk);


    /**
     * [minPk, ) - 取 前 <code>pageSize</code> 行
     * 分页模式, 分步取行 - 每次从最小pk开始
     * @param startPk
     * @param pageSize
     * @return
     */
    String prepareQueryRowByStep(PKData startPk, int pageSize);


    /**
     * 查找 按 多个 pks条件 查询
     * where pk in ( pks )
     * @param pks
     * @return
     */
    String prepareQueryRowByPks(Collection<PKData> pks);


    /**
     * 通过行号获取数据 [startRowNo, endRowNo)
     * 按主键排序
     * @param startRowNo
     * @param endRowNo
     * @return
     */
    String prepareQueryRowByRowNo(long startRowNo, long endRowNo);

    // END QUERY rows

    // QUERY pk

    /**
     * 获取源表的最大pk
     * @param maxPk 不为空时, 为条件
     * @return
     */
    String prepareQueryMaxPkByMaxPk(PKData maxPk);

    /**
     * 获取源表的最小id
     * @param maxPk 不为空时, 为条件
     * @return
     */
    String prepareQueryMinPkByMaxPk(PKData maxPk);

    /**
     * 获取[start, ) <code>pageSize</code>的最大id
     * @param startPk
     * @param pageSize
     * @return
     */
    String prepareQueryMaxPkByStep(PKData startPk, int pageSize);

    // END QUERY pk

    // INSERT
    /**
     *
     * @return
     */
    String prepareInsertRow();

    // END INSERT

    // DELETE

    /**
     * 支持多个pk
     * @param pks
     * @return
     */
    String prepareDeleteRowByPks(List<PKData> pks);

    // END DELETE

    // UPDATE
    /**
     * 更新一行
     * @param oneRow
     * @return
     */
    String prepareUpdateRow4OneRow(Map<String, Object> oneRow, PKData pk);

    // END UPDATE

    // QUERY count

    /**
     * 获取表总记录数count
     * @return
     */
    String prepareCount();

    /**
     * 获取表总记录数count
     * where PK < endPk
     * @param endPk
     * @return
     */
    String prepareCount(PKData endPk);

    /**
     * 获取表总记录数count
     * where PK < endPk and PK > startPk
     * @param startPk
     * @param endPk
     * @return
     */
    String prepareCount(PKData startPk, PKData endPk);

    // END count

    /**
     * 数据库类型
     * @return
     */
    DbVendor getVendor();


}
