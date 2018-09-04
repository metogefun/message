package cn.finegames.message.gateway.domain.permit;

import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * traffic permit repository
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Repository
public interface TrafficPermitRepository {

    /**
     * 根据主键删除数据
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入对象中不为空的数据
     *
     * @param record
     * @return
     */
    int insertSelective(TrafficPermit record);

    /**
     * 根据主键查询
     *
     * @param id
     * @return {@link TrafficPermit}
     */
    TrafficPermit selectByPrimaryKey(Integer id);

    /**
     * 根据主键修改不为空的字段
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TrafficPermit record);


    /**
     * 根据消息类型和ip返回是否为禁止的IP
     *
     * @param messageCategory
     * @param ip
     * @return
     */
    TrafficPermit selectForbiddenByMessageCategoryAndIp(@Param("messageCategory") MessageCategoryEnum messageCategory,
                                                        @Param("ip") String ip);

    /**
     * 根据消息类别和账号列表返回禁止的账号
     *
     * @param messageCategory
     * @param accounts
     * @return
     */
    List<TrafficPermit> selectForbiddenByMessageCategoryAndAccount(@Param("messageCategory") MessageCategoryEnum messageCategory,
                                                                   @Param("accounts") String[] accounts);

    /**
     * 根据消息类型返回可发送的服务器列表
     *
     * @param messageCategory
     * @return
     */
    List<TrafficPermit> selectAllowServersByMessageCategory(@Param("messageCategory") MessageCategoryEnum messageCategory);

}